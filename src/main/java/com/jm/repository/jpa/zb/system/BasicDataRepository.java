package com.jm.repository.jpa.zb.system;


import com.jm.repository.po.zb.user.ZbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ME on 2016/8/17.
 */
public interface BasicDataRepository extends JpaRepository<ZbUser,Integer>{

    List<ZbUser> findZbUserByUserId(Integer userId);

}
