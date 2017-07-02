package com.jm.mvc.vo.online;

import lombok.Data;

import java.util.Date;

@Data
public class HxUserVo {
    private String hxAccount;
    private Date lastChatDate;
    private String lastMsg;
    private Integer isReply;
    private String chatType;
    private Integer channel;

}
