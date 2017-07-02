package com.jm.repository.po.zb.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>用户表</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/8/23/023
 */
@Data
@Entity
public class ZbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;

    private String phoneNumber;

    private String password;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别 0男  1女 默认男")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    private String staffCode; //员工号

    private Integer department; //部门

    @ApiModelProperty(value = "身份证号")
    private String cardNum;

    @ApiModelProperty(value = "身份证照片")
    private String cardNumImg;

    @ApiModelProperty(value = "岗位")
    private Integer post;

    @ApiModelProperty(value = "状态 1待审核  2已过审核")
    private Integer status;

    @ApiModelProperty(value = "审核通过时间")
    private Date reviewTime;

    private Date createDate;

    public ZbUser(){
        this.createDate = new Date();
    }
}

