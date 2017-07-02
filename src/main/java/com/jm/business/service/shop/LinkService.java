/**
 * 
 */
package com.jm.business.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.shop.LinkRepository;
import com.jm.repository.po.system.Link;


/**
 * <p>店铺链接表</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/5/21/021
 */
@Service
public class LinkService {
	
	@Autowired
	private LinkRepository linkRepository;
	
	/**
	 * 
	 *<p>增加菜单</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月23日
	 */
	public Link addShoppringCart(Link link){
		return linkRepository.save(link);
	}
	
	/**
	 * 
	 *<p>删除菜单</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月23日
	 */
	public void deleteLink(Long id){
		linkRepository.delete(id);
	}
	
	/**
	 * 
	 *<p>修改菜单</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月23日
	 */
	public Link updateShopLink(Link link){
		Link sl = linkRepository.findOne(Long.parseLong(link.getId().toString()));
		sl.setLinkName(link.getLinkName());
		sl.setLinkUrl(link.getLinkUrl());
		return linkRepository.save(sl);
	}
	
	/**
	 * 
	 *<p>查询菜单</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月23日
	 */
	public List<Link> getLinks(){
		return linkRepository.findAll();
	}
	
}
