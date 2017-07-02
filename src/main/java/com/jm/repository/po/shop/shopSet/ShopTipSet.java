package com.jm.repository.po.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>店铺消费设置</p>
 *
 * @author wukf
 * @version latest
 * @date 2017-3-22 14:47:32
 */
@Data
@Entity
@ApiModel(description = "店铺打赏设置")
public class ShopTipSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "图文ID")
    private Integer imageTextId;

    @ApiModelProperty(value = "打赏金额1")
    private int tipMoney1;

    @ApiModelProperty(value = "打赏金额2")
    private int tipMoney2;

    @ApiModelProperty(value = "打赏金额3")
    private int tipMoney3;

    @ApiModelProperty(value = "打赏金额4")
    private int tipMoney4;

    @ApiModelProperty(value = "打赏金额5")
    private int tipMoney5;

    @ApiModelProperty(value = "打赏金额6")
    private int tipMoney6;

    @ApiModelProperty(value = "打赏金额显示个数1,2,4")
    private String tipShowIndex;

}
