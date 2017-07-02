package com.jm.mvc.vo.wx.wxred;

import lombok.Data;

@Data
public class WeixinActInfo {
	/**
	 * appid
	 */
	private String  appid;
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
	 * 单人获得红包金额 ,单位分
	 */
	private Integer singleRedPacket;
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
	 * 祝福语
	 */
	private String blessings;
	/**
	 * 备注信息
	 */
	private String remark;
	
	
	/**
	 * 商户key
	 */
	private String appkey;
	
	/**
	 * 店铺id
	 */
	private Integer shopId;
	
	/**
	 * 活动类型
	 */
	private Integer typeId;
	
	/**
	 * 活动id
	 */
	private Integer activityId;
	
	/**
	 * 子商户号
	 */
	private String subMchId;
	
	/**
	 * 	 触达用户appid
	 */
	private String msgappid;
	
	/**
	 * 
	 */
	private String consumeMchId;
	
	/**
	 * 是否有资格发送红包
	 * 0：失败 1：成功
	 */
	private Integer isSuccess;
	/**
	 * 失败推送语
	 */
	private String noWinInfo;
}
