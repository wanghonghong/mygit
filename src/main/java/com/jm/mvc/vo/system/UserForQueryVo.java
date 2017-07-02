package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author htp
 */

@Data
@ApiModel(description = "用户查询")
public class UserForQueryVo {

	private Integer userId;

	private String userName;

	private Date createDate;

	@ApiModelProperty("手机号码")
	private String phoneNumber;

	@ApiModelProperty(value = "头像")
	private String headImg;

	@ApiModelProperty(value = "性别")
	private Integer sex;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "微信号")
	private String wxnum;

	@ApiModelProperty(value = "身份证号")
	private String cardNum;

	@ApiModelProperty(value = "身份证照片")
	private String cardNumImg;

	public UserForQueryVo(){
		this.createDate = new Date();
	}
}
