package com.jm.staticcode.util;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import lombok.extern.log4j.Log4j;

@Log4j
public class Pic {

	/*public static void main (String []args) throws IOException {
		*//*BufferedImage srcImg = ImageIO.read(new File("f:/1.png"));
		// 得到画笔对象

		Pic pic = new Pic();
		//BufferedImage bi = ImageIO.read(new File("f:\\1.jpg"));//根据你实际情况改文件路径吧
		String src = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLAPW5ibEFjy4aJPAP57YgbTUIK2q91NJm7MoVWmG95MRyticx11Er1dkticyWIxiatCuvz8pT1UlRuia5w/0";
		BufferedImage headImgBuf =pic.zoomOutImage(pic.makeRoundedCorner(src),200,200);//头像转为圆图 背景透明
		BufferedImage imageBIcon = pic.markImageByIcon(headImgBuf,srcImg,0,100,100);//将头像添加到二位码框
		BufferedImage fontImg = pic.addFont(imageBIcon,40,340,200,"测试文字","楷体",222);//添加文字到二维码框
		ImageIO.write(fontImg, "png", new File("f:\\test.png"));*//*
        //Integer a = Integer.parseInt("22c2fe",16);

		System.out.println(Integer.toHexString(11111234));
	}*/


	
	/**
	 * 导入本地图片到缓冲区
	 */
	public BufferedImage loadImageLocal(String imgName) {
		try {
			return ImageIO.read(new File(imgName));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 导入网络图片到缓冲区
	 */
	public BufferedImage loadImageUrl(String imgName) {
		try {
			URL url = new URL(imgName);
			return ImageIO.read(url);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 导入网路图片地址
     */
	public URLConnection loadImageLinkUrl(String linkUrl) {
		try {
			URL url = new URL(linkUrl);
			return url.openConnection();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 生成新图片到本地
	 */
	public void writeImageLocal(String newImage, BufferedImage img) {
		if (newImage != null && img != null) {
			try {
				File outputfile = new File(newImage);
				ImageIO.write(img, "jpg", outputfile);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	 /**
     * 对图片进行缩小
     * @param originalImage 原始图片
     * @return 缩小后的Image
     */
	public static BufferedImage  zoomOutImage(BufferedImage  originalImage,int width, int height){
	//	int width = 240;
       // int height = 240;
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;

    }
	
	
	/**
	 * 两张图片生成一张指定坐标的图片并上传至腾讯服务器 
	 * @param ImageOne 底图
	 * @param ImageTwo
	 * @param x
	 * @param y
	 * @return 腾讯服务器文件地址
	 */
	public InputStream createPicReturnUrl(BufferedImage ImageOne, BufferedImage ImageTwo,int x,int y,int zoomWidth, int zoomHeight) throws Exception {
	    //读取第一张图片
	    int width = ImageOne.getWidth();//图片宽度
	    int height = ImageOne.getHeight();//图片高度
	    //从图片中读取RGB
	    int[] ImageArrayOne = new int[width*height];
	    ImageArrayOne = ImageOne.getRGB(0,0,width,height,ImageArrayOne,0,width);

		//对第二张图片做相同的处理
		ImageTwo= zoomOutImage(ImageTwo,zoomWidth,zoomHeight);
		int widthTwo = ImageTwo.getWidth();//图片宽度
		int heightTwo = ImageTwo.getHeight();//图片高度
		int[] ImageArrayTwo = new int[widthTwo*heightTwo];
		ImageArrayTwo = ImageTwo.getRGB(0,0,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);
	       
	    //生成新图片
	    BufferedImage ImageNew = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	    ImageNew.setRGB(0,0,width,height,ImageArrayOne,0,width);//设置左半部分的RGB
	    ImageNew.setRGB(x,y,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);//设置右半部分的RGB

		//将生成的图片转为InputStream
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageNew, "jpg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
	     return is;
	 }


	/**
	 * 给图片添加水印、可设置水印图片旋转角度
	 * @param iconPath 水印图片路径
	 * @param srcImgPath 源图片路径
	 * @param degree 水印图片旋转角度
	 * @param x 水印位置
	 * @param y 水印位置
	 */
	public static BufferedImage markImageByIcon(Image iconPath, BufferedImage srcImgPath, Integer degree,int x,int y) {
		BufferedImage buffImg = null;
		try {
			Image srcImg = srcImgPath;
			buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

			if (null != degree) {
				g.rotate(Math.toRadians(degree),(double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);// 设置水印旋转
			}
			// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);
			// 得到Image对象。
			Image img = imgIcon.getImage();
			float alpha = 0.9f; // 透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
			// 表示水印图片的位置
			g.drawImage(img, x , y, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffImg;
	}


    /**
     * 将头像转为圆形并将圆形背景透明
     * @param headimgurl 头像地址
     * @throws IOException
     */
    public static BufferedImage makeRoundedCorner(String headimgurl) throws IOException {
        URL url = new URL(headimgurl);
        BufferedImage bi1 = ImageIO.read(url);
        // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
        BufferedImage image = new BufferedImage(bi1.getWidth(), bi1.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = image.createGraphics();
        image = g2.getDeviceConfiguration().createCompatibleImage(bi1.getWidth(), bi1.getHeight(), Transparency.TRANSLUCENT);
        g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fill(new Rectangle(image.getWidth(), image.getHeight()));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        g2.dispose();
        return image;

    }


	/**
	 * 给图片添加文字
	 * @param buffImage
	 * @param fontSize 字体大小
	 * @param x 位置
	 * @param y 位置
	 * @param content 文字内容
	 * @param fontType 文字体
	 * @return
	 */
	public static BufferedImage addFont(BufferedImage buffImage,int fontSize,int x,int y,String content,String fontType,int fontColor){
		Graphics g = buffImage.getGraphics();
		Font font = new Font(fontType,Font.PLAIN,fontSize);
		g.setColor(new Color(fontColor));
		g.setFont(font);
		g.drawString(content, x, y);//内容 坐标
		return buffImage;
	}

	/**
	 * 将图片转为InputStream
	 */
	public static InputStream toInputStream(BufferedImage ImageNew) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageNew, "jpg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	/**
	 * 裁剪图片方法
	 * @param bufferedImage 图像源
	 * @param startX 裁剪开始x坐标
	 * @param startY 裁剪开始y坐标
	 * @param endX 裁剪结束x坐标
	 * @param endY 裁剪结束y坐标
	 * @return
	 */
	public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		if (startX == -1) {
			startX = 0;
		}
		if (startY == -1) {
			startY = 0;
		}
		if (endX == -1) {
			endX = width - 1;
		}
		if (endY == -1) {
			endY = height - 1;
		}
		BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
		for (int x = startX; x < endX; ++x) {
			for (int y = startY; y < endY; ++y) {
				int rgb = bufferedImage.getRGB(x, y);
				result.setRGB(x - startX, y - startY, rgb);
			}
		}
		return result;
	}

}
