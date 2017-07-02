package com.jm.mvc.vo.zb.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>shopUo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/12/1
 */
@Data
public class ShopUo {

    @ApiModelProperty("店铺id")
    private int shopId;

    @ApiModelProperty("店铺状态 0：开启  1：关闭 ")
    private int shopStatus;

}
