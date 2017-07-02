package com.jm.staticcode.converter.product;
import com.jm.mvc.vo.product.product.*;
import com.jm.repository.po.product.ProductSpec;
import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ProductSpecConverter {

	public static ProductSpecPcVo toProductSpecPcVo(ProductSpec productSpec) {
		ProductSpecPcVo productSpecPcVo = new ProductSpecPcVo();
		BeanUtils.copyProperties(productSpec, productSpecPcVo);
		if(null !=productSpec.getSpecPic() && !"".equals(productSpec.getSpecPic())){
			productSpecPcVo.setSpecPic(ImgUtil.appendUrl(productSpec.getSpecPic(),100));
		}
		return productSpecPcVo;
	}

	public static ProductSpecPcCo toProductSpecPcCo(ProductSpec productSpec) {
		ProductSpecPcCo productSpecPcCo = new ProductSpecPcCo();
		BeanUtils.copyProperties(productSpec, productSpecPcCo);
		if(null !=productSpec.getSpecPic() && !"".equals(productSpec.getSpecPic())){
			productSpecPcCo.setSpecPic(ImgUtil.appendUrl(productSpec.getSpecPic(),100));
		}
		return productSpecPcCo;
	}

	public static ProductSpec toProductSpec(ProductSpecPcCo productSpecPcCo) {
		ProductSpec productSpec = new ProductSpec();
		BeanUtils.copyProperties(productSpecPcCo, productSpec);
		if(null !=productSpec.getSpecPic() && !"".equals(productSpec.getSpecPic())){
			productSpec.setSpecPic(ImgUtil.substringUrl(productSpec.getSpecPic()));
		}
		return productSpec;
	}

	public static ProductSpec toProductSpec(ProductSpecPcUo productSpecPcUo) {
		ProductSpec productSpec = new ProductSpec();
		BeanUtils.copyProperties(productSpecPcUo, productSpec);
		if(null !=productSpec.getSpecPic() && !"".equals(productSpec.getSpecPic())){
			productSpec.setSpecPic(ImgUtil.substringUrl(productSpec.getSpecPic()));
		}
		return productSpec;
	}

}
