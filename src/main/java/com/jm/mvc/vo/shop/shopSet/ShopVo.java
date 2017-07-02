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
public class ShopVo {

    @ApiModelProperty(value = "模板编码（用于标识样式）")
    private Integer tempId;

}
