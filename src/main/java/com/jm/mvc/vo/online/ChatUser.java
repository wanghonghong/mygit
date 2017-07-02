package com.jm.mvc.vo.online;

import lombok.Data;

import java.util.Date;

@Data
public class ChatUser {
    private String appid;
    private String openid;
    private String nickname;
    private String headimgurl;
    private String account;
    private int chatType;
    private Date lastControlTime;
    private Integer userId;
    private Integer shopId;

    /*private String upper;
    private String lower;*/
    private int isReply;
    private Date subscribeTime;
    private Date lastBuyTime;
    private String lastMsg;

}
