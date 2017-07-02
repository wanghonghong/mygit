package com.jm.repository.jpa.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.product.TransTemplatesRelation;

/**
 * <p>运费模板子表</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/13
 */
public interface TransTemplatesRelationRepository extends JpaRepository<TransTemplatesRelation, Integer> {

	@Query(value = "from TransTemplatesRelation where templatesId =? ")
	List<TransTemplatesRelation> findByTempId(int templatesId);

	@Query(value = "from TransTemplatesRelation where templatesId =?1 and  sendAreaId=-1")
	TransTemplatesRelation findByTempIdDefaul(int templatesId);

	@Query(value = "from TransTemplatesRelation  where templatesId in (?1) ")
	List<TransTemplatesRelation> findByTempIds(List templatesIds);

    @Modifying
	@Query(value = "delete from TransTemplatesRelation where templatesId =? ")
	public void deleteTransTempRelationByTempId(int templatesId);

}