package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>分销设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "佣金流水列表查询")
public class BrokerageDetailListQo {

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "状态")
    private int status;//0未收货，1已收货未满15天，2有效佣金 3退款退货后佣金无效

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public BrokerageDetailListQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
