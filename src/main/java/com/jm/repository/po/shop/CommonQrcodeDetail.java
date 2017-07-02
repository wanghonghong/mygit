package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>通用商品条码明细</p>
 */
@Data
@Entity
public class CommonQrcodeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "归属用户编号")
    private Integer userId;

    @ApiModelProperty(value = "通码编号")
    private Integer commonQrcodeId;

    @ApiModelProperty(value = "使用次数")
    private int frequency;

    @ApiModelProperty(value = "商品编号")
    private Integer productId;

}
