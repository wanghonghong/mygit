package com.jm.business.service.shop;

import com.jm.repository.jpa.shop.GoodsQrcodeEffectRepository;
import com.jm.repository.po.shop.ProductQrcodeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>商品条码效果</p>
 */
@Service
public class GoodsQrcodeEffectService {
	
	@Autowired
	private GoodsQrcodeEffectRepository goodsQrcodeRepository;

    public void save(ProductQrcodeDetail effect){
        goodsQrcodeRepository.save(effect);
    }

}
