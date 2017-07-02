package com.jm.staticcode.util.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.wx.WxAcceptVo;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.aes.AesException;
import com.jm.staticcode.util.aes.WXBizMsgCrypt;

/**
 * <p>微信工具类</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/9/20
 */
@Log4j
public class WxUtil {

	private static WXBizMsgCrypt pc;

	public static WXBizMsgCrypt getPc() throws AesException {
		if (pc==null){
			pc = new WXBizMsgCrypt(Constant.TOKEN,Constant.ENCODINGAESKEY, Constant.COMPONENT_APP_ID);
		}
		return pc;
	}

	/**
	 * 封装微信请求结果
	 * @param request
	 * @param domSource
	 * @return
	 * @throws Exception
	 */
	public static WxAcceptVo toWxAcceptVo(HttpServletRequest request, DOMSource domSource) throws Exception {
		WxAcceptVo wxAcceptVo = xmlParse(domSource); //解析body元素
		setWxAcceptVo(request,wxAcceptVo); //解析url元素
		String fromXML = String.format(Constant.XML_FORMAT, wxAcceptVo.getEncrypt());
		String afterEncrpt = getPc().decryptMsg(wxAcceptVo.getMsgSignature(), wxAcceptVo.getTimestamp(), wxAcceptVo.getNonce(),fromXML);
		Map<String,String> resultMap = parseXmlToMap(afterEncrpt);
		wxAcceptVo.setMap(resultMap);
		return wxAcceptVo;
	}

	/**
	 * 微信请求过来url参数封装成对象
	 * @param request
	 * @return
	 */
	public static void setWxAcceptVo(HttpServletRequest request,WxAcceptVo wxAcceptVo){
		wxAcceptVo.setTimestamp(request.getParameter("timestamp"));
		wxAcceptVo.setNonce(request.getParameter("nonce"));
		wxAcceptVo.setMsgSignature(request.getParameter("msg_signature"));
		wxAcceptVo.setEncryptType(request.getParameter("encrypt_type"));
		wxAcceptVo.setSignature(request.getParameter("signature"));
		wxAcceptVo.setOpenid(request.getParameter("openid"));
		String appid =  request.getParameter("appid");
		if (appid!=null){
			appid = appid.substring(1);
			wxAcceptVo.setAppid(appid);
		}
	}

	/**
	 * xml字符串转map
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> parseXmlToMap(String xml) throws Exception {
		Map<String,String> map = new HashMap<>();
		Document document = DocumentHelper.parseText(xml);
		Element nodeElement = document.getRootElement();
		List node = nodeElement.elements();
		for (Iterator it = node.iterator(); it.hasNext();) {
			Element elm = (Element) it.next();
			map.put(elm.getName(), elm.getText());
		}
		return map;
	}

	/**
	 * 微信post请求封装成VO
	 * @param domSource
	 * @return
	 * @throws Exception
	 */
	public static WxAcceptVo xmlParse(DOMSource domSource) throws Exception {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		Document doc = DocumentHelper.parseText(writer.toString());
		Element root = doc.getRootElement();
		//Element转vo
		WxAcceptVo wxAcceptVo = new WxAcceptVo();
		wxAcceptVo.setEncrypt(root.elementText("Encrypt"));
		wxAcceptVo.setAppid(root.elementText("AppId"));
		wxAcceptVo.setToUserName(root.elementText("ToUserName"));
        /*wxAcceptVo.setFromUserName(root.elementText("FromUserName"));
        wxAcceptVo.setMsgType(root.elementText("MsgType"));
        wxAcceptVo.setCreateTime(root.elementText("CreateTime"));
        wxAcceptVo.setContent(root.elementText("Content"));
        wxAcceptVo.setInfoType(root.elementText("InfoType"));
        wxAcceptVo.setAuthorizationCode(root.elementText("AuthorizationCode"));
        wxAcceptVo.setAuthorizerAppid(root.elementText("AuthorizerAppid"))*/;
		return wxAcceptVo;
	}
	

	/**
	 * 封装微信回调请求结果
	 * @param request
	 * @param domSource
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> toWxWxNotifyMap(HttpServletRequest request, DOMSource domSource) throws Exception {
		//解析body元素
		return notifyXmlParse(domSource);
	}
	
	
	
	/**
	 * 微信回调的xml数据封装成map
	 * @param domSource
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object>  notifyXmlParse(DOMSource domSource) throws Exception {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		Document doc = DocumentHelper.parseText(writer.toString());
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		Map<String,Object> xmlMap = new HashMap<String, Object>();
		for(Element ss :list){
			xmlMap.put(ss.getName(), ss.getTextTrim());
		}
		return xmlMap;
	}


	public  static WxUserSession getWxUserSession(WxUserSession wxUserSession, HttpServletRequest request,ShopService shopService, WxUserService wxUserService) {
	  if(wxUserSession==null){
		  wxUserSession = new WxUserSession();
		  Integer shopId=20;  //测试环境先临时写死 商店
		  Integer userid = 0;//测试环境先临时写死 商店
		  Shop shop =  shopService.findShopById(shopId);
		  wxUserSession.setShopId(shopId);
		  wxUserSession.setShopName(shop.getShopName());
		  wxUserSession.setImgUrl(ImgUtil.appendUrl(shop.getImgUrl(),0));
		  wxUserSession.setTempId(shop.getTempId());
		  wxUserSession.setShareDesc(shop.getShareLan1());
		  wxUserSession.setIsEntity(shop.getIsEntity());
		  wxUserSession.setPromise(shop.getPromise());
		  wxUserSession.setExchange(shop.getExchange());
		  wxUserSession.setDirectSell(shop.getDirectSell());
		  wxUserSession.setIsOpen(shop.getIsOpen());
		  String shareId = request.getParameter("shareId");
		  if (StringUtils.isNotEmpty(shareId)) {
			  wxUserSession.setShareid(Integer.valueOf(shareId));
		  }

		  wxUserSession.setUserId(userid);
		  request.getSession().setAttribute(Constant.SESSION_WX_USER, wxUserSession);
	  }
		return  wxUserSession;
	}
	
	
	 public static String getRequestXml(SortedMap<String, String> parameters) {  
	        StringBuffer sb = new StringBuffer();  
	        sb.append("<xml>");  
	        Set es = parameters.entrySet();  
	        Iterator it = es.iterator();  
	        while (it.hasNext()) {  
	            Map.Entry entry = (Map.Entry) it.next();  
	            String k = (String) entry.getKey();  
	            String v = (String) entry.getValue();  
	            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {  
	                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");  
	            } else {  
	                sb.append("<" + k + ">" + v + "</" + k + ">");  
	            }  
	        }  
	        sb.append("</xml>");  
	        return sb.toString();  
	    } 
	 
	/**
	 * map转为XML
	 * @param parameters
	 * @return
	 */
	 public static String mapToXml(SortedMap<String, String> parameters) {  
	        StringBuffer sb = new StringBuffer();  
	        sb.append("<xml>");  
	        Set es = parameters.entrySet();  
	        Iterator it = es.iterator();  
	        while (it.hasNext()) {  
	            Map.Entry entry = (Map.Entry) it.next();  
	            String k = (String) entry.getKey();  
	            String v = (String) entry.getValue();  
	            sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");   
	        }  
	        sb.append("</xml>");  
	        return sb.toString();  
	    }  
	
}
