package com.jm.business.domain.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>微信红包调用参数</p>
 *
 * @version latest
 * @Author chenyy
 * @Date 2016/10/29
 */
@Data
@ApiModel(description = "微信红包调用参数")
public class WxRedDo {
	
	@ApiModelProperty(value = "appid")
	private String  appid;
	
	@ApiModelProperty(value = "用户openid")
	private String openid;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;

	@ApiModelProperty(value = "付款金额，单位分")
	private Integer totalAmount;

	@ApiModelProperty(value = "红包发放总人数")
	private Integer totalNum;
	
	@ApiModelProperty(value = "红包祝福语")
	private String wishing;
	
	@ApiModelProperty(value = "调用接口的机器Ip地址")
	private String clientIp;
	
	@ApiModelProperty(value = "活动名称")
	private String actName;
	
	@ApiModelProperty(value = "备注信息")
	private String remark;

	public WxRedDo(){
		this.totalNum = 1 ;
	}

}
