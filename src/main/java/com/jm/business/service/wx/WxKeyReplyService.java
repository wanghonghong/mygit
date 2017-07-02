package com.jm.business.service.wx;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.mvc.vo.wx.content.WxKeyReplyCo;
import com.jm.mvc.vo.wx.content.WxKeyReplyQo;
import com.jm.mvc.vo.wx.content.WxKeyReplyUo;
import com.jm.mvc.vo.wx.content.WxKeyReplyVo;
import com.jm.repository.jpa.wx.WxContentRepository;
import com.jm.repository.jpa.wx.WxKeyReplyRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.wx.WxContent;
import com.jm.repository.po.wx.WxKeyReply;
import com.jm.staticcode.converter.wx.WxKeyReplyConverter;

/**
 * 关键字回复
 * @author chenyy
 *
 */
@Slf4j
@Service
public class WxKeyReplyService {
	
	@Autowired
	private WxKeyReplyRepository wxKeyReplyRepository;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private WxAuthService wxAuthService;
	@Autowired
	private WxContentService wxContentService;
	@Autowired
	private WxContentRepository wxContentRepository;
	@Autowired
	private ShopService shopService;
	
	
	
	/**
	 * 新增规格与关键字
	 * @param wxKeyReplyCo
	 * @throws Exception 
	 */
	@Transactional
	public void saveKeyAndRule(WxKeyReplyCo wxKeyReplyCo,String appid) throws Exception{
		if(wxKeyReplyCo.getReplyContentType()==4){//修改的新对象如果是图片类型，需要做特殊处理
			String accessToken = wxAuthService.getAuthAccessToken(appid);
			wxKeyReplyCo.setMediaId(wxContentService.getMediaId(accessToken,wxKeyReplyCo.getPicUrl()));
    	}else if(wxKeyReplyCo.getReplyContentType()==3){//商城链接
    		if(wxKeyReplyCo.getImgTextId()!=null && wxKeyReplyCo.getImgTextId()>0){//图文消息
    			if (wxKeyReplyCo.getImgTextType().intValue() == 1) {// 微信图文，已经上传到永久素材，直接保存mediaId
    				WxContent wxContent = wxContentRepository.findOne(wxKeyReplyCo.getImgTextId());
    				wxKeyReplyCo.setMediaId(wxContent.getMediaId());
    			}
    		}
    	}
    	WxKeyReply reply  = WxKeyReplyConverter.c2p(wxKeyReplyCo, appid);
		wxKeyReplyRepository.save(reply);
	}
	
	
	
	
    /**
     * 关键字回复
     * @param appid
     * @param openid
     * @param accessToken
     * @param response
     * @throws Exception
     */
    public boolean keyReply(String appid,String openid,String accessToken,String key,HttpServletResponse response) throws Exception{
    	response.getWriter().write("");//告诉微信不需要连续发请求，先响应微信，在后续慢慢处理我们的业务
		response.getWriter().close();
		boolean flag = false;
		List<WxKeyReply> replys = wxKeyReplyRepository.findByAppid(appid);
		//匹配关键字
		for (WxKeyReply wxKeyReply : replys) {
			String [] keys = wxKeyReply.getKeyName().split(",");//拆分关键字
			for (String k : keys) {
				if(key.equals(k)){//有匹配到对应的关键字
					//判断是哪种类型的消息
					if(wxKeyReply.getReplyContentType()==1){//发送文本消息
						wxMessageService.sendMsgTofixedAndKey(openid, accessToken, wxKeyReply.getContent(), appid);
					}else if(wxKeyReply.getReplyContentType()==8){//外网链接
						wxMessageService.sendMsg(openid, accessToken, wxKeyReply.getContent(), appid);
					}else if(wxKeyReply.getReplyContentType()==3){//商城链接
						if(wxKeyReply.getImgTextId()!=null && wxKeyReply.getImgTextId()>0){//发送图文消息
							if(wxKeyReply.getImgTextType()==1){
			    				wxMessageService.sendImgTextToMediaId(wxKeyReply.getMediaId(), accessToken, openid);//发送微信图文消息
			    			}else if(wxKeyReply.getImgTextType()==2 || wxKeyReply.getImgTextType()==3){
			    				//项目图文与乐享图文都是在一张表里面，所以id已经是精确查找 不需要再分类型
			    				Shop shop = shopService.getShopByAppId(appid);
			    				PicMsgArticle article = wxContentService.assemblingImgText(wxKeyReply.getImgTextId(),shop.getShopId());
			    				List<PicMsgArticle> articles = new ArrayList<>();
			    				articles.add(article);
			    				wxMessageService.sendImgTextMsg(openid, accessToken, articles);
			    			}
						}else{
							wxMessageService.sendMsgTofixedAndKey(openid, accessToken, wxKeyReply.getContent(), appid);
						}
						
					}else if(wxKeyReply.getReplyContentType()==4){//发送图片消息
						wxMessageService.sendImg(wxKeyReply.getMediaId(), accessToken, openid);
					}
					flag = true;
					break;
				}
			}
		}
		return flag;
    }
    
    /**
     * 修改关键字回复
     * @param wxKeyReplyUo
     * @throws Exception 
     */
    @Transactional
    public void updateWxKeyReply(WxKeyReplyUo wxKeyReplyUo,String appid) throws Exception{
    	WxKeyReply replyOld = wxKeyReplyRepository.findOne(wxKeyReplyUo.getId());
    	String accessToken = wxAuthService.getAuthAccessToken(appid);
    	if(replyOld!=null && !appid.equals(replyOld.getAppid())){
    		throw new  Exception("无权限修改");
    	}
    	if(replyOld.getReplyContentType()==4){//原先的如果是图片类型，就把微信上的素材删掉
    		wxContentService.delMaterial(replyOld.getMediaId(), accessToken);
    	}
    	if(wxKeyReplyUo.getReplyContentType()==4){//修改的新对象如果是图片类型，需要做特殊处理
    		wxKeyReplyUo.setMediaId(wxContentService.getMediaId(accessToken,wxKeyReplyUo.getPicUrl()));
    	}else if(wxKeyReplyUo.getReplyContentType()==3){//商城链接
    		if(wxKeyReplyUo.getImgTextId()!=null && wxKeyReplyUo.getImgTextId()>0){//图文消息
    			if (wxKeyReplyUo.getImgTextType().intValue() == 1) {// 微信图文，已经上传到永久素材，直接保存mediaId
    				WxContent wxContent = wxContentRepository.findOne(wxKeyReplyUo.getImgTextId());
    				wxKeyReplyUo.setMediaId(wxContent.getMediaId());
    			}
    		}
    	}
    	WxKeyReply reply  = WxKeyReplyConverter.u2p(wxKeyReplyUo, appid);
    	wxKeyReplyRepository.save(reply);
    	
    }
    
    /**
     * 删除
     * @param id
     * @throws Exception 
     */
    @Transactional
    public void deleteKeyReply(Integer id,String appid) throws Exception{
    	WxKeyReply replyOld = wxKeyReplyRepository.findOne(id);
    	if(replyOld!=null && !appid.equals(replyOld.getAppid())){
    		throw new  Exception("无权限删除");
    	}
    	wxKeyReplyRepository.delete(id);
    }
    
    /**
     * 获取列表
     * @param wxKeyReplyQo
     * @param appid
     * @return
     */
    public PageItem<WxKeyReplyVo> findAllByAppid(WxKeyReplyQo wxKeyReplyQo ,String appid){
    	 Sort sort=new Sort(Sort.Direction.DESC,"id");
         PageRequest pageRequest = new PageRequest(wxKeyReplyQo.getCurPage(),wxKeyReplyQo.getPageSize(),sort);
    	 Page<WxKeyReply> replyPage =  wxKeyReplyRepository.findByAppid(appid, pageRequest);
    	 return WxKeyReplyConverter.ps2vs(replyPage);
    	 
    }
    
    public WxKeyReplyVo findById(Integer id){
    	WxKeyReply reply = wxKeyReplyRepository.findOne(id);
    	return WxKeyReplyConverter.p2v(reply);
    }

}
