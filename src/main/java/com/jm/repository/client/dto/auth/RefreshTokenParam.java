package com.jm.repository.client.dto.auth;

import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
@Data
public class RefreshTokenParam {

    private String componentAppid;

    private String authorizerAppid;

    private String authorizerRefreshToken;

    public RefreshTokenParam(String componentAppid, String authorizerAppid,String authorizerRefreshToken) {
        this.componentAppid = componentAppid;
        this.authorizerAppid = authorizerAppid;
        this.authorizerRefreshToken = authorizerRefreshToken;
    }
}
