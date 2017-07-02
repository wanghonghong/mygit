package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import com.jm.repository.po.system.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCenterCustomRepository extends JpaRepository<UserCenterCustom, Integer>{
    void deleteByShopId(Integer shopId);

    List<UserCenterCustom> findByShopId(Integer shopId);
}
