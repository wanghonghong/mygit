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
public class ShopResourceQo {

    @ApiModelProperty(value = "资源名")
    private String resName;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "每页显示条数")
    private Integer groupId;

    @ApiModelProperty(value = "店铺编码")
    private Integer shopId;

    @ApiModelProperty(value = "每页显示条数")
    private Integer resType;

    public ShopResourceQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
