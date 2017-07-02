package com.jm.repository.jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.product.ProductGroup;
import org.springframework.data.jpa.repository.Query;


public interface ProductGroupRepository extends JpaRepository<ProductGroup, Integer>{

	/**
	 * 根据店铺id获取商家分组
	 * @param shopId
	 * @return
	 */

	@Query( value = "select * from product_group where shop_id=?1 order by group_sort",nativeQuery=true)
	List<ProductGroup> findProductGroupByShopId(Integer shopId);
	
}
