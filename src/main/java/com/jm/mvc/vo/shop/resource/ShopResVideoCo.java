/**
 * 
 */
package com.jm.mvc.vo.shop.resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>店铺音频提交</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/20 16:40
 */
@Data
public class ShopResVideoCo {

    @ApiModelProperty(value = "资源名")
    private String resName;

    @ApiModelProperty(value = "优酷资源地址")
    private String ykResUrl;

    @ApiModelProperty(value = "腾讯资源地址")
    private String txResUrl;

    
}
