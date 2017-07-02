package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>地区限定</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
@Entity
@ApiModel("地区限定")
public class OrderBookArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "地区ids")
    private String areaCode;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "全部地区名称")
    private String allNames;

    @ApiModelProperty("订单类型的地区限制 0:回收订单 默认0")
    @Column(name="orderType",columnDefinition="int default 0")
    private String orderType;

    @ApiModelProperty("店铺编号")
    private Integer shopId;

    @ApiModelProperty("快递编号")
    private Integer kdId;

    @ApiModelProperty("快递名称")
    private String kdName;

    @ApiModelProperty("回收地址")
    private String recAddress;

}
