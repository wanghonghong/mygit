package com.jm.mvc.vo.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>自定义的会员中心设置</p>
 *
 */
@Data
@ApiModel(description = "自定义的会员中心设置")
public class UserCenterFunsRo {

    private int funId;

    @ApiModelProperty(value = "模板名称")
    private String funName;

    @ApiModelProperty(value = "用于配置每个模板对应的事件")
    private String itemIcon;

    @ApiModelProperty(value = "是否显示 0显示 1不显示")
    private int isShow;

    @ApiModelProperty(value = "用于页面显示分组区分")
    private int group;

}
