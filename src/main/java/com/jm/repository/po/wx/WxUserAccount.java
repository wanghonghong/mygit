package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户帐号表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/15
 */
@Data
@Entity
public class WxUserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "用户编码")
    private Integer userId;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "帐号类型： 1 佣金帐号 2 积分")
    private int accountType;

    @ApiModelProperty(value = "账户总余额")
    private int totalBalance;

    @ApiModelProperty(value = "可提现余额")
    private int kitBalance;

    @ApiModelProperty(value = "账户累计总金额")
    private int totalCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public WxUserAccount(){
        this.createTime = new Date();
    }

}
