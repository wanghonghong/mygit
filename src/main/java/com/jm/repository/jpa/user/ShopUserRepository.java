package com.jm.repository.jpa.user;


import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wx.WxUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * <p>店铺用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/12
 */
public interface ShopUserRepository extends JpaRepository<ShopUser, Integer> {
    ShopUser findByPhoneNumberAndShopId(String phoneNumber,Integer shopId);

    @Query(value = "select * from shop_user where user_name like ?1 ", nativeQuery = true)
    List<ShopUser> findByLikeUserName(String userName);

    @Query(value = "select * from shop_user where phone_number like ?1 ", nativeQuery = true)
    List<ShopUser> findByLikePhoneNumber(String phoneNumber);

    @Query(value = "select * from shop_user where user_name like ?1 and phone_number like ?2 ", nativeQuery = true)
    List<ShopUser> findByLikeUserNameAndPhoneNumber(String userName,String phoneNumber);

    ShopUser findByJmUserId(Integer jmUserId);
}
