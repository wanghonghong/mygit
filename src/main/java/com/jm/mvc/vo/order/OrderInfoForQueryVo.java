package com.jm.mvc.vo.order;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "订单")
public class OrderInfoForQueryVo {
	
    @ApiModelProperty(value = "订单标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "用户标识")
    private Integer userId;
    
    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "产品标识")
    private Integer pid;

    @ApiModelProperty(value = "订单名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private Integer price;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "产品类型")
    private Integer type;
    
    @ApiModelProperty(value = "商品数量")
    private Integer count;

    @ApiModelProperty(value = "产品图片")
    private String pic;

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
    
    @ApiModelProperty(value = "微信昵称")
    private String nickname;
    
    @ApiModelProperty(value = "支付流水号")
    private String payOrderNum;

    @ApiModelProperty(value = "微信收款流水号")
    private String transactionId;

    @ApiModelProperty(value = "微信退款流水号")
    private String refundId;
    
    @ApiModelProperty(value = "创建订单日期")
    private Date createDate;
    
    @ApiModelProperty(value = "订单日期")
    private Date orderBeginDate;

    @ApiModelProperty(value = "退货状态 0:未退货, 1:退货中")
    private int goodStatus;

    @ApiModelProperty(value = "订单日期")
    private Date orderEndDate;
    
    @ApiModelProperty(value = "物流费")
    private Integer sendFee;
    
    @ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;
    
    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    public OrderInfoForQueryVo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }
    
}
