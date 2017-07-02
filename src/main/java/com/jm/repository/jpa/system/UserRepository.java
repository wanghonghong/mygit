package com.jm.repository.jpa.system;

import com.jm.repository.po.system.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>用户</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByPhoneNumber(String phoneNumber);

    User findUserByPhoneNumberAndPassword(String phoneNumber,String password);

	User findByUserId(Integer userId);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
    
    
    
    @Query(value = " select  u.user_id,u.user_name,u.phone_number,r.role_name,ur.id,u.head_img,ur.role_id "
			+ " from user u "
			+ " left join user_role ur  on ur.user_id = u.user_id  "
    		+ " LEFT JOIN role r on r.role_id  = ur.role_id "
			+ " WHERE ur.shop_id=?1 and ur.level BETWEEN 3 and 8 limit ?2,?3",nativeQuery=true)
	List queryUserList(Integer shopId,Integer page,Integer pageSize);
    
    @Query(value = " select  u.user_id,u.user_name,u.phone_number,r.role_name "
			+ " from user u "
			+ " left join user_role ur  on ur.user_id = u.user_id  "
    		+ " LEFT JOIN role r on r.role_id  = ur.role_id "
			+ " WHERE u.phone_number = ?1 and ur.shop_id=?2  ",nativeQuery=true)
	List queryUserListByPhoneNumber(String phonenumber, Integer shopId);


}
