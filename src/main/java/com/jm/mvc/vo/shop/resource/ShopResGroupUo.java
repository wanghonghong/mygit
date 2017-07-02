package com.jm.mvc.vo.shop.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/13 9:37
 */
@Data
public class ShopResGroupUo {
    private Integer id;

    @ApiModelProperty(value = "分组组名")
    private String groupName;


}
