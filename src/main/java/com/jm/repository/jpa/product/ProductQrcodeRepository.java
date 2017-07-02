package com.jm.repository.jpa.product;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.product.ProductQrcode;

import java.util.List;

/**
 * <p>商品二维码</p>
 *
 * @author htp
 * @version latest
 * @date 2016/8/8
 */
public interface ProductQrcodeRepository extends JpaRepository<ProductQrcode, Integer>{
	
	List<ProductQrcode> findProductQrcodeByPidAndShareId(Integer pid, Integer shareId);
}
