package com.jm.business.domain.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>发送商品图文内容</p>
 *
 * @version latest
 * @Author 吴克府
 * @Date 2016/10/31
 */
@Data
@ApiModel(description = "发送商品图文内容")
public class SendContextDo {

	@ApiModelProperty(value = "userId")
	private Integer  userId;

	@ApiModelProperty(value = "appid")
	private String  appid;

	@ApiModelProperty(value = "accessToken")
	private String  accessToken;

	@ApiModelProperty(value = "用户openid")
	private String openid;

	@ApiModelProperty(value = "发送内容ID")
	private Integer contextId;

	@ApiModelProperty("推送类型  4.商品列表;5.商品详情;6.图文列表7.图文详情;8.商城首页 10.微信图文")
	private Integer type;



}
