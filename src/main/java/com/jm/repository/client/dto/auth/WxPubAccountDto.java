package com.jm.repository.client.dto.auth;

import com.jm.repository.client.dto.auth.AuthorizationInfo;
import com.jm.repository.client.dto.auth.AuthorizerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>微信公众账号</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
@ApiModel(description = "微信公众账号基本信息")
@Data
public class WxPubAccountDto implements Serializable {
    
    @ApiModelProperty(value = "公众号基本信息")
    private AuthorizerInfo authorizerInfo;

    @ApiModelProperty(value = "公众号授权信息")
    private AuthorizationInfo authorizationInfo;

    @ApiModelProperty(value = "保存刷新token")
    private String refreshToken;

}
