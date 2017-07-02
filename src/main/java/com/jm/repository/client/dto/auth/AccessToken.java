package com.jm.repository.client.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
public class AccessToken implements Serializable {

    private String accessToken;

    private Integer expiresIn;

    private Long expiresAt;
    
    private String openid;
    
    private String scope;
    
    private String refreshToken;

}
