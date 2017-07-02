package com.jm.mvc.vo.wx.wxred;

import lombok.Data;

/**
 *<p>现金红包请求返回参数</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月27日
 */
@Data
public class RedResultParam {
	
	/**
	 * 发送红包返回的红包记录id，无论成功失败都会返回
	 */
	private Integer redPayId;
	
	/**
	 * 返回状态码      SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 */
	private String returnCode;
	
	/**
	 * 签名失败  返回信息，如非空，为错误原因签名失败参数格式校验错误
	 */
	private String returnMsg;
	
	
	//以下字段在return_code为SUCCESS的时候有返回
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 业务结果   SUCCESS/FAIL
	 */
	private String resultCode;
	
	/**
	 * 错误代码
	 */
	private String errCode;
	
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;
	
	
	//以下字段在return_code和result_code都为SUCCESS的时候有返回
	
	/**
	 * 商户订单号
	 */
	private String mchBillno;
	
	/**
	 * 商户号
	 */
	private String mchId;
	
	/**
	 * 公众账号appid
	 */
	private String wxappid;
	
	/**
	 * 用户openid
	 */
	private String reOpenid;
	
	/**
	 * 付款金额
	 */
	private String totalAmount;
	
	/**
	 * 发放成功时间
	 */
	private Integer sendTime;
	
	/**
	 * 微信单号  红包订单的微信单号
	 */
	private String sendListid;
	 
	
	

}
