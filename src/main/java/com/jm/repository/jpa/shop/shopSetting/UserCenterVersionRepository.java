package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.UserCenterVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 会员中心软件版本
 */
public interface UserCenterVersionRepository extends JpaRepository<UserCenterVersion, Integer>{

    void deleteBySoftId(Integer softId);

    List<UserCenterVersion> findBySoftId(Integer softId);
}
