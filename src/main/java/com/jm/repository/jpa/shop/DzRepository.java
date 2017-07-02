package com.jm.repository.jpa.shop;

import com.jm.repository.po.shop.imageText.DzUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DzRepository extends JpaRepository<DzUser,Integer> {

    @Query("select a from DzUser a where a.imageTextId=?1 and a.openId=?2 ")
    List<DzUser> findByOpenId(Integer id, String openId);
}
