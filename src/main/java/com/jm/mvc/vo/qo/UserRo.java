package com.jm.mvc.vo.qo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(discriminator = "用户中心")
public class UserRo {

    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    private String userName;

    private String headimgurl;

    private String phoneNumber;

    /**
     * 关注时间
     */
    private Date subscribeTime;

    private Integer sex;

    private String nickname;

    private String  remark;

    /**
     * 购买总金额
     */
    private Integer allprice;

    /**
     * 购买次数
     */
    private Integer frequency;

    /**
     * 购买平均金额
     */
    private String average;

    /**
     * 最后购买时间
     */
    private String lasttime;

    /**
     * 累计佣金
     */
    private Integer totalBrokerage;

    /**
     * 已发放的佣金
     */
    private Integer issuedBrokerage;


    private String unissued;
}
