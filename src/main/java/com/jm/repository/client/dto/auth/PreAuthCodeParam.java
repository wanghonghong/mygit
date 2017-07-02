package com.jm.repository.client.dto.auth;

import java.io.Serializable;

import lombok.Data;
/**
 *<p>预授权码</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月21日
 */
@Data
public class PreAuthCodeParam  implements Serializable  {
	
	private String componentAppid;

}
