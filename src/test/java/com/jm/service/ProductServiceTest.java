/*
package com.jm.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jm.business.service.product.ProductTypeService;
import com.jm.repository.po.product.ProductGroup;
import com.jm.repository.po.product.ProductType;
import com.jm.staticcode.converter.product.ProductGrouptConverter;

*/
/**
 * 商品测试
 * @author zhengww
 *
 *//*

public class ProductServiceTest extends BaseServiceTest{

	@Autowired
	private ProductTypeService productTypeService;
	
	*/
/**
	 * 测试获取商品类型
	 *//*

	@Test
	public void test() throws Exception{
		List<ProductType> typeList = productTypeService.queryTypeList();
		Assert.assertTrue(typeList.size()>0);
	}
	
	*/
/**
	 * 新增商家分组
	 *//*

	@Test
	public void test1() throws Exception{
		ProductGroup productGroup = new ProductGroup();
		productGroup.setGroupImagePath("/myImage/gif");
		productGroup.setGroupName("我的分类");
		productGroup.setGroupSlogan("我的标语");
		productGroup.setShopId(1);
	}
	
	*/
/**
	 * 修改商家分组
	 *//*

	
}
*/
