package com.jm.mvc.vo.wx.wxuser;

import lombok.Data;

@Data
public class WxUserCardCo {

    //wxUserid
    private Integer userId;

    //"使用状态：0未使用，1已使用，9删除"
    private Integer status;

    //"卡券类型：0自己领取，1赠送的"
    private int type;

    //"分享人id"
    private Integer shareId;

    private Integer cardId;
}
