package com.jm.staticcode.converter.product;

import com.jm.mvc.vo.product.group.*;
import com.jm.repository.po.product.ProductGroup;
import com.jm.repository.po.product.ProductGroupRelation;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ProductGrouptRelConverter {

	public static ProductGroupRelation toProductGroupRelation(ProductGroupRelationUo productGroupRelationUo) {
		ProductGroupRelation productGroupRelation = new ProductGroupRelation();
		BeanUtils.copyProperties(productGroupRelationUo, productGroupRelation);
		return productGroupRelation;
	}

	public static ProductGroupRelation toProductGroupRelation(ProductGroupRelationCo productGroupRelationCo) {
		ProductGroupRelation productGroupRelation = new ProductGroupRelation();
		BeanUtils.copyProperties(productGroupRelationCo, productGroupRelation);
		return productGroupRelation;
	}

	public static ProductGroupRelationVo toProductGroupRelationVo(ProductGroupRelation productGroupRelation) {
		ProductGroupRelationVo productGroupRelationVo = new ProductGroupRelationVo();
		BeanUtils.copyProperties(productGroupRelation, productGroupRelationVo);
		return productGroupRelationVo;
	}

	public static ProductGroupRelationUo toProductGroupRelationUo(ProductGroupRelation productGroupRelation) {
		ProductGroupRelationUo productGroupRelationUo = new ProductGroupRelationUo();
		BeanUtils.copyProperties(productGroupRelation, productGroupRelationUo);
		return productGroupRelationUo;
	}

    public static ProductGroupRelationCo toProductGroupRelationCo(ProductGroupRelation productGroupRelation) {
		ProductGroupRelationCo productGroupRelationUo = new ProductGroupRelationCo();
		BeanUtils.copyProperties(productGroupRelation, productGroupRelationUo);
		return productGroupRelationUo;
    }
}
