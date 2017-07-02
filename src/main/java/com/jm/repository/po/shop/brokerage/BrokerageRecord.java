package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>佣金收费配置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "佣金流水")
public class BrokerageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "发放流水")
    private String sendNum;

    @ApiModelProperty(value = "发放金额")
    private int sendMoney;

    @ApiModelProperty(value = "发放时间")
    private Date sendTime;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

    @ApiModelProperty(value = "发放类型：1手动发放佣金，2佣金提现,3积分提现")
    private int putType;

    @ApiModelProperty(value = "0:手动发放1:满200,2:定期发放,3:满额发放,4免审核,5需审核,6积分提现")
    private int autoType;

    @ApiModelProperty(value = "红包流水ID")
    private Integer redPayId;

}
