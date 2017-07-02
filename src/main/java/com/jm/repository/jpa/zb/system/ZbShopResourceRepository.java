package com.jm.repository.jpa.zb.system;



import com.jm.repository.po.zb.system.ZbShopResource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 店铺资源
 * @author: cj
 * @date: 2016-6-12
 */
public interface ZbShopResourceRepository extends JpaRepository<ZbShopResource, Integer> {

	
	/*@Query(value = " from ZbShopResource u "
    		+ " where u.shopId=? and u.resType=? and isDel=? order by id desc")
	List getShopResource(Integer shopId, Integer resType, Integer isDel);

	@Query(value = " from ZbShopResource u "
    		+ " where u.id=? ")
	ZbShopResource getShopResourceById(Integer id);

	Page<ZbShopResource> findAll(Specification<ZbShopResource> spec, Pageable pageable);

    @Modifying
	@Query(value = "update ZbShopResource set resGroupId = 0 where resGroupId=?1")
	void updateShopResByResGroupId(Integer resGroupId);

	@Modifying
	@Query(value="update ZbShopResource set resGroupId = ?2 where id in(?1)")
	void updateShopResByIds(List<Integer> shopResIdList, Integer resGroupId);

	@Query(value = "select distinct(resName) from ZbShopResource where resName like ?1 and shopId=?2 and isDel=0")
	List searchRes(String searchName, Integer shopId);*/
}
