package com.jm.repository.jpa.wx;


import com.jm.repository.po.wx.WxAuth;
import com.jm.repository.po.wx.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>微信权限</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/7/15
 */
public interface WxAuthRepository extends JpaRepository<WxAuth, Integer> {

}
