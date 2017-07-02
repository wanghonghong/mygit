package com.jm.business.service.product;

import java.util.ArrayList;
import java.util.List;

import com.jm.mvc.vo.product.ProductSpecVo;
import com.jm.mvc.vo.product.product.ProductSpecPcVo;
import com.jm.staticcode.converter.product.ProductSpecConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.product.ProductSpecRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductSpec;

/**
 *<p>商品规格service</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月11日
 */
@Service
public class ProductSpecService {
	@Autowired
	ProductSpecRepository specRepository;
	
	/**
	 *<p>根据商品id获取规格</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月11日
	 */
	public List<ProductSpecPcVo> getList(Integer pid){
		List<ProductSpec> productSpecList = specRepository.findProductSpecByPid(pid);
		List<ProductSpecPcVo> productSpecVoList = new ArrayList<ProductSpecPcVo>();
		if (productSpecList != null && productSpecList.size() > 0) {
			for (ProductSpec productSpec : productSpecList) {
				ProductSpecPcVo productSpecVo = ProductSpecConverter.toProductSpecPcVo(productSpec);
				productSpecVoList.add(productSpecVo);
			}
		}
		return productSpecVoList;
	}
	
	public ProductSpec getOneSpec(Integer specId){
		return specRepository.findProductSpecByproductSpecId(specId);
	}
	

	public List<ProductSpec> findAll(Iterable<Integer> ids){
		return specRepository.findAll(ids);
	}

}
