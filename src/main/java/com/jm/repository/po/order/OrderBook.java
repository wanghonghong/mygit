package com.jm.repository.po.order;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@ApiModel("回收订单")
public class OrderBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("预定人用户id")
    private Integer userId;

    @ApiModelProperty("订单编号")
    private String orderNum;

    @ApiModelProperty("预定人姓名")
    private String userName;

    @ApiModelProperty("预定人电话")
    private String phoneNumber;

    @ApiModelProperty("预约单类型 0:回收预约 ")
    private int type;

    @ApiModelProperty("预约时间")
    private String bookTime;

    @ApiModelProperty("预约时间段")
    private String bookTimeScope;

    @ApiModelProperty("下单时间")
    private Date createTime;

    @ApiModelProperty("订单状态")
    private int status;

    @ApiModelProperty("店铺id")
    private Integer shopId;

}
