package com.jm.repository.jpa.wx;

import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.wx.ImgTextMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>图文消息</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/9/9
 */
public interface ImgTextMsgRepository extends JpaRepository<ImgTextMsg, Integer> {
    ImgTextMsg findByAppidAndOpenid(String appid,String openid);
}
