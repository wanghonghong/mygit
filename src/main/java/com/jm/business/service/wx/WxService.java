package com.jm.business.service.wx;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;

import com.jm.business.domain.wx.SendContextDo;
import com.jm.business.domain.wx.WxRepeatMsg;
import com.jm.business.service.online.HxUserService;
import com.jm.business.service.product.ProductService;
import com.jm.business.service.shop.*;
import com.jm.business.service.shop.imageText.ImgTextMsgService;
import com.jm.business.service.system.UserRoleService;
import com.jm.mvc.vo.online.HxUserCo;
import com.jm.mvc.vo.wx.wxuser.FortyEightUser;
import com.jm.repository.client.ImageClient;
import com.jm.repository.jpa.wx.WxUserQrcodeRepository;
import com.jm.repository.po.online.HxUser;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.repository.po.shop.ProductQrcodeDetail;
import com.jm.repository.po.shop.QrcodePoster;
import com.jm.repository.po.shop.integral.IntegralSet;
import com.jm.repository.po.system.user.UserRole;
import com.jm.repository.po.wx.*;
import com.jm.staticcode.util.*;
import com.qcloud.UploadResult;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.business.domain.shop.ActivityRedDo;
import com.jm.business.domain.shop.AwardIntegralDo;
import com.jm.business.domain.wx.WxRedDo;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.system.ResourceService;
import com.jm.business.service.system.SystemService;
import com.jm.mvc.vo.system.ButtonVo;
import com.jm.mvc.vo.wx.ImageMessage;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.mvc.vo.wx.QrcodeVo;
import com.jm.mvc.vo.wx.WxAcceptVo;
import com.jm.mvc.vo.wx.wxred.RedResultParam;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.client.HxHttpClient;
import com.jm.repository.client.WxAuthClient;
import com.jm.repository.client.WxClient;
import com.jm.repository.client.dto.HxResultMessage;
import com.jm.repository.client.dto.HxToken;
import com.jm.repository.client.dto.Menu;
import com.jm.repository.client.dto.MenuDto;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.client.dto.WxUserDto;
import com.jm.repository.jpa.wx.WxRedRecordRepository;
import com.jm.repository.jpa.wx.WxReplyRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.converter.WxUserConverter;
import com.jm.staticcode.util.HxUtils;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Pic;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.aes.WXBizMsgCrypt;
import com.jm.staticcode.util.wx.Base64Util;
import com.jm.staticcode.util.wx.MessageUtil;
import com.jm.staticcode.util.wx.WeixinPayUtil;
import com.jm.staticcode.util.wx.WeixinRedUtils;
import com.jm.staticcode.util.wx.WeixinUtil;
import com.jm.staticcode.util.wx.WxUtil;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Slf4j
@Service
public class WxService {
    @Autowired
    public WxClient wxClient;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxPubAccountService wxPubAccountService;
    @Autowired
	private WxQrcodeService qrcodePosterService;
    @Autowired
	private ShopService shopService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private WxAuthClient wxAuthClient;
	@Autowired
	private ImgTextMsgService imgTextMsgService;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private WxReplyRepository wxReplyRepository;
	@Autowired
	private WxContentService wxContentService;
	@Autowired
	private BrokerageSetService brokerageSetService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private ImageClient imageClient;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private HxUserService hxUserService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private WxKeyReplyService wxKeyReplyService;
    @Autowired
    private WxUserQrcodeService wxUserQrcodeService;
    @Autowired
    private SystemService systemService;
	@Autowired
	private WxUserQrcodeRepository wxUserQrcodeRepository;
	@Autowired
	private WxRedRecordRepository wxRedRecordRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ChannelQrcodeService channelQrcodeService;
	@Autowired
	private GoodsQrcodeEffectService goodsQrcodeEffectService;

    public List<ButtonVo> getMenu(String appid) throws Exception {
        MenuDto menuDto = null;
        List<ButtonVo> buttonVos = new ArrayList<ButtonVo>();
		menuDto =  wxClient.getMenu(wxAuthService.getAuthAccessToken(appid));
		buttonVos = menuDto.getMenu().getButton();
        return buttonVos;
    }

    public ResultMsg createMenu(ButtonVo[] buttonVo,String appid,Integer shopId) throws Exception {
        Menu menu = new Menu();
        menu.setButton(Arrays.asList(buttonVo));
        //String access = wxAuthService.getAuthAccessToken(appid);
        return wxClient.createMenu(wxAuthService.getAuthAccessToken(appid),menu);
    }
    
    /**
     * 初始化默认菜单
     * @throws Exception 
     */
    @Transactional
    public ResultMsg defaultMenu(String appid,Integer shopId) throws Exception{
    	ButtonVo btnOne = new ButtonVo();//一级
    	btnOne.setType("view");
    	btnOne.setName("商城首页");
    	btnOne.setUrl(Constant.DOMAIN+"/shop/index?shopId="+shopId);
    	btnOne.setLinkName("商城首页");
    	
    	//我的 下带5个子菜单
    	ButtonVo btnTwo = new ButtonVo();
    	btnTwo.setName("我的");
    	ButtonVo btnTwoSubBtn1 = new ButtonVo();//二级
    	btnTwoSubBtn1.setType("view");
    	btnTwoSubBtn1.setName("会员中心");
    	btnTwoSubBtn1.setUrl(Constant.DOMAIN+"/wxuser/index?shopId="+shopId);
    	btnTwoSubBtn1.setLinkName("会员中心");
    	
    	ButtonVo btnTwoSubBtn2 = new ButtonVo();//二级
    	btnTwoSubBtn2.setType("view");
    	btnTwoSubBtn2.setName("我的订单");
    	btnTwoSubBtn2.setUrl(Constant.DOMAIN+"/order/order_all/11?shopId="+shopId);
    	btnTwoSubBtn2.setLinkName("全部订单");
    	
    	ButtonVo btnTwoSubBtn3 = new ButtonVo();//二级
    	btnTwoSubBtn3.setType("view");
    	btnTwoSubBtn3.setName("我的佣金");
    	btnTwoSubBtn3.setUrl(Constant.DOMAIN+"/wxuser/brokerage?shopId="+shopId);
    	btnTwoSubBtn3.setLinkName("我的佣金");
    	
    	ButtonVo btnTwoSubBtn4 = new ButtonVo();//二级
    	btnTwoSubBtn4.setType("view");
    	btnTwoSubBtn4.setName("下级微客");
    	btnTwoSubBtn4.setUrl(Constant.DOMAIN+"/wxuser/lastuser?shopId="+shopId);
    	btnTwoSubBtn4.setLinkName("下级微客");
   
    	ButtonVo btnTwoSubBtn5 = new ButtonVo();//二级
    	btnTwoSubBtn5.setType("click");
    	btnTwoSubBtn5.setName("二维码海报");
    	btnTwoSubBtn5.setKey("GetCart");
    	btnTwoSubBtn5.setLinkName("二维码海报");
    	ButtonVo [] bt2 ={btnTwoSubBtn1,btnTwoSubBtn2,btnTwoSubBtn3,btnTwoSubBtn4,btnTwoSubBtn5};
    	
    	//品牌 下带2个子菜单
    	ButtonVo btnThree = new ButtonVo();
    	btnThree.setName("品牌");
    	ButtonVo btnThreeSubBtn1 = new ButtonVo();//二级
    	btnThreeSubBtn1.setType("view");
    	btnThreeSubBtn1.setName("招商加盟");
    	btnThreeSubBtn1.setUrl(Constant.DOMAIN+"/imageText/flag/3?shopId="+shopId);
    	btnThreeSubBtn1.setLinkName("招商加盟");
    	ButtonVo btnThreeSubBtn2 = new ButtonVo();//二级
    	btnThreeSubBtn2.setType("view");
    	btnThreeSubBtn2.setName("品牌故事");
    	btnThreeSubBtn2.setUrl(Constant.DOMAIN+"/imageText/flag/1?shopId="+shopId);
    	btnThreeSubBtn2.setLinkName("品牌故事");
    	ButtonVo [] bt3 ={btnThreeSubBtn1,btnThreeSubBtn2};
    	btnTwo.setSubButton(bt2);
    	btnThree.setSubButton(bt3);
    	ButtonVo [] btnArr = {btnOne,btnThree,btnTwo};
    	
    	systemService.saveMenu(btnArr, appid);//保存菜单到本地数据库
    	return createMenu(btnArr, appid, shopId);
    }
    
    
    public QrcodeVo getTicket(Map<String,Object> map ,String accessToken){
    	return wxClient.getTicket(map, accessToken);
    }
    
    
    /**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request, HttpServletResponse response,DOMSource domSource) throws Exception {
		
		WxAcceptVo wxAcceptVo = WxUtil.toWxAcceptVo(request,domSource);
		Map<String,String> requestMap = wxAcceptVo.getMap();
		
		//消息排重
		String respMessage = null;
		String appid = wxAcceptVo.getAppid(); // 消息内容
		String openid = wxAcceptVo.getOpenid(); // 发送方帐号（open_id）
		String toUserName = requestMap.get("ToUserName"); // 公众帐号
		String msgType = requestMap.get("MsgType"); // 消息类型
		String content = requestMap.get("Content"); // 消息内容
		String accessToken = wxAuthService.getAuthAccessToken(appid);
		requestMap.put("accessToken",accessToken);
		if("gh_3c884a361561".equals(toUserName)){ //全网发布微信检测
			checkWeixinAllNetworkCheck(request, response, content, requestMap, WxUtil.getPc(), wxAcceptVo.getTimestamp(), wxAcceptVo.getNonce());
		}
		if(isDuplicate(requestMap)){
			return "";
		}
		if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)){// 事件推送
			String eventType = requestMap.get("Event");
			if(MessageUtil.EVENT_TYPE_VIEW.equals(eventType)){//view 事件
				//收集48小时用户
				FortyEightUserUtil.addFortyEightUser(new FortyEightUser(appid,openid));
				updateUser(appid,openid,accessToken);//更新用户信息
				Map wxAcceptVoMap = wxAcceptVo.getMap();
				if(wxAcceptVoMap != null){
					String url = Toolkit.parseObjForStr(wxAcceptVoMap.get("EventKey"));
					saveMenuAccessCount(appid,openid,url,Constant.WX_MENU_VISIT_LIST);
				}
			}else if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)){// 订阅(关注)事件推送
				requestMap.put("remoteHost",request.getRemoteHost());
				subscribe(appid,openid,requestMap,response,request);
			}else if(MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equals(eventType)){ //取消订阅
				WxUser user = wxUserService.findWxUserByAppidAndOpenid(appid, openid);
				if(user!=null){
					user.setIsSubscribe(0);
					user.setUnSubscribeTime(new Date());
					wxUserService.saveUser(user);
				}
			}else if (MessageUtil.EVENT_TYPE_CLICK.equals(eventType)) { // 自定义菜单点击事件
				String key = requestMap.get("EventKey");
				if("GetCart".equals(key)){//获取名片
					//以图片形式返回，拼装图片对象所需信息
					log.info("------------------------GetCart  start------------------------");
					respMessage = senImage(wxAcceptVo);

					log.info("------------------------senImage  end------------------------");
				}
			}else if (MessageUtil.EVENT_TYPE_SCAN.equals(eventType)){  //扫描二维码事件
				String eventKey = requestMap.get("EventKey");
				String[] ids = null;
				if(eventKey!=null){
					ids = eventKey.split(",");
					if( ids.length==3 ){
						ChannelQrcode channelQrcode =channelQrcodeService.getChannelQrcode(Toolkit.parseObjForInt(ids[2]));
						channelQrcode.setFrequency(channelQrcode.getFrequency()+1);//渠道条码使用次数
						channelQrcodeService.save(channelQrcode);
					}
				}
			}
		}else if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {//文本消息
			WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(appid,openid);

			boolean flag  = wxKeyReplyService.keyReply(appid, openid, accessToken, content, response);
			if(!flag){//没匹配到的时候才进行固定回复
			    autoReply(appid, openid, accessToken, 2,response);//固定回复
			}
			Shop shop = shopService.getCacheShopByAppId(appid);
			sendHxMessage(wxUser,appid,openid,content,shop);
		}else if(MessageUtil.REQ_MESSAGE_TYPE_IMAGE.equals(msgType)){//图片消息
			autoReply(appid, openid, accessToken, 2,response);//固定回复
			String url = requestMap.get("PicUrl");
            WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(appid,openid);
            Shop shop = shopService.getShopByAppId(appid);
            sendPicmessage(wxUser,shop,url,openid,appid);
		}else if(MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)){//地理位置消息
			autoReply(appid, openid, accessToken, 2,response);//固定回复
		}else if(MessageUtil.REQ_MESSAGE_TYPE_LINK.equals(msgType)){//链接消息
			autoReply(appid, openid, accessToken, 2,response);//固定回复
		}else if(MessageUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)){//音频消息
			autoReply(appid, openid, accessToken, 2,response);//固定回复
		}
		 return respMessage;
	}

	/**
	 * <p>保存菜单访问信息</p>
	 *
	 * @Author cj
	 * @version latest
	 * @Date 2017/4/15 12:01
	 */
	private void saveMenuAccessCount(String appid, String openid, String url, Vector<WxMenuVisit> vector) {
		WxMenuVisit wxMenuAccessCount = new WxMenuVisit();
		wxMenuAccessCount.setAppid(appid);
		wxMenuAccessCount.setOpenid(openid);
		wxMenuAccessCount.setUrl(url);
		wxMenuAccessCount.setCreateTime(new Date());
		vector.add(wxMenuAccessCount);
	}

	/**
	 * 最后更新时间超过24小时更新用户信息
	 * @param appid
	 * @param openid
	 * @param accessToken
	 * @throws Exception
	 */
	private void updateUser(String appid,String openid,String accessToken) throws Exception {
		WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(appid,openid);
		if(wxUser!=null){
			long cha = new Date().getTime() - wxUser.getUpdateTime().getTime();
			double result = cha * 1.0 / (1000 * 60 * 60);
			if(result>=24){//超过24小时的用户重新拉取用户信息
				WxUserDto userDto = wxAuthClient.getWxUserDetail(accessToken, wxUser.getOpenid()); //获取用户明细
				wxUser = WxUserConverter.toWxUserForUpdate(wxUser,userDto,wxUser.getAppid());
                wxUser.setUpdateTime(new Date());
				wxUserService.saveUser(wxUser);
			}
		}
	}

	//关注
	public void subscribe(String appid,String openid,Map<String,String> requestMap,HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.getWriter().write("");
		response.getWriter().close();
		String token = requestMap.get("accessToken");
		WxPubAccount account = wxPubAccountService.getCacheWxPubAccount(appid);
		WxUser user = wxUserService.findWxUserByAppidAndOpenid(appid, openid);
		boolean flag = false;
		if (user==null||(user.getIsSubscribe()!=null&&user.getIsSubscribe().equals(2))){  //新用户
			flag = true;
		}
		WxUser newUser = saveUser(appid, openid,token,user,requestMap);//保存用户
		//发送图文消息
		ImgTextMsg msg = imgTextMsgService.findMsgbyAppidAndOpenId(appid,openid);
		if(msg!=null) {
            SendContextDo sendDo = new SendContextDo();
            sendDo.setAccessToken(token);
            sendDo.setAppid(msg.getAppid());
            sendDo.setContextId(msg.getId());
            sendDo.setOpenid(msg.getOpenid());
            sendDo.setType(msg.getType());
			sendDo.setUserId(newUser.getUserId());
			wxMessageService.sendContext(sendDo);
            imgTextMsgService.del(msg.getImgTextId());
		}
		activityService.addPushList(newUser,requestMap.get("remoteHost"),flag);//保存需要推送给用户的信息集合

		if(account.getType()==1){//是聚客红包的公众号
			ActivityRedDo activityRedDo = new ActivityRedDo();
			activityRedDo.setWxUser(newUser);
			activityRedDo.setClientIp(request.getRemoteHost());
			activityRedDo.setType(5);
			activityRedDo.setSubType(1);
			activityRedDo.setPlatform(1);
			activityRedDo.setBuyMoney(0);
			activityService.sendActivityRed(activityRedDo);
		}
		
		
	}

	public void sendUserQrcode(String token,WxUser user,String codeId) throws Exception {
		if(user==null || codeId==null || codeId.equals("") || token ==null){
			return;
		}
		QrcodePoster qrcodePoster = qrcodePosterService.getQrcodePosterById(Toolkit.parseObjForInt(codeId));
		String baseQrcode = wxUserQrcodeService.getBaseQrcode(user,0,0);//基础二维码地址
		if(qrcodePoster!=null){
			String imagesrc=qrcodePoster.getImageSrc(); //图片地址
			if(StringUtils.isNotEmpty(imagesrc)){
				Pic pic = new Pic();
				BufferedImage imageOne=pic.loadImageUrl(ImgUtil.appendUrl(imagesrc,0));//底图
				BufferedImage imageTwo=pic.loadImageUrl(ImgUtil.appendUrl(baseQrcode,0));//基础二维码
                  //裁剪图片
				imageTwo= pic.cropImage(imageTwo,25,25,405,405);
				if (imageOne!=null && imageTwo!=null){
					String nickName = Base64Util.getFromBase64(user.getNickname());
					double a = 2.5;//pc端页面显示倍数
					double b = 2.5;//pc端页面显示倍数
					int leftPosition =(int) ( Toolkit.parseObjForInt(qrcodePoster.getLeftPosition())*a );
					int topPosition = (int) ( Toolkit.parseObjForInt(qrcodePoster.getTopPosition())*b );
					InputStream is = pic.createPicReturnUrl(imageOne, imageTwo,leftPosition ,topPosition ,180,180);//合成图片
					InputStream uploadIs = is;
					if(qrcodePoster.getCodeFormat()==1){//二维码B版
						BufferedImage bi = ImageIO.read(is);
						int userBoxleftPosition =(int) ( Toolkit.parseObjForInt(qrcodePoster.getUserBoxleftPosition())*a )+30;//头像位置
						int userBoxtopPosition = (int) ( Toolkit.parseObjForInt(qrcodePoster.getUserBoxtopPosition())*b )+33;//头像位置
						int nickNameleftPosition= (int) ( Toolkit.parseObjForInt(qrcodePoster.getNickNameleftPosition())*a);//昵称位置
						int nickNametopPosition= (int) ( Toolkit.parseObjForInt(qrcodePoster.getNickNametopPosition())*b)+30;//昵称位置

						BufferedImage headImgBuf =  pic.zoomOutImage(pic.makeRoundedCorner(user.getHeadimgurl()),165,165);//头像转为圆图 背景透明
						BufferedImage imageBIcon = pic.markImageByIcon(headImgBuf,bi,0,userBoxleftPosition,userBoxtopPosition);//将头像添加到二位码框
						BufferedImage fontImg = pic.addFont(imageBIcon,36,nickNameleftPosition,nickNametopPosition,nickName,qrcodePoster.getFontType(),qrcodePoster.getFontColor());//添加文字到二维码框
						uploadIs = pic.toInputStream(fontImg);
					}

					String url = resourceService.uploadUrlImg(uploadIs);//上传图片到腾讯云， 返回url给我
					Shop shop =  shopService.getShopByAppId(user.getAppid());
					QrcodePoster startCode=qrcodePosterService.getDefault(shop.getShopId());//1.获取默认二维码
					if(startCode!=null){
						if(startCode.getQrcodeId() == qrcodePoster.getQrcodeId()){//启用的二维码 和 合成的这张二维码一致
							List<WxUserQrcode> wxUserQrcodes = wxUserQrcodeRepository.findByUserId(user.getUserId());
							if(wxUserQrcodes!=null && wxUserQrcodes.size()>0){
								WxUserQrcode wxUserQrcode = wxUserQrcodes.get(0);
								wxUserQrcode.setUserQrcode(url);
								wxUserQrcodeService.save(wxUserQrcode,user.getUserId());
							}
						}
					}

					WeixinUtil weixinUtil = new WeixinUtil();
					String mediaId = weixinUtil.upMedia("image", token,URLEncoder.encode(ImgUtil.appendUrl(url)));
					wxMessageService.sendImg(mediaId,token,user.getOpenid()); //发送二维码图片给用户
				}
			}
		}

	}


	/**
	 * 将图片传到微信的服务器，然后返回媒体的id
	 * @data 2016年7月7日
	 */
	public String getMediaId(String accessToken,WxUser wxUser) throws Exception{
        if(wxUser==null){
            return "";
        }
        String url =  wxUserQrcodeService.getBaseQrcode(wxUser,1,0);
		log.info("-----------baseQrcode :"+url);
        WeixinUtil weixinUtil = new WeixinUtil();
        String mediaId = weixinUtil.upMedia("image", accessToken,URLEncoder.encode(ImgUtil.appendUrl(url)));
		return mediaId;
	}

	/**
	 * 获取上级用户
	 * @return
	 */
	private WxUser getUpUser(String[] ids){
		WxUser user = null ;
		if(ids!=null && ids.length>0){
			String id =ids[0].replace(" ","");
			if(id!=null && !id.equals("")){
				user =  wxUserService.getWxUser(Toolkit.parseObjForInt(id));
			}
		}
		return user;
	}

	/**
	 * 保存微信用户到数据库，如果已经存在则修改
	 * @data 2016年7月7日
	 */
	public WxUser saveUser(String appid,String openid,String accessToken,WxUser user,Map<String,String> requestMap) throws Exception{
		String[] ids = null;
		String eventKey = requestMap.get("EventKey");
		if(eventKey!=null && eventKey.indexOf("qrscene_")>=0){
			String codeStr = eventKey.substring(8); //根据扫码的参数，将用户信息取出，作为上级
			ids = codeStr.split(",");
		}
		ChannelQrcode channelQrcode = null;
		if(null!=ids &&  ids.length==3 ){
			channelQrcode = channelQrcodeService.getChannelQrcode(Toolkit.parseObjForInt(ids[2]));
			channelQrcode.setFrequency(channelQrcode.getFrequency()+1);//使用次数
		}

		if(user==null){
			WxUserDto userDto = wxAuthClient.getWxUserDetail(accessToken, openid); //获取用户明细
			user = WxUserConverter.toWxUserForCreate(userDto,appid);
			Shop shop = shopService.getCacheShopByAppId(appid);
			WxUser upUser = null; //上级微客
			if(channelQrcode==null){
				upUser = getUpUser(ids);
			}else{
				channelQrcode.setFansType(1);
                boolean isExp = false;
				if(channelQrcode.getValidType()==1 ){ //永久有效
					isExp = true;
				}else{
					isExp = Toolkit.compareDate(channelQrcode.getEndTime(),new Date());
				}
				if(channelQrcode.getStatus()==0 && isExp ){//正常授权
					upUser = getUpUser(ids);
				}
			}

			if(upUser!=null && !openid.equals(upUser.getOpenid())){ //上级用户不为空，并且不是自己
				BrokerageSet set = brokerageSetService.getCacheBrokerageSet(shop.getShopId());
				if (set!=null){
					if(set.getIsOpen()==1){
						if(set.getOne()>0){
							user.setUpperOne(upUser.getUserId());//上级就是分享二维码的
						}
						if(set.getTwo()>0){
							user.setUpperTwo(upUser.getUpperOne());
						}
					}
				}
				IntegralSet integralSet = integralService.getCacheIntegralSet(shop.getShopId());
				if (integralSet == null || integralSet.getIsOpen() == 0 || integralSet.getIsAward() == 0) {
				}else {
					//调用推荐积分奖励任务
					AwardIntegralDo awardIntegralDo = new AwardIntegralDo();
					awardIntegralDo.setIntegralType(2);
					awardIntegralDo.setShopId(shop.getShopId());
					awardIntegralDo.setUserId(user.getUserId());
					awardIntegralDo.setUpOneUserId(upUser.getUserId());
					awardIntegralDo.setUpTwoUserId(upUser.getUpperOne());
					integralService.awardIntegral(awardIntegralDo);
				}
			}
			WxUser userNew = wxUserService.saveUser(user);
			if(channelQrcode!=null){
				if(channelQrcode.getStatus()==0) { //没有取消授权
					if(null!=ids && ids.length==3 ){ //加粉人数
						ProductQrcodeDetail effect = new ProductQrcodeDetail();//条码效果表
						effect.setUserId(user.getUserId());
						effect.setGoodsQrcodeId(Toolkit.parseObjForInt(ids[2]));
						effect.setType(0);
						goodsQrcodeEffectService.save(effect);

						Product product = productService.getProduct(Toolkit.parseObjForInt(ids[1]));
						List<PicMsgArticle> articles = new ArrayList<>();
						PicMsgArticle article = new PicMsgArticle();
						article.setPicurl(ImgUtil.appendUrl(product.getPicSquare(),720));
						article.setTitle(product.getName());
						article.setDescription(product.getShare());
						article.setUrl(Constant.DOMAIN+"/product/"+ids[1]+"?shopId="+shop.getShopId());
						articles.add(article);
						wxMessageService.sendImgTextMsg(user.getOpenid(),accessToken,articles);//回推商品图文消息

					}
				}
			}

			//系统通知
			if(shop!=null){
				wxUserService.sendMsg(userNew,upUser,shop.getShopId());
			}
		}else if(user.getIsSubscribe().intValue()==0){
			WxUserDto userDto = wxAuthClient.getWxUserDetail(accessToken, openid); //获取用户明细
			WxUserConverter.toWxUserForUpdate(user,userDto,appid);
			user.setIsSubscribe(1); //已存在，并且状态是未关注的，做修改状态操作
			wxUserService.saveUser(user);
		}else if(user.getIsSubscribe().intValue()==2){
			WxUserDto userDto = wxAuthClient.getWxUserDetail(accessToken, openid); //获取用户明细
			user = WxUserConverter.toWxUserForUpdate(user,userDto,appid);
			user.setIsSubscribe(1); //已存在，并且状态是未关注的，做修改状态操作
			wxUserService.saveUser(user);
			Shop shop = shopService.getCacheShopByAppId(appid);
			//系统通知
			if(shop!=null){
				WxUser upUser = wxUserService.getWxUser(user.getUpperOne());
				wxUserService.sendMsg(user,upUser,shop.getShopId());
			}

		}
		if(channelQrcode != null){
			channelQrcodeService.save(channelQrcode);
		}
		return user;
	}
	
	/**
	 *<p>发送图片消息</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws Exception 
	 * @data 2016年7月11日
	 */
	public String senImage(WxAcceptVo wxAcceptVo) throws Exception{
        String appid = wxAcceptVo.getAppid();
        String openid = wxAcceptVo.getOpenid();
        String toUserName = wxAcceptVo.getToUserName();

        String token = wxAuthService.getAuthAccessToken(appid);
        WxUser user = wxUserService.findWxUserByAppidAndOpenid(appid, openid);
		ImageMessage message = new ImageMessage();
		message.setFromUserName(toUserName);
		message.setToUserName(openid);
		message.setCreateTime(System.currentTimeMillis()); 
		message.setFuncFlag(0);  
		message.setMsgType(MessageUtil.REQ_MESSAGE_TYPE_IMAGE);
		String mediaId = getMediaId(token,user);
		message.getImage().setMediaId(mediaId);
		String respMessage= MessageUtil.imageMessageToXml(message);//转成xml形式
		respMessage = WxUtil.getPc().encryptMsg(respMessage, wxAcceptVo.getTimestamp(), wxAcceptVo.getNonce()); //加密返回
		return respMessage;
	}

	/**
	 * 全网发布检测
	 * @throws Exception 
	 */
	public void  checkWeixinAllNetworkCheck(HttpServletRequest request,HttpServletResponse response,
			String content,Map<String,String> requestMap,WXBizMsgCrypt pc,String timestamp,String nonce) throws Exception{
		String msgType = requestMap.get("MsgType");  
		String fromUserName = requestMap.get("FromUserName");
		String toUserName = requestMap.get("ToUserName");
		
		if(msgType.equals("event")){// 返回类型值，做一下区分  
            String event = requestMap.get("Event"); 
            log.info("------------------------event------------------------");
            replyEventMessage(request,response,event,fromUserName,toUserName,pc,timestamp, nonce);  
        } 
		if(msgType.equals("text")){ //标示文本消息，  
            String content2 = requestMap.get("Content");  
           processTextMessage(request,response,content2,fromUserName,toUserName,pc,timestamp, nonce);//用文本消息去拼接字符串。微信规定  
        }
	
	}
	
    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response,   
            String event, String fromUserName, String toUserName,WXBizMsgCrypt pc,String timeStamp, String nonce)   
                    throws Exception {  
    	String content = event + "from_callback";  
    	replyTextMessage(request,response,content,fromUserName,toUserName,pc,timeStamp,nonce);  
}  
    
    
    public void processTextMessage(HttpServletRequest request, HttpServletResponse response,  
            String content,String fromUserName, String toUserName,WXBizMsgCrypt pc,String timeStamp, String nonce)   
                    throws Exception{  
    	if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)){  
	    String returnContent = content+"_callback";  
	    log.info("------------------------text------------------------");
	    replyTextMessage(request,response,returnContent,fromUserName,toUserName,pc, timeStamp, nonce);  
    	}else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")){  
    		log.info("------------------------code------------------------");
    		response.getWriter().print("");//需在5秒内返回空串表明暂时不回复，然后再立即使用客服消息接口发送消息回复粉丝  
    		log.info("-----------------QUERY_AUTH_CODE类型------------------------------------");  
	    //接下来客服API再回复一次消息  
	    //此时 content字符的内容为是 QUERY_AUTH_CODE
    	replyApiTextMessage(content.split(":")[1],fromUserName,toUserName);  
    	} 
    } 
    
    public void replyTextMessage(HttpServletRequest request, HttpServletResponse response,   
            String content,String fromUserName, String toUserName,WXBizMsgCrypt pc,String timeStamp, String nonce)   
                                throws Exception {  
		Long createTime = System.currentTimeMillis() / 1000;  
		StringBuffer sb = new StringBuffer(512);  
		sb.append("<xml>");  
		sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");  
		sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");  
		sb.append("<CreateTime>"+createTime.toString()+"</CreateTime>");  
		sb.append("<MsgType><![CDATA[text]]></MsgType>");  
		sb.append("<Content><![CDATA["+content+"]]></Content>");  
		sb.append("</xml>");  
		String replyMsg = sb.toString();  
		//log.info("确定发送的XML为："+replyMsg);
		output(response, pc.encryptMsg(replyMsg, timeStamp, nonce));
		//return replyMsg;
    }  
    
    
    public void replyApiTextMessage(String auth_code, String fromUserName,String toUserName) throws Exception {  
        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来  
        String accessToken  = wxAuthClient.getAuthAccessToken(wxAuthService.getCommonAccessToken(), auth_code).getAuthorizerAccessToken();
        log.info("---------------处理api消息----------------------------");
        sendKfMsgCheck(fromUserName, "text", auth_code+"_from_api",accessToken);
         
        //log.info("客服发送接口返回值:"+result);  
}   
    
    /**
	 * 发送客服消息测试的
	 * @throws Exception 
	 * @data 2016年7月7日
	 */
	public ResultMsg sendKfMsgCheck(String touser ,String msgtype,String value,String accessToken) throws Exception{
		Map<String,Object> bigMap = new HashMap<>();
		Map<String,Object> smallMap = new HashMap<>();
			bigMap.put("touser", touser);
			bigMap.put("msgtype", "text");
			smallMap.put("content", value);
			bigMap.put("text", smallMap);
			log.info("----------------进入调用客服接口--------------------");
			ResultMsg sss = wxClient.sendWeixinMsg(bigMap, accessToken);
			return sss;
	}
	
    /** 
     * 工具类：回复微信服务器"文本消息" 
     * @param response 
     * @param returnvaleue 
     */  
    public void output(HttpServletResponse response,String returnvaleue){  
        try {  
            PrintWriter pw = response.getWriter();  
            pw.write(returnvaleue);  
            pw.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }

    /**
     * 发送微信红包
     * @param requestParam
     * @return
     * @throws Exception 
     */
    public RedResultParam sendRed(WeixinActInfo requestParam) throws Exception{
    	//金额至少在1元以上
		if(requestParam.getTotalAmount().intValue()<100){
			return new RedResultParam();
		}
		RedResultParam param = new RedResultParam();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		String mchBillno = Toolkit.getOrderNum("R"); //红包订单号
		WxPubAccount account = wxPubAccountService.getCacheWxPubAccount(requestParam.getWxappid());
        requestParam.setMchId(account.getMchId());
        requestParam.setAppkey(account.getAppKey());
        requestParam.setMchBillno(mchBillno);
		String xmlContent = WeixinRedUtils.addParam(packageParams, requestParam,account);//拼装xml参数
		String mchId = "";
		if(null!=account.getIsSub() && account.getIsSub().intValue()==1){
			//是子服务商
			mchId = Constant.SERVICE_MCHID;
		}else{
			mchId = account.getMchId();
		}
		String xmlRes = WeixinRedUtils.checkApiclientcert(mchId,xmlContent,WxUrl.WEIXIN_RED);//验证证书并且提交红包发送
		Map<String, Object> json  = WeixinUtil.converXml2Json(xmlRes);
		log.info("============="+json.toString()+"=================");
		String resultCode = (String) json.get("result_code");
		//如果返回的是失败，就把失败理由放入对象
		param.setResultCode(resultCode);
		
		//创建红包记录
		WxRedRecord redRecord = new WxRedRecord();
		redRecord.setMchId(mchId);
		redRecord.setAppid(requestParam.getWxappid());
		redRecord.setOpenid(requestParam.getReOpenid());
		redRecord.setMchBillno(mchBillno);
		redRecord.setAmount(requestParam.getTotalAmount());
		if("FAIL".equals(resultCode)){
			redRecord.setStatus(3);//设置状态为发送失败
			param.setErrCode( (String) json.get("err_code"));
			param.setErrCodeDes((String)json.get("err_code_des"));
			redRecord.setReason((String)json.get("err_code_des"));//记录失败原因
		}else {
			redRecord.setStatus(2);
		}
		WxRedRecord returnRedCord = wxRedRecordRepository.save(redRecord);
		param.setRedPayId(returnRedCord.getRedPayId());//将redPayId返回
		return param;
    }
    
    
    



	/**
	 * 发送红包
	 * @param wxRedDo
	 * @return
	 * @throws Exception
	 */
	public RedResultParam sendRed(WxRedDo wxRedDo) throws Exception{
		WeixinActInfo requestParam = new WeixinActInfo();
		BeanUtils.copyProperties(wxRedDo,requestParam);
		requestParam.setWxappid(wxRedDo.getAppid());
		requestParam.setReOpenid(wxRedDo.getOpenid());
		requestParam.setSendName(wxRedDo.getShopName());
		return sendRed(requestParam);
	}

    /**
     * 处理微信回调请求
     * @return
     * @throws Exception 
     */
    public Map<String,Object> wxNotify(HttpServletRequest request, DOMSource domSource) throws Exception{
    	return WxUtil.toWxWxNotifyMap(request,domSource);
    }
    /**
     * 临时方法 告诉微信已经回调成功
     * @return
     */
    public String returnWx(){
    	String sss = "<xml>"
    			+ "<return_code><![CDATA[SUCCESS]]></return_code>"
    			+ "<return_msg><![CDATA[OK]]></return_msg>"
    			+ "</xml>";
    	return sss;
    }
    /**
     * 支付成功后回调生成签名
     * @return
     */
    public String getNotifySign(Map<String,Object> xmlMap,HttpServletRequest request, DOMSource domSource,Integer isSub){
    			//防止伪造支付篡改数据，对回调参数做支付签名验证
    			SortedMap<String, String> packageParams = new TreeMap<String, String>();
    			packageParams.put("appid", (String)xmlMap.get("appid"));
    	        packageParams.put("mch_id",(String)xmlMap.get("mch_id"));
    	        String appkey = "";
    	        if(isSub!=0 && isSub.intValue()==1){ //是子商户
    	        	if(null!=xmlMap.get("sub_mch_id")){
    	    	    	 packageParams.put("sub_mch_id", Toolkit.parseObjForStr(xmlMap.get("sub_mch_id")));
    	    	    }
    	        	if(null!=xmlMap.get("sub_openid")){
    	    	    	 packageParams.put("sub_openid",Toolkit.parseObjForStr(xmlMap.get("sub_openid")));
    	    	    }
    	        	if(null!=xmlMap.get("sub_is_subscribe")){
    	    	   	 packageParams.put("sub_is_subscribe", Toolkit.parseObjForStr(xmlMap.get("sub_is_subscribe")));
    	    	    }
    	        	if(null!=xmlMap.get("sub_appid")){
    	    	    	 packageParams.put("sub_appid", Toolkit.parseObjForStr(xmlMap.get("sub_appid")));
    	    	    }
    	        	appkey = Constant.SERVICE_APPKEY;
    	    	 }else{//非子商户
    	    		 if(null!=xmlMap.get("openid")){
    	    			 packageParams.put("openid", Toolkit.parseObjForStr(xmlMap.get("openid")));
    	    			 WxPubAccount account = wxPubAccountService.getCacheWxPubAccount(Toolkit.parseObjForStr(xmlMap.get("appid"))); //获取微信公众账号
    	    		     appkey = account.getAppKey();
    	    		 }
    	    	 }
    	        
    	        if(null!=xmlMap.get("device_info")){
    	        	 packageParams.put("device_info", Toolkit.parseObjForStr(xmlMap.get("device_info")));
    	        }
    	        if(null!=xmlMap.get("nonce_str")){
    	        	packageParams.put("nonce_str", Toolkit.parseObjForStr(xmlMap.get("nonce_str")));
    	        }
    	        if(null!=xmlMap.get("return_code")){
    	        	packageParams.put("return_code", Toolkit.parseObjForStr(xmlMap.get("return_code")));
    	        }
    	        if(null!=xmlMap.get("return_msg")){
    	        	packageParams.put("return_msg", Toolkit.parseObjForStr(xmlMap.get("return_msg")));
    	        }
    	        if(null!=xmlMap.get("result_code")){
    	        	packageParams.put("result_code", Toolkit.parseObjForStr(xmlMap.get("result_code")));
    	        }
    	        if(null!=xmlMap.get("err_code")){
    	        	packageParams.put("err_code", Toolkit.parseObjForStr(xmlMap.get("err_code")));
    	        }
    	        if(null!=xmlMap.get("err_code_des")){
    	        	packageParams.put("err_code_des", Toolkit.parseObjForStr(xmlMap.get("err_code_des")));
    	        }
    	        if(null!=xmlMap.get("is_subscribe")){
    	        	packageParams.put("is_subscribe", Toolkit.parseObjForStr(xmlMap.get("is_subscribe")));
    	        }
    	        if(null!=xmlMap.get("trade_type")){
    	        	packageParams.put("trade_type", Toolkit.parseObjForStr(xmlMap.get("trade_type")));
    	        }
    	        if(null!=xmlMap.get("bank_type")){
    	        	packageParams.put("bank_type", Toolkit.parseObjForStr(xmlMap.get("bank_type")));
    	        }
    	        if(null!=xmlMap.get("total_fee")){
    	        	packageParams.put("total_fee", Toolkit.parseObjForStr(xmlMap.get("total_fee")));
    	        }
    	        if(null!=xmlMap.get("fee_type")){
    	        	packageParams.put("fee_type", Toolkit.parseObjForStr(xmlMap.get("fee_type")));
    	        }
    	        if(null!=xmlMap.get("cash_fee")){
    	        	packageParams.put("cash_fee", Toolkit.parseObjForStr(xmlMap.get("cash_fee")));
    	        }
    	        if(null!=xmlMap.get("cash_fee_type")){
    	        	packageParams.put("cash_fee_type", Toolkit.parseObjForStr(xmlMap.get("cash_fee_type")));
    	        }
    	        if(null!=xmlMap.get("coupon_fee")){
    	        	packageParams.put("coupon_fee", Toolkit.parseObjForStr(xmlMap.get("coupon_fee")));
    	        }
    	        if(null!=xmlMap.get("coupon_count")){
    	        	packageParams.put("coupon_count", Toolkit.parseObjForStr(xmlMap.get("coupon_count")));
    	        }
    	        if(null!=xmlMap.get("coupon_id_$n")){
    	        	packageParams.put("coupon_id_$n", Toolkit.parseObjForStr(xmlMap.get("coupon_id_$n")));
    	        }
    	        if(null!=xmlMap.get("coupon_fee_$n")){
    	        	packageParams.put("coupon_fee_$n", Toolkit.parseObjForStr(xmlMap.get("coupon_fee_$n")));
    	        }
    	        if(null!=xmlMap.get("transaction_id")){
    	        	packageParams.put("transaction_id", Toolkit.parseObjForStr(xmlMap.get("transaction_id")));
    	        }
    			if(null!=xmlMap.get("out_trade_no")){
    			   packageParams.put("out_trade_no", Toolkit.parseObjForStr(xmlMap.get("out_trade_no")));
    			}
    			if(null!=xmlMap.get("time_end")){
    			   packageParams.put("time_end", Toolkit.parseObjForStr(xmlMap.get("time_end")));
    			}
    			if(null!=xmlMap.get("attach")){
    			   packageParams.put("attach", Toolkit.parseObjForStr(xmlMap.get("attach")));
    	        }
    	        String sign = WeixinPayUtil.createSign(packageParams,appkey);
    	        log.info("================huidiaoSign success===============");
    	        return sign;
    }
    
  
    /**
     * 自动回复（固定回复、关注自动回复）
     * @throws Exception 
     */
    public void autoReply(String appid,String openid,String accessToken,Integer replyType,HttpServletResponse response) throws Exception{
    	response.getWriter().write("");//告诉微信不需要连续发请求，先响应微信，在后续慢慢处理我们的业务
		response.getWriter().close();
		autoReply(appid, openid, accessToken, replyType);
    }

	public void autoReply(String appid,String openid,String accessToken,Integer replyType) throws Exception{
		//关注回复
		if(replyType.intValue()==1){
			//查询是否有添加关注回复内容
			WxReply reply = wxReplyRepository.findByAppidAndReplyType(appid, 1);
			if(reply!=null){
				HandelAutoReply(reply, openid, accessToken, appid,replyType);
			}
			//固定回复
		}else if(replyType.intValue()==2){
			//查询是否开启固定回复
			WxPubAccount account = wxPubAccountService.getCacheWxPubAccount(appid);
			if(null != account.getIsFixedReply() && account.getIsFixedReply().intValue()==1){
				WxReply reply = wxReplyRepository.findByAppidAndReplyType(appid, 2);
				//是否有固定回复内容
				if(reply!=null){
					HandelAutoReply(reply, openid, accessToken, appid,replyType);
				}
			}
		}
	}
    
    /**
     * 处理回复业务
     * @param reply
     * @param openid
     * @param accessToken
     * @param appid
     */
    public void HandelAutoReply(WxReply reply,String openid,String accessToken,String appid,Integer replyType){
    		if(reply.getReplyContentType().intValue()==1){//普通文本消息
    			wxMessageService.sendMsgTofixedAndKey(openid, accessToken, reply.getContent(),appid);//发送文本
    		}else if(reply.getReplyContentType().intValue()==8){//外网链接，不带尾链接
    			wxMessageService.sendMsg(openid, accessToken, reply.getContent(),appid);
    		}else if(reply.getReplyContentType().intValue()==3){//商城链接
    			if(reply.getImgTextId()!=null && reply.getImgTextId()>0){//图文消息
        			if(reply.getImgTextType().intValue()==1){//1、微信图文
        				wxMessageService.sendImgTextToMediaId(reply.getMediaId(), accessToken, openid);//发送微信图文消息
        			}else if(reply.getImgTextType().intValue()==2 || reply.getImgTextType().intValue()==3){
        				//项目图文与乐享图文都是在一张表里面，所以id已经是精确查找 不需要再分类型
        				Shop shop = shopService.getShopByAppId(appid);
        				PicMsgArticle article = wxContentService.assemblingImgText(reply.getImgTextId(),shop.getShopId());
        				List<PicMsgArticle> articles = new ArrayList<>();
        				articles.add(article);
        				wxMessageService.sendImgTextMsg(openid, accessToken, articles);
        			}
    			}else{//非图文消息
    				if(replyType!=null && replyType.intValue()==2){
    					//固定回复需要带尾链接
    					wxMessageService.sendMsgTofixedAndKey(openid, accessToken, reply.getContent(), appid);
    				}else{
    					wxMessageService.sendMsg(openid, accessToken, reply.getContent(),appid);
    				}
    			}
    			
    		}else if(reply.getReplyContentType().intValue()==4){
    			wxMessageService.sendImg(reply.getMediaId(), accessToken, openid);
    		}
    }

    /**
     * 处理公众号接收的文本消息
     */
    private String sendHxMessage(WxUser wxUser, String appid, String openid, String content, Shop shop) throws Exception {
        //接收消息 1.通过从消息中获取的openId获取微信用户信息，
        //2.判断是否有环信账号，没有就注册一个，并且保存到微信用户中
        //3.判断商户的环信账号是否存在，不存在则注册,并保存
        //4.通过这个环信账号给商户发消息，
        String sendUser = getHxUserAccount(wxUser, "txt", content);

        if (sendUser != null) {
            // 获取商家的客服人员
            String to = Equalizer.getOnlineService(shop.getShopId(), sendUser);
            log.info("-------get toUser:"+to+"------");
            if (to == null) {
                to = shopHxUserReg(to, shop);
            }
            String[] toUser = new String[]{to};
            postDateToHx(sendUser, "", content, toUser, openid, appid);
            //刷新客户的在线时间
            Equalizer.addCustormer(sendUser, shop.getShopId(), to);
			log.info("sendUser:"+sendUser+"toUser"+to);
        } else {
            log.info("-------环信消息发送失败-------,发送账号wxAppid", wxUser.getAppid());
            return "发送失败";
        }
        //HxHttpClient client = new HxHttpClient();
        return "发送成功";
    }

    public String shopHxUserReg(String to, Shop shop) throws Exception {
        UserRole userRole = userRoleService.findShopMasterByShopId(shop.getShopId());
        to = userRole.getHxAccount();
        if (null == to || "".equals(to)) {
            String shopUserHxAcc = "hxs_" + shop.getAppId() + "_" + userRole.getUserId();
            if (hxReg(shopUserHxAcc, shop.getShopName())) {
                userRole.setHxAccount(shopUserHxAcc);
                userRoleService.updateUser(userRole);
                to = shopUserHxAcc;
				log.info("to user2 "+to);
            }
        }
        return to;
    }



    /**
     * 获取微信用户环信账号并且更新环信用户表信息
     * */
    public String getHxUserAccount(WxUser wxUser,String type,String content) throws Exception {
    	if(wxUser==null){
    		return null;
		}
        Integer userId = wxUser.getUserId();
        HxUser hxUser = hxUserService.getHxUserByUid(userId);
        String fromUser;
        String lastMsg = null;
        switch(type){
            case "pic":
                lastMsg ="[图片]";
                break;
            case "txt":
                lastMsg = content;
                break;
            case "file":
                lastMsg = "[文件]";
                break;
        }
        if(null==hxUser){
            if(hxReg("hx_"+wxUser.getOpenid(),wxUser.getNickname())){
                fromUser = "hx_"+userId;
                HxUserCo hxUserCo = new HxUserCo();
                hxUserCo.setHxAccount(fromUser);
                hxUserCo.setUserId(userId);
                hxUserCo.setCreateTime(new Date());
                hxUserCo.setChannel(1);
                hxUserCo.setChatType(type);
                hxUserCo.setLastChatDate(new Date());
                hxUserCo.setIsReply(0);
                hxUserCo.setLastMsg(Base64Util.enCoding(lastMsg));
                hxUserService.save(hxUserCo);
            }else{
                return null;
            }
        }else{
            hxUser.setLastMsg(Base64Util.enCoding(lastMsg));
            hxUser.setLastChatDate(new Date());
            hxUser.setChannel(1);
            hxUser.setChatType(type);
            hxUserService.save(hxUser);
            fromUser = hxUser.getHxAccount();
        }
        return fromUser;
    }


    /**
	 *上传公众号图片
	 * */
	private String uploadImg(String url) throws IOException {
		Pic pic = new Pic();
		BufferedImage imageNew=pic.loadImageUrl(url);
		ByteArrayOutputStream os = new ByteArrayOutputStream(); //将生成的图片转为InputStream
		String suffix = ImgUtil.getSuffix(url,"gif");
		ImageIO.write(imageNew, suffix, os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		UploadResult uInfo = imageClient.uploadInputStream(is,suffix);
		return uInfo.downloadUrl;
	}
    /**
     * 处理公众号的图片消息
     * */
    public boolean sendPicmessage(WxUser wxUser,Shop shop,String wxImgurl,String openid,String appid) throws Exception {
		String url = uploadImg(wxImgurl);
        String fromUser = getHxUserAccount(wxUser,"pic",null);
        String to = Equalizer.getOnlineService(shop.getShopId(),fromUser);
        if(to==null){
            UserRole userRole = userRoleService.findShopMasterByShopId(shop.getShopId());
            to = userRole.getHxAccount();
            if(null==to||"".equals(to)){
                to = shopHxUserReg(to,shop);
            }
        }
        String[] to_users = new String[]{to};
        log.info("-------------------------------- wx message to user:"+to);
        return postPicToHx(fromUser,shop.getShopName(),url,to_users,appid,openid);
    }

    /**
     * 推送文字消息到环信IM
     * */
    public boolean postDateToHx(String from_user,String shopName,String content,String[] to_users) throws Exception {
        return postDateToHx(from_user,shopName,content,to_users,"","");
    }
    /**
     * 环信注册
     * */
    public boolean hxReg(String hxaccount,String nickname) throws Exception {
        Map<String,Object> regOptions = new HashMap<String,Object>();
        regOptions.put("nikcname",nickname);
        regOptions.put("password","123456");
        regOptions.put("username",hxaccount);
        HxHttpClient client = new HxHttpClient();
        HxResultMessage resultMessage = client.postHxMsg(HxUtils.HX_URL+ HxUtils.ORG_NAME+HxUtils.APP_NAME+"/users",null,regOptions);
        String errorCode = resultMessage.getError();
        if(null==errorCode||"".equals(errorCode)||errorCode.equals("duplicate_unique_property_exists")){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 发送图片消息到环信
     * */
    public boolean postPicToHx(String from_user,String shopName,String url,String [] toUsers,String appid,String openid) throws Exception {
        Map<String,Object> msg = new HashMap<>();
        msg.put("type","img");
        msg.put("url",url);
        msg.put("filename","pic.jpg");
        Map<String,Object> ext = new HashMap<>();
        ext.put("type","weixin");
        ext.put("shopName",shopName);
        ext.put("openid",openid);
        ext.put("appid",appid);
        return postToHx(from_user,toUsers,msg,ext);
    }
    /**
     * 发送消息到环信
     * */
    public boolean postToHx(String from_user,String[] to_users,Map<String,Object> messages,Map<String,Object> ext) throws Exception {
        HxHttpClient client = new HxHttpClient();
        HxToken token = client.getToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization"," Bearer "+token.getAccess_token());
        Map<String,Object> msg = new HashMap<>();
        msg.put("target_type","users");
        msg.put("from",from_user);
        msg.put("target",to_users);
        JSONObject msgObj = new JSONObject(messages);
        msg.put("msg",msgObj);
        JSONObject extObj = new JSONObject(ext);
        msg.put("ext",extObj);
        HxResultMessage result = client.postHxMsg(HxUtils.HX_URL+ HxUtils.ORG_NAME+HxUtils.APP_NAME+"/messages",headers,msg);
        if(null!=result.getError()){
            return true;
        }
        return false;
    }
    /**
     * 发送文本消息到环信
     * */
    public boolean postDateToHx(String from_user,String shopName,String content,String[] to_users,String opendId,String appid) throws Exception {
        Map<String,Object> message = new HashMap<>();
        message.put("type","txt");
        message.put("msg",content);
        Map<String,Object> ext = new HashMap<>();
        ext.put("type","weixin");
        ext.put("shopName",shopName);
        ext.put("openid",opendId);
        ext.put("appid",appid);
        return postToHx(from_user,to_users,message,ext);
    }

	/**
	 * 获取永久公众号基础二维码
	 * @return
	 */
	public String getForeverQrcode(String accessToken){
		String pubQrcodeUrl = wxClient.getStrTicket(accessToken," ");
		return pubQrcodeUrl;
	}
	
	
	
	private static final int MESSAGE_CACHE_SIZE = 500;  
    private static List<WxRepeatMsg> MESSAGE_CACHE_LIST = new ArrayList<WxRepeatMsg>(MESSAGE_CACHE_SIZE); 
	
	/**
	 * 消息排重
	 * @param request
	 * @return
	 */
	public static boolean isDuplicate(Map<String, String> request) {  
		 String fromUserName = request.get("FromUserName");  
	     String createTime = request.get("CreateTime");  
		WxRepeatMsg repeatMsg  = new WxRepeatMsg();
    	repeatMsg.setCreateTime(createTime);  
    	repeatMsg.setFromUserName(fromUserName); 
	        if (MESSAGE_CACHE_LIST.contains(repeatMsg)) {  
	            // 缓存中存在，直接pass  
	            return true;  
	        } else {  
	            setMessageToCache(repeatMsg);  
	            return false;  
	        }  
	    } 
	
	
	private static void setMessageToCache(WxRepeatMsg repeatMsg) {  
        if (MESSAGE_CACHE_LIST.size() >= MESSAGE_CACHE_SIZE) {  
        	MESSAGE_CACHE_LIST.clear();
        }  
        MESSAGE_CACHE_LIST.add(repeatMsg);  
    } 

}
