package com.jm.mvc.vo.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>店铺模板设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "店铺模板配置")
public class ShopTemplateVo {
    @ApiModelProperty(value = "主键id")
    private int tempId;

    @ApiModelProperty(value = "模板名称")
    private String tempName;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "模板类型0：简约1：简素2：酷密")
    private int tempType;

    @ApiModelProperty(value = "模板编码（用于标识样式）")
    private String tempCode;

    @ApiModelProperty(value = "模板编码（用于标识样式）")
    private int status;

    @ApiModelProperty(value = "配置json")
    private String mainJson;



}
