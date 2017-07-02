package com.jm.mvc.vo.product.trad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *<p>商品新增vo</p>
 *
 * @author zhengww
 * @version latest
 * @data 2016年5月23日
 */
@Data
@ApiModel(description = "用于交易模式商品修改状态--支持批量")
public class TradProductQo {

	@ApiModelProperty(value = "活动名称名称")
	private String name;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "活动状态 0：未开始   1：进行中  2：暂停活动  3：已结束  9：删除")
	private int  status;

	@ApiModelProperty(value = "交易模式类型 1:一元夺宝   2：拼团 3.秒杀  4.拍卖  5.众筹")
	private int  type;

	@ApiModelProperty(value = "活动开始开始时间")
	private Date startDate;

	@ApiModelProperty(value = "活动开始结束时间")
	private Date startDate2;

	@ApiModelProperty(value = "活动结束开始时间")
	private Date endDate;

	@ApiModelProperty(value = "创建结束结束时间")
	private Date endDate2;

	@ApiModelProperty(value = "每页大小")
	private Integer pageSize;

	@ApiModelProperty(value = "当前页")
	private Integer curPage;

	public TradProductQo(){
		this.pageSize=10;
		this.curPage=0;
	}
}
