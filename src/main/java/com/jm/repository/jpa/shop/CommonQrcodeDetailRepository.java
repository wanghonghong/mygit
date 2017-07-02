package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.CommonQrcode;
import com.jm.repository.po.shop.CommonQrcodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>通用商品条码明细</p>
 */
public interface CommonQrcodeDetailRepository extends JpaRepository<CommonQrcodeDetail, Integer> {
	
}
