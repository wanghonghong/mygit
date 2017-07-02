package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>商品条码效果表</p>
 */
@Data
@Entity
public class ProductQrcodeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "条码编号")
    private Integer goodsQrcodeId;

    @ApiModelProperty(value = "渠道编号 （代理商、分销商Id）")
    private Integer userId;

    @ApiModelProperty(value = "关注类型 0:渠道条码  1:通用条码")
    private int type;

}
