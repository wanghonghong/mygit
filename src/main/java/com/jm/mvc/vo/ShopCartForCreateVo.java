/**
 * 
 */
package com.jm.mvc.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 *<p></p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
public class ShopCartForCreateVo {
	
	@NotEmpty(message = "订单名称不能为null！")
    private String name;
	
	@NotEmpty(message = "商品价格不能为0！")
	private Double price;
}
