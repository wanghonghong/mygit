package com.jm.repository.jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.product.ProductSpec;
import org.springframework.data.jpa.repository.Query;


/**
 *<p>商品规格</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月11日
 */
public interface ProductSpecRepository extends JpaRepository<ProductSpec, Integer> {
	/**
	 *<p>根据商品id获取规格</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月11日
	 */
	//public List<ProductSpec> findProductSpecByPid(Integer pid);
	@Query( value = "select * from product_spec where pid=?1 and status=0 order by spec_value_one,spec_value_two,spec_value_three",nativeQuery=true)
	List<ProductSpec> findProductSpecByPid(Integer pid);
	/**
	 *<p>根据规格的id获取规格</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月14日
	 */
	public ProductSpec findProductSpecByproductSpecId(Integer productSpecId);

}
