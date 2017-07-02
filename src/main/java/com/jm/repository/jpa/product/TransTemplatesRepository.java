package com.jm.repository.jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.product.TransTemplates;

/**
 * <p>运费模板</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/13
 */
public interface TransTemplatesRepository extends JpaRepository<TransTemplates, Integer> {

	@Query(value="from TransTemplates where shopId=?1 order by templatesId desc")
	List<TransTemplates> findByShopId(int shopId);
	
}