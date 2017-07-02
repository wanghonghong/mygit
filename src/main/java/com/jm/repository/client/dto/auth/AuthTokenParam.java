package com.jm.repository.client.dto.auth;

import lombok.Data;

/**
 *<p>授权token参数对象</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月2日
 */
@Data
public class AuthTokenParam {
	
	private String code;
	
	private String appid;
	
	private String refreshToken;
	
	private String componentAccessToken;
	
	private String componentAppid;

}
