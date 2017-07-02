package com.jm.mvc.vo.product.product;

import com.jm.mvc.vo.product.ProductSpecVo;
import com.jm.mvc.vo.product.group.ProductGroupRelationUo;
import com.jm.mvc.vo.product.group.ProductGroupRelationVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *<p>商品修改vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
public class ProductDetailUo {

	@ApiModelProperty(value = "商品主表")
	private ProductUo product;

	@ApiModelProperty(value = "分类关系表")
	private List<ProductGroupRelationUo> groupRelationList;

	@ApiModelProperty(value = "商品规格表")
	private List<ProductSpecPcUo> productSpecList;

	@ApiModelProperty(value = "商品角色限购表")
	private List<ProductRoleUo> productRoleList;

	@ApiModelProperty(value = "删除的规格id，以逗号分隔")
	private String delId;

}
