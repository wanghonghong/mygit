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
public class AuthInfoParam {

    private String componentAppid;

    private String authorizationCode;

    public AuthInfoParam(String componentAppid, String authorizationCode) {
        this.componentAppid = componentAppid;
        this.authorizationCode = authorizationCode;
    }
}
