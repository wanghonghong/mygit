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
public class ShopCartForUpdateVo {
	
	@NotEmpty(message = "数量不能为0！")
    private String totalCount;
}
