package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>分销设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "分销设置")
public class BrokerageSetCo {

    @ApiModelProperty(value = "是否开通")
    private int isOpen;

    @ApiModelProperty(value = "是否开通 0无权限  1开启 2关闭 ")
    private int isFansShop;

    @ApiModelProperty(value = "类型：1:1-2级分销2:1-4档代理3:分销系统+佣金升级4:分销代理5:分销代理+佣金升级6:动销代理7:动销代理+佣金升级")
    private int type;

    @ApiModelProperty(value = "一级佣金")
    private int one;

    @ApiModelProperty(value = "二级佣金")
    private int two;

    @ApiModelProperty(value = "三级佣金")
    private int three;

    @ApiModelProperty(value = "a档收费")
    private int afee;

    @ApiModelProperty(value = "b档收费")
    private int bfee;

    @ApiModelProperty(value = "c档收费")
    private int cfee;

    @ApiModelProperty(value = "一级佣金a档升级")
    private int oneAup;

    @ApiModelProperty(value = "一级佣金b档升级")
    private int oneBup;

    @ApiModelProperty(value = "一级佣金c档升级")
    private int oneCup;

    @ApiModelProperty(value = "二级佣金a档升级")
    private int twoAup;

    @ApiModelProperty(value = "二级佣金b档升级")
    private int twoBup;

    @ApiModelProperty(value = "二级佣金c档升级")
    private int twoCup;

    @ApiModelProperty(value = "代理A")
    private int agentA;

    @ApiModelProperty(value = "代理B")
    private int agentB;

    @ApiModelProperty(value = "代理C")
    private int agentC;

    @ApiModelProperty(value = "代理D")
    private int agentD;

    @ApiModelProperty(value = "代理A费用")
    private int agentAfee;

    @ApiModelProperty(value = "代理B费用")
    private int agentBfee;

    @ApiModelProperty(value = "代理C费用")
    private int agentCfee;

    @ApiModelProperty(value = "代理D费用")
    private int agentDfee;

    @ApiModelProperty(value = "异地提取")
    private int differentKit;

    @ApiModelProperty(value = "结算类型0：无1：折扣2：原价")
    private int settleType;

    @ApiModelProperty(value = "背景风格1:选择预置模板,2:上传自定义模板")
    private int backStyle;

    @ApiModelProperty(value = "预置类型：1,2,3")
    private int preStyle;

    @ApiModelProperty(value = "背景图片地址")
    private String backUrl;

    @ApiModelProperty(value = "模式1免费模式，2收费模式")
    private int mode;

    @ApiModelProperty(value = "金额")
    private int money;

    @ApiModelProperty(value = "免费小店数量")
    private int freeCount;

    private BrokerageSetCo(){
        this.freeCount=2000;
    }

}
