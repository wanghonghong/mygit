package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>商品类型</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@Entity
@ApiModel(description = "交易模式活动主表")
public class TradActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键id")
    private Integer  id;

    @ApiModelProperty(value = "商家Id")
    private Integer shopId;

    @ApiModelProperty(value = "交易模式类型 1:一元夺宝   2：拼团 3.秒杀  4.拍卖  5.众筹")
    private int  type;

    @ApiModelProperty(value = "交易名称")
    private String  name;

    @ApiModelProperty(value = "类型：夺宝  1. 1元夺宝  2. 10元夺宝  3. 20元夺宝   99.自定义")
    private int  indianaType;

    @ApiModelProperty(value = "类型：拼团类型  1. 1人  2. 2人  3. 3人   4.以此类推")
    private int  groupType;

    @ApiModelProperty(value = "自定义类型夺宝价")
    private int  indianaPrice;

    @ApiModelProperty(value = "角标类型：1：A类   2：B类   3：C类  99：自定义类型")
    private int  sign;

    @ApiModelProperty(value = "自定义角标：图片地址")
    private String  imageUrl;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty(value = "活动状态 0：未开始   1：进行中  2：暂停活动  3：已结束  9：删除")
    private int  status;

    @ApiModelProperty(value = "商品价格：拼团：拼团价，夺宝：商品价")
    private int  price;

    @ApiModelProperty(value = "位置：1：上  2:下  3：左 4：右")
    private int position;

    @ApiModelProperty(value = "夺宝期数")
    private int  times;

    @ApiModelProperty(value = "完成次数")
    private int  count;

    @ApiModelProperty(value = "商品销量")
    private int saleCount;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date creatTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

//    @ApiModelProperty(value = "活动状态")
//    private int  status;

}
