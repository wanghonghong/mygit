package com.jm.repository.jpa.shop.activity;

import com.jm.repository.po.shop.activity.EnrolmentUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * <p>报名人员</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 11:25
 */
public interface EnrolmentUserRepository extends JpaRepository<EnrolmentUser, Integer>{

}
