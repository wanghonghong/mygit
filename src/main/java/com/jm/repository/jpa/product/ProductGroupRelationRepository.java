package com.jm.repository.jpa.product;

import com.jm.repository.po.product.ProductGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>商品分类</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/23
 */
public interface ProductGroupRelationRepository extends JpaRepository<ProductGroupRelation, Integer>{

	/**
	 * 根据分组id获取商品分类
	 * @param groupId
	 * @return
	 */
	@Query( value = "select * from product_group_relation where group_id=?1 and status=0 ",nativeQuery=true)
	List<ProductGroupRelation> findProductGroupRelationByGroupId(Integer groupId);

	/**
	 * 根据商品id获取商品分类信息
	 * @param pid
	 * @return
	 */
	List<ProductGroupRelation> findProductGroupRelationByPid(Integer pid);

}
