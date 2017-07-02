package com.jm.mvc.vo.wx.wxuser;


import com.jm.repository.po.shop.activity.CardProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WxUserCardVo implements Comparable{

    private Integer ticketId;

    //wxUserid
    private Integer userId;

   //卡券id
    private Integer cardId;

    //到期时间
    private Date expireTime;

    //"使用状态：0未使用，1已使用，9删除"
    private Integer status;

   //"卡券类型：0自己领取，1赠送的"
    private int type;

    //"分享人id"
    private Integer shareId;

    private String shareName;

    @ApiModelProperty(value = "归属于该店铺")
    private Integer shopId;

    private String shopName;

    @ApiModelProperty(value = "卡券名称")
    private String cardName;

    @ApiModelProperty(value = "卡券金额")
    private int cardMoney;

    @ApiModelProperty(value = "生效时间")
    private Date startTime;

    @ApiModelProperty(value = "失效时间")
    private Date endTime;

    @ApiModelProperty(value = "1 固定有效期 2 自定义有效期")
    private int periodType;

    @ApiModelProperty(value = "使用条件 0 是不限制，1 是购买达到多少金额")
    private Integer userCondition;

    @ApiModelProperty(value = "购买达到多少金额")
    private int buyMoney;

    @ApiModelProperty(value = "使用限定 0 是全店通用，1 是指定商品")
    private int userLimit;

    @ApiModelProperty(value = "指定商品的商品id")
    private List<CardProduct> cardProducts;

    @Override
    public int compareTo(Object o) {
      if(o instanceof WxUserCardVo ){
        int money = ((WxUserCardVo) o).getCardMoney();
       return   money - this.getCardMoney();
      }else{
        return -1;
      }
    }
}
