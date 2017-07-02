package com.jm.repository.client.dto.auth;

import java.io.Serializable;

import lombok.Data;
/**
 *<p>设置用户的备注名称参数</p>
 *
 * @author hantp
 * @version latest
 * @data 2016年6月2日
 */
@Data
public class SetRemarkParam  implements Serializable  {
	
	private String openid;
	
	private String remark;

}
