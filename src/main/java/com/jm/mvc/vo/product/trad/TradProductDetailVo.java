package com.jm.mvc.vo.product.trad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>商品类型</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@ApiModel(description = "交易模式活动主表")
public class TradProductDetailVo {

    @ApiModelProperty(value = "主键id")
    private Integer  id;

    @ApiModelProperty(value = "商品名称")
    private String  productName;

    @ApiModelProperty(value = "商品id")
    private Integer pid;

    @ApiModelProperty(value = "商品价格：拼团：拼团价，夺宝：商品价")
    private int  price;

    @ApiModelProperty(value = "夺宝期数")
    private int  times;

    @ApiModelProperty(value = "完成次数")
    private int  count;

    @ApiModelProperty(value = "商品销量")
    private int saleCount;

    @ApiModelProperty(value = "商品价格")
    private int productPrice;

    @ApiModelProperty(value = "交易名称")
    private String  name;

    @ApiModelProperty(value = "类型：夺宝  1. 1元夺宝  2. 10元夺宝  3. 20元夺宝   99.自定义")
    private int  indianaType;

    @ApiModelProperty(value = "类型：拼团类型  1. 1人  2. 2人  3. 3人   4.以此类推")
    private int  groupType;

    @ApiModelProperty(value = "自定义类型夺宝价")
    private int  indianaPrice;

    @ApiModelProperty(value = "活动状态 0：未开始   1：进行中  2：暂停活动  3：已结束  9：删除")
    private int  status;

    @ApiModelProperty(value = "自定义角标：图片地址")
    private String  imageUrl;

    @ApiModelProperty(value = "角标类型：1：A类   2：B类   3：C类  99：自定义类型")
    private int  sign;

    @ApiModelProperty(value = "位置：1：上  2:下  3：左 4：右")
    private int position;

    @ApiModelProperty(value = "商品图片正方形")
    private String picSquare;

    @ApiModelProperty(value = "商品图片长方形")
    private String picRectangle;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


}
