package com.jm.mvc.vo.shop.activity;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActivityUserVo {
	
		@ApiModelProperty(value = "发放记录id")
		private Integer activityUserId;
	
	    @ApiModelProperty(value = "活动ID")
	    private Integer activityId;
	   
	    @ApiModelProperty(value = "appId")
	    private String appId;
	    
	    @ApiModelProperty(value = "公众号名称")
	    private String pubNickName;
	    
	    @ApiModelProperty(value = "头像")
	    private String headimgurl;
	    
	    @ApiModelProperty(value = "用户昵称")
	    private String nickname;
	    
	    @ApiModelProperty(value = "用户ID")
	    private Integer userId;
	    
	    @ApiModelProperty("活动类型 1:现金红包 2：卡券红包 3：红包墙 4：天天红包 5：聚客红包")
	    private Integer type;
	    
	    @ApiModelProperty("活动小类 1:首次关注发红包 2：已关注粉丝发红包 3：购买商品发红包 4：确认收货发红包 5：卡卷转赠发红包")
	    private Integer subType;
	    
	    @ApiModelProperty("红包平台 / 1:微信商城 2：微博商城")
	    private Integer platform;
	    
	    @ApiModelProperty(value = "0全部，1男，2女")
	    private int sex;
	    
	    @ApiModelProperty(value = "地区编号多个用,")
	    private String areaIds;
	    
	    @ApiModelProperty(value = "地区名称多个用,")
	    private String areaNames;
	    
	    @ApiModelProperty(value = "红包金额（分）")
	    private int redMoney;
	    
	    @ApiModelProperty(value = "领取状态： 0：未发送   1：未领取    2 已领取   3：发放失败    4：已退款")
	    private int status;
	    
	    @ApiModelProperty(value = "创建时间")
	    private Date createTime;
	

}
