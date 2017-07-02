package com.jm.repository.client.dto.auth;

import lombok.Data;

/**
 *<p>commonToken</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月20日
 */
@Data
public class CommonAccessTokenParam {
	
	private String componentAppid;
	
	private String componentAppsecret;
	
	private String componentVerifyTicket;
	
}
