package com.jm.business.service.wx;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.ResourceService;
import com.jm.repository.client.ImageClient;
import com.jm.repository.client.WxClient;
import com.jm.repository.jpa.user.ShopUserRepository;
import com.jm.repository.jpa.wx.WxUserQrcodePosterRepository;
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
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

/**
 * <p>微信用户二维码</p>
 */
@Slf4j
@Service
public class WxUserQrcodePosterService {
    @Autowired
    private WxUserQrcodePosterRepository wxUserQrcodePosterRepository;


    public WxUserQrcodePoster findByWxUserQrcodeIdAndQrcodePosterId(Integer wxUserQrcodeId,Integer prcodePosterId){
        return wxUserQrcodePosterRepository.findByWxUserQrcodeIdAndQrcodePosterId(wxUserQrcodeId,prcodePosterId);
    }


    public WxUserQrcodePoster save(WxUserQrcodePoster wxUserQrcodePoster){
        return wxUserQrcodePosterRepository.save(wxUserQrcodePoster);
    }


    @Transactional
    public void deleteByUserId(Integer userId){
        List<WxUserQrcodePoster> wxUserQrcodePosters = wxUserQrcodePosterRepository.findByWxUserQrcodeId(userId);
        for (WxUserQrcodePoster qrcodePoster:wxUserQrcodePosters) {
            ImageClient.delPic(qrcodePoster.getUrl()); //删除图片资源
        }
        wxUserQrcodePosterRepository.deleteByUserId(userId);
    }



}



