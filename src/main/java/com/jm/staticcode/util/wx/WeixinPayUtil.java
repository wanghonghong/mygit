package com.jm.staticcode.util.wx;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import com.jm.mvc.vo.wx.WeixinPayReturnParam;
import com.jm.mvc.vo.wx.WeixinPayVo;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JmRestTemplate;
import com.jm.staticcode.util.JsonMapper;

@Slf4j
public class WeixinPayUtil {
	
	/**
	 *<p>* 调用统一支付接口,获取预支付prepay_id并封装jsapi跳转的参数</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年6月4日
	 */
	public static String goconfirm(HttpServletRequest request,WeixinPayVo weixinPayVo,String appkey,Integer isSub) throws Exception { 
		//获取openid
		String openId  = weixinPayVo.getOpenid();
		log.info("支付时的微信帐号openId="+openId);
		// 支付代码
		String subMchId = "";
		String subAppid="";
		if(isSub!=null && isSub.intValue()==1){//是子商户
			subMchId = weixinPayVo.getSubMchId();
			subAppid = weixinPayVo.getSubAppid();
		}
		String prePayId = WeixinPayUtil.getWeiXinUnifiedOrder(weixinPayVo.getAppid(),weixinPayVo.getMchId(),weixinPayVo.getBody(), weixinPayVo.getAttach(), weixinPayVo.getOutTradeNo(), weixinPayVo.getSpbillCreateIp(),
				weixinPayVo.getNotifyUrl(), "JSAPI",weixinPayVo.getTotalFee(), openId,appkey,isSub,subMchId,subAppid);
		SortedMap<String, String> nativeObj = new TreeMap<String, String>();
		nativeObj.put("appId", weixinPayVo.getAppid());
		nativeObj.put("timeStamp", System.currentTimeMillis()/1000+"");
		nativeObj.put("nonceStr",weixinPayVo.getNonceStr());
		nativeObj.put("package", "prepay_id=" + prePayId);
		nativeObj.put("signType", "MD5");
		nativeObj.put("paySign", WeixinPayUtil.createSign(nativeObj,appkey));
		return JsonMapper.toJson(nativeObj);    
   }
	
	
	
	/**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(SortedMap<String, String> packageParams,String appkey) {
        StringBuffer sb = new StringBuffer();
        Set<Entry<String, String>> es = packageParams.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + appkey);
        log.info("======sb:" + sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        log.info("packge签名:" + sign);
        return sign;
    }
    
    
    /**
     * 4.1获取微信预支付订单ID
     * @param body 商品描述
     * @param attach 附加数据，原样返回
     * @param out_trade_no 商户系统内部的订单号,32 个字符内、可包含字母,确保 在商户系统唯一
     * @param spbill_create_ip 订单生成的机器 IP
     * @param notify_url 接收微信支付成功通知 
     * @param trade_type JSAPI、NATIVE、APP 
     * @param total_fee 订单总金额，单位为分，不 能带小数点 
     * @param openid 用户在商户 appid 下的唯一 标识，trade_type 为 JSAPI 时，此参数必传，获取方式 见表头说明。
     * @return
     * @throws IOException 
     */
    public static String getWeiXinUnifiedOrder(String appid,String mch_id,String body, String attach, String out_trade_no,
                                                String spbill_create_ip, String notify_url, String trade_type,
                                                Integer total_fee, String openid,String appkey,Integer isSub,String subMchId,String subAppid) throws IOException {
         String nonce_str = UUID.randomUUID().toString().replace("-", "");
         SortedMap<String, String> packageParams = new TreeMap<String, String>();
         packageParams.put("appid", appid);
         packageParams.put("mch_id",mch_id);
         if(isSub!=null && isSub.intValue()==1){//是子商户，需要加subMchId
        	 packageParams.put("sub_mch_id", subMchId);
        	 packageParams.put("sub_appid", subAppid);
        	 packageParams.put("sub_openid", openid);
         }else{
        	 packageParams.put("openid", openid);
         }
         packageParams.put("nonce_str", nonce_str);
         packageParams.put("body", body);
         packageParams.put("out_trade_no", out_trade_no);
         packageParams.put("total_fee", String.valueOf(total_fee));
         packageParams.put("spbill_create_ip", spbill_create_ip);
         packageParams.put("notify_url", notify_url);
         packageParams.put("trade_type", trade_type);
         packageParams.put("attach", attach);
         String sign = createSign(packageParams,appkey);
         packageParams.put("sign", sign);

         StringBuffer xmlContent = new StringBuffer();
         xmlContent.append("<xml>");
         xmlContent.append("<appid><![CDATA[").append(appid).append("]]></appid>");
         xmlContent.append("<mch_id><![CDATA[").append(mch_id).append("]]></mch_id>");
         if(isSub!=null && isSub.intValue()==1){//是子商户，需要加subMchId
        	 xmlContent.append("<sub_mch_id><![CDATA[").append(subMchId).append("]]></sub_mch_id>");
        	 xmlContent.append("<sub_appid><![CDATA[").append(subAppid).append("]]></sub_appid>");
        	 xmlContent.append("<sub_openid><![CDATA[").append(openid).append("]]></sub_openid>");
         }else{
        	 xmlContent.append("<openid><![CDATA[").append(openid).append("]]></openid>");
         }
         xmlContent.append("<nonce_str><![CDATA[").append(nonce_str).append("]]></nonce_str>");
         xmlContent.append("<sign><![CDATA[").append(sign).append("]]></sign>");
         xmlContent.append("<body><![CDATA[").append(body).append("]]></body>");
         xmlContent.append("<attach><![CDATA[").append(attach).append("]]></attach>");
         xmlContent.append("<out_trade_no><![CDATA[").append(out_trade_no).append("]]></out_trade_no>");
         xmlContent.append("<total_fee><![CDATA[").append(String.valueOf(total_fee)).append("]]></total_fee>");
         xmlContent.append("<spbill_create_ip><![CDATA[").append(spbill_create_ip).append("]]></spbill_create_ip>");
         xmlContent.append("<notify_url><![CDATA[").append(notify_url).append("]]></notify_url>");
         xmlContent.append("<trade_type><![CDATA[").append(trade_type).append("]]></trade_type>");
         xmlContent.append("</xml>");
       // String xmlContent = getRequestXml(packageParams);
         String prePayJson = WeixinUtil.httpRequest(WxUrl.WEIXIN_PRE_PAY_URL, "POST", xmlContent.toString());
         if(isSub!=null && isSub.intValue()==1){//是子商户
        	 log.info("=====================支付方式：服务商模式支付");
         }else{
        	 log.info("=====================支付方式：普通模式支付");
         }
         log.info("=====================请求参数" + xmlContent.toString());
         log.info("======================支付接口返回：" + prePayJson.toString());
         WeixinPayReturnParam sss = JsonMapper.parse(prePayJson, WeixinPayReturnParam.class);
         if ("SUCCESS".equals(sss.getReturnCode())) {
             return sss.getPrepayId();
         } else {
             return "";
         }
     }

}
