package com.jm.business.service.wx;


import com.google.zxing.common.BitMatrix;
import com.jm.business.domain.wx.WxRefundDo;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.system.ResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.wx.RefundRecodCo;
import com.jm.mvc.vo.wx.WeixinPayVo;
import com.jm.mvc.vo.wx.wxred.WxRefundDto;
import com.jm.repository.client.WxClient;
import com.jm.repository.jpa.system.JmRechargeQrcodeRepository;
import com.jm.repository.jpa.wx.RefundRecodRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.RefundRecod;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.system.JmRechargeOrder;
import com.jm.repository.po.system.JmRechargeQrcode;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.ZxingUtil;
import com.jm.staticcode.util.wx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信支付相关业务
 * @author chenyy
 * date 2016-10-31
 */
@Slf4j
@Service
public class WxPayService {
	
	@Autowired
	private RefundRecodRepository recodRepository;
	@Autowired
	private JmRechargeQrcodeRepository jmRechargeQrcodeRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private WxPubAccountService wxpubAccountService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private WxClient wxClient;
    @Autowired
    private ResourceService resourceService;
	/**
	 * 退款
	 * @return
	 * @throws Exception 
	 */
	public JmMessage reFun(RefundRecodCo refunRecodCo) throws Exception{
		OrderInfo orderInfo = orderService.getOrderInfo(refunRecodCo.getOrderId());
		if(orderInfo==null){//订单为空
			return new JmMessage(-1, "error！","订单为空！");
		}
		WxPubAccount account = wxpubAccountService.findWxPubAccountByAppid(refunRecodCo.getAppid());
		WxRefundDo  funDo = new WxRefundDo();
		String nonceStr = UUID.randomUUID().toString().replace("-", "");
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String appKey = "";
		if(null!=account.getIsSub() && account.getIsSub().intValue()==1){//是子服务商
			funDo.setAppid(Constant.SERVICE_APPID);
			funDo.setMchId(Constant.SERVICE_MCHID);
			funDo.setSubAppid(account.getAppid());
			funDo.setSubMchId(account.getMchId());
			appKey = Constant.SERVICE_APPKEY;
		}else{
			funDo.setAppid(account.getAppid());
			funDo.setMchId(account.getMchId());
			appKey = account.getAppKey();
		}
		funDo.setDeviceInfo(refunRecodCo.getClientIp());
		funDo.setOutTradeNo(orderInfo.getOrderNum());
		String outRefundNo = account.getMchId()+format.format(new Date())+TenpayUtil.buildRandom(10)+"";
		funDo.setOutRefundNo(outRefundNo);
		funDo.setNonceStr(nonceStr);
		funDo.setTotalFee(orderInfo.getRealPrice());
		funDo.setRefundFee(refunRecodCo.getRefundFee());
		log.info("=========funDo.getTotalFee()======"+funDo.getTotalFee());
		String xmlContent = addParam(funDo, appKey,account.getIsSub());
		String xmlRes = WeixinRedUtils.checkApiclientcert(funDo.getMchId(),xmlContent,WxUrl.WX_PAY_RE_FUN);
		Map<String, Object> json  = WeixinUtil.converXml2Json(xmlRes);
		WxRefundDto dto = JsonMapper.map2Obj(json,WxRefundDto.class);
		log.info("=========Refun result param======"+dto.toString());
		return saveRefunRecod(dto, refunRecodCo, orderInfo.getOrderInfoId(), account.getMchId());
	}
	
	
	
	 /**
     * 保存流水
     */
    public JmMessage saveRefunRecod(WxRefundDto dto,RefundRecodCo refunRecodCo,Long orderInfoId,String mchId){
		// 成功才插入流水
		if ("SUCCESS".equals(dto.getReturnCode())&& "SUCCESS".equals(dto.getResultCode())) {
			DateFormat format = new SimpleDateFormat("yyyyMMdd");
			// 处理自己本地业务
			RefundRecod recod = new RefundRecod();
			recod.setOrderId(orderInfoId);
			recod.setOpUserId(refunRecodCo.getOpUserId());
			recod.setRefundFee(refunRecodCo.getRefundFee());
			recod.setRefundId(dto.getRefundId());
			recod.setReFunType(1);
			recod.setReFunTime(new Date());
			recod.setOutRefundNo(dto.getOutRefundNo());
			String reFunNo = mchId + format.format(new Date())+ TenpayUtil.buildRandom(10) + "";
			recod.setReFunNo(reFunNo);
			recodRepository.save(recod);
			log.info("========REFUND SUCCESS=========");
			return new JmMessage(0, "OK");
		} else {
			log.info("========errcode=========== "+dto.getErrCode());
			log.info("========errormsg========== "+dto.getErrCodeDes());
			return new JmMessage(-1, dto.getErrCode(), dto.getErrCodeDes());
		}
    	
    }
	
	
	/**
	 * 拼装xml参数
	 * @param funDo
	 * @param appkey
	 * @return
	 */
	public String addParam(WxRefundDo funDo,String appkey,Integer isSub){
		 StringBuffer xmlContent = new StringBuffer();
		 String sign = createReFunSign(funDo, appkey,isSub);
		  xmlContent.append("<xml>");
	         xmlContent.append("<appid><![CDATA[").append(funDo.getAppid()).append("]]></appid>");
	         xmlContent.append("<mch_id><![CDATA[").append(funDo.getMchId()).append("]]></mch_id>");
	         if(isSub!=null && isSub.intValue()==1){
	        	 //是子服务商
	        	 xmlContent.append("<sub_appid><![CDATA[").append(funDo.getSubAppid()).append("]]></sub_appid>");
		         xmlContent.append("<sub_mch_id><![CDATA[").append(funDo.getSubMchId()).append("]]></sub_mch_id>");
	         }
	         xmlContent.append("<nonce_str><![CDATA[").append(funDo.getNonceStr()).append("]]></nonce_str>");
	         xmlContent.append("<op_user_id><![CDATA[").append(funDo.getMchId()).append("]]></op_user_id>");
	         xmlContent.append("<out_refund_no><![CDATA[").append(funDo.getOutRefundNo()).append("]]></out_refund_no>");
	         xmlContent.append("<out_trade_no><![CDATA[").append(funDo.getOutTradeNo()).append("]]></out_trade_no>");
	         xmlContent.append("<refund_fee><![CDATA[").append(String.valueOf(funDo.getRefundFee())).append("]]></refund_fee>");
	         xmlContent.append("<total_fee><![CDATA[").append(String.valueOf(funDo.getTotalFee())).append("]]></total_fee>");
	         xmlContent.append("<refund_account><![CDATA[").append("REFUND_SOURCE_RECHARGE_FUNDS").append("]]></refund_account>");
	         xmlContent.append("<sign><![CDATA[").append(sign).append("]]></sign>");
	         xmlContent.append("</xml>");
		 return xmlContent.toString();
	}
	
	/**
	 * 生成退款签名
	 * @param funDo
	 * @param appkey
	 * @return
	 */
	public String createReFunSign(WxRefundDo funDo,String appkey,Integer isSub){
		SortedMap<String, String> nativeObj = new TreeMap<String, String>();
		nativeObj.put("appid", funDo.getAppid());
		nativeObj.put("mch_id", funDo.getMchId());
		if(isSub!=null && isSub.intValue()==1){//是子服务商
			nativeObj.put("sub_appid",funDo.getSubAppid());
			nativeObj.put("sub_mch_id",funDo.getSubMchId());
		}
		nativeObj.put("nonce_str",funDo.getNonceStr());
		nativeObj.put("op_user_id",funDo.getMchId());//操作员帐号, 默认为商户号
		nativeObj.put("out_trade_no",funDo.getOutTradeNo());
		nativeObj.put("out_refund_no", funDo.getOutRefundNo());
		nativeObj.put("total_fee", String.valueOf(funDo.getTotalFee()));
		nativeObj.put("refund_fee",String.valueOf(funDo.getRefundFee()));
		nativeObj.put("refund_account",String.valueOf("REFUND_SOURCE_RECHARGE_FUNDS"));
		return WeixinPayUtil.createSign(nativeObj,appkey);
	}
	
	
	/**
	 * 普通购买商品参数组装
	 * @param payVo
	 * @param orderInfo
	 * @param isSub
	 */
	public void purchasePruduct(WeixinPayVo payVo,OrderInfo orderInfo,String shopName,int isSub){
		payVo.setOutTradeNo(orderInfo.getOrderNum()); //传订单号
		payVo.setBody(shopName+"购物");
		payVo.setNotifyUrl(Constant.DOMAIN+"/pay/pay_record");//支付成功回调方法，不可以带参数
		Integer payMoney = orderInfo.getRealPrice();
		payVo.setTotalFee(payMoney);//orderInfo.getTotalPrice()+orderInfo.getSendFee()
		//将订单id，流水号，价格 设置到attach里，支付完成回调就取这里面的。
		log.info("===================totalPrice" + orderInfo.getTotalPrice()+orderInfo.getSendFee());

		payVo.setAttach(orderInfo.getOrderInfoId()+","+payMoney+","+isSub);
	}
	
	/**
	 * 积分充值
	 * @param payVo
	 * @param isSub
	 */
	public void integralRecharge(WeixinPayVo payVo,RechargeOrder rechargeOrder,String shopName,int isSub){
		payVo.setOutTradeNo(rechargeOrder.getOrderNum()); //传订单号
		
		String  title = "";
		if(rechargeOrder.getType()!=null && rechargeOrder.getType().intValue()==1)title="积分充值";
		else title="渠道充值";
		payVo.setBody(shopName+title);
		payVo.setNotifyUrl(Constant.DOMAIN+"/pay/integral_recode");//支付成功回调方法，不可以带参数
		payVo.setTotalFee(rechargeOrder.getMoney());
		Integer payMoney =  rechargeOrder.getMoney();
		payVo.setAttach(rechargeOrder.getOrderInfoId()+","+payMoney+","+isSub);
	}
	
	
	/**
	 * 拼装VO
	 * @param wxUserSession
	 * @param account
	 * @return
	 */
	public WeixinPayVo getPayVo(WxUserSession wxUserSession,WxPubAccount account,Long orderInfoId,int orderType){
		WeixinPayVo payVo = new WeixinPayVo();
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length()); //8位日期
		String strRandom = TenpayUtil.buildRandom(4) + ""; //四位随机数
		String strReq = strTime + strRandom; //10位序列号,可以自行调整。
		//判断如果是子商户就用聚米服务商的信息，否则就用用户自己本身的信息
		if(null!=account.getIsSub() && account.getIsSub().intValue()==1){//是子商户
			//直接用聚米为谷的服务商信息
			payVo.setMchId(Constant.SERVICE_MCHID);
			payVo.setAppid(Constant.SERVICE_APPID);
			payVo.setSubMchId(account.getMchId());
			payVo.setSubAppid(wxUserSession.getAppid());
		}else{
			//不是子商户
			payVo.setAppid(wxUserSession.getAppid());
			payVo.setMchId(account.getMchId());
		}
		
		payVo.setNonceStr(strReq);
		payVo.setTradeType("JSAPI");
		payVo.setOpenid(wxUserSession.getOpenid());
		payVo.setShopName(wxUserSession.getShopName());
		payVo.setOrderInfoId(orderInfoId);
		payVo.setType(orderType);
		
		if(orderType==0){//普通购买支付
			OrderInfo orderInfo = orderService.getOrderInfo(payVo.getOrderInfoId());
			payVo.setOrderInfo(orderInfo);
		}else if(orderType==1){//积分充值或渠道充值支付
			RechargeOrder rechargeOrder = integralService.findRechargeById(payVo.getOrderInfoId());
			payVo.setRechargeOrder(rechargeOrder);
			payVo.setIntegraType(rechargeOrder.getType());
		}
		return payVo;
	}
	
	/**
	 * 封装前端需要用的jsapi
	 * @param account
	 * @param payVo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getWxJsApi(WxPubAccount account,WeixinPayVo payVo,HttpServletRequest request) throws Exception{
		String appKey = "";
		//判断如果是子商户就用聚米服务商的信息，否则就用用户自己本身的信息
		if(null!=account.getIsSub() && account.getIsSub().intValue()==1){//是子商户
			appKey = Constant.SERVICE_APPKEY;
		}else{
			appKey = account.getAppKey();
		}
		payVo.setSpbillCreateIp(request.getRemoteHost());
		int isSub = 0;
		if(null != account.getIsSub()){
			isSub = account.getIsSub();
		}
		if(payVo.getType()==0){//普通购物为0
			//普通购物
			OrderInfo orderInfo = payVo.getOrderInfo(); //orderService.getOrderInfo(payVo.getOrderInfoId());
			purchasePruduct(payVo, orderInfo, payVo.getShopName(), isSub);
		}else if(payVo.getType()==1){
			//积分充值
			RechargeOrder rechargeOrder =payVo.getRechargeOrder(); //integralService.findRechargeById(payVo.getOrderInfoId());
			integralRecharge(payVo,rechargeOrder,payVo.getShopName(), isSub);
		}
		String wx_js_api = WeixinPayUtil.goconfirm(request, payVo,appKey,account.getIsSub());
		return wx_js_api;

	}
	
	/**
	 * 微信扫码支付 返回支付二维码图片
	 * @return
	 * @throws Exception 
	 */
	public String wxCodePay(JmRechargeOrder rechargeOrder,HttpServletRequest request,Integer userId) throws Exception{
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length()); //8位日期
		String strRandom = TenpayUtil.buildRandom(4) + ""; //四位随机数
		String mch_id = Constant.QRCODE_PAY_MCH_ID;//固定的收入号
		String key = Constant.QRCODE_PAY_KEY;
		String nonce_str  = strTime + strRandom; //随机数
		String order_price = rechargeOrder.getMoney()+""; // 价格   注意：价格的单位是分  
		String body  = "聚米为谷商家账户充值";//body
		String out_trade_no = rechargeOrder.getOrderNum(); // 订单号  
		String spbill_create_ip = request.getRemoteHost();
		String notify_url = Constant.PC_DOMAIN+"/pay/qrcode_pay_callback";
		String trade_type = "NATIVE";
		 	SortedMap<String,String> packageParams = new TreeMap<String,String>();  
	        packageParams.put("appid", Constant.SERVICE_APPID);  
	        packageParams.put("mch_id", mch_id);  
	        packageParams.put("nonce_str", nonce_str);  
	        packageParams.put("body", body);  
	        packageParams.put("attach", rechargeOrder.getOrderInfoId()+""); 
	        packageParams.put("out_trade_no", out_trade_no);  
	        packageParams.put("total_fee", order_price);  
	        packageParams.put("spbill_create_ip", spbill_create_ip);  
	        packageParams.put("notify_url", notify_url);  
	        packageParams.put("trade_type", trade_type);  
	        packageParams.put("sign_type", "MD5");
	       	packageParams.put("sign", WeixinPayUtil.createSign(packageParams,key));
	       	String requestXML = WxUtil.getRequestXml(packageParams);
			String resultXML  = wxClient.sendPostToCodePay(requestXML);
			Map<String,String> resultMap = WxUtil.parseXmlToMap(resultXML);
			JmMessage msg  = getQrcoce(resultMap.get("code_url"), request);
			
			//将二维码加到数据库
			JmRechargeQrcode rechargeQrcode = new JmRechargeQrcode();
			rechargeQrcode.setUserId(userId);
			rechargeQrcode.setCreateTime(new Date());
			rechargeQrcode.setQrcodeUrl(msg.getMsg());
			jmRechargeQrcodeRepository.save(rechargeQrcode);
		return msg.getMsg();
	}
	
	

	/**
	 *  生成二维码并上传到图片服务器
	 * @param url
	 * @return JmMessage
	 * @throws Exception
	 */
	public JmMessage getQrcoce(String url,HttpServletRequest request) throws Exception{
		//生成二维码  
		BitMatrix bi = ZxingUtil.toQRCodeMatrix(url, null, null);
		BufferedImage ImageOne = ZxingUtil.writeToFile(bi);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageOne, "jpg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		JmMessage msg=resourceService.uploadFile(is);
		return msg;
	}
	
	
}
