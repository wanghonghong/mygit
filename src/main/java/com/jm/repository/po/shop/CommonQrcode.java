package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>通用商品条码</p>
 */
@Data
@Entity
public class CommonQrcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer shopId;

    @ApiModelProperty(value = "商品编号")
    private Integer productId;

    @ApiModelProperty(value = "条形码名称")
    private String name;

    @ApiModelProperty(value = "条码类型")
    private int codeType;

    @ApiModelProperty(value = "有效类型 0按有效时间 1永久有效  ")
    private int validType;

    @ApiModelProperty(value = "有效时间  开始时间")
    private Date startTime;

    @ApiModelProperty(value = "有效时间  结束时间")
    private Date endTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "印刷备注")
    private String printRemark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态  0：正常   1：无效")
    private int status;

    @ApiModelProperty(value = "条码数量")
    private int count;

    @ApiModelProperty(value = "打包图片地址")
    private String zipUrl;

    @ApiModelProperty(value = "条码明细批号")
    private String detailNum;

}
