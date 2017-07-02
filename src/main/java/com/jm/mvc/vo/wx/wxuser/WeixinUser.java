package com.jm.mvc.vo.wx.wxuser;

import lombok.Data;

/**
 *<p>微信用户</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月15日
 */
@Data
public class WeixinUser {
	/**
	 * 关注该公众账号的总用户数
	 */
	private Integer total;
	/**
	 * 拉取的OPENID个数，最大值为10000
	 */
	private Integer count;
	/**
	 * 拉取列表的最后一个用户的OPENID
	 */
	private String nextOpenid;
	
	/**
	 * 列表数据，OPENID的列表
	 */
	private OpenIdData data = new OpenIdData();

}
