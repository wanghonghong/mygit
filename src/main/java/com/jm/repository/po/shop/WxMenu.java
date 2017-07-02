package com.jm.repository.po.shop;

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
 * @author wukf
 * @version latest
 * @date 2016/5/18/018
 */
@Data
@Entity
@ApiModel(description = "微信菜单")
public class WxMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "appid")
    private String appid;

    @ApiModelProperty(value = "菜单的响应动作类型")
    private String type;

    @ApiModelProperty(value = "菜单标题，不超过16个字节，子菜单不超过40个字节")
    private String name;

    @ApiModelProperty(value = "链接名称")
    private String linkName;

    @ApiModelProperty(value = "view类型必须 网页链接，用户点击菜单可打开链接，不超过1024字节")
    private String url;

    @ApiModelProperty(value = "media_id类型和view_limited类型必须 调用新增永久素材接口返回的合法media_id")
    private String mediaId;

    @ApiModelProperty(value = "key值")
    private String wxKey;

    @ApiModelProperty(value = "父级ID")
    private int parentId;

    @ApiModelProperty(value = "商品分类Id")
    private int groupId;

    @ApiModelProperty(value = "具体商品Id")
    private int pid;

}
