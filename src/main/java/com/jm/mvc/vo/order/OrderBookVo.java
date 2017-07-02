package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * <p>订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Data
@ApiModel(description = "订单")
public class OrderBookVo {

    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "总条数")
    private Integer total;

    @ApiModelProperty("订单编号")
    private String orderNum;

    @ApiModelProperty("预定人姓名")
    private String userName;

    @ApiModelProperty("预定人电话")
    private String phoneNumber;

    @ApiModelProperty("预约单类型 0:回收预约 ")
    private int orderBookType;

    @ApiModelProperty("预约时间")
    private String bookTime;

    @ApiModelProperty("预约时间段")
    private String bookTimeScope;

    @ApiModelProperty("下单时间")
    private Date createTime;

    @ApiModelProperty("订单状态")
    private int status;

    @ApiModelProperty("用户备注")
    private String userRemark;

    @ApiModelProperty("收货备注")
    private String receiveRemark;

    @ApiModelProperty("客服备注")
    private String customRemark;

    @ApiModelProperty("微信头像")
    @Column(length=200)
    private String headImgUrl;

    @ApiModelProperty("图片")
    @Column(length=200)
    private String imgUrl;

    @ApiModelProperty("图片")
    @Column(length=200)
    private String imgUrl2;

    @ApiModelProperty("图片")
    @Column(length=200)
    private String imgUrl3;

    @ApiModelProperty("重量")
    private String weight;

    @ApiModelProperty("指派上门用户")
    private Integer userId;

    @ApiModelProperty("回收物品类型")
    private int type;

    @ApiModelProperty("是否已奖励")
    private int reward;

    @ApiModelProperty("奖励类型")
    private int rewardType;

    @ApiModelProperty("省")
    private String provice;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String third;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("仓库地址")
    private String recAddress;

    @ApiModelProperty("用户手机")
    private String shopUserPhone;

    @ApiModelProperty("用户姓名")
    private String shopUserName;

    @ApiModelProperty("快递商")
    private String kdName;

    public OrderBookVo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }
    
}
