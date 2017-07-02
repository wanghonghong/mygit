package com.jm.repository.po.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>自定义的会员中心设置</p>
 *
 */
@Data
@Entity
@ApiModel(description = "自定义的会员中心设置")
public class UserCenterCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "会员设置选项Id")
    private Integer funsId;

    @ApiModelProperty(value = "店铺Id")
    private Integer shopId;

    @ApiModelProperty(value = "自定义名称")
    private String name;

}
