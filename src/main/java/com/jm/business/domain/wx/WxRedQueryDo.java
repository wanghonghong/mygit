package com.jm.business.domain.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信红包记录查询do
 * @author chenyy
 *
 */
@Data
public class WxRedQueryDo {
	
	@ApiModelProperty(value = "商户订单号")
	private String mchBillno;
	
	@ApiModelProperty(value = "Appid")
	private String appid;

}
