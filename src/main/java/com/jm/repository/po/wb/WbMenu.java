package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>微博菜单</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/4 14:26
 */
@Data
@Entity
@ApiModel(description = "微信菜单")
public class WbMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "shopId")
    private Integer shopId;

    @ApiModelProperty(value = "菜单的响应动作类型")
    private String type;

    @ApiModelProperty(value = "菜单标题，不超过16个字节，子菜单不超过40个字节")
    private String name;

    @ApiModelProperty(value = "链接名称")
    private String linkName;

    @ApiModelProperty(value = "view类型必须 网页链接，用户点击菜单可打开链接，不超过1024字节")
    private String url;

    @ApiModelProperty(value = "父级ID")
    private int parentId;


}
