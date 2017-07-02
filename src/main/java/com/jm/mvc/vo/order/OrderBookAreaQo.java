package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>地区限定</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class OrderBookAreaQo {

    @ApiModelProperty("店铺Id")
    private Integer shopId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public OrderBookAreaQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }

}
