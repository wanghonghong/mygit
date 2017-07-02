package com.jm.mvc.vo.shop.commQrcode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "")
public class ChannelQrcodeCo {

    @ApiModelProperty(value = "渠道编号 （代理商、分销商Id）")
    private Integer userId;

    private Integer shopId;

    @ApiModelProperty(value = "商品编号")
    private Integer productId;

    @ApiModelProperty(value = "条形码名称")
    private String name;

    @ApiModelProperty(value = "条码类型")
    private int codeType;

    @ApiModelProperty(value = "有效时间  开始时间")
    private Date startTime;

    @ApiModelProperty(value = "有效时间  结束时间")
    private Date endTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "有效类型 0按有效时间 1永久有效  ")
    private int validType;

}
