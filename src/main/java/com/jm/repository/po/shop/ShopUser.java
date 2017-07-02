package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>店铺用户表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/11
 */
@Data
@Entity
public class ShopUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer shopId;

    private String userName;

    private String phoneNumber;

    private String password;

    @ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
    private int agentRole ;

    @ApiModelProperty(value = "支付宝账号")
    private String alipay;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "通知注册次数")
    private int sendTimes;

    @ApiModelProperty(value = "聚米userId")
    private Integer jmUserId;
    
}
