package com.jm.business.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.shop.ShopCategoryRepository;
import com.jm.repository.po.shop.ShopCategory;


/**
 * <p>主营类目</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6.29
 */
@Service
public class ShopCategoryService {
	
	@Autowired 
	private ShopCategoryRepository shopCategoryRepository;
	
	public List<ShopCategory>  findAll(){
		return shopCategoryRepository.findAll();
	}

	public ShopCategory findOne(Integer id){
		return shopCategoryRepository.findOne(id);
	}

}
