package com.jm.business.domain.shop;

import com.jm.repository.po.wx.WxUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>活动红包领域对象模型</p>
 *
 * @version latest
 * @Author wukf
 * @Date 2016/10/27
 */
@Data
@ApiModel(description = "活动红包领域对象模型,做活动时候发送现金红包的领域对象")
public class ActivityRedDo {

    private WxUser wxUser;

    private String clientIp;

    @ApiModelProperty("活动类型 1:现金红包 2：优惠卷红包 3：红包墙  5：聚客红包")
    private Integer type;

    @ApiModelProperty("活动小类 1:首次关注发红包 2：已关注粉丝发红包 3：购买商品发红包 4：确认收货发红包 5：卡卷转赠发红包")
    private Integer subType;

    @ApiModelProperty(value = "奖励类型：1 登录 2 推荐关注 3 购买返利")
    private Integer platform;

    @ApiModelProperty(value = "购买金额")
    private Integer buyMoney;

    public ActivityRedDo(){
        this.platform = 1;
        this.type = 1;
    }

    public ActivityRedDo(WxUser wxUser,String clientIp,Integer subType){
        this.platform = 1;
        this.type = 1;
        this.wxUser = wxUser;
        this.clientIp = clientIp;
        this.subType = subType;
    }

    public ActivityRedDo(WxUser wxUser,String clientIp,Integer subType,Integer buyMoney){
        this.platform = 1;
        this.type = 1;
        this.wxUser = wxUser;
        this.clientIp = clientIp;
        this.subType = subType;
        this.buyMoney = buyMoney;
    }



}
