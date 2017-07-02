package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderInfoDetailVo {

    @ApiModelProperty(value = "订单标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "收货地址")
    private String address;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "微信流水号")
    private String transactionId;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;

    @ApiModelProperty(value = "支付日期")
    private Date updatePriceDate;

    @ApiModelProperty(value = "收货日期")
    private Date takeDate;

    @ApiModelProperty(value = "创建订单日期")
    private Date createDate;

    @ApiModelProperty(value = "卖家备注")
    private String sellerNote;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

    @ApiModelProperty(value = "发货时间")
    private Date sendDate;

    @ApiModelProperty(value = "卖家申请退款日期")
    private Date createRefundDate;

    @ApiModelProperty(value = "卖家同意或拒绝退款日期")
    private Date agreeRefundDate;

    @ApiModelProperty(value = "买家退货日期")
    private Date createGoodDate;

    @ApiModelProperty(value = "卖家入库日期")
    private Date storageDate;

    @ApiModelProperty(value = "物流公司代码（code）")
    private String transCode;

    @ApiModelProperty(value = "物流单号")
    private String transNumber;

    @ApiModelProperty(value = "退款状态: 0:申请退款; 1:已退款(同意退款); 2:拒绝退款; ")
    private Integer refundStatus;

    @ApiModelProperty(value = "总金额")
    private Integer totalPrice;

    @ApiModelProperty(value = "物流费")
    private Integer sendFee;

    @ApiModelProperty(value = "总条数")
    private Integer total;

    @ApiModelProperty(value = "物流信息")
    private String transMsg;

    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @ApiModelProperty(value = "收货人姓名")
    private String phoneNumber;

    @ApiModelProperty(value = "收货人地址")
    private String detailAddress;

    @ApiModelProperty(value="实付金额")
    private Integer realPrice;

    @ApiModelProperty(value="订单类型 0:普通订单 1.拼团 2.一元夺宝 3.秒杀 5.体验装 A 6.礼品")
    private Integer type;

    @ApiModelProperty(value = "平台标识 0:微信 1:微博")
    private int platform;

    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetailVo> orderDetails = new ArrayList<>();

    @ApiModelProperty(value = "发货详情列表")
    private List<OrderDeliveryVo> orderDeliveryVo = new ArrayList<>();

}
