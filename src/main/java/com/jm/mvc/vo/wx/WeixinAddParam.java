package com.jm.mvc.vo.wx;

import lombok.Data;

/**
 *<p>获取地址返回前端需要的参数</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年7月1日
 */
@Data
public class WeixinAddParam {
	private String code;//code
	private String nonceStr;//随机字符串
	private String accessToken;//授权返回的token，不是官方的token
	private String timestamp;//时间戳
	private String appid;

}
