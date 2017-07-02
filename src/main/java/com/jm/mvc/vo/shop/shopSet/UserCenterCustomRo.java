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
public class UserCenterCustomRo {

    private Integer id;

    @ApiModelProperty(value = "会员设置选项Id")
    private Integer funsId;

    @ApiModelProperty(value = "自定义名称")
    private String name;

    @ApiModelProperty(value = "选项名称")
    private String funName;

    @ApiModelProperty(value = "用于页面显示分组区分")
    private int group;

    @ApiModelProperty(value = "用于配置每个模板对应的事件")
    private String itemIcon;

}
