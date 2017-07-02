package com.jm.business.service.product;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jm.mvc.vo.product.ProductForQueryVo;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.po.product.Product;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository repository;

	/**
	 * 
	 *<p>根据id获取单条商品</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月10日
	 */
	public Product getProduct(Integer id){
		return repository.findOne(id);
	}

	@Cacheable(value ="product_cache", key="#pid")
	public Product getCacheProduct(Integer pid){
		return getProduct(pid);
	}

	public Product save(Product product){
		return repository.save(product);
	}

	public List<Product> findAll(Iterable<Integer> ids){
		return repository.findAll(ids);
	}

	public boolean findByTransId(Integer templatesId) {
		Integer count = repository.findBytransId(templatesId);
		if(count>0){
			return false;
		}
		return true ;
	}
}
