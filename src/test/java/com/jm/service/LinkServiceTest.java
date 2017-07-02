/**
 * 
 */
package com.jm.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jm.business.service.shop.LinkService;
import com.jm.repository.po.system.Link;

/**
 * <p>店铺链接测试</p>
 * @author liangrs
 * @version latest
 * @date 2016/5/23
 */
public class LinkServiceTest extends BaseServiceTest {
	
	@Autowired
	private LinkService linkService;
	
	@Test
    public void test1() throws Exception{
    	List<Link> shopLink = linkService.getLinks();
        Assert.assertTrue(shopLink.size()>0);
    }
}
