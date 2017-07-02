package com.jm.repository.jpa.shop.shopSetting;

import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**商城搭建
 * wenwen
 * 2016年9月12日15:35:03
 */
public interface UserCenterFunsRepository extends JpaRepository<UserCenterFuns, Integer>{

    @Query(value="select * from user_center_funs order by sort" ,nativeQuery = true)
    List<UserCenterFuns> findUserCenterFunsList();

    @Query(value="select * from user_center_funs where status=?1 " ,nativeQuery = true)
    List<UserCenterFuns> findUserCenterFunsList(Integer status);

    @Query(value="select * from user_center_funs where fun_id in(?1) " ,nativeQuery = true)
    List<UserCenterFuns> findByFunId(List<Integer> ids);

}
