package com.jm.repository.po.order;

import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>供货配置信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/3/3
 */
@Data
@Entity
@ApiModel(description = "订单信息")
public class SupplySet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "供货配置信息标识")
    private Integer supplyId;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "供货价格")
    private Integer supplyPrice;

}
