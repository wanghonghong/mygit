package com.jm.repository.client.dto.auth;

import java.io.Serializable;

import lombok.Data;
/**
 *<p>预授权码</p>
 *
 * @author hantp
 * @version latest
 * @data 2016年6月2日
 */
@Data
public class SetRemarkCode  implements Serializable  {
	
	private Integer errcode;

	private String errmsg;

}
