package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * <p>订单详情</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@Entity
@ApiModel(description = "订单详情")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单详情标识")
    private Long orderDetailId;

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "产品标识")
    private Integer pid;
     
    @ApiModelProperty(value = "商品规格标识")
    private Integer productSpecId;
   
    @ApiModelProperty(value = "价格")
    private Integer price;
    
    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "聚米注册的供货商用户ID")
    private Integer supplyUserId;

    @ApiModelProperty(value = "供货价")
    private Integer supplyPrice;

}
