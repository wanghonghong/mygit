package com.jm.mvc.vo.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderInfoVo {
	
    @ApiModelProperty(value = "订单标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;
    
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "状态 0：待付款; 1:待发货（已付款）; 2:待收货（已发货）; 3:已完成; 4:已关闭; 5:退款中")
    private Integer status;

    @ApiModelProperty(value = "付费类型")
    private String feeType;
    
    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;
    
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;
    
    @ApiModelProperty(value = "收货地址")
    private String address; 
    
    @ApiModelProperty(value = "卖家备注")
    private String sellerNote;
    
    @ApiModelProperty(value = "总金额")
    private Integer totalPrice;
    
    @ApiModelProperty(value = "总数量")
    private Integer totalCount;
    
    @ApiModelProperty(value = "微信昵称")
    private String nickname;
    
    @ApiModelProperty(value = "微信流水号")
    private String transactionId;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;

    @ApiModelProperty(value = "收货日期")
    private Date takeDate;
    
    @ApiModelProperty(value = "创建订单日期")
    private Date createDate;

    @ApiModelProperty(value = "发货日期")
    private Date sendDate;
    
    @ApiModelProperty(value = "物流费")
    private Integer sendFee;
    
    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "总条数")
    private Integer total;

    @ApiModelProperty(value = "退货状态 0:未退货, 1:退货中")
    private int goodStatus;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

    @ApiModelProperty("退款流水号")
    private String refundId;

    @ApiModelProperty(value = "黄信账号")
    private String hxAccount;

    @ApiModelProperty(value = "微信头像")
    private String headimgurl;

    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @ApiModelProperty(value = "收货人姓名")
    private String phoneNumber;

    @ApiModelProperty(value = "收货人地址")
    private String detailAddress;

    @ApiModelProperty(value = "优惠积分")
    private Integer benefits;

    @ApiModelProperty(value = "优惠券")
    private Integer coupon;

    @ApiModelProperty(value="分销商折扣")
    private Integer discount;

    @ApiModelProperty(value="实付金额")
    private Integer realPrice;

    @ApiModelProperty(value = "订单详情列表")
	private List<OrderDetailVo> orderDetails = new ArrayList<>();

    @ApiModelProperty(value="订单类型 0:普通订单 1.拼团 2.一元夺宝 3.秒杀 5.体验装 A 6.礼品")
    private Integer type;

    @ApiModelProperty(value="主单id")
    private Long parentOrderId;

    @ApiModelProperty(value="礼品单已收集个数")
    private int collectCount;

    @ApiModelProperty(value = "用户备注")
    private String wxRemark;

    @ApiModelProperty(value = "平台标识 0:微信 1:微博")
    private int platform;

    @ApiModelProperty(value = "微博用户uid")
    private Long uid;

    public OrderInfoVo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }
    
}
