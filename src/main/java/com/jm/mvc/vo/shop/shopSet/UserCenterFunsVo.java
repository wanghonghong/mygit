package com.jm.mvc.vo.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>会员设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "会员设置选项")
public class UserCenterFunsVo {

    @ApiModelProperty(value = "id")
    private int funId;

    @ApiModelProperty(value = "模板名称")
    private String funName;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "0必须1可配2关闭")
    private int status;

    @ApiModelProperty(value = "权限")
    private int role;

    @ApiModelProperty(value = "用于配置每个模板对应的事件")
    private String url;

    @ApiModelProperty(value = "用于配置每个模板对应的事件")
    private String itemIcon;

    @ApiModelProperty(value = "用于页面显示分组区分")
    private int group;

}
