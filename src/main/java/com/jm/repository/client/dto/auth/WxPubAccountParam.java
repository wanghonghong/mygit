package com.jm.repository.client.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>微信公众账号</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
@Data
@ApiModel(description = "获取微信公众账号参数")
public class WxPubAccountParam {

    @ApiModelProperty(value = "服务appid")
    private String componentAppid;

    @ApiModelProperty(value = "授权方appid")
    private String authorizerAppid;

    public WxPubAccountParam(String componentAppid, String authorizerAppid) {
        this.componentAppid = componentAppid;
        this.authorizerAppid = authorizerAppid;
    }
}
