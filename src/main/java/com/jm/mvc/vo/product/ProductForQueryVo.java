package com.jm.mvc.vo.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 *<p>商品查詢vo</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月9日
 */
@Data
public class ProductForQueryVo {
	
	@ApiModelProperty(value = "产品标识")
    private Integer id;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "产品别名")
    private String alias;
    
    @ApiModelProperty(value = "产品价格")
    private Double price;

    @ApiModelProperty(value = "产品描述")
    private String remark;

    @ApiModelProperty(value = "产品类型")
    private Integer type;

    @ApiModelProperty(value = "产品图片")
    private String pic;

    @ApiModelProperty(value = "产品销量")
    private String saleCount;

    @ApiModelProperty(value = "产品销量")
    private String totalCount;

}
