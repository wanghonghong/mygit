package com.jm.mvc.vo.shop;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 新增商品分组
 * @author zhengww
 *
 */
@Data
@ApiModel(description = "佣金设置")
public class ShopSettingForUpdateVo {

    @ApiModelProperty(value = "佣金设置ID")
    private Integer shopSetId;
	
	@ApiModelProperty(value = "店铺id")
	private Integer shopId;
	
	@ApiModelProperty(value = "是否启用分销系统")
	private Integer isOpen;
	
	@ApiModelProperty(value = "一级佣金比例")
	private Integer brokerageOne;
	
	@ApiModelProperty(value = "二级佣金比例")
	private Integer brokerageTwo;
	
	@ApiModelProperty(value = "三级佣金比例")
	private Integer brokerageThree;
	
	@ApiModelProperty(value = "发放类型")
	private Integer payType;//0为立即发放，1.延时发放，2，人工审核

	@ApiModelProperty(value = "是否正常发放")
	private Integer isSend;//0为正常发放，1为暂停发放

	@ApiModelProperty(value = "延迟类型")
	private Integer timeSetting;//0为延迟天数，1为次月
	
	@ApiModelProperty(value = "延迟天数")
	private Integer delayDate;//1-99天
	
	@ApiModelProperty(value = "次月日期")
	private Date nextMonth;//次月

	public ShopSettingForUpdateVo(){
		this.isSend=0;
	}
}
