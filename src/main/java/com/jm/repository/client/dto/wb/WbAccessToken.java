package com.jm.repository.client.dto.wb;

import lombok.Data;

/**
 * <p>微博授权后回调信息</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/22 14:27
 */
@Data
public class WbAccessToken  {
    private String accessToken; //	用户授权的唯一票据，用于调用微博的开放接口
    private String remindIn; // access_token的生命周期（该参数即将废弃
    private String expiresIn; //	access_token的生命周期，单位是秒数。
    private Long uid; //	授权用户的UID,只有access_token才是用户授权的唯一票据
}
