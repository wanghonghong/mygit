package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>按钮</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/6 17:11
 */
@Data
@ApiModel(description = "菜单按钮")
public class WbPushDataVo {

    @ApiModelProperty(value = "推送事件类型")
    private String subtype;

}
