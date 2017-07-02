package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@Entity
@ApiModel(description = "商品规格")
public class ProductSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "商品规格标识")
    private Integer productSpecId;

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "规格标识1")
    private Integer specIdOne;

    @ApiModelProperty(value = "规格标识2")
    private Integer specIdTwo;

    @ApiModelProperty(value = "规格标识3")
    private Integer specIdThree;

    @ApiModelProperty(value = "规格名称1")
    private String specNameOne;

    @ApiModelProperty(value = "规格名称2")
    private String specNameTwo;

    @ApiModelProperty(value = "规格名称3")
    private String specNameThree;

    @ApiModelProperty(value = "规格值")
    private String specValueOne;

    @ApiModelProperty(value = "规格值")
    private String specValueTwo;

    @ApiModelProperty(value = "规格值")
    private String specValueThree;

    @ApiModelProperty(value = "商品规格价格")
    private Integer specPrice;

    @ApiModelProperty(value = "商品规格图片")
    private String specPic;

    @ApiModelProperty(value = "商品销量")
    private int saleCount;

    @ApiModelProperty(value = "商品数量")
    private int totalCount;
    
    @ApiModelProperty(value = "商品货号")
    private String productCode;

    @ApiModelProperty(value = "规格状态：0表示正常，1表示删除")
    private int status;

}
