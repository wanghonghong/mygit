package com.jm.mvc.vo.shop;

import com.jm.repository.po.order.OrderInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户佣金详情")
public class UserCommissionVo {

	@ApiModelProperty(value = "店铺ID")
	private Integer shopId;

	@ApiModelProperty(value = "用户ID")
	private Integer userid;

}
