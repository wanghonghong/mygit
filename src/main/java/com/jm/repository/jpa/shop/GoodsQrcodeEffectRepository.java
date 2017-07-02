package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.ProductQrcodeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>商品条码效果</p>
 */
public interface GoodsQrcodeEffectRepository extends JpaRepository<ProductQrcodeDetail, Integer> {
	
}
