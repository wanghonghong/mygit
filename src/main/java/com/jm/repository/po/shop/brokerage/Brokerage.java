package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>佣金设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "待发佣金清单:关联用户账户表")
public class Brokerage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

}
