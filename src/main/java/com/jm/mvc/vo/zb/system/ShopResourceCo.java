package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>角色权限Co</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class ShopResourceCo {

    @ApiModelProperty(value = "资源类型：图片1，优酷视频2，语音3，腾讯视频4")
    private Integer resType;

    @ApiModelProperty(value = "资源地址")
    private String resUrl;

    @ApiModelProperty(value = "资源分组标识 /默认0")
    private Integer groupId;

    @ApiModelProperty(value = "压缩比例")
    private Integer compress;

    @ApiModelProperty(value = "是否删除")
    private int isDel;//0不删除，1删除

}
