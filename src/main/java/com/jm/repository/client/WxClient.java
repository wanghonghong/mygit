package com.jm.repository.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.application.main.WebApplication;
import com.jm.business.domain.wx.WxTemplateData;
import com.jm.business.domain.wx.WxTemplateValue;
import com.jm.mvc.vo.wx.wxuser.WxUserUpDo;
import com.jm.repository.client.dto.*;
import com.jm.repository.client.dto.wxuser.WxUserUpdateDto;
import com.jm.staticcode.util.JmRestTemplate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Repository;

import com.jm.mvc.vo.wx.QrcodeVo;
import com.jm.mvc.vo.wx.content.WxContentParam;
import com.jm.mvc.vo.wx.content.WxContentUpNews;
import com.jm.mvc.vo.wx.wxmessage.PicNewsDto;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JsonMapper;

/**
 * <p>微信接口调用</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Slf4j
@Repository
public class WxClient extends BaseClient {

    /**
     * 获取菜单
     * @param accessToken
     * @return
     */
    public MenuDto getMenu(String accessToken) throws Exception {
        String url = WxUrl.MENU_GET.replace(Constant.ACCESS_TOKEN,accessToken);
        return getForObject(url,MenuDto.class);
    }

    /**
     * 创建菜单
     * @param accessToken
     * @param menu
     * @return
     */
    public ResultMsg createMenu(String accessToken, Menu menu) throws Exception {
        String url = WxUrl.MENU_CREATE.replace(Constant.ACCESS_TOKEN,accessToken);
        return postForObject(url,menu,ResultMsg.class);
    }
    
    /**
     *<p>获取ticket（二维码）</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年6月13日
     */
    public QrcodeVo getTicket(Map<String,Object> map ,String accessToken){
    	String url = WxUrl.GET_TICKET.replace("TOKEN", accessToken);
    	return restTemplate.postForObject(url,map,QrcodeVo.class);
    }

    public String getTicket(String accessToken,int flag,int sceneId){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        String param;
        if (flag==0){
            param = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+sceneId+"}}}";
        }else{
            param = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+sceneId+"}}}";
        }
        QrcodeVo vo = restTemplate.postForObject(url.replace("TOKEN", accessToken),param,QrcodeVo.class);
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+vo.getTicket();
    }

    public String getStrTicket(String accessToken,String sceneStr){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        String param = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+sceneStr+"\"}}}";
        QrcodeVo vo = restTemplate.postForObject(url.replace("TOKEN", accessToken),param,QrcodeVo.class);
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+vo.getTicket();
    }
    
    /**
     * 获取永久二维码（此为公众号基础二维码）
     * @param accessToken
     * @return
     *//*
    public String getForeverQrcode(String accessToken){
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        String param = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \" \"}}}";
        QrcodeVo vo = restTemplate.postForObject(url.replace("TOKEN", accessToken),param,QrcodeVo.class);
        return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+vo.getTicket();
    }
    
    */
    

    //客服接口发消息
    public ResultMsg sendWeixinMsg(Map<String,Object> map,String accessToken){
    	String url = WxUrl.WEIXIN_KEFU.replace("ACCESS_TOKEN", accessToken);
    	return restTemplate.postForObject(url, map, ResultMsg.class);
    }
    /**
     * 客服接口发送图文消息
     * @param picNewsDto
     * @param accessToken
     * @return
     */
    public String sendPicMsg(PicNewsDto picNewsDto,String accessToken){
    	String url = WxUrl.WEIXIN_KEFU.replace("ACCESS_TOKEN", accessToken);
    	String sss = restTemplate.postForObject(url, picNewsDto, String.class);
    	return sss;
    }

    /**
     * 长链接转成短链接
     * @param accessToken
     * @param longUrl
     * @return
     */
    public String long2short(String accessToken,String longUrl){
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
        Map paramMap = new HashMap();
        paramMap.put("access_token",accessToken);
        paramMap.put("action","long2short");
        paramMap.put("long_url",longUrl);
        Map<String,String> map = restTemplate.postForObject(url, paramMap, Map.class);
        return map.get("short_url");
    }
    
    /**
     * 上传图文消息到微信服务器 返回媒体id
     * @param wxContentUpNews
     * @param accessToken
     * @return
     * @throws IOException 
     */
    public String uploadNews(WxContentUpNews wxContentUpNews,String accessToken) throws IOException{
    	String url = WxUrl.WX_UPLOAD_NEWS.replace("ACCESS_TOKEN", accessToken);
    	String sss = restTemplate.postForObject(url,wxContentUpNews,String.class);
    	WxContentDto wxContentDto  = JsonMapper.parse(sss, WxContentDto.class);
    	return  wxContentDto.getMediaId();
    }
    
    /**
     * 群发消息
     * @param map
     * @return
     * @throws Exception 
     */
    public WxContentDto sendContent(Map map,String accessToken,String url) throws Exception{
    	String sss = restTemplate.postForObject(url,map,String.class);
    	log.info("============="+sss+"===============");
    	WxContentDto dto = JsonMapper.parse(sss, WxContentDto.class);
    	return dto;
    }
    /**
     * 新增永久图文素材
     * @param wxContentUpNews
     * @param accessToken
     * @return
     * @throws Exception 
     */
    public String addNews(WxContentUpNews wxContentUpNews,String accessToken) throws Exception{
    	String url = WxUrl.WX_ADD_NEWS.replace("ACCESS_TOKEN", accessToken);
    	String sss = restTemplate.postForObject(url,wxContentUpNews,String.class);
    	log.info(sss);
    	WxContentDto wxContentDto  = JsonMapper.parse(sss, WxContentDto.class);
    	return  wxContentDto.getMediaId();
    }
    /**
     * 预览图文消息
     * @param map
     * @param accessToken
     * @return
     * @throws Exception
     */
    public WxContentDto previewMpnews(Map map,String accessToken) throws Exception{
    	String url = WxUrl.PREVIEW.replace("ACCESS_TOKEN", accessToken);
    	String sss = restTemplate.postForObject(url,map,String.class);
    	WxContentDto dto = JsonMapper.parse(sss, WxContentDto.class);
    	return dto;
    }
    /**
     * 删除永久素材
     * @param contentParam
     * @param accessToken
     * @return
     */
    public String delMaterial(WxContentParam contentParam,String accessToken){
    	String url = WxUrl.DELMATERIAL.replace("ACCESS_TOKEN", accessToken);
    	String sss = restTemplate.postForObject(url,contentParam,String.class);
    	return sss;
    }
    /**
     * 获取模板id
     * @return
     */
    public String getTemplateId(String accessToken,String templateIdShort){
    	String url = WxUrl.WX_GET_TEMPLATE_ID.replace("ACCESS_TOKEN", accessToken);
    	Map<String,Object> map = new HashMap<>();
    	map.put("template_id_short", templateIdShort);
    	WxTemplateData sss = restTemplate.postForObject(url,map,WxTemplateData.class);
    	return sss.getTemplateId();
    }
    
    public String sendTemplateMsg(WxTemplateData dataDo ,String accessToken) throws IOException{
    	String url = WxUrl.WX_SEND_TEMPLATE_MSG.replace("ACCESS_TOKEN", accessToken);
		String sss = restTemplate.postForObject(url,dataDo,String.class);
		log.info(sss.toString());
		return sss;
    }
    /**
     * 微信扫码支付
     * @return
     */
    public String sendPostToCodePay(String paramXML){
    	String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    	String sss = restTemplate.postForObject(url, paramXML, String.class);
    	return sss;
    }

    /**
     * 批量拉取用户,更新用户时用到，新增不可用
     * @param paramMap
     * @param accessToken
     * @return
     */
    public WxUserUpdateDto batchGetUserInfo(Map<String,Object> paramMap, String accessToken){
        String url = WxUrl.BATCHGET_USER_INFO.replace("ACCESS_TOKEN",accessToken);
        WxUserUpdateDto sss = restTemplate.postForObject(url,paramMap,WxUserUpdateDto.class);
        return sss;
    }
/*
    *//**
     * 查询已发送的红包状态
     * @return
     *//*
    public WxRedQueryDto queryRedStatus(String paramXml){
    	String url = WxUrl.WX_RED_SEND_STATUS;
    	String sss = restTemplate.postForObject(url, paramXml, String.class);
    	return null;
    }*/

}
