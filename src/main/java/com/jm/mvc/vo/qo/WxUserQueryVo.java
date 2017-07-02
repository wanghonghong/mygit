package com.jm.mvc.vo.qo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "微信用户查询vo")
public class WxUserQueryVo {
	
	 @ApiModelProperty(value = "昵称")
	 private String nikename;
	 
	 @ApiModelProperty(value = "姓名")
	 private String name;
	 
	 @ApiModelProperty(value = "手机号")
	 private String phoneNum;
	 
	 @ApiModelProperty(value = "地区")
	 private String area;
	 
	 @ApiModelProperty(value = "性别")
	 private String sex;
	 
	 @ApiModelProperty(value = "角色id")
	 private String roleId;
	 
	 /**
	     * 关注开始时间
	     */
	 private String starSubscribeTime;
	    
	    /**
	     * 关注结束时间
	     */
	 private String endSubscribeTime;
	 
	 /**
	     * 购买开始时间
	     */
	 private String starBuyTime;
	    
	    /**
	     * 购买结束时间
	     */
	 private String endBuyTime;
	 
	 /**
	     * 佣金发放开始时间
	     */
	 private String starSendTime;
	    
	    /**
	     * 佣金发放结束时间
	     */
	 private String endSendTime;
	 
	 /**
	     *是否关注  1已经关注，0未关注
	  */
	 private Integer isSubscribe;
	 
	 @ApiModelProperty(value = "每页大小")
	 private Integer pageSize;
	    
    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    
    @ApiModelProperty(value = "appid")
    private String appid;
    
    private Integer shopid;

	private int lastType;

    public WxUserQueryVo(){
        this.pageSize=10;
        this.curPage=0;
    }


	 
	 
}
