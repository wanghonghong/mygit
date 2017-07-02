/**
 * 
 */
package com.jm.repository.jpa.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.system.Link;

/**
 * <p>店铺链接表</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/5/23
 */
public interface LinkRepository extends JpaRepository<Link, Long> {
	
	List<Link> findAll();
}
