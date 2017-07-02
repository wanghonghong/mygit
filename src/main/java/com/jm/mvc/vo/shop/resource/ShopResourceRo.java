/**
 * 
 */
package com.jm.mvc.vo.shop.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>店铺资源</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/8 11:38
 */
@Data
public class ShopResourceRo {


	private Integer id;

    @ApiModelProperty(value = "资源名")
    private String resName;

    @ApiModelProperty(value = "资源类型：图片1，优酷视频2，语音3，腾讯视频4")
    private Integer resType;

    @ApiModelProperty(value = "资源地址")
    private String resUrl;

    @ApiModelProperty(value = "资源分组标识 /默认0")
    private Integer resGroupId;

}
