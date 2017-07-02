/**
 * 
 */
package com.jm.mvc.vo.shop;

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
public class LinkCreateVo {
	
	@ApiModelProperty(value = "店铺链接标识")
    private Integer id;

	@ApiModelProperty(value = "链接名称")
    private String linkName;
    
	@ApiModelProperty(value = "链接地址")
    private String linkUrl;
	
	@ApiModelProperty(value = "父标识")
	private Integer parentId;

    @ApiModelProperty(value = "菜单类型,0 直接返回 ,1 有查询信息")
    private String linkType;

    @ApiModelProperty(value = "链接编码，ptype 商品类型，product 商品")
    private String linkCode;
}
