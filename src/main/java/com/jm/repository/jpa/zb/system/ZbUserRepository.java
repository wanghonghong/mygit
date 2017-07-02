package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.user.ZbUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ME on 2016/8/17.
 */
public interface ZbUserRepository extends JpaRepository<ZbUser,Integer>{

    ZbUser findZbUserByPhoneNumberAndPassword(String phoneNumber, String password);

    ZbUser findZbUserByPhoneNumber(String phoneNumber);

    Page<ZbUser> findAll(Specification<ZbUser> spec, Pageable pageable);

    @Query(value = "select * from zb_user where status = 2  order by review_time desc limit 1 ", nativeQuery = true)
    ZbUser queryStaffCodeByReviewTime();

    @Modifying
    @Query(value = "delete from zb_user where user_id in (?1)", nativeQuery = true)
    void deleteByIds(List<Integer> ids);

    List<ZbUser> findByDepartment(int department);

    List<ZbUser> findByPost(int post);

}
