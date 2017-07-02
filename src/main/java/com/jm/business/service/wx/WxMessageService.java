package com.jm.business.service.wx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.business.service.product.ProductGroupService;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.business.domain.wx.SendContextDo;
import com.jm.business.domain.wx.WxSendDo;
import com.jm.business.domain.wx.WxTemplateData;
import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.domain.wx.WxTemplateValue;
import com.jm.business.service.product.ProductService;
import com.jm.business.service.shop.imageText.ImageTextService;
import com.jm.business.service.shop.imageText.ImageTextTypeService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.mvc.vo.wx.content.WxContentVo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgCo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgQo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgVo;
import com.jm.mvc.vo.wx.wxmessage.PicNewsDto;
import com.jm.repository.client.WxClient;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.jpa.system.WxContentSubRepository;
import com.jm.repository.jpa.wx.WxContentRepository;
import com.jm.repository.jpa.wx.WxContentSentRepository;
import com.jm.repository.jpa.wx.WxReplyRepository;
import com.jm.repository.jpa.wx.WxTemplateIdRepository;
import com.jm.repository.jpa.wx.WxTemplateMsgRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductGroup;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ImageTextType;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxContentSent;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxReply;
import com.jm.repository.po.wx.WxTemplate;
import com.jm.repository.po.wx.WxTemplateMsg;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxTemplateNum;
import com.jm.staticcode.converter.wx.WxTemplateConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.MessageUtil;

/**
 * <p>微信授权</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Log4j
@Service
public class WxMessageService {
	@Autowired
	private ProductGroupService productGroupService;
	@Autowired
	private ImageTextService imageTextService;
	@Autowired
	private ImageTextTypeService imageTextTypeService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private WxClient wxClient;
	@Autowired
	private WxPubAccountService pubAccountService;
	@Autowired
	private WxReplyRepository wxReplyRepository;
	@Autowired
	private WxAuthService wxAuthService;
	@Autowired
	private WxTemplateIdRepository wxTemplateIdRepository;
	@Autowired
	private WxContentRepository wxContentRepository;
	@Autowired
	private WxContentSubRepository wxContentSubRepository;
	@Autowired
	private WxTemplateMsgRepository wxTemplateMsgRepository;
 	@Autowired
	protected JdbcUtil jdbcUtil;
 	@Autowired
	private WxContentSentRepository wxContentSentRepository;
 	@Autowired
 	private WxContentService wxContentService;
 	@Autowired
 	private AreaRepository areaRepository;

	/**
	 * 发送文本客服消息
	 * @param touser 接收者
	 * @param accessToken
	 * @param content 文本内容
	 */
	public ResultMsg sendMsg(String touser,String accessToken,String content,String appid){
		
		if(StringUtils.isEmpty(content)){
			return new ResultMsg(-1, "content is null");
		}
		Map<String,Object> bigMap = new HashMap<>();
		Map<String,Object> smallMap = new HashMap<>();
		bigMap.put("touser", touser);
		bigMap.put("msgtype", "text");
		smallMap.put("content", content);
		bigMap.put("text", smallMap);
		return  wxClient.sendWeixinMsg(bigMap, accessToken);
	}
	
	/**
	 * 固定回复与关键字回复文本内容专用
	 * @param touser
	 * @param accessToken
	 * @param content
	 * @param appid
	 * @return
	 */
	public ResultMsg sendMsgTofixedAndKey(String touser,String accessToken,String content,String appid){
		//只有固定回复和关键字回复的时候才加尾链接
		
		if(StringUtils.isEmpty(content)){
			return new ResultMsg(-1, "content is null");
		}
		//判断是否开启尾链接
		WxPubAccount account = pubAccountService.findWxPubAccountByAppid(appid);
		if(null!=account.getIsLastLink() && account.getIsLastLink()==1){
			WxReply reply = wxReplyRepository.findByAppidAndReplyType(appid, 3);
			if(reply!=null && null!=reply.getContent()){
				content+=reply.getContent();
			}
		}
		Map<String,Object> bigMap = new HashMap<>();
		Map<String,Object> smallMap = new HashMap<>();
		bigMap.put("touser", touser);
		bigMap.put("msgtype", "text");
		smallMap.put("content", content);
		bigMap.put("text", smallMap);
		return  wxClient.sendWeixinMsg(bigMap, accessToken);
	}
	
	/**
	 * 根据永久素材的媒体id 发送图文消息
	 * @param mediaId
	 * @param accessToken
	 * @param touser
	 * @return
	 */
	public ResultMsg sendImgTextToMediaId(String mediaId,String accessToken,String touser){
		Map<String,Object> bigMap = new HashMap<>();
		Map<String,Object> smallMap = new HashMap<>();
		bigMap.put("touser", touser);
		bigMap.put("msgtype", "mpnews");
		smallMap.put("media_id", mediaId);
		bigMap.put("mpnews", smallMap);
		 return  wxClient.sendWeixinMsg(bigMap, accessToken);
	}

	/**
	 * 发送图片消息
	 * @param mediaId
	 * @param accessToken
	 * @param touser
	 * @return
	 */
	public ResultMsg sendImg(String mediaId,String accessToken,String touser){
		Map<String,Object> bigMap = new HashMap<>();
		Map<String,Object> smallMap = new HashMap<>();
		bigMap.put("touser", touser);
		bigMap.put("msgtype", "image");
		smallMap.put("media_id", mediaId);
		bigMap.put("image", smallMap);
		 return  wxClient.sendWeixinMsg(bigMap, accessToken);
	}
	
	/**
	 * 发送微信图文消息
	 */
	public void sendWxImgTextMsg(String toUser,String accessToken,Integer contentId,Integer userId,Integer shopId){
		WxContent wxContent = wxContentRepository.findByIdAndStatus(contentId, 1);
		List<WxContentSub> wxContentSubs = wxContentSubRepository.findByContentId(wxContent.getId());
		List<PicMsgArticle> articles = new ArrayList<>();
		PicMsgArticle contentAr = new PicMsgArticle();
		//主图
		contentAr.setTitle(wxContent.getTitle());
		contentAr.setDescription(wxContent.getDigest());
		contentAr.setUrl(Constant.DOMAIN + "/content/detail_page/"+wxContent.getId()+"?shopId="+shopId+"&shareId="+userId);
		contentAr.setPicurl(wxContent.getThumbUrl());
		articles.add(contentAr);
		//子图文
		for (WxContentSub wxContentSub : wxContentSubs) {
			PicMsgArticle contentSubAr = new PicMsgArticle();
			contentSubAr.setTitle(wxContentSub.getTitle());
			contentSubAr.setDescription(wxContentSub.getDigest());
			contentSubAr.setUrl(Constant.DOMAIN + "/content/sub_detail_page/"+wxContentSub.getId()+"?shopId="+shopId+"&shareId="+userId);
			contentSubAr.setPicurl(wxContentSub.getThumbUrl());
			articles.add(contentSubAr);
		}
		sendImgTextMsg(toUser, accessToken, articles);
		
	}


	/**
	 * 客服接口发送图文消息 chenjun 2016-10-11
	 * @param accessToken
	 */
	public void sendImgTextMsg(String toUser,String accessToken,List<PicMsgArticle> articles){
		PicNewsDto picNewsDto = new PicNewsDto();
		picNewsDto.setTouser(toUser);
		picNewsDto.setMsgtype(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		picNewsDto.getNews().setArticles(articles);
		String sss = wxClient.sendPicMsg(picNewsDto, accessToken);
		//errcode如果是40015代表用户已经超过24小时没有和公众号发生交互了，如果有需要可返回
		log.info(sss);
	}
	
	
	public void sendContext(SendContextDo sendContextDo){
		Integer type = sendContextDo.getType();
		Integer contextId = sendContextDo.getContextId();
		PicMsgArticle pma = new PicMsgArticle();
		if (4==type){ //商品列表
			ProductGroup productGroup = productGroupService.getCacheProductGroup(contextId);
			if(productGroup==null){
				return;
			}
			pma.setTitle(productGroup.getGroupName());
			pma.setDescription(productGroup.getGroupSlogan());
			pma.setUrl(Constant.DOMAIN+"/app/groupList/"+contextId+"?shopId="+productGroup.getShopId()+"&shareId="+sendContextDo.getUserId()); //跳链接地址
			pma.setPicurl(ImgUtil.appendUrl(productGroup.getGroupImagePath(),720)); //图片地址
		}else if (5==type){ //商品详情
			Product product = productService.getCacheProduct(contextId);
			if(product==null){
				return;
			}
			pma.setTitle(product.getName());
			pma.setDescription(product.getShare());
			pma.setPicurl(ImgUtil.appendUrl(product.getPicRectangle(),720)); //图片地址
			pma.setUrl(Constant.DOMAIN+"/product/"+contextId+"?shopId="+product.getShopId()+"&shareId="+sendContextDo.getUserId()); //跳链接地址
		}else if (6==type){ //图文列表
			ImageTextType imageTextType = imageTextTypeService.findImageTextTypeById(contextId);
			if(imageTextType==null){
				return;
			}
			pma.setTitle(imageTextType.getTypeName());
			pma.setDescription(imageTextType.getShareText());
			pma.setUrl(Constant.DOMAIN+"/shop/image_text_type/"+contextId+"?shopId="+imageTextType.getShopId()+"&shareId="+sendContextDo.getUserId()); //跳链接地址
			pma.setPicurl(ImgUtil.appendUrl(imageTextType.getImageUrl(),720)); //图片地址
		}else if (7==type){ //图文详情
			ImageText imageText = imageTextService.getCacheImageText(contextId);
			if(imageText==null){
				return;
			}
			pma.setTitle(imageText.getImageTextTile());
			pma.setDescription(imageText.getShareText());
			pma.setUrl(Constant.DOMAIN+"/imageText/"+contextId+"?shopId="+imageText.getShopId()+"&shareId="+sendContextDo.getUserId()); //跳链接地址
			pma.setPicurl(ImgUtil.appendUrl(imageText.getImageUrl(),720)); //图片地址
		}else if (8==type){
			Shop shop = shopService.getCacheShopByAppId(sendContextDo.getAppid());
			if(shop==null){
				return;
			}
			pma.setTitle(shop.getShopName());
			pma.setDescription(shop.getShareLan1());
			pma.setUrl(Constant.DOMAIN+"/shop/index?shopId="+shop.getShopId()+"&shareId="+sendContextDo.getUserId()); //跳链接地址
			pma.setPicurl(ImgUtil.appendUrl(shop.getImgUrl(),720)); //图片地址
		}else if (10==type){
			Shop shop = shopService.getCacheShopByAppId(sendContextDo.getAppid());
			sendWxImgTextMsg(sendContextDo.getOpenid(),sendContextDo.getAccessToken(),sendContextDo.getContextId(),sendContextDo.getUserId(),shop.getShopId());
			return;
		}
		List<PicMsgArticle> articles = new ArrayList<>();
		articles.add(pma);
		sendImgTextMsg(sendContextDo.getOpenid(),sendContextDo.getAccessToken(),articles);
	}
	
	/**
	 * 发送模板消息
	 * @param wxTemplateDo
	 * @throws Exception
	 */
	public ResultMsg sendTemplate(WxTemplateDo wxTemplateDo) throws Exception{
		String accessToken = wxAuthService.getAuthAccessToken(wxTemplateDo.getAppid());
		String templateId = getTemplate(wxTemplateDo.getAppid(), wxTemplateDo.getType(), accessToken);
		if(templateId==null || "".equals(templateId)){
			return new ResultMsg(40036, "invalid template_id size");
		}
    	WxTemplateData TemplateData = new WxTemplateData();
    	TemplateData.setTemplateId(templateId);
    	TemplateData.setTouser(wxTemplateDo.getTouser());
    	TemplateData.setUrl(wxTemplateDo.getUrl());
    	Map<String,WxTemplateValue> m = new HashMap<String,WxTemplateValue>();
		WxTemplateValue first = new WxTemplateValue();
		WxTemplateValue remark = new WxTemplateValue();
		//设置头行消息
		if(wxTemplateDo.getType()==6 || wxTemplateDo.getType()==7){
			first.setValue(wxTemplateDo.getFirst());
	    	first.setColor("#F75000");
	    	remark.setValue(wxTemplateDo.getRemark());
	    	remark.setColor("#F75000");
		}else{
			first.setValue(wxTemplateDo.getFirst());
	    	first.setColor("#173177");
	    	remark.setValue(wxTemplateDo.getRemark());
	    	remark.setColor("#173177");
		}
    	m.put("first", first);
    	m.put("remark", remark);
    	if(wxTemplateDo.getType()==1){//发货通知
    		sendDeliverGoodsTemp(TemplateData, wxTemplateDo,m);
    	}else if(wxTemplateDo.getType()==2){//退货通知
    		sendReturnGoodsTemp(TemplateData, wxTemplateDo, m);
    	}else if(wxTemplateDo.getType()==3){//签收通知
    		sendOrderSignTemp(TemplateData, wxTemplateDo, m);
    	}else if(wxTemplateDo.getType()==4){//退款通知
    		sendRefundTemp(TemplateData, wxTemplateDo, m);
    	}else if(wxTemplateDo.getType()==5 || wxTemplateDo.getType()==13){//佣金通知,回大叔积分通知
    		sendCommissionTemp(TemplateData, wxTemplateDo, m);
    	}else if(wxTemplateDo.getType()==6 || wxTemplateDo.getType()==7  || wxTemplateDo.getType()==10 ||  wxTemplateDo.getType()==12){//下级用户发生通知或消息精推，回大叔订单预约
    		sendWaitMakeTemp(TemplateData, wxTemplateDo, m);
    		
    	}else if(wxTemplateDo.getType()==8){//商品状态变更通知
    		
    	}else if(wxTemplateDo.getType()==9){//注册通知
			sendwaitMakeRegister(TemplateData, wxTemplateDo, m);
		}
		String ss =  wxClient.sendTemplateMsg(TemplateData, accessToken);//发送
     	return JsonMapper.parse(ss, ResultMsg.class);
	}
	
	/**
	 * 发货通知
	 */
	public void sendDeliverGoodsTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,Map<String,WxTemplateValue> m ){
    	WxTemplateValue keyword1 = new WxTemplateValue();
    	keyword1.setValue(wxTemplateDo.getOrderNum());
    	m.put("keyword1", keyword1);
    	WxTemplateValue keyword2 = new WxTemplateValue();
    	keyword2.setValue(wxTemplateDo.getLogisticsName());
    	m.put("keyword2", keyword2);
    	WxTemplateValue keyword3 = new WxTemplateValue();
    	keyword3.setValue(wxTemplateDo.getLogisticsNum());
    	m.put("keyword3", keyword3);
    	TemplateData.setData(m);
	}
	
	/**
	 * 退货通知
	 */
	public void sendReturnGoodsTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		WxTemplateValue keyword1 = new WxTemplateValue();
    	keyword1.setValue(wxTemplateDo.getOrderNum());
    	m.put("keyword1", keyword1);
    	WxTemplateValue keyword2 = new WxTemplateValue();
    	keyword2.setValue(wxTemplateDo.getRefundCount());
    	m.put("keyword2", keyword2);
    	WxTemplateValue keyword3 = new WxTemplateValue();
    	keyword3.setValue(wxTemplateDo.getRefundStatus());
    	m.put("keyword3", keyword3);
    	TemplateData.setData(m);
	}
	
	
	/**
	 * 签收通知
	 */
	public void sendOrderSignTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		WxTemplateValue keyword1 = new WxTemplateValue();
    	keyword1.setValue(wxTemplateDo.getOrderNum());
    	m.put("keyword1", keyword1);
		WxTemplateValue keyword2 = new WxTemplateValue();
    	keyword2.setValue(wxTemplateDo.getOrderStatus());
    	m.put("keyword2", keyword2);
    	WxTemplateValue keyword3 = new WxTemplateValue();
    	keyword3.setValue(wxTemplateDo.getLogisticsName());
    	m.put("keyword3", keyword3);
    	WxTemplateValue keyword4 = new WxTemplateValue();
    	keyword4.setValue(wxTemplateDo.getLogisticsNum());
    	m.put("keyword4", keyword4);
    	WxTemplateValue keyword5 = new WxTemplateValue();
    	keyword5.setValue(wxTemplateDo.getSingTime());
    	m.put("keyword5", keyword5);
    	TemplateData.setData(m);
	}
	
	/**
	 * 退款通知
	 */
	public void sendRefundTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		WxTemplateValue reason = new WxTemplateValue();
		reason.setValue(wxTemplateDo.getRefundReason());
    	m.put("reason", reason);
    	WxTemplateValue refund = new WxTemplateValue();
    	refund.setValue(wxTemplateDo.getRefundAmount()+"元");
    	m.put("refund", refund);
    	TemplateData.setData(m);
	}
	
	
	/**
	 * 佣金通知
	 */
	public void sendCommissionTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		WxTemplateValue keyword1 = new WxTemplateValue();
		keyword1.setValue(wxTemplateDo.getIncomeType());
    	m.put("keyword1", keyword1);
    	WxTemplateValue keyword2 = new WxTemplateValue();
    	keyword2.setValue(wxTemplateDo.getIncomeMoney()+wxTemplateDo.getCompany());
		m.put("keyword2", keyword2);
    	WxTemplateValue keyword3 = new WxTemplateValue();
    	keyword3.setValue(wxTemplateDo.getIncomeTime());
    	m.put("keyword3", keyword3);
    	TemplateData.setData(m);
		
	}
	
	/**
	 * 发送待办任务模板消息
	 */
	public void sendWaitMakeTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		//目前 下级用户通知，精确推送  都使用待办任务消息模板
		WxTemplateValue keyword1 = new WxTemplateValue();
    	keyword1.setValue(wxTemplateDo.getWaitForTask());
    	m.put("keyword1", keyword1);
    	WxTemplateValue keyword2 = new WxTemplateValue();
    	keyword2.setValue(wxTemplateDo.getDelayTask());
    	m.put("keyword2", keyword2);
    	TemplateData.setData(m);
	}
	/**
	 * 发送注册提示模板消息
	 * @param TemplateData
	 * @param wxTemplateDo
	 * @param m
	 */
	public void sendwaitMakeRegister(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
									 Map<String,WxTemplateValue> m){
		WxTemplateValue keyword1 = new WxTemplateValue();
		keyword1.setValue(wxTemplateDo.getRegisterPerson());
		m.put("keyword1", keyword1);
		WxTemplateValue keyword2 = new WxTemplateValue();
		keyword2.setValue(wxTemplateDo.getRegisterNum());
		m.put("keyword2", keyword2);
		WxTemplateValue keyword3 = new WxTemplateValue();
		keyword3.setValue(wxTemplateDo.getRegisterTime());
		m.put("keyword3", keyword3);
		TemplateData.setData(m);
	}
/*	*//**
	 * 下级用户通知
	 *//*
	public void sendNextUserTemp(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
	
		
	}
	*//**
	 * 精确推送
	 *//*
	public void sendAccurateMsg(WxTemplateData TemplateData,WxTemplateDo wxTemplateDo,
			Map<String,WxTemplateValue> m){
		
	}*/
	
	/**
	 * 获取模板id
	 * @param appid
	 * @param type
	 * @param accessToken
	 */
	@Transactional
	public String getTemplate(String appid,int type,String accessToken){
		String wxTemplateNum = "";
		if(type==1){
			wxTemplateNum =WxTemplateNum.DELIVER_GOODS_TEMP;
		}else if(type==2){
			wxTemplateNum= WxTemplateNum.RETURNGOODSTEMP;
		}else if(type==3){
			wxTemplateNum= WxTemplateNum.ORDER_SIGN_TEMP;
		}else if(type==4){
			wxTemplateNum= WxTemplateNum.REFUND_TEMP;
		}else if(type==5 || type==13){//佣金通知，回大叔积分通知
			wxTemplateNum= WxTemplateNum.COMMISSION_TEMP;
		}else if(type==6 || type ==7 || type==10 || type==12){ // 待办事项
			wxTemplateNum= WxTemplateNum.WAIT_MAKE_TEMP;
		}else if(type==8){//商品变更
			
		}else if(type==9){//注册通知
			wxTemplateNum = WxTemplateNum.WAIT_MAKE_REGISTER;
		}
		WxTemplate wxTemplate = wxTemplateIdRepository.findByAppidAndTypeAndTemplateNum(appid, type,wxTemplateNum);
		String tempId = "";
		if(wxTemplate==null || null==wxTemplate.getTemplateId()){
			tempId = wxClient.getTemplateId(accessToken,wxTemplateNum);
			if(tempId!=null){
				if(wxTemplate==null){
					//模板不存在
					WxTemplate t =  new WxTemplate();
					t.setAppid(appid);
					t.setType(type);
					t.setTemplateId(tempId);
					t.setTemplateNum(wxTemplateNum);
					wxTemplateIdRepository.save(t);
				}else{
					//模板已经存在但ID为空
					wxTemplate.setTemplateId(tempId);
					wxTemplateIdRepository.save(wxTemplate);
				}
			}
		}else{
			tempId = wxTemplate.getTemplateId();
		}
		return tempId;
	}

	/**
	 * 精推消息-使用客服接口图文消息方式发送
	 * @param openids
	 * @param contentId
	 * @return
	 */
	public synchronized  JmMessage accurateSendImgText(List<String> openids,Integer contentId,List<PicMsgArticle> articles,String appid) throws Exception {
		WxContentSent sent = wxContentSentRepository.findByContentId(contentId);
		if(sent.getStatus()==0){
			sent.setStatus(99);//改为发送中
			wxContentSentRepository.save(sent);
			if(openids.size()>0){
				for (String openid : openids) {
				String accessToken = wxAuthService.getAuthAccessToken(appid);
					sendImgTextMsg(openid,accessToken,articles);
				}
				sent.setStatus(1);
				wxContentSentRepository.save(sent);
				return new JmMessage(0, "ok");
			}else{
				sent.setStatus(2);//发送对象为空
				wxContentSentRepository.save(sent);
				return new JmMessage(-1, "发送对象为空");
			}
		}
		return null;
	}
	
	/**
	 * 发送精推消息
	 * @throws Exception 
	 */
	public synchronized JmMessage accurateSenTempMsg(WxTemplateDo wxTemplateDo,List<String> openids,Integer contentId) throws Exception{
		WxContentSent sent = wxContentSentRepository.findByContentId(contentId);
		if(sent.getStatus()==0){
			sent.setStatus(99);//改为发送中
			wxContentSentRepository.save(sent);
			if(openids.size()>0){
				for (String openid : openids) {
					wxTemplateDo.setTouser(openid);
					ResultMsg msg = sendTemplate(wxTemplateDo);
					if(msg.getErrcode()!=0 && msg.getErrmsg().contains("invalid template_id size")){
						sent.setStatus(2);
						sent.setErrorMsg("发送失败,请确认是否开通模板插件");
						wxContentSentRepository.save(sent);
						return new JmMessage(-1, "发送失败,请确认已经开通模板消息插件！");
					}
				}
				sent.setStatus(1);
				wxContentSentRepository.save(sent);
	        	return new JmMessage(0, "ok");
			}else{
				return new JmMessage(-1, "发送对象为空");
			}
		}
		return null;
		
	}
	
	/**
	 * 保存精推消息
	 * @param wxTemplateMsgCo
	 * @return
	 */
	public WxTemplateMsg saveWxTemplateMsg(WxTemplateMsgCo wxTemplateMsgCo,Integer userId,String appid){
		WxTemplateMsg wxTemplateMsg = new  WxTemplateMsg();
		wxTemplateMsg.setAppid(appid);
		wxTemplateMsg.setCreateTime(new Date());
		wxTemplateMsg.setUserId(userId);
    	WxTemplateConverter.c2p(wxTemplateMsgCo, wxTemplateMsg);
    	return wxTemplateMsgRepository.save(wxTemplateMsg);
	}
	
	/**
	 * 获取已发送的精推消息
	 * @return
	 * @throws Exception 
	 */
	public  PageItem<WxTemplateMsgVo> findAsTemplate(WxTemplateMsgQo wxTemplateMsgQo ,String appid) throws Exception{
		String sqlList = "select distinct t.*,m.description,m.first,m.remark,m.url_name,g.name as group_name,us.user_name "
				+ " from wx_content_sent t "
				+ " left join wx_template_msg m on t.content_id = m.id"
				+ " left join wx_user_group g on (t.groupid=g.groupid and g.appid='"+appid+"')"
				+ " left join user us on us.user_id=m.user_id"
				+ " where t.appid='"+appid+"' and t.type=1 order by t.send_time desc";
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList,wxTemplateMsgQo.getCurPage(),wxTemplateMsgQo.getPageSize());
		PageItem<WxTemplateMsgVo> pageItems = WxTemplateConverter.p2v(pageItem);
		for (WxTemplateMsgVo p : pageItems.getItems()) {
			if(null!=p.getAreaIds() && !"".equals(p.getAreaIds())){
				String areaIds = p.getAreaIds();
				String [] areaIdsStr =  areaIds.split(",");
				List list = Arrays.asList(areaIdsStr);
				List<Area> areas = areaRepository.findAreaAll(list);
				String areaName = "";
				for (Area area : areas) {
					areaName+=area.getAreaName()+",";
				}
				areaName=areaName.substring(0, areaName.length()-1);
				p.setAreaName(areaName);
			}
		}
		return pageItems;
	} 
	
	/**
	 * 定时发送精推消息
	 * @throws Exception 
	 */
	public void taskTemplateMsg(WxContentSent wxContentSent) throws Exception{
		 	WxTemplateMsg tempMsg = wxTemplateMsgRepository.findOne(wxContentSent.getContentId());
			WxContentVo wxContentVo = new WxContentVo();
		 	if(null != wxContentSent.getAreaIds() && !"".equals(wxContentSent.getAreaIds())){
		 		List<String> areaIds = new ArrayList<>();
			 	String[] aresStr = wxContentSent.getAreaIds().split(",");
			 	for (String area : aresStr) {
					areaIds.add(area);
				}
				wxContentVo.setAreaIds(areaIds);
		 	}
		 	wxContentVo.setSex(wxContentSent.getSex());
		 	wxContentVo.setRole(wxContentSent.getRole());
		 	wxContentVo.setGroupid(wxContentSent.getGroupid());
		 	if(null!=wxContentSent.getSubStartDate()){//关注时间
		 		wxContentVo.setSubStartDate(wxContentSent.getSubStartDate());
		 	}
		 	if(null!=wxContentSent.getSubEndtDate()){
		 		wxContentVo.setSubEndtDate(wxContentSent.getSubEndtDate());
		 	}
		 	WxContentVo resultVo = wxContentService.findSendTemplateOpenids(wxContentVo, wxContentSent.getAppid());
			List<PicMsgArticle> articles = new ArrayList<>();
			PicMsgArticle article = new PicMsgArticle();
			article.setTitle(tempMsg.getFirst());
			article.setDescription(tempMsg.getRemark());
			article.setPicurl(tempMsg.getPicUrl());
			article.setUrl(tempMsg.getUrl());
			articles.add(article);
			accurateSendImgText(resultVo.getOpenids(),tempMsg.getId(),articles,tempMsg.getAppid());
			//accurateSenTempMsg(wxTemplateDo, resultVo.getOpenids(), tempMsg.getId());
	 	
		
	}
	
	
	
	/**
	 * 定时发图文消息
	 * @throws Exception 
	 */
	public void taskImgTextMsg(WxContentSent wxContentSent) throws Exception{
		 	WxContent wxContent  = wxContentRepository.findByIdAndStatus(wxContentSent.getContentId(), 1);
		 	String accessToken = wxAuthService.getAuthAccessToken(wxContent.getAppid());
		 	WxContentVo vo = new WxContentVo();
		 	if(null!=wxContentSent.getAreaIds() && !"".equals(wxContentSent.getAreaIds())){
		 		List<String> areaIds = new ArrayList<>();
			 	String[] aresStr = wxContentSent.getAreaIds().split(",");
			 	for (String area : aresStr) {
					areaIds.add(area);
				}
			 	vo.setAreaIds(areaIds);
		 	}
		 	
		 	vo.setSex(wxContentSent.getSex());
		 	vo.setGroupid(wxContentSent.getGroupid());
		 	vo.setRole(wxContentSent.getRole());
		 	
		 	WxContentVo resultVo = wxContentService.handleSentType(vo, wxContent.getAppid());
		  	WxSendDo wxSendDo = new WxSendDo();
		  	wxSendDo.setContent(wxContent.getMediaId());
		  	wxSendDo.setGroupid(wxContentSent.getGroupid());
		  	wxSendDo.setMsgtype(wxContent.getSendType());
		  	wxSendDo.setType(resultVo.getType());
		  	wxSendDo.setOpenids(resultVo.getOpenids());
		  	wxContentService.sendContent(wxSendDo,accessToken,wxContentSent.getId());
	 	
	}
	
	
	public void sendTask() throws Exception{
		List<WxContentSent> sents = wxContentSentRepository.findNotSend();
		for (WxContentSent wxContentSent : sents) {
			if(wxContentSent.getType()==0){//群发图文
				if(wxContentSent.getStatus()==0){
					taskImgTextMsg(wxContentSent);
				}
			}else if(wxContentSent.getType()==1){//群发精推
				if(wxContentSent.getStatus()==0){
					taskTemplateMsg(wxContentSent);
				}
			}
		}
	}

}
