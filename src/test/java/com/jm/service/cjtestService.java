/*
package com.jm.service;

import com.jm.business.service.product.ProductTransPriceService;
import com.jm.business.service.wx.WxIntfServie;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

*/
/**
 * Created by cj on 2016/6/27.
 *//*

public class cjtestService  extends BaseServiceTest{

    @Autowired
    private WxIntfServie wxIntfServie;
    @Autowired
    private ProductTransPriceService productTransPriceService;

   // @Test
    public void test2(){

            try {



                        InputStream imagein2=new FileInputStream("C:\\Users\\cj\\Desktop\\餐巾纸.jpg");
                        InputStream imagein=new FileInputStream("C:\\Users\\cj\\Desktop\\love.jpg");

                        BufferedImage image= ImageIO.read(imagein);
                        BufferedImage image2=ImageIO.read(imagein2);
                        Graphics g=image.getGraphics();
                        g.drawImage(image2,image.getWidth()-image2.getWidth()-15,image.getHeight()-image2.getHeight()-10,image2.getWidth()+10,image2.getHeight()+5,null);
                        OutputStream outImage=new FileOutputStream("C:\\Users\\cj\\Desktop\\"+5+".jpg");
                        JPEGImageEncoder enc= JPEGCodec.createJPEGEncoder(outImage);
                        enc.encode(image);
                        imagein.close();
                        imagein2.close();
                        outImage.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


    }
@Test
    public void test33(){
//    WeixinActInfo info = new WeixinActInfo();
//
//    try {
//        for(int i=0 ; i<46 ;i++){
//            wxIntfServie.isWinActivity(info);
//        }
//
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//    22
//    24
//    26
//    36
//    82
//    ProductTrans pt1 = new ProductTrans();
//    pt1.setPid(22);
//    pt1.setAreaId("350100");
//    pt1.setBuyCount(2);
//    ProductTrans pt2 = new ProductTrans();
//    pt2.setPid(24);
//    pt2.setAreaId("350100");
//    pt2.setBuyCount(2);
//    ProductTrans pt3 = new ProductTrans();
//    pt3.setPid(26);
//    pt3.setAreaId("350100");
//    pt3.setBuyCount(2);
//    List<ProductTrans> list = new ArrayList();
//    list.add(pt1);
//    list.add(pt2);
//    list.add(pt3);
//    productTransPriceService.getTransPrice(list);

}
}
*/
