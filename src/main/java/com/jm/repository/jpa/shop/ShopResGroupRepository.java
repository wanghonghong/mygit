package com.jm.repository.jpa.shop;


import com.jm.repository.po.shop.ShopResGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/13 9:54
 */
public interface ShopResGroupRepository extends JpaRepository<ShopResGroup, Integer> {

	
	@Query(value = " from ShopResGroup u "
    		+ " where u.shopId=?1 and u.resType=?2 order by id ")
	List<ShopResGroup> getShopResGroup(Integer shopId,Integer resType);

	@Query(value = " from ShopResGroup u "
			+ " where u.id=? ")
	ShopResGroup getShopResGroupById (Integer id);

}
