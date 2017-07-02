package com.jm.business.service.system;

import com.jm.business.service.shop.ShopResourceService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.zb.ZbSoftMenuService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.resource.ResourceCo;
import com.jm.mvc.vo.shop.resource.ShopResourceQo;
import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.repository.client.ImageClient;
import com.jm.repository.client.dto.CosResourceDto;
import com.jm.repository.jpa.resource.ResourceRepository;
import com.jm.repository.jpa.resource.RoleResourceRepository;
import com.jm.repository.jpa.shop.ShopResourceRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopResource;
import com.jm.repository.po.system.user.Resource;
import com.jm.repository.po.system.user.RoleResource;
import com.jm.repository.po.zb.system.ZbSoftMenu;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.system.ResourceConverter;
import com.jm.staticcode.converter.system.UploadFileConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Pic;
import com.qcloud.UploadResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>资源服务</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/06
 */
@Service
public class ResourceService {

    private static long imageMaxSize = 1*1024*1024; //图片最大值

    private static long voiceMaxSize = 20*1024*1024; //音频最大值

    private static String[] imageFormat = new String[]{"jpg","gif","png","jpeg"};

    private static String[] voiceFormat = new String[]{"mp3","mp4"};

    @Autowired
    private ResourceRepository  resourceRepository;

    @Autowired
    private ShopResourceService shopResourceService;

    @Autowired
    private ShopResourceRepository shopResourceRepository;

    @Autowired
    private ImageClient imageClient;

    @Autowired
    private RoleResourceRepository roleResourceRepository;

    @Autowired
    private ShopService shopService;
    @Autowired
    private ZbSoftMenuService zbSoftMenuService;

    /**
     * 上传图片文件到万象优图
     * @param files
     * @param resourceCo
     * @return
     * @throws IOException
     */
    public JmMessage addResource(MultipartFile[] files,ResourceCo resourceCo) throws Exception {
        int res = checkRes(files,resourceCo);
        if (res>0){
            return getJmMessage(res,null);
        }
        //上传图片并且保存到数据库
        List<ShopResource> shopResources = new ArrayList<>();
        for (MultipartFile file : files){
            String origName = file.getOriginalFilename();
            String suffix = ImgUtil.getSuffix(origName);
            if (resourceCo.getResType().intValue()==3){ //语音
                CosResourceDto cos = imageClient.uploadCosRes("voice",suffix,file.getBytes());
                if (0==cos.getCode()){
                    //加到数据库
                    ShopResource shopResource = ResourceConverter.toShopResource(cos.getFileId(),resourceCo);
                    shopResource.setResName(file.getOriginalFilename());
                    shopResources.add(shopResource);
                }else{
                    return getJmMessage(3,null);
                }
            }else{
                UploadResult uInfo = imageClient.uploadPic(file,suffix);
                if (uInfo==null){
                    return getJmMessage(3,null);
                }
                //加到数据库
                ShopResource shopResource = ResourceConverter.toShopResource(uInfo.fileId,resourceCo);
                shopResource.setResName(file.getOriginalFilename());
                shopResources.add(shopResource);
            }
        }
        shopResourceRepository.save(shopResources); //保存到数据库
        return getJmMessage(0,ResourceConverter.p2v(shopResources,resourceCo.getCompress()));
    }

    /**
     * 验证资源
     * @param files
     * @param resourceCo
     * @return
     */
    private int checkRes(MultipartFile[] files,ResourceCo resourceCo){
        //校验资源信息
        if (files==null||files.length==0){
            return 4;
        }
        for (MultipartFile file : files){
            String origName = file.getOriginalFilename();
            String suffix = ImgUtil.getSuffix(origName);
            suffix = suffix.toLowerCase();
            if (resourceCo.getResType().intValue()==3){//音频
                if (file.getSize()>voiceMaxSize){
                    return 1;
                }
                if (!Arrays.asList(voiceFormat).contains(suffix)){
                    return 2;
                }
            }else if(resourceCo.getResType().intValue()==2){//视频
                return 0;
            }else {
                if (file.getSize()>imageMaxSize){
                    return 1;
                }
                if (!Arrays.asList(imageFormat).contains(suffix)){
                    return 2;
                }
            }

        }
        return 0;
    }

    private JmMessage getJmMessage(int res,List<ShopResourceRo> shopResourceRos){
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
     * 上传网络URL图片
     * @param resourceCo
     * @return
     * @throws IOException
     */
    public JmMessage addUrlResource(ResourceCo resourceCo) throws IOException {
        ShopResource shopResource;
        if(2==resourceCo.getResType()||4==resourceCo.getResType()){
            shopResource = ResourceConverter.toShopResource(resourceCo);
        }else{
            String resUrl = URLDecoder.decode(resourceCo.getResUrl(), "UTF-8");
            Pic pic = new Pic();
            BufferedImage imageNew=pic.loadImageUrl(resUrl);
            ByteArrayOutputStream os = new ByteArrayOutputStream(); //将生成的图片转为InputStream
            String suffix = ImgUtil.getSuffix(resUrl,"gif");
            ImageIO.write(imageNew, suffix, os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            if (os.size()>imageMaxSize){ //图片太大
                return getJmMessage(1,null);
            }
            UploadResult uInfo = imageClient.uploadInputStream(is,suffix);
            if (uInfo==null){
                return getJmMessage(3,null);
            }
            shopResource = ResourceConverter.toShopResource(uInfo.fileId,resourceCo);
            shopResource.setResName(ImgUtil.getFileName(resUrl));
        }
        List<ShopResource> shopResources = new ArrayList<>(); //兼容多个
        shopResourceRepository.save(shopResource);
        shopResources.add(shopResource);
        return getJmMessage(0,ResourceConverter.p2v(shopResources,resourceCo.getCompress()));
    }

    /**
     * 上传网络图片内部系统使用
     * @param resUrl
     * @return
     * @throws IOException
     */
    public String uploadUrlImg(String resUrl) throws IOException {
        Pic pic = new Pic();
        BufferedImage imageNew=pic.loadImageUrl(resUrl);
        ByteArrayOutputStream os = new ByteArrayOutputStream(); //将生成的图片转为InputStream
        ImageIO.write(imageNew, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        UploadResult uInfo = imageClient.uploadInputStream(is,"png");
        if (uInfo==null){
            return "";
        }
        return ImgUtil.substringUrl(uInfo.downloadUrl);
    }

    public String uploadUrlImg(InputStream inputStream) throws IOException {
        UploadResult uInfo = imageClient.uploadInputStream(inputStream,"png");
        if (uInfo==null){
            return "";
        }
        return ImgUtil.substringUrl(uInfo.downloadUrl);
    }

    public PageItem<ShopResourceRo> findAll(ShopResourceQo shopResourceQo) {
        Specification spec = getSpec(shopResourceQo);
        Integer curPage = shopResourceQo.getCurPage()==null ? 0 : shopResourceQo.getCurPage();
        Integer pageSize = shopResourceQo.getPageSize()==null ? 20 : shopResourceQo.getPageSize();
        Sort sort=new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(curPage,pageSize,sort);
        Page<ShopResource> shopResourcePage = shopResourceService.findAll(spec,pageRequest);
        PageItem<ShopResourceRo> pageItem = UploadFileConverter.toShopResourceRo(shopResourcePage);
        return pageItem;
    }

    @ApiOperation("查询条件")
    public Specification<ShopResource> getSpec(final ShopResourceQo shopResourceQo){
        Specification<ShopResource> spec = new Specification<ShopResource> () {
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

    public List<Resource> getResource(Integer status) {
        return resourceRepository.findResourceByStatus(status);
    }

    public List<Resource> findResourceByRole(JmUserSession user,int status) {
        if(user ==null){
            return null;
        }

        if(user.getAppId().equals(Constant.JK_MANAGE_APPID)){ //聚客红包管理
            Shop shop = shopService.getShop(user.getShopId());
            if(null!=shop.getSoftId()){
                List<ZbSoftMenu> softMenus = zbSoftMenuService.findBySoftId(shop.getSoftId());
                List<Integer> resourceIds = new ArrayList<>();
                for (ZbSoftMenu zbSoftMenu:softMenus) {
                    resourceIds.add(zbSoftMenu.getResourceId());
                }
                if(resourceIds.size()>0){
                    List<Resource> resources = resourceRepository.findResourceByIdsAndStatus(resourceIds,status);
                    return resources;
                }
            }
            return null;
        }

        if(user.getAppId().equals("菜单管理")){ //菜单管理
            List<Integer> resourceIds = new ArrayList<>();
            resourceIds.add(1);
            List<Resource> resources = resourceRepository.findResourceByIdsAndStatus(resourceIds,status);
            Resource rec = new Resource();
            rec.setResourceId(10086);
            rec.setTplName("/zb/system/soft");
            rec.setStatus(0);
            rec.setParentRsourceId(1);
            rec.setResourceName("软件版本");
            resources.add(rec);
            return resources;
        }

        /*if(user.getRoleId()==2){//店主获取所有菜单
            return getResource(status);
        }*/
        //根据角色获取菜单
        Shop shop = shopService.getShop(user.getShopId());
        List<RoleResource> roleResources = roleResourceRepository.findByRoleIdAndSoftId(user.getRoleId(),shop.getSoftId());
        List<Integer> resourceIds = new ArrayList<>();
        for (RoleResource roleResource:roleResources) {
            resourceIds.add(roleResource.getResourceId());
        }
        if(resourceIds.size()>0){
            List<Resource> resources = resourceRepository.findResourceByIdsAndStatus(resourceIds,status);
            return resources;
        }

        return null;
    }

    public static JmMessage uploadFile(InputStream inputStream) throws Exception {
        UploadResult uInfo = ImageClient.uploadInputStream(inputStream,"png");
        if (uInfo==null){
            return  new JmMessage(1,"false");
        }
        return new JmMessage(0,uInfo.fileId);
    }

    @Transactional
    public JmMessage delete(String ids){
        boolean flag = shopResourceService.delShopResource(ids);
        if(true==flag){
            return new JmMessage(0,"删除成功!!");
        }
        return new JmMessage(1,"删除失败!!");
    }

}
