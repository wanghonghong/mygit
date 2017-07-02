package com.jm.repository.client.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * <p>微信用户表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
public class WxUserDto {

    private Integer subscribe;

    private String openid;

    private String nickname;
    
    private Integer sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String  headimgurl;

    private long subscribeTime;

    private String  unionid;

    private String  remark;

    private String  qrcodeurl;

    private int groupid;

    private List<WxUserDto> userInfoList;
    
}
