package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>分销设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "分销设置")
public class BrokerageConfigUo {

    @ApiModelProperty(value = "主键id")
    private int id;

    @ApiModelProperty(value = "收费类型：1分销升级收费，2代理收费")
    private int feeType;

    @ApiModelProperty(value = "封面图片")
    private String imgUrl;

    @ApiModelProperty(value = "分享语")
    private String share;

    @ApiModelProperty(value = "升级说明")
    private String instruction;

}
