package com.jm.repository.po.online;

import lombok.Data;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class HxUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hxId;

    private Integer userId;

    private String hxAccount;

    private Date createTime;

    private Date lastChatDate;

    private String lastMsg;

    private Integer isReply;

    private String chatType;

    private Integer channel;
}
