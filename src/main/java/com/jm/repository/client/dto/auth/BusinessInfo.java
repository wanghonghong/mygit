package com.jm.repository.client.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>功能列表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
@Data
@ApiModel(description = "用以了解以下功能的开通状况（0代表未开通，1代表已开通）")
public class BusinessInfo {

    @ApiModelProperty(value = "是否开通微信门店功能")
    private Integer openStore;

    @ApiModelProperty(value = "是否开通微信扫商品功能")
    private Integer openScan;

    @ApiModelProperty(value = "是否开通微信支付功能")
    private Integer openPay;

    @ApiModelProperty(value = "是否开通微信卡券功能")
    private Integer openCard;

    @ApiModelProperty(value = "是否开通微信摇一摇功能")
    private Integer openShake;

}
