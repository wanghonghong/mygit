package com.jm.mvc.vo.shop.integral;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@ApiModel(description = "积分商品")
public class IntegralProductQo {

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public IntegralProductQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
