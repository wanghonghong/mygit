package com.jm.mvc.vo.order;

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
public class OrderInfoCreateVo {
	

    @ApiModelProperty(value = "用户标识")
    private Integer userId;
    
    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "价格")
    private Integer totalPrice;
    
    @ApiModelProperty(value = "运费")
    private Integer sendFee;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "状态：代付款，待发货，已发货，未退款，已付款")
    private Integer status;
    
    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;
    
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;
    
    @ApiModelProperty(value = "收货地址")
    private String address;

    @ApiModelProperty(value = "收货区号")
    private String areaCode;

    @ApiModelProperty(value = "地址id")
    private Integer userAddrId;

    @ApiModelProperty(value = "卡券Id")
    private Integer userCardId;

    @ApiModelProperty(value = "积分")
    private Integer point;

    @ApiModelProperty(value="单位")
    private Integer unit;
    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetailCreateVo> detailVos;
    
}
