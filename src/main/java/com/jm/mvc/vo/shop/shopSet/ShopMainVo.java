package com.jm.mvc.vo.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>商城首页搭建</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "商城首页")
public class ShopMainVo implements Serializable {

    @ApiModelProperty(value = "主键id")
    private int id;

    @ApiModelProperty(value = "详情json")
    private String detailJson;

    @ApiModelProperty(value = "当前版本")
    private String curVersion;

    @ApiModelProperty(value = "分享语")
    private String share;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
}
