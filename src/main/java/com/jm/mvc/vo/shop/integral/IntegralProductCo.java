package com.jm.mvc.vo.shop.integral;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

/**
 * <p>积分</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@ApiModel(description = "积分商品")
public class IntegralProductCo {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "商品ID")
    private Integer pid;

    @ApiModelProperty(value = "0 全额抵扣 1 是 限额抵扣")
    private Integer type;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;
    
    @ApiModelProperty(value = "抵扣积分")
    private Integer integral;

}
