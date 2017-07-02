//package com.jm.staticcode.util.wx;
//
//import java.io.*;
//import java.net.*;
//import java.security.MessageDigest;
//import java.util.*;
//
//import com.jm.repository.client.HxHttpClient;
//import com.jm.repository.client.dto.HxResultMessage;
//import com.jm.repository.client.dto.HxToken;
//import com.jm.staticcode.util.HttpClientHelper;
//import com.jm.staticcode.util.HxUtils;
//import com.jm.staticcode.util.sms.SmsSingleSender;
//import com.jm.staticcode.util.sms.SmsSingleSenderResult;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.util.Json;
//import lombok.extern.log4j.Log4j;
//import org.json.JSONObject;
//import sun.net.www.http.HttpClient;
//
//
///*
//网址http://www.17int.cn/xxAdmin/
//帐号ysf
//密码asd159951
//接口文档http://www.17int.cn/xxAdmin/xxhttpProto.htm
//测试提示：1.平台支持浏览器:
//Chrome 基乎所有版本
//FireFox 3.0 +
//Safari 4.0 +
//IE 10.0 +
//IE 10.0 以下需要用极速模式，2.【xxx】+内容， 例：【广东科技】你的验证码是2222。
//*/
//
//@Log4j
//public class SMSUtil {
//
//	private final static String serviceUrl = "http://www.17int.cn";
//	private final static String sendMsgUrl = "/xxsmsweb/smsapi/send.json";
//	private final static String account = "ysf";
//	private final static String password = "asd159951";
//
//	/**
//	 * 产生随机的六位数
//	 * @return
//	 */
//	public static String getSixCode(){
//		Random rad=new Random();
//		String result  = rad.nextInt(1000000) +"";
//		if(result.length()!=6){
//			return getSixCode();
//		}
//		return result;
//	}
//
//
//	/**
//	 * 字符串转大写32位MD5
//	 * @param s
//	 * @return
//	 */
//	public final static String md5_32UpCase(String s) {
//		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
//		try {
//			byte[] btInput = s.getBytes();
//			// 获得MD5摘要算法的 MessageDigest 对象
//			MessageDigest mdInst = MessageDigest.getInstance("MD5");
//			// 使用指定的字节更新摘要
//			mdInst.update(btInput);
//			// 获得密文
//			byte[] md = mdInst.digest();
//			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//
//
//	/**
//	 * 发送短信
//	 * @param msg 信息内容
//	 * @param phone 手机号码
//	 * @return
//	 */
//	public static String sendMsg(String msg,String phone) {
//		HttpClientHelper client = new HttpClientHelper();
//		Map<String,Object> message = new HashMap<String,Object>();
//		Map<String,String> headers = new HashMap<>();
//		String sms="";
//		try {
//			headers.put("Content-type","application/json; charset=utf-8");
//			message.put("account",account);
//			message.put("password",md5_32UpCase(password));
//			message.put("mobile",phone);
//			message.put("content",msg);
//			message.put("requestId","");
//			message.put("extno","");
//			sms = client.postRequest(serviceUrl+sendMsgUrl,headers,message,0);
//			System.out.println(sms);
//			JSONObject myJsonObject = new JSONObject(sms);
//			String errorCode = myJsonObject.get("errorCode")+"";
//			if(errorCode.equals("ALLSuccess")){
//				return "1";
//			}else{
//				return "";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sms;
//	}
//
//	/*public static void main(String[] args) throws Exception {
//			sendMsg("【聚米为谷】注册验证码[655406] 这是一条测试短信","15980578222");
//		*//*ArrayList<String> params = new ArrayList<>();
//		params.add("测试1");
//		params.add("测试2");
//		System.out.print(singleTemp("13055771643",111,params));*//*
//	}*/
//
//	//*****************************以下为腾讯短信******************************
//
//
//	/**
//	 *
//	 * @param phoneNumber 手机号码
//	 * @param templId 指定模板Id
//	 * @param params 模板参数
//	 * @return 发送成功：0
//	 * @throws Exception
//	 */
//	public static int singleTemp(String phoneNumber,int templId,ArrayList<String> params) throws Exception {
//		SmsSingleSender singleSender = new SmsSingleSender();
//		SmsSingleSenderResult singleSenderResult;
//		// 指定模板单发
//		// 假设短信模板内容为：测试短信，{1}，{2}，{3}，上学。
//		/* ArrayList<String> params = new ArrayList<>();
//		 params.add("测试1");
//		 params.add("测试2");*/
//		singleSenderResult = singleSender.sendWithParam("86", phoneNumber, templId, params, "", "", "");
//		System.out.println(singleSenderResult);
//		return singleSenderResult.result;
//	}
//
//
//}
