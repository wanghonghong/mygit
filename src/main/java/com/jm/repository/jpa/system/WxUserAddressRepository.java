package com.jm.repository.jpa.system;

import com.jm.repository.po.wx.WxUserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>用户</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
public interface WxUserAddressRepository extends JpaRepository<WxUserAddress, Integer> {

	List<WxUserAddress> findUserAddrByUserId(Integer userId);
	
	WxUserAddress findByUserIdAndDefaultShow(Integer userId,int defaultShow);

}
