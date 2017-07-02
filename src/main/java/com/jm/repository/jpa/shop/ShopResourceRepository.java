package com.jm.repository.jpa.shop;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.shop.ShopResource;

import java.util.List;

/**
 * 店铺资源
 * @author: cj
 * @date: 2016-6-12
 */
public interface ShopResourceRepository extends JpaRepository<ShopResource, Integer> {

	
	@Query(value = " from ShopResource u "
    		+ " where u.shopId=? and u.resType=? and isDel=? order by id desc")
	List getShopResource(Integer shopId, Integer resType ,Integer isDel);
	
	@Query(value = " from ShopResource u "
    		+ " where u.id=? ")
	ShopResource getShopResourceById (Integer id);

	Page<ShopResource> findAll(Specification<ShopResource> spec, Pageable pageable);

    @Modifying
	@Query(value = "update ShopResource set resGroupId = 0 where resGroupId=?1")
	void updateShopResByResGroupId(Integer resGroupId);

	@Modifying
	@Query(value="update ShopResource set resGroupId = ?2 where id in(?1)")
	void updateShopResByIds(List<Integer> shopResIdList, Integer resGroupId);

	@Query(value = "select distinct(resName) from ShopResource where resName like ?1 and shopId=?2 and isDel=0")
	List searchRes(String searchName, Integer shopId);
}
