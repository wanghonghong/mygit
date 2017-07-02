package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class UserForStaffUo {

    private Integer userId;

    private String userName;

//    private String phoneNumber;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    private String staffCode; //员工号

    private Integer department; //部门

    private Integer post;//岗位

    private Integer roleId;//角色
}
