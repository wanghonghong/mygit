package com.jm.mvc.vo.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(description = "店铺表")
public class ShopSignVo {

    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

}
