/**
 * 
 */
package com.jm.repository.jpa.shop;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.shop.ShopCategory;

/**
 * <p>主营类目</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6.29
 */
public interface ShopCategoryRepository extends JpaRepository<ShopCategory, Integer> {
	
}
