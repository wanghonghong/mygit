package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class UserUo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @ApiModelProperty(value = "头像")
    private String headImg;


    @ApiModelProperty(value = "最后聊天记录")
    private String lastMsg;

    @ApiModelProperty(value="最后聊天时间")
    private Date lastChatDate;

    @ApiModelProperty(value="商家环信聊天账号")
    private String hxAccount;

    @ApiModelProperty(value="微信账号 S:商家 C:用户")
    private String accountFlag;
}
