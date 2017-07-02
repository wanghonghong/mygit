package com.jm.mvc.vo;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import lombok.Data;

@Data
public class UserAccountVo {
	
	@ApiModelProperty(value = "Id")
    private Integer id;

    @ApiModelProperty(value = "用户编码")
    private Integer userId;

    @ApiModelProperty(value = "帐号类型： 0 现金，1 米币 2 积分")
    private int accountType;

    @ApiModelProperty(value = "账户余额")
    private int balance;

    @ApiModelProperty(value = "账户累计总金额")
    private int totalCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
