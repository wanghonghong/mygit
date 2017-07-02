package com.jm.repository.po.system.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>聚米用户帐号表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
@Data
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    public UserAccount(){
    	this.createTime = new Date();
    }

}
