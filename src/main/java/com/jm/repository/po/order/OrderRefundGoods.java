package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>退货表</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/9/2
 */

@Data
@Entity
@ApiModel(description = "退货信息")

public class OrderRefundGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "退款货信息标识")
    private Integer orderGoodId;

    @ApiModelProperty(value = "订单信息标识id")
    private Long orderInfoId;

    @ApiModelProperty(value = "退款金额")
    private Integer refundMoney;

    @ApiModelProperty(value = "退款类型,1:退单, 2:售后 ")
    private int refundType;

    @ApiModelProperty(value = "1:订单全额退款, 2:订单部分退款, 3:协商一致退款不退货, 4:先退货后退款")
    private Integer refundWay;

    @ApiModelProperty(value = "物流公司")
    private String transCompany;

    @ApiModelProperty(value = "物流代码")
    private String transCode;

    @ApiModelProperty(value = "物流编号")
    private String transId;

    @ApiModelProperty(value = "订单金额")
    private Integer totalMoney;

    @ApiModelProperty(value = "商品金额")
    private Integer productMoney;

    @ApiModelProperty(value = "运费金额")
    private Integer sendFee;

    @ApiModelProperty(value = "退货状态: 0:退货中; 1:已入库")
    private int goodStatus;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "拒绝退款原因")
    private String refuseReason;

    @ApiModelProperty(value = "入库备注")
    private String storageNote;

    @ApiModelProperty(value = "退货创建时间")
    private Date createTime;

    @ApiModelProperty(value = "买家退货日期")
    private Date createGoodDate;

    @ApiModelProperty(value = "卖家入库日期")
    private Date storageDate;

    @ApiModelProperty(value = "平台标识 0:微信 1:微博")
    private int platform;
}
