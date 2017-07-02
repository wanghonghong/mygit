package com.jm.mvc.vo.product.product;

import com.jm.mvc.vo.product.group.ProductGroupVo;
import com.jm.mvc.vo.product.trans.TransTempVo;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *<p>商品新增基础数据返回Vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
public class ProductBascVo {

	@ApiModelProperty(value = "分组列表")
	private List<ProductGroupVo> groupList;
	@ApiModelProperty(value = "运费模板列表")
	private List<TransTempVo> transList;
	@ApiModelProperty(value = "佣金设置")
	private BrokerageSet brokerageSet;

}
