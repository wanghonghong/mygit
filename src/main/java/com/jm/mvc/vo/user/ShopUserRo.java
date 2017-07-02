package com.jm.mvc.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>店铺用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/12
 */
@Data
public class ShopUserRo {

	private Integer id;

	private Integer shopId;

	private String userName;

	private String phoneNumber;

	@ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 ")
	private int agentRole ;

	@ApiModelProperty(value = "支付宝账号")
	private String alipay;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
}
