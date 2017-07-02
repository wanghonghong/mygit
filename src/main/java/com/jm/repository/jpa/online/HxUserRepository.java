package com.jm.repository.jpa.online;


import com.jm.repository.po.online.HxUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HxUserRepository extends JpaRepository<HxUser, Long> {

    HxUser findHxUserByHxId(Integer hxId);

    HxUser findHxUserByHxAccount(String hxAccount);

    HxUser findHxUserByUserId(Integer userId);

}
