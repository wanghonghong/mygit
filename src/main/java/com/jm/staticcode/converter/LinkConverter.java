/**
 * 
 */
package com.jm.staticcode.converter;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.shop.LinkCreateVo;
import com.jm.repository.po.system.Link;




/**
 * 
 *<p>店铺链接数据转换</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
public class LinkConverter {
	
	 public static Link toShopLink(LinkCreateVo linkCreateVo) {
        Link link = new Link();
        BeanUtils.copyProperties(linkCreateVo,link);
        return link;
	 }
}
