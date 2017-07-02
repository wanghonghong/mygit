package com.jm.business.service.shop.imageText;

import com.jm.repository.jpa.wx.ImgTextMsgRepository;
import com.jm.repository.po.wx.ImgTextMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>图文消息</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/9/9
 */
@Service
public class ImgTextMsgService {

    @Autowired
    private ImgTextMsgRepository imgTextMsgRepository;
    
    public ImgTextMsg save(ImgTextMsg msg) {
        return imgTextMsgRepository.save(msg);
    }

    public ImgTextMsg findMsgbyAppidAndOpenId(String appid,String openid) {
        return imgTextMsgRepository.findByAppidAndOpenid(appid,openid);
    }

    public void del(int id){
        imgTextMsgRepository.delete(id);
    }

}
