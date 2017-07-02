package com.jm.mvc.vo.shop;

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
public class WxUserAccountVo {

    @ApiModelProperty(value = "用户编码")
    private Integer userId;

    @ApiModelProperty(value = "帐号类型： 0 现金，1 佣金帐号 2 积分")
    private int accountType;

    @ApiModelProperty(value = "账户余额")
    private int balance;

    @ApiModelProperty(value = "账户累计总金额")
    private int totalCount;

    @ApiModelProperty(value = "不可提现余额")
    private int noKit;

}
