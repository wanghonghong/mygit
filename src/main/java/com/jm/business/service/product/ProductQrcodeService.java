package com.jm.business.service.product;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.jm.repository.po.product.Product;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.common.BitMatrix;
import com.jm.business.service.system.ResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.repository.jpa.product.ProductQrcodeRepository;
import com.jm.repository.po.product.ProductQrcode;

@Service
public class ProductQrcodeService {
	
	@Autowired
	ProductQrcodeRepository repository;
	
    @Autowired
    private ResourceService resourceService;

	public ProductQrcode save(ProductQrcode productQrcode){
		return repository.save(productQrcode);
	}
	
	public List<ProductQrcode> findProductQrcodeByPidAndShareId(Integer pid, Integer shareId){
		return repository.findProductQrcodeByPidAndShareId(pid,shareId);
	}
	
	
	/**
	 *  生成二维码并上传到图片服务器
	 * @param url
	 * @return JmMessage
	 * @throws Exception
	 */
	/*public JmMessage getQrcoce(String url,HttpServletRequest request) throws Exception{
		//生成二维码  
		BitMatrix bi = ZxingUtil.toQRCodeMatrix(url, null, null);
		BufferedImage ImageOne = ZxingUtil.writeToFile(bi);
		Pic pic = new Pic();
		String imgurl= Constant.THIRD_URL+"/our/img/ewmframe.png";
		BufferedImage ImageTwo=pic.loadImageUrl(imgurl);
		//合成图片
		InputStream is = pic.createPicReturnUrl( ImageTwo,ImageOne, 100, 70,250,250);
		JmMessage msg=resourceService.uploadFile(is);
		return msg;
	}*/

	/**
	 *  生成二维码并上传到图片服务器
	 * @param url
	 * @return JmMessage
	 * @throws Exception
	 */
	public JmMessage getQrcoce(String url,Product product) throws Exception{
		Pic pic = new Pic();
		String imgurl= Constant.THIRD_URL+"/our/img/ewmframe.png";//底图路径
		BufferedImage ImageOne=pic.loadImageUrl(imgurl);//底图
		if(product == null){
			return new JmMessage(1, "商品不存在！");
		}
		if(ImageOne == null){
			return new JmMessage(1, "底图不存在！");
		}
		//生成二维码
		BitMatrix bi2 = ZxingUtil.toQRCodeMatrix(url, null, null);
		BufferedImage ImageTwo = ZxingUtil.writeToFile(bi2); // 生成的二维码
		if(ImageTwo == null){
			return new JmMessage(1, "二维码生成失败！");
		}
		String picRectangle = ImgUtil.appendUrl(product.getPicSquare()); //商品图片
		if(product.getPicRectangle()!=null){
			picRectangle = ImgUtil.appendUrl(product.getPicRectangle());
		}
		BufferedImage productImg = pic.loadImageUrl(picRectangle);//商品图
		InputStream is1 = pic.createPicReturnUrl( ImageOne,productImg, 64, 24,430,340);//合成 底图和商品图
		ImageOne = ImageIO.read(is1); // 合成后的 底图和商品图
		InputStream is = pic.createPicReturnUrl( ImageOne,ImageTwo, 160, 620,240,240);//合成 底图和二维码图
		ImageOne = ImageIO.read(is); // 合成后的 底图和二维码图
		String productName = product.getName();
		float price = (float)(product.getPrice())/100;
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数
		String sPrice = df.format(price);//返回的是String类型
		int subStrByetsL = productName.getBytes("GBK").length;//字节长度
		if(subStrByetsL<26){
                /*计算文字的距离 start  263为居中的位置 7表示每个字节所要显示的距离 */
			int a = subStrByetsL-1;
			int b = a * 7;
			int c = 263 - b;
                /*计算文字的距离 end */
			ImageOne = pic.addFont(ImageOne,32,c,484,product.getName(),"宋体",0x3a3a3a);//底图添加商品名
			ImageOne = pic.addFont(ImageOne,32,230,540,"￥"+sPrice,"宋体",0xfd4f2d);//底图添加价格
		}else{
			String str = StringUtil.subStr(productName, 26);//按字节截取字符串
			String str1 = productName.split(str)[1];
                 /*计算文字的距离 start */
			int a = str1.getBytes("GBK").length;
			int b = a * 7;
			int c = 263 - b;
                /*计算文字的距离 end */
			ImageOne = pic.addFont(ImageOne,32,80,450,str,"宋体",0x3a3a3a);//底图添加商品名
			ImageOne = pic.addFont(ImageOne,32,c,500,str1,"宋体",0x3a3a3a);//底图添加商品名
			ImageOne = pic.addFont(ImageOne,32,230,550,"￥"+sPrice,"宋体",0xfd4f2d);//底图添加价格
		}

		InputStream iss = pic.toInputStream(ImageOne);
		JmMessage msg=resourceService.uploadFile(iss);
		return msg;
	}


	/**
	 *  生成二维码并上传到图片服务器
	 * @param url
	 * @return JmMessage
	 * @throws Exception
	 */
	public JmMessage getPcQrcoce(String url) throws Exception{
		//生成二维码
		BitMatrix bi = ZxingUtil.toQRCodeMatrix(url, null, null);
		BufferedImage ImageOne = ZxingUtil.writeToFile(bi);
		Pic pic = new Pic();
		//String imgurl = "f:/pcewmframe.png";
		String imgurl= Constant.THIRD_URL+"/our/img/pcewmframe.png";//底图路径
		BufferedImage ImageTwo=pic.loadImageUrl(imgurl);
		//合成图片
		InputStream is = pic.createPicReturnUrl( ImageTwo,ImageOne, 100, 70,250,250);
		JmMessage msg=resourceService.uploadFile(is);
		return msg;
	}


}
