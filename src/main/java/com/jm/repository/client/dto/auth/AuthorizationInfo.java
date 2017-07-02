package com.jm.repository.client.dto.auth;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>授权信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
@Data
@ApiModel(description = "公众号授权信息")
public class AuthorizationInfo {

    private String authorizerAppid;

    /* 公众号授权给开发者的权限集列表，ID为1到15时分别代表：
   消息管理权限
   用户管理权限
   帐号服务权限
   网页服务权限
   微信小店权限
   微信多客服权限
   群发与通知权限
   微信卡券权限
   微信扫一扫权限
   微信连WIFI权限
   素材管理权限
   微信摇周边权限
   微信门店权限
   微信支付权限
   自定义菜单权限*/
    FuncscopeCategory[] funcInfo;

}
