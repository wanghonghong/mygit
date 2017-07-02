package com.jm.repository.jpa.system;

import com.jm.repository.po.wx.WxMenuVisit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>微信菜单访问记录</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/15 11:34
 */


public interface WxMenuAccessCountRepository extends JpaRepository<WxMenuVisit, Long> {
}
