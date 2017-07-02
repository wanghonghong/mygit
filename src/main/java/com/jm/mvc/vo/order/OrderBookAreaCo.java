package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * <p>地区限定</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class OrderBookAreaCo {

    private Integer id;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "地区ids")
    private String areaCode;

    @ApiModelProperty(value = "全部地区名称")
    private String allNames;

    @ApiModelProperty("快递编号")
    private Integer kdId;

    @ApiModelProperty("快递名称")
    private String kdName;

    @ApiModelProperty("回收地址")
    private String recAddress;

}
