package com.jm.business.service.shop;

import com.google.zxing.common.BitMatrix;
import com.jm.mvc.vo.shop.commQrcode.CommonQrcodeCo;
import com.jm.repository.client.ImageClient;
import com.jm.repository.client.dto.CosResourceDto;
import com.jm.repository.client.dto.CosUrlDto;
import com.jm.repository.jpa.shop.CommonQrcodeDetailRepository;
import com.jm.repository.po.shop.CommonQrcode;
import com.jm.repository.po.shop.CommonQrcodeDetail;
import com.jm.staticcode.util.FileUtil;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.ZxingUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <p>通用商品条码</p>
 */
@Log4j
@Service
public class CommonQrcodeDetailService {
	
	@Autowired
	private CommonQrcodeDetailRepository commonQrcodeDetailRepository;
	@Autowired
	private ImageClient imageClient;
	@Autowired
	private CommonQrcodeService commonQrcodeService;


	public void save(CommonQrcodeDetail detail){
		commonQrcodeDetailRepository.save(detail);
	}

	public void save(List<CommonQrcodeDetail> details){
		commonQrcodeDetailRepository.save(details);
	}

	/**
	 * 保存通码详情 并生成图片
	 * @param commonQrcode
	 * @param co
	 */
	public void saveDetailAndImg(final CommonQrcode commonQrcode,CommonQrcodeCo co,final String basePath,final int shopId){
		final int count = co.getCount();
		//启动线程
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cachedThreadPool.execute(new Runnable() {
			public void run() {
				try {
						String path  = this.getClass().getResource("/temp/qrcode").getPath();
						File[] srcfile = new File[count];
						int detailId = 0;
						for (int i= 0;i<count;i++){
							CommonQrcodeDetail detail  = new CommonQrcodeDetail();
							detail.setCommonQrcodeId(commonQrcode.getId());
							detail.setProductId(commonQrcode.getProductId());
							commonQrcodeDetailRepository.save(detail);
							detailId = detail.getId();
							//生成二维码
							String url = basePath+"/commonQrcode/entrance/"+detail.getId()+"?shopId="+shopId;
							BitMatrix bi = ZxingUtil.toQRCodeMatrix(url, null, null);
							BufferedImage ImageOne = ZxingUtil.writeToFile(bi);
							File file = new File(path+"/"+detail.getId()+".jpg");
							ImageIO.write(ImageOne, "jpg", file);
							srcfile[i]=file;
						}
						File zipfile = new File(path+"\\3.zip");
						FileUtil.zipFiles(srcfile, zipfile);//压缩文件
						CosResourceDto cos = imageClient.uploadCosRes("qrcode","zip",Toolkit.File2byte(path+"\\3.zip"));//上传zip包
						CosUrlDto data = cos.getData();
						commonQrcode.setZipUrl(ImgUtil.substringUrl(data.getAccessUrl()));
						commonQrcode.setDetailNum(detailId-count+1+"-"+detailId);
						commonQrcodeService.save(commonQrcode);
						FileUtil.delAllFile(path);//清空二维码文件夹底下的文件
				} catch (Exception e) {
						e.printStackTrace();
						log.error(e.getMessage());
				}
			}
		});


	}



	public CommonQrcodeDetail getCommonQrcodeDetail(Integer id){
		return commonQrcodeDetailRepository.findOne(id);
	}


	public List<CommonQrcodeDetail> findCommonQrcodeDetails(List<Integer> ids){
		return commonQrcodeDetailRepository.findAll(ids);
	}


}
