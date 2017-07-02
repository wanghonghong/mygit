package com.jm.mvc.vo.order;

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
@ApiModel(description = "订单详情+商品+商品规格+退款")
public class OrderDetailVo {

	//**************商品列表*******************//
	@ApiModelProperty(value = "产品标识")
    private Integer pid;

    @ApiModelProperty(value = "产品名称")
    private String name;
    
    @ApiModelProperty(value = "产品图片")
    private String pic;

    //**************订单详情列表*******************//
    @ApiModelProperty(value = "价格")
    private Integer price;
    
    @ApiModelProperty(value = "商品数量")
    private Integer count;
	
    
    //**************规格列表*******************//
    @ApiModelProperty(value = "规格id")
    private Integer productSpecId;

    @ApiModelProperty(value = "规格图片")
    private String specPic;

	@ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;

    //***************退单列表**********************//
    @ApiModelProperty(value = "退款单状态")
    private Integer refundStatus;

    @ApiModelProperty(value = "拒绝退款原因")
    private String refuseReason;

    //****************退货列表*********************//

    @ApiModelProperty(value = "退货单Id")
    private Integer orderGoodId;

    @ApiModelProperty(value = "退货单状态")
    private Integer goodStatus;

    @ApiModelProperty(value = "物流备注")
    private String deliveryNote;

    @ApiModelProperty(value ="订单详情id")
    private Long orderDetailId;

    @ApiModelProperty( value ="发货信息Id")
    private Integer orderDeliveryId;

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流公司代码（code）")
    private String transCode;

    @ApiModelProperty(value = "物流单号")
    private String transNumber;

    @ApiModelProperty(value = "供货价")
    private Integer supplyPrice;

    @ApiModelProperty(value = "供货商id")
    private Integer supplyUserId;

    @ApiModelProperty(value = "供货商姓名")
    private String supplyUserName;
    ;
}
