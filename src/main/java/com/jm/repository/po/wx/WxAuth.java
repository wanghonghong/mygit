package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信权限表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/7/15
 */
@Data
@Entity
public class WxAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty(value = "appid，第三方应用id")
    private String appid;
    @ApiModelProperty(value = "appsecret，第三方应用secret")
    private String appsecret;
    @ApiModelProperty(value = "component_verify_ticket，用于获取第三方平台接口调用凭据")
    private String ticket;
    @ApiModelProperty(value = "component_access_token，来获取自己的接口调用凭据")
    private String token;
    @ApiModelProperty(value = "token过期时间")
    private long expiresAt;
    @ApiModelProperty(value = "pre_auth_code，获取用于授权流程准备的预授权码")
    private String code;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "appDomain")
    private String appDomain;
    @ApiModelProperty(value = "pcDomain")
    private String pcDomain;
}
