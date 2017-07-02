package com.jm.mvc.vo.shop.activity;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityUserQo {
	
    @ApiModelProperty("活动名称")
    private String activityName;
    
    @ApiModelProperty("公众号名称")
    private String pubNickName;

    @ApiModelProperty("红包平台 / 1:微信商城 2：微博商城")
    private int platform;
    
    @ApiModelProperty(value = "appid")
    private String appid;
    
    @ApiModelProperty(value = "平台用户id，非微信用户id")
    private Integer userId;
    
    @ApiModelProperty(value = "领取状态： 0：未发送   1：未领取    2 已领取   3：发放失败    4：已退款")
    private int status;
    

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    @ApiModelProperty(value = "开始时间")
    private Date startDate;
    
    @ApiModelProperty(value = "结束时间")
    private Date endtDate;
    
    public ActivityUserQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }

}
