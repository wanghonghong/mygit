package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class ZbUserUo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

    private String staffCode; //员工号

    private Integer department; //部门
}
