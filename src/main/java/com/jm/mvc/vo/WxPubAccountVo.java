package com.jm.mvc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxPubAccountVo {
	
	@ApiModelProperty(value = "授权方appid")
	private String appid;
	
	@ApiModelProperty(value = "授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号")
	private Integer serviceTypeIinfo;
	
	@ApiModelProperty(value = "授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
	private Integer verifyTypeInfo;
	
    @ApiModelProperty(value = "授权方头像")
    private String headImg;
    
    @ApiModelProperty(value = "授权方昵称")
    private String nickName;

    @ApiModelProperty(value = "授权方公众号的原始ID")
    private String userName;

    @ApiModelProperty(value = "授权方公众号所设置的微信号，可能为空")
    private String alias;
	
    @ApiModelProperty(value = "是否开启自动回复(1：是 0：否)")
    private Integer isFixedReply;
    
    @ApiModelProperty(value = "是否开启尾连接(1:是 0：否)")
    private Integer isLastLink;

    @ApiModelProperty(value = "所属pc端账号")
    private String phoneNumber;


}
