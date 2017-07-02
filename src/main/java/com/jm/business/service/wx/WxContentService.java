package com.jm.business.service.wx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.jm.business.domain.wx.WxSendDo;
import com.jm.business.service.shop.imageText.ImageTextService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.mvc.vo.wx.WxMedia;
import com.jm.mvc.vo.wx.content.WxContentArticles;
import com.jm.mvc.vo.wx.content.WxContentSentVo;
import com.jm.mvc.vo.wx.content.WxContentParam;
import com.jm.mvc.vo.wx.content.WxContentResult;
import com.jm.mvc.vo.wx.content.WxContentRo;
import com.jm.mvc.vo.wx.content.WxContentText;
import com.jm.mvc.vo.wx.content.WxContentUo;
import com.jm.mvc.vo.wx.content.WxContentUpNews;
import com.jm.mvc.vo.wx.content.WxContentVo;
import com.jm.mvc.vo.wx.reply.WxReplyCo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgCo;
import com.jm.repository.client.WxClient;
import com.jm.repository.client.dto.WxContentDto;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.jpa.system.WxContentSubRepository;
import com.jm.repository.jpa.wx.WxContentSentRepository;
import com.jm.repository.jpa.wx.WxContentRepository;
import com.jm.repository.jpa.wx.WxReplyRepository;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxContentSent;
import com.jm.repository.po.wx.WxContentSub;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxReply;
import com.jm.repository.po.wx.WxTemplateMsg;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.converter.wx.WxContentSentConverter;
import com.jm.staticcode.converter.wx.WxContentConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.WeixinUtil;

/**
 * 微信内容营销service
 * @author chenyy
 *
 */
@Service
public class WxContentService {
	
    	@Autowired
		private WxClient wxClient;
    	@Autowired
    	private WxContentRepository wxContentRepository;
    	@Autowired
    	private WxContentSubRepository wxContentSubRepository;
    	@Autowired
    	private WxReplyRepository wxReplyRepository;
    	@Autowired
    	private ImageTextService imageTextService;
    	@Autowired
    	private WxContentSentRepository asRepository;
    	@Autowired
    	protected JdbcUtil jdbcUtil;
    	@Autowired
    	private WxUserService wxUserService;
    	@Autowired
    	private WxpublicAccountService wxpublicAccountService;
    	@Autowired
    	private  AreaRepository areaRepository;
    	@Autowired
    	private WxContentSentRepository wxContentSentRepository;
    	@Autowired
    	private WxAuthService wxAuthService;
	
	/**
	 * 上传图片到微信服务器返回url，在内容里面需要用到
	 * @param accessToken
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getPicUrl(String accessToken,String filePath) throws Exception{
		String pathUrL = WxUrl.WX_CONTENT_IMG_UPLOAD.replace("ACCESS_TOKEN", accessToken);
		String result = WeixinUtil.sendToWeixin(accessToken, pathUrL, filePath);
		WxMedia media = JsonMapper.parse(result, WxMedia.class);
		return media.getUrl();
	}
	/**
	 * 上传图片获取媒体Id，图文缩略图需要使用  此方法永久图片消息
	 * @param accessToken
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	public String getMediaId(String accessToken,String filePath) throws Exception{
		String pathUrL =WxUrl.WX_ADD_MATERIAL.replace("ACCESS_TOKEN", accessToken);
		String result = WeixinUtil.sendToWeixin(accessToken, pathUrL, filePath);
		WxMedia media = JsonMapper.parse(result, WxMedia.class);
		return media.getMediaId();
	}

	public WxContent saveWxContent(WxContent wxContent){
		return 	wxContentRepository.save(wxContent);
	}
	
	/**
	 * 上传图文素材到微信
	 * @param wxContentUpNews
	 * @param accessToken
	 * @return
	 * @throws Exception 
	 */
	public String uploadNews(WxContentUpNews wxContentUpNews,String accessToken) throws Exception{
		return wxClient.uploadNews(wxContentUpNews, accessToken);
	}
	
	
	@Transactional
	public WxContent saveWxContentAndSub(List<WxContent>contents,String accessToken,Integer userId) throws Exception{
		contents.get(0).setUserId(userId);
		WxContent returnWxContent = wxContentRepository.save(contents.get(0));//主表保存第一条
		//本集合是从第二条开始 下标从1开始  做转换保存到sub表; 大于1条才做子表插入
		List<WxContentSub> wxContentSubs = new ArrayList<>();
		if(contents.size()>1){
			List<WxContent> tmpContent = new ArrayList<>();
			for (int i = 1; i < contents.size(); i++) {
				tmpContent.add(contents.get(i));
			}
			wxContentSubs = WxContentConverter.content2sub(tmpContent, returnWxContent.getId());
			saveWxContentSub(wxContentSubs);
		}
		
		WxContentUpNews wxContentUpNews = WxContentConverter.WxContentsToWxContentUpNews(returnWxContent,wxContentSubs);
		//上传图文到微信永久素材,做内容处理添加二维码
		WxPubAccount account  = wxpublicAccountService.findWxPubAccountByAppid(returnWxContent.getAppid());
		String qrcode = account.getQrcodeUrl();
		String apdContent = "<div style=\" margin:0 auto; width:150px;height:150px\"><img src=" + qrcode + "/><div>";
		for (WxContentArticles wxContentArticles : wxContentUpNews.getArticles()) {
			String newContent = wxContentArticles.getContent()+apdContent;
			wxContentArticles.setContent(newContent);
		}
		String mediaId = wxClient.addNews(wxContentUpNews, accessToken);
		returnWxContent.setMediaId(mediaId);
		return wxContentRepository.save(returnWxContent);
	}
	
	/**
	 * 修改
	 * @throws Exception
	 */
	@Transactional
	public void updateContetnAndSub(WxContentUo wxContentUo,String accessToken,String appid) throws Exception{
		List<WxContentUo> uos = wxContentUo.getWxContentUos();
		//判断是否有删除的子项
		if(null!=wxContentUo.getDeleteIds()){
			String [] ids = wxContentUo.getDeleteIds().split(",");
			List<Integer> arrIds = new ArrayList<>();
			for (String id : ids) {
				arrIds.add(Integer.parseInt(id));
			}
			List<WxContentSub> returnSubs =  wxContentSubRepository.findAllByIds(arrIds);
			List<WxContentSub> tmpSubs = new ArrayList<>();
			for (WxContentSub wxContentSub : returnSubs) {
				wxContentSub.setStatus(0);
				tmpSubs.add(wxContentSub);
			}
			wxContentSubRepository.save(tmpSubs);
		}
		
		WxContent wxContent = wxContentRepository.findOne(uos.get(0).getId());
		Toolkit.copyPropertiesIgnoreNull(uos.get(0),wxContent);
		String mediaId = wxContent.getMediaId();
		WxContent returnContent = wxContentRepository.save(wxContent);
		//删除微信永久素材,把图文的mediaId和缩略图在微信上删掉
		delMaterial(mediaId, accessToken);
		if(uos.size()>1){
			List<WxContentUo> tmpUos = new ArrayList<>();
			for (int i = 1; i < uos.size(); i++) {
				WxContentUo uo = uos.get(i);
				WxContentSub sub  = null;
				if(null!=uo.getId()){
					sub =  wxContentSubRepository.findOne(uo.getId());
					Toolkit.copyPropertiesIgnoreNull(uo,sub);
				}else{
					sub = new WxContentSub();
					Toolkit.copyPropertiesIgnoreNull(uo,sub);
				}
				tmpUos.add(uos.get(i));
			}
			List<WxContentSub> subs = WxContentConverter.us2subPs(tmpUos,returnContent.getId());
			wxContentSubRepository.save(subs);
		}
		//需要同步做排序到微信，所以要再查一次
		List<WxContentSub> returnSubs =  wxContentSubRepository.findByContentId(returnContent.getId());
		//修改图文消息，mediaId 也需要重新改掉
		WxContentUpNews wxContentUpNews = WxContentConverter.WxContentsToWxContentUpNews(returnContent,returnSubs);
		
		WxPubAccount account  = wxpublicAccountService.findWxPubAccountByAppid(wxContent.getAppid());
		String qrcode = account.getQrcodeUrl();
		String apdContent = "<div style=\" margin:0 auto; width:150px;height:150px\"><img src=" + qrcode + "/><div>";
		for (WxContentArticles wxContentArticles : wxContentUpNews.getArticles()) {
			String newContent = wxContentArticles.getContent()+apdContent;
			wxContentArticles.setContent(newContent);
		}
		returnContent.setMediaId(wxClient.addNews(wxContentUpNews, accessToken));
		wxContentRepository.save(returnContent);
	}
	
	/**
	 * 批量保存子内容
	 * @param wxContentSubs
	 */
	public List<WxContentSub> saveWxContentSub(List<WxContentSub> wxContentSubs){
		return wxContentSubRepository.save(wxContentSubs);
	}
	
	public WxContent findById(Integer id){
		//只查询状态正常的
		return wxContentRepository.findByIdAndStatus(id,1);
	}
	
	public WxContentSub findWxContetSubById(Integer id){
		return wxContentSubRepository.findOne(id);
	}
	
	/**
	 * 获取子类
	 * @return
	 */
	public List<WxContentSub> findeContentSubByContentId(Integer contentId){
		return wxContentSubRepository.findByContentId(contentId);
	}
	
	/**
	 * 发送消息图文消息
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public synchronized void sendContent(WxSendDo wxSendDo,String accessToken,Integer id) throws Exception{
		WxContentDto dto = new WxContentDto();
		 WxContentSent sent = wxContentSentRepository.findOne(id);
		if(sent.getStatus()==0){
			sent.setStatus(99);//设置为发送中
			wxContentSentRepository.save(sent);
			//type:群发的类型 1：全部群发  2：根据分组群发  3：根据openid群发
			if(wxSendDo.getType()==1 || wxSendDo.getType()==2){
				dto = addParamAndSend(wxSendDo,accessToken,null);
				if(!dto.getErrcode().equals("0")){
					sent.setStatus(2);
					sent.setErrorMsg(dto.getErrmsg());
					wxContentSentRepository.save(sent);
					return;
				}
			}else{
				//根据openid群发；微信规定openid一次最多10000个，需要做拆分
				List<List<String>>  batchOpenids = dealBySubList(wxSendDo.getOpenids(), 10000);//
				for (List<String> openidList : batchOpenids) {
					dto = addParamAndSend(wxSendDo,accessToken,openidList);
				}
				if(!dto.getErrcode().equals("0")){
					sent.setStatus(2);
					sent.setErrorMsg(dto.getErrmsg());
					wxContentSentRepository.save(sent);
					return;
				}
			}
			sent.setStatus(1);
			wxContentSentRepository.save(sent);
		}

		
	}
	public WxContentDto addParamAndSend(WxSendDo wxSendDo,String accessToken,List<String> openidList) throws Exception{
		Map<String,Object> bigMap = new HashMap<String,Object>();
		if(wxSendDo.getType()==1 || wxSendDo.getType()==2){
			Map<String,Object> filter =new HashMap<>();
			if(wxSendDo.getType()==1){
				filter.put("is_to_all", true);//全部群发
			}else if(wxSendDo.getType()==2){
				//根据分组id群发
				filter.put("is_to_all", false);//全部根据分组id群发
				filter.put("group_id", wxSendDo.getGroupid());
			}
			bigMap.put("filter", filter);
		}else{
			bigMap.put("touser", openidList);
		}
		
		if("image".equals(wxSendDo.getMsgtype())){//发送图片消息
			WxContentParam param = new WxContentParam();
			param.setMediaId(wxSendDo.getContent());
			bigMap.put("image", param);
			
		}else if("text".equals(wxSendDo.getMsgtype())){//发送文本消息
			WxContentText text = new WxContentText();
			text.setContent(wxSendDo.getContent());
			bigMap.put("text", text);
			
		}else if("mpnews".equals(wxSendDo.getMsgtype())){//发送图文消息
			WxContentParam param = new WxContentParam();
			param.setMediaId(wxSendDo.getContent());
			bigMap.put("mpnews", param);
		}
		bigMap.put("msgtype", wxSendDo.getMsgtype());
		
		String url = "";
    	if(wxSendDo.getType()==1 || wxSendDo.getType()==2){
    		//根据分组或全部发送
    		url = WxUrl.WX_CONTENT_SEND_TO_ALL.replace("ACCESS_TOKEN", accessToken);
    	}else{
    		//根据openid发送
    		url = WxUrl.WX_CONTENT_SEND.replace("ACCESS_TOKEN", accessToken);
    	}
		return wxClient.sendContent(bigMap, accessToken,url);
	}
	
	
	/**
	 * 分割list 批处理
	 * @param sourList  集合
	 * @param batchCount 每次割据条数
	 */
	public  List<List<String>> dealBySubList(List<String> sourList, int batchCount){
        int sourListSize = sourList.size();
        int subCount = sourListSize%batchCount==0 ? sourListSize/batchCount : sourListSize/batchCount+1;
        int startIndext = 0;
        int stopIndext = 0;
        List<List<String>> openids = new ArrayList<>();
        for(int i=0;i<subCount;i++){
            stopIndext = (i==subCount-1) ? stopIndext + sourListSize%batchCount : stopIndext + batchCount;
            List<String> tempList = new ArrayList<String>(sourList.subList(startIndext, stopIndext)); 
            openids.add(tempList);
            startIndext = stopIndext;
        }
        return openids;
    }
	
	/**
	 * 处理发送的类型；返回 根据哪种类型发送数据
	 * @param wxContentVo
	 * @return
	 */
	public WxContentVo handleSentType(WxContentVo wxContentVo,String appid){
		int sex = wxContentVo.getSex();
		int groupid = wxContentVo.getGroupid();
		List<String> areaIds = wxContentVo.getAreaIds();
		if(sex==-1&&groupid==-1&&areaIds.size()<=0 && wxContentVo.getRole()==-1){
			//全部条件为空，全部群发
			wxContentVo.setType(1);
		}else if(sex==-1&&areaIds.size()==0&&groupid>-1 && wxContentVo.getRole()==-1){
			//性别为空，地区为空，分组不为空，根据分组id群发
			wxContentVo.setType(2);
		}else{
			wxContentVo.setType(3);
			//其他的条件组合，全部根据openid群发
			List<String> openidS = wxUserService.queryUser(wxContentVo, appid);
		    wxContentVo.setOpenids(openidS);
		}
		return wxContentVo;
	}
	
	/**
	 * 查询发送模板消息的对象
	 * @param wxContentVo
	 * @param appid
	 * @return
	 */
	public WxContentVo findSendTemplateOpenids(WxContentVo wxContentVo,String appid){
		List<String> openids = wxUserService.queryUser(wxContentVo, appid);
		wxContentVo.setOpenids(openids);
		return wxContentVo;
	}
	
	
    public Specification<WxContent> getSpec(final WxContentRo contentRo, final String appid){
    	Specification<WxContent> spec = new Specification<WxContent>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("appid").as(String.class), appid));
                predicates.add(cb.equal(root.get("status").as(Integer.class), 1));
                 if ( null != contentRo ) {
                	 if(!StringUtils.isEmpty(contentRo.getSendType())){
                		predicates.add(cb.equal(root.get("sendType").as(String.class), contentRo.getSendType()));
                	 }
                	 if(!StringUtils.isEmpty(contentRo.getTitle())){
                		 predicates.add(cb.like(root.get("title").as(String.class), contentRo.getTitle()));
                	 }
                }
                predicate.getExpressions().addAll(predicates);
                return predicate;
            }
        };
        return spec;
    }

	/**
	 * 获取图文消息列表
	 * @return
	 */
	public PageItem<WxContentResult>  getContentList(WxContentRo contentRo,String appid){
		Sort s = new Sort(Direction.DESC,"saveTime");;
		
		PageRequest pageRequest = new PageRequest(contentRo.getCurPage(), contentRo.getPageSize(), s);
		Page<WxContent> wxContentPage = wxContentRepository.findAll(getSpec(contentRo, appid), pageRequest);
		PageItem<WxContentResult> pageItem = WxContentConverter.toWxcontetRestlt(wxContentPage);
		
		PageItem<WxContentResult> pageItemRes = new PageItem<>();
		List<WxContentResult> results = pageItem.getItems();

		if (results == null || results.size() == 0) {
			pageItemRes.setCount(0);
			pageItemRes.setItems(results);
			return pageItemRes;
		}
		List<Integer> contentIds = new ArrayList<>();
		for (WxContentResult result : results) {
			contentIds.add(result.getId());
		}
		List<WxContentSub> subs = wxContentSubRepository.findAllByContentIds(contentIds);
		pageItemRes.setCount(Integer.parseInt(wxContentPage.getTotalElements() + ""));
		pageItemRes.setItems(WxContentConverter.toWxcontennResultSub(results, subs));
		return pageItemRes;
	}
	
	/**
	 * 伪删除图文消息
	 */
	@Transactional
	public void deleteContent(Integer id,String accessToken){
		//删除微信素材
		WxContent wxContent = findById(id);
		wxContent.setStatus(0);//伪删除状态为0
		wxContentRepository.save(wxContent);
		delMaterial(wxContent.getMediaId(), accessToken);
		
	}
	
	/**
	 * 保存回复内容
	 * @param wxReply
	 * @throws Exception 
	 */
	@Transactional
	public WxReply saveReply(WxReply wxReply,WxReplyCo wxReplyCo,String accessToken,String appid) throws Exception{
		//关注回复与固定还有尾链接都只能有一条数据，如果已经存在就修改掉
		WxReply reply = wxReplyRepository.findByAppidAndReplyType(appid,wxReply.getReplyType());
		if(reply!=null){
			wxReply.setId(reply.getId());
		}
		if(wxReplyCo.getReplyContentType().intValue()==4){
			//图片类型
			wxReply.setMediaId(getMediaId(accessToken, wxReplyCo.getPicUrl()));
		}else if(wxReplyCo.getReplyContentType().intValue()==3){//商城链接
			if(wxReplyCo.getImgTextId()!=null&&wxReplyCo.getImgTextId()>0){//判断是否图文消息
				if(wxReplyCo.getImgTextType().intValue()==1){//微信图文，已经上传到永久素材，直接保存mediaId
					WxContent wxContent  = wxContentRepository.findOne(wxReplyCo.getImgTextId());
					wxReply.setMediaId(wxContent.getMediaId());
				}
			}
			
		}  
		wxReply.setSaveTime(new Date());
		return wxReplyRepository.save(wxReply);
	}
	
	/**
	 * 拼裝自動回復的圖文消息(项目图文与乐享图文)
	 * @return
	 */
	public PicMsgArticle assemblingImgText(Integer imgTextId,Integer shopId){
		PicMsgArticle article = new PicMsgArticle();//要发送的图文消息
			ImageText text = imageTextService.findImageTextById(imgTextId);
			article.setTitle(text.getImageTextTile());
			article.setPicurl(ImgUtil.appendUrl(text.getImageUrl(), 720));
			article.setDescription(text.getShareText());
			article.setUrl(Constant.DOMAIN+"/imageText/"+text.getId()+"?shopId="+shopId);
		return article;
		
	}
	
	
	public List<WxReply> findWxReplyByAppid(String appid){
		return wxReplyRepository.findByAppid(appid);
	}
	
	/**
	 * 图文消息预览
	 * @param mediaId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public JmMessage previewMpnews(String mediaId,String accessToken,String towxname) throws Exception{
		Map bigMap = new HashMap();
		WxContentParam param = new WxContentParam();
		param.setMediaId(mediaId);
		bigMap.put("towxname", towxname);
		bigMap.put("mpnews", param);
		bigMap.put("msgtype", "mpnews");
		WxContentDto dto = wxClient.previewMpnews(bigMap, accessToken);
		if("40132".equals(dto.getErrcode()) || "43004".equals(dto.getErrcode())){
			return new JmMessage(-1, "未关注或用户名无效");
		}else{
			return new JmMessage(0,"ok");
		}
	}
	/**
	 * 删除微信永久素材
	 */
	public void delMaterial(String mediaId,String accessToken){
		WxContentParam param = new WxContentParam();
		if(null!=mediaId){//删除图文mediaId
			param.setMediaId(mediaId);
			wxClient.delMaterial(param, accessToken);
		}
	}
	
	/**
	 * 新增发送消息（定时发送或已发送） 图文消息
	 * @param wxcontent
	 * @return 
	 */
	public WxContentSent saveAlreadySent(WxContent wxcontent,WxContentVo wxContentVo){
		WxContentSent alreadysent = new WxContentSent();
		alreadysent.setSex(wxContentVo.getSex());
	  	if(wxContentVo.getAreaIds().size()>0){
	  		 String areaCode = "";
    		 int size = wxContentVo.getAreaIds().size();
    		 for (int i = 0; i < size; i++) {
    			 areaCode+=wxContentVo.getAreaIds().get(i)+",";
			}
    		 areaCode = areaCode.substring(0,areaCode.length()-1);
    		 alreadysent.setAreaIds(areaCode);
	  	}
	  	alreadysent.setRole(wxContentVo.getRole());
	  	alreadysent.setGroupid(wxContentVo.getGroupid());
	  	alreadysent.setCount(wxContentVo.getOpenids().size());
	  	
	  	alreadysent.setStatus(0);//状态为未发送
		if(wxContentVo.getIsTiming()==1){
			//定时发送
			alreadysent.setSendTime(wxContentVo.getTimingSendTime());//定时时间
		}else{
			alreadysent.setSendTime(new Date());
		}
		alreadysent.setContentId(wxcontent.getId());
		alreadysent.setAppid(wxcontent.getAppid());
		return asRepository.save(alreadysent);
	}
	
	//新增发送消息（定时发送或已发送） 精推消息
	public void saveAlreadySent(WxTemplateMsg wxTemplateMsg,WxTemplateMsgCo wxTemplateMsgCo){
		WxContentSent wxContentSent = new WxContentSent();
		wxContentSent.setSex(wxTemplateMsgCo.getSex());
	  	if(wxTemplateMsgCo.getAreaIds().size()>0){
	  		 String areaCode = "";
    		 int size = wxTemplateMsgCo.getAreaIds().size();
    		 for (int i = 0; i < size; i++) {
    			 areaCode+=wxTemplateMsgCo.getAreaIds().get(i)+",";
			}
    		 areaCode = areaCode.substring(0,areaCode.length()-1);
    		 wxContentSent.setAreaIds(areaCode);
	  	}
	  	wxContentSent.setRole(wxTemplateMsgCo.getRole());
	  	wxContentSent.setGroupid(wxTemplateMsgCo.getGroupid());
	  	wxContentSent.setType(1);
	  	wxContentSent.setCount(wxTemplateMsgCo.getOpenids().size());
	  	wxContentSent.setStatus(0);//状态为未发送
	  	if(null!=wxTemplateMsgCo.getSubStartDate()){
	  		wxContentSent.setSubStartDate(wxTemplateMsgCo.getSubStartDate());
	  	}
	  	if(null!=wxTemplateMsgCo.getSubEndtDate()){
	  		wxContentSent.setSubEndtDate(wxTemplateMsgCo.getSubEndtDate());
	  	}
		if(wxTemplateMsgCo.getIsTiming()==1){
			//定时发送
			wxContentSent.setSendTime(wxTemplateMsgCo.getTimingSendTime());//定时时间
		}else{
			wxContentSent.setSendTime(new Date());
		}
		wxContentSent.setContentId(wxTemplateMsg.getId());
		wxContentSent.setAppid(wxTemplateMsg.getAppid());
		asRepository.save(wxContentSent);
	}
	
	
	/**
	 * 获取已发送群发消息
	 * @return
	 * @throws IOException 
	 */
	public PageItem<WxContentSentVo> findAsList(WxContentRo contentRo,String appid) throws IOException{
		String sqlList = "select distinct t.*,c.title,c.thumb_url,c.author,u.user_name,c.jm_content,c.content,"
				+ " c.send_type,g.name as group_name"
				+ " from wx_content_sent t "
				+ " left join wx_content c on t.content_id = c.id"
				+ " left join wx_user_group g on (t.groupid=g.groupid and g.appid='"+appid+"')"
				+ " left join user u on u.user_id=c.user_id"
				+ " where t.appid='"+appid+"' and t.type=0 ";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(JdbcUtil.appendAnd("c.send_type",contentRo.getSendType()));
		sqlCondition.append(JdbcUtil.appendOrderBy("t.send_time"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,contentRo.getCurPage(),contentRo.getPageSize());
		PageItem<WxContentSentVo> pageItems = WxContentSentConverter.p2v(pageItem);
		List<WxContentSentVo> wxContentAss =pageItems.getItems();
		for (WxContentSentVo wxContentSentVo : wxContentAss) {
			if(null != wxContentSentVo.getAreaIds() && !"".equals(wxContentSentVo.getAreaIds())){
				String areaIds = wxContentSentVo.getAreaIds();
				String [] areaIdsStr =  areaIds.split(",");
				List list = Arrays.asList(areaIdsStr);
				List<Area> areas = areaRepository.findAreaAll(list);
				String areaName = "";
				for (Area area : areas) {
					areaName+=area.getAreaName()+",";
				}
				areaName=areaName.substring(0, areaName.length()-1);
				wxContentSentVo.setAreaName(areaName);
			}
		}
		
		List<Integer> contentIds = new ArrayList<>();
		for (WxContentSentVo as : wxContentAss) {
			contentIds.add(as.getContentId());
		}
		if(contentIds.size()>0){
			List<WxContentSub> subs = wxContentSubRepository.findAllByContentIds(contentIds);
			WxContentSentConverter.addSub(pageItems, subs);
		}
		return pageItems;
	}
	
	
	
	/**
	 * 根据id查询已发送图文消息
	 * @param id
	 * @return
	 */
	public WxContentSent findAsById(Integer id){
		return asRepository.findOne(id);
	}
	
	/**
	 * 删除已发消息
	 * @param id
	 */
	@Transactional
	public void deleteAsById(Integer id){
		asRepository.delete(id);
	}
	
}
