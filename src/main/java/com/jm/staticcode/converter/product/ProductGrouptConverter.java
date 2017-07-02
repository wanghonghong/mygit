package com.jm.staticcode.converter.product;

import com.jm.mvc.vo.product.group.*;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.BeanUtils;

import com.jm.repository.po.product.ProductGroup;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ProductGrouptConverter {

	public static ProductGroupVo toProductGrouptVo(ProductGroup productGroup) {
		ProductGroupVo productGroupVo = new ProductGroupVo();
		//图片转换
		BeanUtils.copyProperties(productGroup, productGroupVo);
		if(StringUtil.isNotNull(productGroup.getGroupImagePath())){
			productGroupVo.setGroupImagePath(ImgUtil.appendUrl(productGroup.getGroupImagePath(),100));
		}
		return productGroupVo;
	}

	public static ProductGroup toProductGroupt(ProductGroupCo productGroupCo) {
		ProductGroup productGroup = new ProductGroup();
		BeanUtils.copyProperties(productGroupCo, productGroup);
		//图片转换
		if(StringUtil.isNotNull(productGroup.getGroupImagePath())){
		productGroup.setGroupImagePath(ImgUtil.substringUrl(productGroup.getGroupImagePath()));
		}
		return productGroup;
	}
	public static ProductGroup toProductGroupt(ProductGroupUo productGroupUo) {
		ProductGroup productGroup = new ProductGroup();
		BeanUtils.copyProperties(productGroupUo, productGroup);
		//图片转换
		if(StringUtil.isNotNull(productGroup.getGroupImagePath())) {
			productGroup.setGroupImagePath(ImgUtil.substringUrl(productGroup.getGroupImagePath()));
		}
		return productGroup;
	}

}
