package com.jm.mvc.vo.shop;

import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class NewShopRo {
    @ApiModelProperty("店铺主营类目名称")
    private String cateName;

    @ApiModelProperty("主营类目")
    private List<ShopCategory> shopCategorys;

    @ApiModelProperty("预置店铺")
    private Shop shop;

}
