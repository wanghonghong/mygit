package com.jm.repository.jpa.wx;


import com.jm.repository.po.wx.WxUserQrcodePoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>微信海报列表/p>
 */
public interface WxUserQrcodePosterRepository extends JpaRepository<WxUserQrcodePoster, Integer> {

    WxUserQrcodePoster findByWxUserQrcodeIdAndQrcodePosterId(Integer wxUserQrcodeId,Integer prcodePosterId);

    @Modifying
    @Query(value = "delete from WxUserQrcodePoster  where wxUserQrcodeId=?1")
    void deleteByUserId(Integer userId);

    List<WxUserQrcodePoster> findByWxUserQrcodeId(Integer userId);
}
