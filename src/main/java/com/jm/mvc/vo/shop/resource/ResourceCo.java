package com.jm.mvc.vo.shop.resource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>资源表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@ApiModel(description = "资源表菜单设置")
public class ResourceCo {

    @ApiModelProperty(value = "资源类型：图片1，优酷视频2，语音3")
    private Integer resType;

    @ApiModelProperty(value = "资源类型：腾讯视频0，优酷视频1")
    private int resSubType;

    @ApiModelProperty(value = "资源名称")
    private String resName;

    @ApiModelProperty(value = "资源地址")
    private String resUrl;

    @ApiModelProperty(value = "资源分组标识 /默认0")
    private Integer groupId;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "压缩比例")
    private Integer compress;

    @ApiModelProperty(value = "是否删除")
    private int isDel;//0不删除，1删除
}
