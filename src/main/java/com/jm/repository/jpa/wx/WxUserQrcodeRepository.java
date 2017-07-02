package com.jm.repository.jpa.wx;


import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserQrcode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * <p>微信用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/18
 */
public interface WxUserQrcodeRepository extends JpaRepository<WxUserQrcode, Integer> {

    List<WxUserQrcode> findByUserId(Integer userId);
}
