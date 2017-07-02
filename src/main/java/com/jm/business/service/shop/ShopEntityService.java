package com.jm.business.service.shop;

import com.jm.mvc.vo.shop.ShopEntityForQueryVo;
import com.jm.repository.jpa.shop.ShopEntityRepository;
import com.jm.repository.po.shop.ShopEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Service
public class ShopEntityService {

    @Autowired
    private ShopEntityRepository entityStoreRepository;


	public Page<ShopEntity> queryEntityStores(ShopEntityForQueryVo queryVo, Integer shopId) {
		Sort sort=new Sort(Direction.DESC, "storeId");
        PageRequest pageRequest = new PageRequest(queryVo.getCurPage(),queryVo.getPageSize(),sort);
        Page<ShopEntity> es = entityStoreRepository.findAll(shopId, pageRequest);
		return es;
	}

	 public Specification<ShopEntity> getSpec(final Integer shopId){
	    	Specification<ShopEntity> spec = new Specification<ShopEntity>() {
	            @Override
	            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
	                Predicate predicate = cb.conjunction();
	                List<Predicate> predicates = new ArrayList<>();
					predicates.add(cb.equal(root.get("shopId").as(Integer.class), shopId));
	                predicate.getExpressions().addAll(predicates);
	                return predicate;
	            }
	        };
	        return spec;
	    }

	public ShopEntity save(ShopEntity entityStore) {
		return entityStoreRepository.save(entityStore);
	}

	public ShopEntity findShopEntityById(Integer id) {
		return entityStoreRepository.findOne(id);
	}

	public List<ShopEntity> findShopEntityByShopId(Integer shopId) {
		return entityStoreRepository.findShopEntityByShopId(shopId);
	}

	public void delete(Integer id) {
		entityStoreRepository.delete(id);
	}


    
    
}
