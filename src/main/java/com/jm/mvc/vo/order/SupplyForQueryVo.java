package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/3/2
 */
@Data
@ApiModel(description = "订单")
public class SupplyForQueryVo {

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "店主姓名")
    private String shopUserName;

    @ApiModelProperty(value = "店主手机")
    private String shopUserPhone;

    @ApiModelProperty(value = "状态 0：待付款; 1:待发货（已付款）; 2:待收货（已发货）; 3:已完成; 4:已关闭; 5:退款中")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date supplySettlementBeginDate;

    @ApiModelProperty(value = "结束时间")
    private Date supplySettlementEndDate;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public SupplyForQueryVo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }
    
}
