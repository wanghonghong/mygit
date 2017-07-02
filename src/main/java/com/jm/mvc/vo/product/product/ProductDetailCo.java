package com.jm.mvc.vo.product.product;

import com.jm.mvc.vo.product.group.ProductGroupRelationCo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *<p>商品新增vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
public class ProductDetailCo {
	
	@ApiModelProperty(value = "商品主表")
	private ProductCo product;
	
	@ApiModelProperty(value = "分类关系表")
	private List<ProductGroupRelationCo> groupRelationList;

	@ApiModelProperty(value = "商品规格表")
	private List<ProductSpecPcCo> productSpecList;

	@ApiModelProperty(value = "商品角色限购表")
	private List<ProductRoleCo> productRoleList;

}
