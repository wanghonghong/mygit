package com.jm.mvc.vo.product.product;

import com.jm.mvc.vo.product.group.ProductGroupRelationVo;
import com.jm.mvc.vo.product.group.ProductGroupVo;
import com.jm.mvc.vo.product.trans.TransTempVo;
import com.jm.repository.po.product.*;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *<p>商品修改数据回填vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
public class ProductDetailVo {

	@ApiModelProperty(value = "商品主表")
	private ProductVo product;
	
	@ApiModelProperty(value = "分类关系表")
	private List<ProductGroupRelationVo> groupRelationList;
	
	@ApiModelProperty(value = "商品规格表")
	private List<ProductSpecPcVo> productSpecList;

	@ApiModelProperty(value = "获取分组")
	private List<ProductGroupVo> groupList;

	@ApiModelProperty(value = "商品角色限购表")
	private List<ProductRoleVo> productRoleList;

	@ApiModelProperty(value = "获取运费模板")
	private List<TransTempVo> transList;

	@ApiModelProperty(value = "佣金设置")
	private BrokerageSet brokerageSet;
}
