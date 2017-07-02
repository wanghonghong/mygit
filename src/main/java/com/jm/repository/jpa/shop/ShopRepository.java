package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>店铺</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */
public interface ShopRepository extends JpaRepository<Shop, Integer> {

	
	@Query(value = " select  s.shop_id,s.shop_name,s.img_url,ur.role_id,r.level,s.app_id,s.status "
			+ " from user u "
			+ " left join user_role ur  on ur.user_id = u.user_id "
    		+ " left join role r on  r.role_id = ur.role_id "
			+ " left join shop  s on ur.shop_id = s.shop_id "
    		+ " where ur.user_id=?1  ",nativeQuery=true)
	List<Object> getShop(Integer userId);
	
	@Query(value = " select  s.* "
			+ " from user u "
			+ " left join user_role ur  on ur.user_id = u.user_id "
    		+ " left join role r on  r.role_id = ur.role_id "
			+ " left join shop  s on ur.shop_id = s.shop_id "
    		+ " where ur.user_id=?1 and s.status=?2 ",nativeQuery=true)
	List<Shop> getShopStatusList(Integer userId,Integer status);

	Shop findShopByShopName(String shopName);

	Shop findShopByShopId(Integer shopId);
	
	Shop findShopByAppId(String appid);

	Shop findShopByAppId_(String appid);

    @Modifying
	@Query(value = "update Shop s set s.showFormatXm=?1 where s.shopId=?2")
	void saveShowFormatXm(Integer showFormat, Integer shopId);

    @Modifying
	@Query(value = "update Shop s set s.showFormatLx=?1 where s.shopId=?2")
	void saveShowFormatLx(Integer showFormat, Integer shopId);

    @Modifying
	@Query(value = "update Shop s set s.showFormatPx=?1 where s.shopId=?2")
	void saveShowFormatPx(Integer showFormat, Integer shopId);

	// cj 2017 -03-06  add
	@Query(value = "select s.* from shop s left join wb_shop_user wb on s.wb_uid = wb.uid where wb.access_token =?1",nativeQuery = true)
	Shop findShopByWbUid(String accessToken);

	/*@Query(value ="SELECT U FROM User LEFT JOIN Shop S WHERE S.appId = ?1 ")
	List<User> findShopUsersByAppid(String appid);*/
}
