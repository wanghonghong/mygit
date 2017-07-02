package com.jm.mvc.vo.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@ApiModel(description = "分销红包发放参数")
public class RedSendParam{

	@ApiModelProperty(value = "主机ip")
	private String ip;

	@ApiModelProperty(value = "用户id")
	private Integer userId;

	private Integer shopId;

	@ApiModelProperty(value = "每次发放金额，没有特殊要求的话，该值传20000")
	private int everySend;

	@ApiModelProperty(value = "发放类型：1手动，2提现,3积分提现")
	private int putType;

	@ApiModelProperty(value = "0:手动发放1:满200,2:定期发放,3:满额发放,4免审核,5需审核,6积分提现")
	private int autoType;

	@ApiModelProperty(value = "发放金额，如果传0，默认全额发放")
	private int sendMoney;

	@ApiModelProperty(value = "提现 id  无不传，有的话传id")
	private Integer kitId;
}
