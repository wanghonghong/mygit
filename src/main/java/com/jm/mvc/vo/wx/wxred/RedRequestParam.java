package com.jm.mvc.vo.wx.wxred;
import lombok.Data;

/**
 *<p>红包请求参数</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月27日
 */
@Data
public class RedRequestParam {
	/**
	 * 随机字符串，不长于32位
	 */
	private String nonceStr;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 商户订单号（每个订单号必须唯一）组成：mch_id+yyyymmdd+10位一天内不能重复的数字。接口根据商户订单号支持重入，如出现超时可再调用。
	 */
	private String mchBillno;
	
	/**
	 * 微信支付分配的商户号
	 */
	private String mchId;
	
	/**
	 * 公众账号appid
	 */
	private String wxappid;
	
	/**
	 * 商户名称
	 */
	private String sendName;
	
	/**
	 * 用户openid
	 */
	private String reOpenid;
	
	/**
	 * 付款金额，单位分
	 */
	private Integer totalAmount;
	
	/**
	 * 红包发放总人数
	 */
	private Integer totalNum;
	
	/**
	 * 红包祝福语
	 */
	private String wishing;
	
	/**
	 * 调用接口的机器Ip地址
	 */
	private String clientIp;
	
	/**
	 * 活动名称
	 */
	private String actName;
	
	/**
	 * 备注信息
	 */
	private String remark;
	
	
	//跟微信无关
	private String appkey;
	

}
