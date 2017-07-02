package com.jm.repository.client.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>授权信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */

@Data
@ApiModel(description = "公众号基本信息")
public class AuthorizerInfo {
    @ApiModelProperty(value = "二维码图片的URL，开发者最好自行也进行保存")
    private String qrcodeUrl;
    @ApiModelProperty(value = "授权方昵称")
    private String nickName;
    @ApiModelProperty(value = "授权方头像")
    private String headImg;
    @ApiModelProperty(value = "IDC")
    private Integer idc;
    @ApiModelProperty(value = "授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号")
    private ServiceTypeInfo serviceTypeInfo;
    @ApiModelProperty(value = "授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
    private VerifyTypeInfo verifyTypeInfo;
    @ApiModelProperty(value = "授权方公众号的原始ID")
    private String userName;
    @ApiModelProperty(value = "授权方公众号所设置的微信号，可能为空")
    private String alias;
    @ApiModelProperty(value = "公众号功能的开通状况")
    private BusinessInfo businessInfo;

}
