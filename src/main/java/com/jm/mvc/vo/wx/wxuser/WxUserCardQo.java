package com.jm.mvc.vo.wx.wxuser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WxUserCardQo {

    //卡券id
    private Integer cardId;

    //"使用状态：0未使用，1已使用，9删除"
    private Integer status;

    @ApiModelProperty(value = "使用条件 0 是不限制，1 是购买达到多少金额")
    private Integer userCondition;

    @ApiModelProperty(value = "购买达到多少金额")
    private int buyMoney;

    @ApiModelProperty(value = "使用限定 0 是全店通用，1 是指定商品")
    private int userLimit;

    @ApiModelProperty(value = "生效时间")
    private Date startTime;

    @ApiModelProperty(value = "失效时间")
    private Date endTime;

    @ApiModelProperty(value = "归属于该店铺")
    private Integer shopId;

    private Integer userId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public WxUserCardQo(){
        this.pageSize = 10;
        this.curPage = 0;
    }

}
