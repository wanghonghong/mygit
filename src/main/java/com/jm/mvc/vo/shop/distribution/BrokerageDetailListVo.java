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
public class BrokerageDetailListVo {

    @ApiModelProperty(value = "订单信息标识")
    private Long orderInfoId;

    @ApiModelProperty(value="订单编号")
    private String orderNum;

    @ApiModelProperty(value = "收货时间")
    private Date takeDate;

    @ApiModelProperty(value = "佣金比例")
    private int brokerage;

    @ApiModelProperty(value = "商品总金额")
    private int totalPrice;

    @ApiModelProperty(value = "佣金金额")
    private int commissionPrice;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public BrokerageDetailListVo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
