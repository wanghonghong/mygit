package com.jm.mvc.vo.shop;

import com.jm.repository.po.shop.ShopEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class ContactusUo {

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "主图地址")
    private String imgUrl;

    @ApiModelProperty(value = "联系人")
    private String  linkMan;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "具体地址")
    private String specificAddr;

    @ApiModelProperty(value = "门店   1 有  0无")
    private int isEntity;

    private List<ShopEntity> shopEntitys;

}
