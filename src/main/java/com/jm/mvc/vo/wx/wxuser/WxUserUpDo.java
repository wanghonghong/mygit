package com.jm.mvc.vo.wx.wxuser;

import lombok.Data;

/**
 * chenyy 2017/1/17.
 * 更新微信用户信息请求do
 */

@Data
public class WxUserUpDo {

    private String openid;

    private String lang;

    public WxUserUpDo(){
        this.lang = "zh-CN";
    }
}
