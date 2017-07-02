package com.jm.repository.jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.product.Product;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>商品主表</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/23
 */
public interface ProductRepository extends JpaRepository<Product, Integer>{

	@Query(value="select distinct isUseTrans from Product where pid in ?1 ")
	List findByPids(List pids);

	@Query(value="select max(transFare) from Product where pid in ?1")
	Integer findMaxFaceByPids(List pids);

	@Query(value="select distinct transId from Product where pid in ?1 ")
	List findTransIdByPids(List pids);

	@Query(value="select count(1) from Product where  transId = ?1 ")
	Integer findBytransId(Integer templatesId);
}
