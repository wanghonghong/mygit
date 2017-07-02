package com.jm.business.service.wx;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.ResourceService;
import com.jm.repository.client.WxClient;
import com.jm.repository.jpa.user.ShopUserRepository;
import com.jm.repository.jpa.wx.WxUserQrcodeRepository;
import com.jm.repository.po.shop.QrcodePoster;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserQrcode;
import com.jm.repository.po.wx.WxUserQrcodePoster;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Pic;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * <p>微信用户二维码</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/11.18
 */
@Slf4j
@Service
public class WxUserQrcodeService {
    @Autowired
    private WxQrcodeService qrcodePosterService;
    @Autowired
    private WxUserQrcodeRepository wxUserQrcodeRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private ShopUserRepository shopUserRepository;
    @Autowired
    private WxClient wxClient;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WxUserQrcodePosterService wxUserQrcodePosterService;

    /**
     * 生成用户基础二维码
     * @param wxUser
     * @param type 1：二维码海报  2：乐享图文 3：内容营销
     * @param  imgTextType 乐享图文二维码版式
     * @return
     * @throws Exception
     */
    public synchronized String getBaseQrcode(WxUser wxUser,int type,int imgTextType) throws Exception {
        if(wxUser==null){
            return "";
        }
        //==========================用户基础二维码===========================
        String baseUrlCos = "";//根据Type返回的二维码地址
        boolean isExpire = false;//基础二维码是否过期过
        int flag = 0;
        if (wxUser.getShopUserId()!=null){
            ShopUser shopUser = shopUserRepository.findOne(wxUser.getShopUserId());
            if(shopUser!=null){
                if (shopUser.getAgentRole()>0){
                    flag = 1;
                }
            }
        }
        List<WxUserQrcode> wxUserQrcodes =  wxUserQrcodeRepository.findByUserId(wxUser.getUserId());
        WxUserQrcode wxUserQrcode;
        //1.没有创建过二维码
        if(wxUserQrcodes == null || wxUserQrcodes.size() == 0){
            String accessToken = wxAuthService.getAuthAccessToken(wxUser.getAppid());
            String baseQrcode = wxClient.getTicket(accessToken,flag,wxUser.getUserId());//生成基础二维码
            baseUrlCos = resourceService.uploadUrlImg(baseQrcode); //上传到万象优图
            wxUserQrcode = new WxUserQrcode();
            wxUserQrcode.setBaseQrcode(baseUrlCos);
            wxUserQrcode.setUserId(wxUser.getUserId());
            isExpire = true;
        }else{
            wxUserQrcode = wxUserQrcodes.get(0);
            //需要永久二维码
            if(flag==1){
                if(StringUtils.isEmpty(wxUserQrcode.getBaseQrcode()) || wxUserQrcode.getType()==0){ //没有生成永久二维码
                    String accessToken = wxAuthService.getAuthAccessToken(wxUser.getAppid());
                    String baseQrcode = wxClient.getTicket(accessToken,flag,wxUser.getUserId());//生成基础二维码
                    baseUrlCos = resourceService.uploadUrlImg(baseQrcode); //上传到万象优图
                    wxUserQrcode.setBaseQrcode(baseUrlCos);
                    isExpire = true;
                }
            }else{
                //2.二维码过期或者二维码不存在
                if(StringUtils.isEmpty(wxUserQrcode.getBaseQrcode())||( wxUserQrcode.getType()==0 &&(System.currentTimeMillis()/1000)>wxUserQrcode.getExpiresAt() )) {
                    String accessToken = wxAuthService.getAuthAccessToken(wxUser.getAppid());
                    String baseQrcode = wxClient.getTicket(accessToken,flag,wxUser.getUserId());//生成基础二维码
                    baseUrlCos = resourceService.uploadUrlImg(baseQrcode); //上传到万象优图
                    wxUserQrcode.setBaseQrcode(baseUrlCos);
                    wxUserQrcodePosterService.deleteByUserId(wxUser.getUserId()); //删除已经生成的二维码海报
                    isExpire = true;
                }
            }
        }
        if(isExpire){
            if (flag==0){
                wxUserQrcode.setExpiresAt((int) (2300000+System.currentTimeMillis()/1000)); //当前时间29天后的时间
            }else{ //代理商使用永久二维码
                wxUserQrcode.setType(1);
            }
        }
        //==========================用户基础二维码 end===========================
        if(type == 1){ //  二维码海报===============================
            if(isExpire){
                save(wxUserQrcode,wxUser.getUserId());
            }
            baseUrlCos = getDefaultUserQrcode(wxUserQrcode,wxUser,isExpire);
            return baseUrlCos;
        }
        else if (type == 2){//  乐享图文二维码===============================
            if(imgTextType==2){
                if (StringUtils.isEmpty(wxUserQrcode.getImgTextQrcode())||isExpire) {//基础二维码海报过期 或 乐享图文二维码不存在
                    baseUrlCos = getImgTextQrcode(wxUserQrcode);
                    isExpire = true;
                }else{
                    baseUrlCos = wxUserQrcode.getImgTextQrcode();
                }
            }
        }
        else if (type == 3){  //   内容营销二维码===============================
            if(imgTextType==1){
                baseUrlCos = wxUserQrcode.getBaseQrcode();
            }else if(imgTextType==2){
                if (StringUtils.isEmpty(wxUserQrcode.getImgTextQrcode())||isExpire) {//基础二维码海报过期 或 乐享图文二维码不存在  注:内容营销1，2 版式和乐享图文一样
                    baseUrlCos = getImgTextQrcode(wxUserQrcode);
                    isExpire = true;
                }else{
                    baseUrlCos = wxUserQrcode.getImgTextQrcode();
                }
            }else if(imgTextType==3){
                if (StringUtils.isEmpty(wxUserQrcode.getContentQrcode())||isExpire) {//基础二维码海报过期 或 内容营销二维码不存在  注:内容营销1，2 版式和乐享图文一样
                    baseUrlCos = getContentQrcode(wxUserQrcode,wxUser);
                    isExpire = true;
                }else{
                    baseUrlCos = wxUserQrcode.getContentQrcode();
                }
            }
        }
        if(baseUrlCos.equals("")){
            baseUrlCos = wxUserQrcode.getBaseQrcode();
        }
        if(isExpire){
            save(wxUserQrcode,wxUserQrcode.getUserId());
        }
        return baseUrlCos;
    }


    /**
     * 获取用户二维码海报
     */
    private String getDefaultUserQrcode(WxUserQrcode wxUserQrcode,WxUser wxUser,boolean isExpire) throws Exception {
        if(wxUserQrcode ==null){
            return "";
        }
        if(wxUser ==null){
            return "";
        }
        Shop shop=shopService.getShopByAppId(wxUser.getAppid());
        if(shop ==null){
            return "";
        }
        int shopId =shop.getShopId();
        String baseQrcode = getBaseQrcode(wxUser,0,0);//基础二维码地址
        String defulUserQrcode = baseQrcode;
        QrcodePoster qrcodePoster=qrcodePosterService.getDefault(shopId);//1.获取默认二维码海报的框
        if(qrcodePoster!=null){
            WxUserQrcodePoster wxUserQrcodePoster = wxUserQrcodePosterService.findByWxUserQrcodeIdAndQrcodePosterId(wxUserQrcode.getUserId(),qrcodePoster.getQrcodeId());
                if(wxUserQrcodePoster !=null ){//不存在
                    boolean isBreak = false ;
                    if(wxUserQrcodePoster.getCreateTime()!=null && qrcodePoster.getUploadTime()!=null){
                        isBreak = compareDate(wxUserQrcodePoster.getCreateTime(),qrcodePoster.getUploadTime());
                    }
                    if(isBreak && isExpire == false){ //二维码底图没有更新 且 基础二维码没有过期
                        return wxUserQrcodePoster.getUrl();
                    }
                }else{
                    wxUserQrcodePoster = new WxUserQrcodePoster();
                }

                String imagesrc=qrcodePoster.getImageSrc(); //图片地址
                if(StringUtils.isNotEmpty(imagesrc)){
                    Pic pic = new Pic();
                    BufferedImage imageOne=pic.loadImageUrl(ImgUtil.appendUrl(imagesrc,0));//底图
                    BufferedImage imageTwo=pic.loadImageUrl(ImgUtil.appendUrl(baseQrcode,0));//基础二维码
                 /* 裁剪图片*/
                    imageTwo= pic.cropImage(imageTwo,25,25,405,405);
                    if (imageOne!=null && imageTwo!=null){
                        String nickName = Base64Util.getFromBase64(wxUser.getNickname());
                        double a = 2.5;//pc端页面显示倍数
                        double b = 2.5;//pc端页面显示倍数
                        int leftPosition =(int) ( Toolkit.parseObjForInt(qrcodePoster.getLeftPosition())*a );
                        int topPosition = (int) ( Toolkit.parseObjForInt(qrcodePoster.getTopPosition())*b );
                        InputStream is = pic.createPicReturnUrl(imageOne, imageTwo,leftPosition ,topPosition ,180,180);//合成图片
                        InputStream uploadIs = is;
                        if(qrcodePoster.getCodeFormat()==1){//二维码B版
                            BufferedImage bi = ImageIO.read(is);
                            int userBoxleftPosition =(int) ( Toolkit.parseObjForInt(qrcodePoster.getUserBoxleftPosition())*a )+30;//头像位置
                            int userBoxtopPosition = (int) ( Toolkit.parseObjForInt(qrcodePoster.getUserBoxtopPosition())*b )+33;//头像位置
                            int nickNameleftPosition= (int) ( Toolkit.parseObjForInt(qrcodePoster.getNickNameleftPosition())*a);//昵称位置
                            int nickNametopPosition= (int) ( Toolkit.parseObjForInt(qrcodePoster.getNickNametopPosition())*b)+30;//昵称位置
                            BufferedImage headImgBuf =  pic.zoomOutImage(pic.makeRoundedCorner(wxUser.getHeadimgurl()),165,165);//头像转为圆图 背景透明
                            BufferedImage imageBIcon = pic.markImageByIcon(headImgBuf,bi,0,userBoxleftPosition,userBoxtopPosition);//将头像添加到二位码框
                            BufferedImage fontImg = pic.addFont(imageBIcon,36,nickNameleftPosition,nickNametopPosition,nickName,qrcodePoster.getFontType(),qrcodePoster.getFontColor());//添加文字到二维码框
                            uploadIs = pic.toInputStream(fontImg);
                        }

                        defulUserQrcode = resourceService.uploadUrlImg(uploadIs);
                        wxUserQrcodePoster.setCreateTime(new java.util.Date());
                        wxUserQrcodePoster.setQrcodePosterId(qrcodePoster.getQrcodeId());
                        wxUserQrcodePoster.setUrl(defulUserQrcode);
                        wxUserQrcodePoster.setWxUserQrcodeId(wxUserQrcode.getUserId());
                        wxUserQrcodePosterService.save(wxUserQrcodePoster);
                    }
                }
        }
        return defulUserQrcode;
    }

    /**
     * 合成乐享图文二维码
     * @param wxUserQrcode
     * @return
     * @throws Exception
     */
    private String getImgTextQrcode(WxUserQrcode wxUserQrcode) throws Exception {
        //合成
        Pic pic = new Pic();
        BufferedImage ImageOne=pic.loadImageUrl(Constant.THIRD_URL+"/our/img/img_text_qrcode.png");
        BufferedImage ImageTwo=pic.loadImageUrl(ImgUtil.appendUrl(wxUserQrcode.getBaseQrcode(),0));//基础二维码
        //合成图片
        InputStream is = pic.createPicReturnUrl(ImageOne, ImageTwo, 0,0,200,200);
        String baseUrlCos = resourceService.uploadUrlImg(is); //上传到万象优图
        wxUserQrcode.setImgTextQrcode(baseUrlCos);
        return baseUrlCos;
    }

    /**
     * 合成内容营销二维码
     * @param wxUserQrcode
     * @return
     * @throws Exception
     */
    private String getContentQrcode(WxUserQrcode wxUserQrcode,WxUser wxUser) throws Exception {
        if(wxUserQrcode ==null){
            return "";
        }
        if(wxUser ==null){
            return "";
        }
        Shop shop=shopService.getShopByAppId(wxUser.getAppid());
        if(shop ==null){
            return "";
        }
        if (shop.getImgUrl()==null || shop.getImgUrl().equals("")){
            return "";
        }

        String imgUrl = ImgUtil.appendUrl(shop.getImgUrl(),300) ;//店铺主图 logo
        //合成
        Pic pic = new Pic();
        BufferedImage ImageOne=pic.loadImageUrl(Constant.THIRD_URL+"/our/img/content_qrcode.png");
        BufferedImage logo =  pic.zoomOutImage(pic.makeRoundedCorner(imgUrl),200,200);//将店铺logo转为圆形背景透明
        BufferedImage is = pic.markImageByIcon(logo,ImageOne,0,0,0);//将logo添加到 主图中

        BufferedImage qrcode=pic.loadImageUrl(ImgUtil.appendUrl(wxUserQrcode.getBaseQrcode(),0));//基础二维码
        InputStream in = pic.createPicReturnUrl(is, qrcode, 275,0,200,200);//将二维码添加到 主图中

        String baseUrlCos = resourceService.uploadUrlImg(in); //上传到万象优图
        wxUserQrcode.setContentQrcode(baseUrlCos);
        return baseUrlCos;
    }




    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(java.util.Date d1, java.util.Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }


    /**
     * 合成二维码
     * @param qrcodePoster 底图类
     * @param qrcode 二维码图
     * @return
     * @throws Exception
     */
    public String composeQrcode(QrcodePoster qrcodePoster,String qrcode) throws Exception {
        String qrcodeUrl = "";
        if(qrcodePoster!=null){
            String imagesrc=qrcodePoster.getImageSrc(); //图片地址
            if(StringUtils.isNotEmpty(imagesrc)){
                Pic pic = new Pic();
                BufferedImage imageOne=pic.loadImageUrl(ImgUtil.appendUrl(imagesrc,0));//底图
                BufferedImage imageTwo=pic.loadImageUrl(qrcode);//基础二维码
                 /* 裁剪图片*/
                imageTwo= pic.cropImage(imageTwo,25,25,405,405);
                if (imageOne!=null && imageTwo!=null){
                    double a = 2.5;//pc端页面显示倍数
                    double b = 2.5;//pc端页面显示倍数
                    int leftPosition =(int) ( Toolkit.parseObjForInt(qrcodePoster.getLeftPosition())*a );
                    int topPosition = (int) ( Toolkit.parseObjForInt(qrcodePoster.getTopPosition())*b );
                    InputStream is = pic.createPicReturnUrl(imageOne, imageTwo,leftPosition ,topPosition ,180,180);//合成图片
                    InputStream uploadIs = is;
                    qrcodeUrl = resourceService.uploadUrlImg(uploadIs);
                }
            }
        }
        return qrcodeUrl;
    }


    public WxUserQrcode save(WxUserQrcode wxUserQrcode,Integer userId) {
        if(wxUserQrcode == null ){
            return null;
        }
        if(userId == null ){
            return null;
        }

        List<WxUserQrcode> oldwxUserQrcodes = wxUserQrcodeRepository.findByUserId(userId);
        WxUserQrcode oldwxUserQrcode = null ;
        if(oldwxUserQrcodes!=null && oldwxUserQrcodes.size()>0){
            oldwxUserQrcode = oldwxUserQrcodes.get(0);
        }
        if(oldwxUserQrcode == null){
                return wxUserQrcodeRepository.save(wxUserQrcode);
        }else{
            if(wxUserQrcode.getUserId()!=null){
                if(wxUserQrcode.getUserId().equals(oldwxUserQrcode.getUserId())){
                     return wxUserQrcodeRepository.save(wxUserQrcode);
                }
            }
        }
        return null;
    }

}



