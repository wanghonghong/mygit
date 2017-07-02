package com.jm.repository.po.system.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>用户表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;

    private Date updateDate;

    private String phoneNumber;

    private String address;

    private String email;

    private String password;

    private Date createDate;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    @ApiModelProperty(value = "身份证号")
    private String cardNum;

    @ApiModelProperty(value = "身份证照片")
    private String cardNumImg;

    public User(){
        this.createDate = new Date();
    }

}
