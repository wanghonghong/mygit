package com.jm.mvc.vo.shop.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>门店卡券</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/25
 */
@Data
@ApiModel(description = "门店卡券")
public class ShopCardQo {
    
    @ApiModelProperty(value = "归属于该店铺")
    private Integer shopId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ShopCardQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }

}
