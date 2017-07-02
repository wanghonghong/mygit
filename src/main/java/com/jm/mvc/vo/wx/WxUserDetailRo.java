package com.jm.mvc.vo.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WxUserDetailRo {

	private Integer userId;

	private String nickname;

	private String userName;

	private Integer sex;

	private String groupName;

	private Integer platform;

	private String phoneNumber;

	private String agentRole;

	private String levelName;

	private List<String> adds;

	private String upNickname;

	private String upUserName;

	private String upPhoneNumber;

	private Integer oneCount;

	private Integer twoCount;

	@ApiModelProperty(value = "备注")
	private String  remark;

	@ApiModelProperty(value = "累计佣金")
	private int sumBrokerage;

	@ApiModelProperty(value = "未发佣金")
	private int noSendBrokerage;

	@ApiModelProperty(value = "累计积分")
	private int sumIntegral;

	@ApiModelProperty(value = "余额积分")
	private int integral;

	@ApiModelProperty(value = "累计卡券")
	private int sumCard;

	@ApiModelProperty(value = "未用卡券")
	private int card;
}
