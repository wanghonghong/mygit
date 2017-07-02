package com.jm.mvc.vo.order;

import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
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
 * @date 2017/4/6
 */
@Data
@ApiModel(description = "订单")
public class OrderBookQo {

    @ApiModelProperty("订单编号")
    private String orderNum;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机")
    private String userNumber;

    @ApiModelProperty("取件手机")
    private String phoneNumber;

    @ApiModelProperty("预约单类型 0:回收预约 ")
    private Integer type;

    @ApiModelProperty("预约时间")
    private Date bookTime;

    @ApiModelProperty("预约时间段")
    private String bookTimeScope;

    @ApiModelProperty("下单时间")
    private String orderBeginDate;

    @ApiModelProperty("下单时间")
    private String orderEndDate;

    @ApiModelProperty("取件时间")
    private String orderBeginDate1;

    @ApiModelProperty("取件时间")
    private String orderEndDate1;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    public OrderBookQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }

}
