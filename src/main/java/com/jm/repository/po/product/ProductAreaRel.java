package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@Entity
@ApiModel(description = "地区供货")
public class ProductAreaRel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "供货地区编码")
    private String areaCode;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "供货地区名称")
    private String areaName;

    @ApiModelProperty(value = "供货商userId")
    private Integer userId;

    @ApiModelProperty(value = "供货价")
    private Integer supplyPrice;

}
