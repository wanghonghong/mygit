package com.jm.mvc.vo.wx.wxmessage;

import lombok.Data;

@Data
public class WxMessageQo {
    private String appid;
    private String openid;
    private String msg;
    private String type;
}
