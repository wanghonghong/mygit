package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.repository.po.shop.CommonQrcode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>通用商品条码</p>
 */
public interface CommonQrcodeRepository extends JpaRepository<CommonQrcode, Integer> {
	
}
