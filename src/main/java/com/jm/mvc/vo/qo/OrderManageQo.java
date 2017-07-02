/**
 * 
 */
package com.jm.mvc.vo.qo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;

/**
 * 
 *<p>查询订单管理信息列表</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月25日
 */

@Data
public class OrderManageQo {
	
	@ApiModelProperty(value = "订单详情标识")
    private Long orderDetailId;
	
	@ApiModelProperty(value = "订单号")
    private String orderCode;
	
	@ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;

    @ApiModelProperty(value = "商品名称")
    private String productName;
    
    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品规格ID")
    private Integer productSpecId;
    
    @ApiModelProperty(value = "商品图片")
    private String pic;

	@ApiModelProperty(value = "价格")
	private Double price;
	
	@ApiModelProperty(value = "数量")
	private Integer count;
	
	@ApiModelProperty(value = "购买时间")
	private Date createTime;
	
	@ApiModelProperty(value = "总金额")
    private Double totalPrice;

    @ApiModelProperty(value = "状态： 0：待付款,1:已付款 ，2：待发货，3:已发货，4:未退款")
    private Integer status;

    @ApiModelProperty(value = "付费类型")
    private String feeType;
    
    @ApiModelProperty(value = "支付流水号")
    private String payOrderNum;

    @ApiModelProperty(value = "描述")
    private String remark;
    
    @ApiModelProperty(value = "总页数")
    private Integer totalRows;
    
    @ApiModelProperty(value = "总条数")
    private Integer counts;

}
