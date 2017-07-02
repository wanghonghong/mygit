package com.jm.repository.client.dto.auth;

import java.io.Serializable;

import lombok.Data;

/**
 *<p>commonToken</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月20日
 */
@Data
public class CommonAccessToken  implements Serializable  {
	
	private String componentAccessToken;
	
	private Integer expiresIn;

	private Long expiresAt;

}
