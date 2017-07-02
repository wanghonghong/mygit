package com.jm.staticcode.util.wx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import com.jm.repository.po.wx.WxPubAccount;

import lombok.extern.log4j.Log4j;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.Toolkit;


/**
 *<p>微信红包工具类</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月27日
 */
@Log4j
public class WeixinRedUtils {
	
	
	/**
	 *<p>发送红包</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年6月28日
	 */
	/*public static RedResultParam sendRed(WeixinActInfo requestParam) throws Exception{
		//金额至少在1元以上
		if(requestParam.getTotalAmount().intValue()<100){
			return new RedResultParam();
		}
		RedResultParam param = new RedResultParam();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		String xmlContent = addParam(packageParams, requestParam);
		String xmlRes = checkApiclientcert(requestParam.getMchId(),xmlContent);
		Map<String, Object> json  = WeixinUtil.converXml2Json(xmlRes);
		String resultCode = (String) json.get("result_code");
		//如果返回的是失败，就把失败理由放入对象
		param.setResultCode(resultCode);
		if("FAIL".equals(resultCode)){
			param.setErrCode( (String) json.get("err_code"));
		}
		return param;
	}*/
	
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
    


 /*   public static void main(String[] args) throws Exception {
    	WeixinActInfo requestParam = new WeixinActInfo();
    	requestParam.setWxappid("wx46506c1578a96cac");// wxb41784fb4c29bfa2shawan
    	requestParam.setMchId("1342370001"); //1366212802shawan
    	requestParam.setAppkey("000111222333444555666777888999jm");//000111222333444555666777888999jm
    	requestParam.setSendName("聚米");
    	requestParam.setReOpenid("oMVHCw2Sonkoqf4nM9_BKUHChe90");////  oeVPhvmSA06MfVwQQQ78sA_MsSRo  shawan
    	requestParam.setTotalAmount(100);
    	requestParam.setClientIp("127.0.0.1");
    	requestParam.setTotalNum(1);
    	requestParam.setWishing("聚米");
    	requestParam.setActName("聚米的内侧活动");
    	requestParam.setRemark("聚米红包");
    	requestParam.setSubMchId("1363925702");//子商户号
    	requestParam.setMsgappid("wxb41784fb4c29bfa2");//触达appid
    	sendRed(requestParam);
    	System.exit(0);
	}*/
    
    /**
     * 验证证书  post提交到微信服务器,请求微信
     **/
    public static String checkApiclientcert(String mchId,String parameter,String postUrl){
    	StringBuilder result = new StringBuilder();  
    	KeyStore keyStore;
		try {
			keyStore  = KeyStore.getInstance("PKCS12");
			// E:/cyywork/msa/src/main/resources/apiclient_cert.p12
			//   /root/apiclient_cert/"+mchId+".p12
			InputStream is =  new FileInputStream("/root/apiclient_cert/"+mchId+".p12");
			//InputStream is =  new FileInputStream("E:/msa/src/main/resources/apiclient_cert.p12");
	        try {
	            keyStore.load(is, mchId.toCharArray());
	        } finally {
				if(is!=null){
					is.close();
				}
	        }
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {
	            HttpPost httpost = new HttpPost(postUrl);
	            httpost.addHeader("Connection", "keep-alive");
	        	httpost.addHeader("Accept", "*/*");
	        	httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	        	httpost.addHeader("Host", "api.mch.weixin.qq.com");
	        	httpost.addHeader("X-Requested-With", "XMLHttpRequest");
	        	httpost.addHeader("Cache-Control", "max-age=0");
	        	httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
	    		httpost.setEntity(new StringEntity(parameter, "UTF-8"));
	            CloseableHttpResponse response = httpclient.execute(httpost);
	            try {
	                HttpEntity entity = response.getEntity();
	                log.info("----------------------------------------");
	                log.info("------参数："+parameter);
	                log.info(response.getStatusLine());
	                if (entity != null) {
	                	log.info("Response content length: " + entity.getContentLength());
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                    	result.append(text);
	                    }
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
		} catch (Exception e) {
			log.error(e.getMessage());
			//e.printStackTrace();
		}
		return result.toString();
    }
    /**
     * 拼装参数
     *<p></p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年7月18日
     */
    public static String addParam(SortedMap<String, String> packageParams, WeixinActInfo requestParam, WxPubAccount account){
    	String nonce_str = UUID.randomUUID().toString().replace("-", "");
		//DateFormat format = new SimpleDateFormat("yyyyMMdd");
		StringBuffer xmlContent = new StringBuffer();
		String sign = createRedSing(packageParams, requestParam, nonce_str, requestParam.getMchBillno(),account);//调用签名
		xmlContent.append("<xml>");
		xmlContent.append("<sign><![CDATA[").append(sign).append("]]></sign>");
		xmlContent.append("<nonce_str><![CDATA[").append(nonce_str).append("]]></nonce_str>");
		xmlContent.append("<mch_billno><![CDATA[").append(requestParam.getMchBillno()).append("]]></mch_billno>");
		//判断是否子商户
		if(account.getIsSub()!=null && account.getIsSub().intValue()==1){//是子商户
			//是子商户则信息用服务商的
			xmlContent.append("<wxappid><![CDATA[").append(Constant.SERVICE_APPID).append("]]></wxappid>");
			xmlContent.append("<mch_id><![CDATA[").append(Constant.SERVICE_MCHID).append("]]></mch_id>");
			xmlContent.append("<sub_mch_id><![CDATA[").append(account.getMchId()).append("]]></sub_mch_id>");
			xmlContent.append("<msgappid><![CDATA[").append(account.getAppid()).append("]]></msgappid>");
		}else{
			xmlContent.append("<wxappid><![CDATA[").append(account.getAppid()).append("]]></wxappid>");
			xmlContent.append("<mch_id><![CDATA[").append(account.getMchId()).append("]]></mch_id>");
		}
		xmlContent.append("<send_name><![CDATA[").append(requestParam.getSendName()).append("]]></send_name>");
		xmlContent.append("<re_openid><![CDATA[").append(requestParam.getReOpenid()).append("]]></re_openid>");
		xmlContent.append("<total_amount><![CDATA[").append(String.valueOf(requestParam.getTotalAmount())).append("]]></total_amount>");
		xmlContent.append("<client_ip><![CDATA[").append(requestParam.getClientIp()).append("]]></client_ip>");
		xmlContent.append("<total_num><![CDATA[").append(String.valueOf(requestParam.getTotalNum())).append("]]></total_num>");
		xmlContent.append("<wishing><![CDATA[").append(requestParam.getWishing()).append("]]></wishing>");
		xmlContent.append("<act_name><![CDATA[").append(requestParam.getActName()).append("]]></act_name>");
		xmlContent.append("<remark><![CDATA[").append(requestParam.getRemark()).append("]]></remark>");
		xmlContent.append("</xml>");
		return xmlContent.toString();
    }
    /**
     *<p>生成红包签名</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年7月18日
     */
    public static String createRedSing(SortedMap<String, String> packageParams,WeixinActInfo requestParam,String nonce_str,String mch_billno,WxPubAccount account){
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("mch_billno", mch_billno);
		String appkey = "";
		if(account.getIsSub()!=null && account.getIsSub().intValue()==1){
			//判断是否子服务商
			packageParams.put("wxappid", Constant.SERVICE_APPID);
			packageParams.put("mch_id",Constant.SERVICE_MCHID);
			packageParams.put("sub_mch_id", requestParam.getMchId());
			packageParams.put("msgappid", requestParam.getWxappid());
			appkey = Constant.SERVICE_APPKEY;
		}else{
			packageParams.put("wxappid", requestParam.getWxappid());
			packageParams.put("mch_id",requestParam.getMchId());
			appkey = requestParam.getAppkey();
		}
		
		packageParams.put("send_name", requestParam.getSendName());
		packageParams.put("re_openid", requestParam.getReOpenid());
		packageParams.put("total_amount", String.valueOf(requestParam.getTotalAmount()));
		packageParams.put("client_ip", requestParam.getClientIp());
		packageParams.put("total_num", String.valueOf(requestParam.getTotalNum()));
		packageParams.put("wishing", requestParam.getWishing());
		packageParams.put("act_name", requestParam.getActName());
		packageParams.put("remark", requestParam.getRemark());
	
		String sign = createSign(packageParams,appkey);
		packageParams.put("sign", sign);
		return sign;
    }

}
