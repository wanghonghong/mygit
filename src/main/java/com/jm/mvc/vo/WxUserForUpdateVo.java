package com.jm.mvc.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
public class WxUserForUpdateVo {

	 private String phoneNumber;
	
	 private Integer userId;
	 
	 private String remark;
	 
	 private String openId;
}
