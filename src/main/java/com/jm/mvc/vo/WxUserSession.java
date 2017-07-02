/**
 * 
 */
package com.jm.mvc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 *<p></p>
 *
 * @author wukf
 * @version latest
 * @data 2016年7月21日
 */
@Data
public class WxUserSession  implements Serializable {
	
	private Integer userId;

	private Integer shopId;

	private String shopName;

	private String imgUrl;

	private Integer tempId;

	private String shareDesc;

	private String appid;

	private String openid;

	private String accessToken;

	private String refreshToken;

	private Long expiresAt;

	private String scope;

	private Integer shareid;

	//是否有门店 0是没有，1有
	private int isEntity;

	private Integer isSubscribe;
	
	private String nickName;
	
	private String headimgurl;

	private String hxAccount;

	private Integer isOpen;

	@ApiModelProperty(value = "无假货承诺 0:未认证 1：已认证")
	private int promise;

	@ApiModelProperty(value = "7天无理由退换 0:未认证 1：已认证")
	private int exchange;

	@ApiModelProperty(value = "官方直营 0:未认证 1：已认证")
	private int directSell;

	@ApiModelProperty(value = "我的小店权限0：没有权限，1：免费版权限，2：收费版权限")
	private int isMyShop;



}
