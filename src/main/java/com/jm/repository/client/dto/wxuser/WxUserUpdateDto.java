package com.jm.repository.client.dto.wxuser;

import com.jm.repository.client.dto.WxUserDto;
import lombok.Data;

import java.util.List;

/**
 * 更新微信用户Dto
 */
@Data
public class WxUserUpdateDto {

    private Integer subscribe;

    private String openid;

    private String nickname;

    private Integer sex;

    private String language;

    private String  headimgurl;

    private long subscribeTime;

    private String  unionid;

    private String  remark;

    private String  qrcodeurl;

    private int groupid;

    private List<WxUserUpdateDto> userInfoList;
}
