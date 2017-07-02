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
public class WbButtonVo {
    @ApiModelProperty(value = "菜单id")
    private Integer id;

    @ApiModelProperty(value = "菜单的响应动作类型")
    private String type;

    @ApiModelProperty(value = "菜单标题，不超过16个字节，子菜单不超过40个字节")
    private String name;

    @ApiModelProperty(value = "view类型必须 网页链接，用户点击菜单可打开链接，不超过1024字节")
    private String url;

    @ApiModelProperty(value = "click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节")
    private String key;

    @ApiModelProperty(value = "二级菜单数组，个数应为1~5个")
    private WbButtonVo[] subButton;

    @ApiModelProperty(value = "菜单链接名称")
    private String linkName;

    @ApiModelProperty(value = "父级ID")
    private int parentId;

}
