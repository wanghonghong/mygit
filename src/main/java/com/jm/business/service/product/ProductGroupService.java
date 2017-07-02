package com.jm.business.service.product;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.group.ProductGroupCo;
import com.jm.mvc.vo.product.group.ProductGroupQo;
import com.jm.mvc.vo.product.group.ProductGroupUo;
import com.jm.mvc.vo.product.group.ProductGroupVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.product.JdbcProductRepository;
import com.jm.repository.jpa.product.ProductGroupRelationRepository;
import com.jm.repository.jpa.product.ProductGroupRepository;
import com.jm.repository.po.product.ProductGroup;
import com.jm.repository.po.product.ProductGroupRelation;
import com.jm.staticcode.converter.product.ProductGrouptConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 * `
 * 
 * @author zhengwww
 * @version latest
 * @date 2016/5/13
 */
@Service
public class ProductGroupService {

	@Autowired
	private ProductGroupRepository productGroupRepository;

	@Autowired
	private ProductGroupRelationRepository productTypeRelationRepository;
	
	@Autowired
	private JdbcProductRepository jdbcRepository;

	/**
	 * <p>
	 * 查询商家分组所有数据
	 * </p>
	 * @param shopId 
	 * 
	 * @return
	 */
	public List<ProductGroupVo> queryGroupListByShopId(Integer shopId) {
		List<ProductGroup> productGroups =  productGroupRepository.findProductGroupByShopId(shopId);
		List<ProductGroupVo> productGroupRos = new ArrayList<>();
		if(productGroups.size() > 0 &&productGroups != null ){
			for(ProductGroup productGroup : productGroups){
				ProductGroupVo productGroupVo = ProductGrouptConverter.toProductGrouptVo(productGroup);
				productGroupRos.add(productGroupVo);
			}
		}
		return productGroupRos;
	}


	/**
	 * 查询商家所有分组列表
	 * @param productGroupQo
	 * @return
	 */
	public PageItem<Map<String, Object>> queryGroupList(ProductGroupQo productGroupQo, Integer shopId) {
		return jdbcRepository.queryGroupList(productGroupQo,shopId);
	}


	/**
	 * 新增商家分组
	 * @param productGroupCo,shopId
	 * @return
	 */
	public ProductGroupVo saveProductGroup(ProductGroupCo productGroupCo, Integer shopId) {
		ProductGroup productGroup = ProductGrouptConverter.toProductGroupt(productGroupCo);
		productGroup.setShopId(shopId);
		productGroup = productGroupRepository.save(productGroup);
		return ProductGrouptConverter.toProductGrouptVo(productGroup);
	}

	/**
	 * 修改商家分组信息
	 * @param productGroupUo,shopId
	 * @return
	 */
	public ProductGroupVo updateProductGroup(ProductGroupUo productGroupUo) {
		ProductGroup productGroup = ProductGrouptConverter.toProductGroupt(productGroupUo);
		//获取分组
		ProductGroup productGroup2 = productGroupRepository.findOne(productGroup.getGroupId());
		if(null != productGroup.getGroupName()){//分组名
			productGroup2.setGroupName(productGroup.getGroupName());
		}
		if(null != productGroup.getGroupImagePath()){//分组图
			productGroup2.setGroupImagePath(productGroup.getGroupImagePath());
		}
		if(null != productGroup.getGroupSlogan()){//分组标语
			productGroup2.setGroupSlogan(productGroup.getGroupSlogan());
		}
		if(0 != productGroup.getGroupSort()){//分组顺序
			productGroup2.setGroupSort(productGroup.getGroupSort());
		}
		productGroup = productGroupRepository.save(productGroup2);
		return	ProductGrouptConverter.toProductGrouptVo(productGroup);
	}


	/**
	 * 删除分组
	 * @param groupId
	 * @return
	 */
	public Boolean delProductGroup(Integer groupId) {
		Boolean flag = false;
		List<ProductGroupRelation> list = new ArrayList<ProductGroupRelation>();
		list = productTypeRelationRepository.findProductGroupRelationByGroupId(groupId);
		if(null == list || list.size()==0){
			productGroupRepository.delete(groupId);
			flag = true;
		}else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 分组
	 * @param groupId
	 * @return
	 */

	@Cacheable(value ="product_group_cache", key="#groupId")
	public ProductGroup getCacheProductGroup(Integer groupId) {
		ProductGroup productGroup = productGroupRepository.findOne(groupId);
		return productGroup;
	}
	/**
	 * 查询单个分组详细信息
	 * @param groupId
	 * @return
	 */
	public ProductGroupVo queryGroupByGroupId(Integer groupId) {
		ProductGroupVo productGroupVo = new ProductGroupVo();
		ProductGroup productGroup = productGroupRepository.findOne(groupId);
		if(productGroup!=null){
			productGroupVo = ProductGrouptConverter.toProductGrouptVo(productGroup);
		}
		return productGroupVo;
	}

	/**
	 * 修改分组顺序
	 * @param productGroup
	 * @return
	 */
	public ProductGroup updateGroupSort(ProductGroup productGroup) {
		ProductGroup productGroup2 = productGroupRepository.findOne(productGroup.getGroupId());
		productGroup2.setGroupSort(productGroup.getGroupSort());
		return productGroupRepository.save(productGroup2);
	}
}
