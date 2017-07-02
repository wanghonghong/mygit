package com.jm.repository.jpa.activities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.shop.QrcodePoster;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * <p>二维码海报</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/18
 */
public interface QrcodePosterRepository extends JpaRepository<QrcodePoster, Integer> {

	QrcodePoster findByShopIdAndTypeAndIsDel(Integer shopId,Integer type,int isDel);
	
	//List<QrcodePoster> findQrcodePosterByShopId(Integer shopId);

	List<QrcodePoster> findQrcodePosterByAppId(String shopId);

	List<QrcodePoster> findQrcodePosterByShopIdAndIsDel(Integer shopId,int isDel);

	QrcodePoster findQrcodePosterByAppIdAndType(String appid,Integer type);

	@Modifying
	@Query( value = "update qrcode_poster  set type=0 where shop_id=?1 ",nativeQuery=true)
	void updateTypeByShopId(Integer shopId);
}
