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
@ApiModel(description = "佣金清单列表查询")
public class BrokerageOrderVo {

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "名字")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "商品总金额")
    private int totalPrice;

    @ApiModelProperty(value = "佣金比例")
    private int brokerage;

    @ApiModelProperty(value = "佣金金额")
    private int commissionPrice;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

    @ApiModelProperty(value = "订单生成时间")
    private Date orderDate;

    @ApiModelProperty(value = "状态")
    private int status;//0未收货，1已收货未满15天，2有效佣金 3退款退货后佣金无效

    @ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
    private int agentRole ;
}
