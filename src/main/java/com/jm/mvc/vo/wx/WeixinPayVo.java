package com.jm.mvc.vo.wx;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.jm.mvc.vo.qo.OrderDetailQo;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.shop.RechargeOrder;

/**
 *<p>微信支付传递参数实体类</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月1日
 */
@Data
public class WeixinPayVo implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
		private String appid;//微信分配的公众账号ID
		
		private String mchId;//微信支付分配的商户号
		
		private String subMchId;//子商户号
		
		private String subAppid;//子商户appid
		
		private String deviceInfo;//终端设备号
		
		private String nonceStr;//随机字符串，不长于32位
		
		private String sign;//签名
		
		private String body;//商品或支付单简要描述
		
		private String detail;//商品名称明细列表
		
		private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
		
		private String outTradeNo;//商户系统内部的订单号,32个字符内、可包含字母
		
		private String feeType;//符合ISO 4217标准的三位字母代码，默认人民币：CNY
		
		private Integer totalFee;//订单总金额，单位为分
		
		private String spbillCreateIp;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
		
		private String timeStart;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
		
		private String timeExpire;//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。	注意：最短失效时间间隔必须大于5分钟
		
		private String goodsTag;//商品标记，代金券或立减优惠功能的参数
		
		private String notifyUrl;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数
		
		private String tradeType;//取值如下：JSAPI，NATIVE，APP
		
		private String productId;//trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
		
		private String limitPay;//no_credit--指定不能使用信用卡支付
		
		private String openid;//openid用户标识
		
		private String subOpenid;//子商户下的用户openid
		
		
		//多加字段，自己系统内使用的
		private Long orderInfoId;
		private int type;
		private String appkey;
		private String shopName;
		private OrderInfo orderInfo;
		private RechargeOrder rechargeOrder;
		
		private int integraType;//积分充值或者渠道充值的时候此值才有用
		
		public WeixinPayVo(){
			this.orderInfo=new OrderInfo();
			this.rechargeOrder = new RechargeOrder();
		}
	
		
		
		
		
}
