package com.jm.business.service.zb.system;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.zb.system.ShopResourceCo;
import com.jm.mvc.vo.zb.system.ZbShopResourceVo;
import com.jm.repository.client.ImageClient;
import com.jm.repository.jpa.zb.system.ZbShopResourceRepository;
import com.jm.repository.po.zb.system.ZbShopResource;
import com.jm.staticcode.converter.zb.ZbShopResourceConverter;
import com.jm.staticcode.util.ImgUtil;
import com.qcloud.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author: cj
 * @date: 2016-6-3
 */
@Service
public class JmResourceService {

	private static long imageMaxSize = 1*1024*1024; //图片最大值

	private static long videoMaxSize = 5*1024*1024; //音频最大值

	private static String[] imageFormat = new String[]{"jpg","gif","png"};

	@Autowired
	private ImageClient imageClient;

	@Autowired
	private ZbShopResourceRepository zbShopResourceRepository;
	/**
	 * 上传图片文件到万象优图
	 * @param files
	 * @param shopResourceCo
	 * @return
	 * @throws IOException
	 */
	public JmMessage addImage(MultipartFile[] files, ShopResourceCo shopResourceCo) throws Exception {
		int res = checkRes(files,shopResourceCo);
		if (res>0){
			return getJmMessage(res,null);
		}
		//上传图片并且保存到数据库
		List<ZbShopResource> zbShopResources = new ArrayList<>();
		for (MultipartFile file : files){
			String origName = file.getOriginalFilename();
			String suffix = ImgUtil.getSuffix(origName);
			if (shopResourceCo.getResType().intValue()==3){ //语音
//				Map<Object,String> map = imageClient.uploadCosRes(file.getInputStream(),suffix);
//				if ("0".equals(map.get("code"))){
//					//加到数据库
//					ZbShopResource shopResource = getShopResource(map.get("data"),resourceCo);
//					shopResource.setResName(file.getOriginalFilename());
//					zbShopResources.add(shopResource);
//				}else{
//					return getJmMessage(3,null);
//				}
			}else{
				UploadResult uInfo = imageClient.uploadPic(file,suffix);
				if (uInfo==null){
					return getJmMessage(3,null);
				}
				//加到数据库
				ZbShopResource zbShopResource = getShopResource(uInfo.fileId,shopResourceCo);
				zbShopResource.setResName(file.getOriginalFilename());
				zbShopResources.add(zbShopResource);
			}
		}
		zbShopResourceRepository.save(zbShopResources); //保存到数据库
		return getJmMessage(0, ZbShopResourceConverter.p2v(zbShopResources,shopResourceCo.getCompress()));
	}

	/**
	 * 验证资源
	 * @param files
	 * @param shopResourceCo
	 * @return
	 */
	private int checkRes(MultipartFile[] files,ShopResourceCo shopResourceCo){
		//校验资源信息
		if (files==null||files.length==0){
			return 4;
		}
		for (MultipartFile file : files){
			String origName = file.getOriginalFilename();
			String suffix = ImgUtil.getSuffix(origName);
			if (shopResourceCo.getResType().intValue()==3){
				if (file.getSize()>videoMaxSize){
					return 1;
				}
			}else {
				if (file.getSize()>imageMaxSize){
					return 1;
				}
			}
			if (!Arrays.asList(imageFormat).contains(suffix)){
				return 2;
			}
		}
		return 0;
	}

	private JmMessage getJmMessage(int res, List<ZbShopResourceVo> shopResourceRos){
		if(res==0){
			return new JmMessage(0,"上传成功",shopResourceRos);
		}else if (res==1){
			return new JmMessage(1,"上传失败,文件超过1M!!");
		}else if (res==2){
			return new JmMessage(1,"上传失败,文件格式错误!!");
		}else if (res==3){
			return new JmMessage(1,"上传文件失败!!");
		}else if (res==4){
			return new JmMessage(1,"文件列表为空");
		}else {
			return new JmMessage(1,"上传失败,未知错误!!");
		}
	}



	/**
	 * 获取资源对象
	 * @param fileId
	 * @param resourceCo
	 * @return
	 */
	private ZbShopResource getShopResource(String fileId, ShopResourceCo resourceCo){
		ZbShopResource zbShopResource = new ZbShopResource();
		zbShopResource.setIsDel(resourceCo.getIsDel());
		zbShopResource.setResGroupId(resourceCo.getGroupId());
		zbShopResource.setResType(resourceCo.getResType());
		zbShopResource.setResUrl(fileId);
		return zbShopResource;
	}

	/*public ZbPageItem<ShopResourceRo> findAll(ShopResourceQo shopResourceQo) {
		Specification spec = getSpec(shopResourceQo);
		Integer curPage = shopResourceQo.getCurPage()==null ? 0 : shopResourceQo.getCurPage();
		Integer pageSize = shopResourceQo.getPageSize()==null ? 20 : shopResourceQo.getPageSize();
		Sort sort=new Sort(Sort.Direction.DESC, "id");
		PageRequest pageRequest = new PageRequest(curPage,pageSize,sort);
		Page<ZbShopResource> shopResourcePage = shopResourceService.findAll(spec,pageRequest);
		ZbPageItem<ShopResourceRo> pageItem = UploadFileConverter.toShopResourceRo(shopResourcePage);
		return pageItem;
	}

	private void delResource(List<ShopResourceRo> lst){
		for (ShopResourceRo shopResourceRo : lst){
			if (shopResourceRo.getResUrl()!=null){
				ImageClient.delPic(shopResourceRo.getResUrl());
			}
		}
	}

	@ApiOperation("查询条件")
	public Specification<ZbShopResource> getSpec(final ShopResourceQo shopResourceQo){
		Specification<ZbShopResource> spec = new Specification<ZbShopResource> () {
			@Override
			public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Predicate> predicates = new ArrayList<>();
				if(StringUtils.isNotEmpty(shopResourceQo.getResName())){
					predicates.add(cb.like(root.get("resName").as(String.class), "%"+shopResourceQo.getResName()+"%"));
				}
				if(shopResourceQo.getShopId()!=null){
					predicates.add(cb.equal(root.get("shopId").as(Integer.class), shopResourceQo.getShopId()));
				}
				if(shopResourceQo.getGroupId()!=null){
					predicates.add(cb.equal(root.get("resGroupId").as(Integer.class), shopResourceQo.getGroupId()));
				}
				predicates.add(cb.equal(root.get("resType").as(Integer.class), shopResourceQo.getResType())); //文件类型 1：图片 2：视频 3 ：语音
				predicates.add(cb.equal(root.get("isDel").as(Integer.class), 0)); //0:在用 1：注销
				predicate.getExpressions().addAll(predicates);
				return predicate;
			}

		};
		return spec;
	}

	public List<ZbResource> getResource(Integer status) {
		return resourceRepository.findResourceByStatus(status);
	}



	public static JmMessage uploadFile(InputStream inputStream,String shopId) throws Exception {
		CosCloud cos = new CosCloud(ZbCosBaseConf.APP_ID, ZbCosBaseConf.SECRET_ID, ZbCosBaseConf.SECRET_KEY, 60);

		if("".equals(StringUtil.formatNull(shopId))){
			shopId="0";
		}
		String folderPath = ZbCosBaseConf.DIR_REMOTE_PATH+shopId+"/user/";
		//判断该目录是否存在
		String isFolder = cos.getFolderStat(ZbCosBaseConf.bucketName, folderPath);
		JSONObject folderJsons = new JSONObject(isFolder);
		String fileName = System.currentTimeMillis()+ Toolkit.getFour()
				+".png";
		// 上传到腾讯云服务器
		String code ="-1";
		if("0".equals(folderJsons.get("code").toString())){ // 文件夹已存在
			String uploadJsons  = cos.uploadFile(ZbCosBaseConf.bucketName, folderPath+fileName, inputStream);
			JSONObject uploadJs = new JSONObject(uploadJsons);
			code = StringUtil.formatNull(uploadJs.get("code"));
		}else{
			String createJsons = cos.createFolder(ZbCosBaseConf.bucketName,folderPath);
			JSONObject createJs = new JSONObject(createJsons);
			code = StringUtil.formatNull(createJs.get("code"));
			if("0".equals(code)){ //创建文件夹成功
				String uploadJsons  = cos.uploadFile(ZbCosBaseConf.bucketName, folderPath+fileName, inputStream);
				JSONObject uploadJs = new JSONObject(uploadJsons);
				code = StringUtil.formatNull(uploadJs.get("code"));
			}
		}
		if(!"0".equals(code)){
			return  new JmMessage(1,"false");
		}else{
			String showPath = folderPath+fileName;
			return new JmMessage(0,showPath);
		}
	}*/


}
