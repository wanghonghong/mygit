package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>分销设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "分销设置")
public class ChannelRecordVo {

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value="渠道流水编号")
    private String orderNum;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    private String userName;

    private String nickname;

    private String headimgurl;

    private String phoneNumber;

    @ApiModelProperty(value = "渠道类型 1：代理商a，2:代理商b，3:代理商c，4:代理商d，5：分销代理a 6：分销代理b，7:分销代理c")
    private Integer agentRole;

    @ApiModelProperty(value = "收费金额")
    private int money;

    @ApiModelProperty(value = "支付时间")
    private Date payDate;

}
