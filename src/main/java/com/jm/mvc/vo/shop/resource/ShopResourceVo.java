/**
 * 
 */
package com.jm.mvc.vo.shop.resource;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺链接</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/5/23/021
 */
@Data
public class ShopResourceVo {
	
	@ApiModelProperty(value = "图片标识")
	private Integer id;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "资源类型：图片1，视频2，语音3")
    private Integer resType;

    @ApiModelProperty(value = "资源地址")
    private String resUrl;

    @ApiModelProperty(value = "资源分组标识 /默认0")
    private Integer resGroupId;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;//0不删除，1删除
    
}
