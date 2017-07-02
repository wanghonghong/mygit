package com.jm.mvc.vo.wx.wxuser;

import java.util.Date;

import lombok.Data;

@Data
public class WxUserVo {

    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    private String userName;

    private String headimgurl;

    private String phoneNumber;

    private String appid;

    private String openid;

    private String unionId;
    /**
     * 关注时间
     */
    private Date subscribeTime;

    private Integer sex;

    private String areaCode;

    private String nickname;
    /**
     * 上级
     */
    private Integer upperOne;

    /**
     * 上级的上级
     */
    private Integer upperTwo;
    /**
     * 上级的上级的上级
     */
    private Integer upperThree;

    private String  remark;

    /**
     * 累计佣金
     */
    private Integer totalBrokerage;

    /**
     * 已发放的佣金
     */
    private Integer issuedBrokerage;

    /**
     * 发放时间
     */
    private Date sendTime;

    /**
     *是否关注  1已经关注，0未关注
     */
    private Integer isSubscribe;

    /**
     * 二维码地址
     */
    private String  qrcodeurl;

    /**
     * 二维码过期时间
     */
    private Date expirationTime;

    /**
     * 用户星级1-5
     */
    private int star;

    /**
     * 用户对应的环信账号
     * */
    private String hxAccount;

    /**
     * 最后聊天记录
     * */
    private String lastChatMsg;

    /**
     * 最后聊天时间
     * */
    private Date lastChatDate;

    /**
     * 是否回复
     * */
    private Integer isReply;
}
