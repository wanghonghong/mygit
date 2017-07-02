package com.jm.mvc.vo.wx.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>微信菜单</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/17 9:39
 */
@Data
@ApiModel(description = "微信菜单")
public class WxMenuVo {

    private Integer id;

    @ApiModelProperty(value = "菜单标题")
    private String name;

}
