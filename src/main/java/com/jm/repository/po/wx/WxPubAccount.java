package com.jm.repository.po.wx;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <p>微信公众账号</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/24/024
 */
@Data
@Entity
@ApiModel(description = "微信公众账号")
public class WxPubAccount implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @ApiModelProperty(value = "授权方appid")
    private String appid;
    @ApiModelProperty(value = "授权方昵称")
    private String nickName;
    @ApiModelProperty(value = "授权方头像")
    private String headImg;
    @ApiModelProperty(value = "授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号")
    private Integer serviceTypeIinfo;
    @ApiModelProperty(value = "授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
    private Integer verifyTypeInfo;
    @ApiModelProperty(value = "授权方公众号的原始ID")
    private String userName;
    @ApiModelProperty(value = "授权方公众号所设置的微信号，可能为空")
    private String alias;
    @ApiModelProperty(value = "二维码图片的URL，开发者最好自行也进行保存")
    private String qrcodeUrl;
    @ApiModelProperty(value = "公众号授权给开发者的权限集列表")
    private String functions;
    @ApiModelProperty(value = "是否开通微信门店功能")
    private int openStore;
    @ApiModelProperty(value = "是否开通微信扫商品功能")
    private int openScan;
    @ApiModelProperty(value = "是否开通微信支付功能")
    private int openPay;
    @ApiModelProperty(value = "是否开通微信卡券功能")
    private int openCard;
    @ApiModelProperty(value = "是否开通微信摇一摇功能")
    private int openShake;

    @ApiModelProperty(value = "是否已经拉取过用户信息")
    private int isGet;
    @ApiModelProperty(value = "商户id")
    private String mchId;
    @ApiModelProperty(value = "商户秘钥（支付的秘钥，非普通秘钥）")
    private String appKey;
    @ApiModelProperty(value = "保存token")
    private String token;
    @ApiModelProperty(value = "保存刷新token")
    private String refreshToken;
    @ApiModelProperty(value = "jsapi_ticket")
    private String jsapiTicket;
    @ApiModelProperty(value = "token过期时间")
    private long expiresAt;
    @ApiModelProperty(value = "ticket过期时间")
    private long ticketExpiresAt;
    @ApiModelProperty(value = "是否授权(1：已授权 0：未授权)")
    private int isAuth;
    @ApiModelProperty(value = "是否子商户(1：是 0：否)")
    private Integer isSub;
    @ApiModelProperty(value = "公众号基础二维码")
    private String pubQrcodeUrl;
    @ApiModelProperty(value = "是否开启自动回复(1：是 0：否)")
    private Integer isFixedReply;
    @ApiModelProperty(value = "是否开启尾连接(1:是 0：否)")
    private Integer isLastLink;
    @ApiModelProperty(value = "类型  0：分销商家  1：聚客红包")
    private int type;
    @ApiModelProperty(value = "公众号所属用户id")
    private Integer userId;

    @ApiModelProperty(value = "二维码海报")
    private String qrcodePosterUrl;
}
