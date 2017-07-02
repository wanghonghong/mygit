package com.jm.mvc.vo.shop.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>门店卡券</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/25
 */
@Data
@ApiModel(description = "门店卡券")
public class ShopCardUo {

    private Integer id;
    
    @ApiModelProperty(value = "归属于该店铺")
    private Integer shopId;

    @ApiModelProperty(value = "卡券名称")
    private String cardName;
    
    @ApiModelProperty(value = "卡券金额")
    private int cardMoney;

    @ApiModelProperty(value = "颜色")
    private String colour;

    @ApiModelProperty(value = "1 固定有效期 2 自定义有效期")
    private int periodType;

    @ApiModelProperty(value = "固定几天有效")
    private int fixDate;

    @ApiModelProperty(value = "生效时间")
    private Date startTime;

    @ApiModelProperty(value = "失效时间")
    private Date endTime;

    @ApiModelProperty(value = "到期提醒")
    private int expireWarn;

    @ApiModelProperty(value = "使用条件 0 是不限制，1 是购买达到多少金额")
    private int userCondition;

    @ApiModelProperty(value = "购买达到多少金额")
    private int buyMoney;

    @ApiModelProperty(value = "使用限定 0 是全店通用，1 是指定商品")
    private int userLimit;

    @ApiModelProperty(value = "指定商品ID列表以逗号隔开")
    private String pids;

    @ApiModelProperty(value = "使用说明")
    private String userNote;


}
