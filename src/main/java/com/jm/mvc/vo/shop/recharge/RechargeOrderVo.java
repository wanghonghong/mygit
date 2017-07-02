package com.jm.mvc.vo.shop.recharge;

import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/2
 */
@Data
@ApiModel(description = "充值订单信息")
public class RechargeOrderVo {

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "充值金额")
    private Integer money;

    @ApiModelProperty(value = "支付日期")
    private Date createDate;
    
}
