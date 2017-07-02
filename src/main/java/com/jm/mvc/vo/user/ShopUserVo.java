package com.jm.mvc.vo.user;

import lombok.Data;

/**
 * <p>店铺用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/12
 */
@Data
public class ShopUserVo {

	 private String oldPhoneNumber;

	 private String phoneNumber;

	 private String alipay;

	private String password;

	private String code;

	private String userName;
}
