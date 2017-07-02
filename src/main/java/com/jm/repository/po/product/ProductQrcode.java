package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>商品二维码</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/8
 */
@Data
@Entity
@ApiModel(description = "商品二维码")
public class ProductQrcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "二维码标识")
    private Integer qrid;

    @ApiModelProperty(value = "商品编号")
    private Integer pid;

    @ApiModelProperty(value = "分享人编号")
    private Integer shareId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "商品永久二维码地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
