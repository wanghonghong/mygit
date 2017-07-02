package com.jm.mvc.controller.system;

import com.jm.business.service.order.OrderService;
import com.jm.business.service.product.ProductGroupService;
import com.jm.business.service.shop.distribution.BrokerageRedService;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.erp.ErpWxUserQo;
import com.jm.mvc.vo.erp.ErpWxUserRo;
import com.jm.mvc.vo.product.group.ProductGroupVo;
import com.jm.mvc.vo.shop.CommVo;
import com.jm.repository.jpa.shop.distribution.BrokerageKitRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageRecordRepository;
import com.jm.repository.jpa.shop.distribution.PutSetRepository;
import com.jm.repository.jpa.wx.WxUserAccountRepository;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.BrokerageRecord;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.repository.po.shop.brokerage.PutSet;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>微信个人中心</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/6
 */

@Api
@RestController
@RequestMapping(value = "/wxuser")
public class WxUserController {

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductGroupService productGroupService;


	@Autowired
	private WxUserAccountRepository wxUserAccountRepository;

	@Autowired
	private BrokerageRecordRepository brokerageRecordRepository;

	@Autowired
	private PutSetRepository putSetRepository;

	@Autowired
	private BrokerageKitRepository brokerageKitRepository;

	@Autowired
	private BrokerageRedService brokerageRedService;

	@Autowired
	private BrokerageService brokerageService;

	@Autowired
	private BrokerageSetService brokerageSetService;

	@Autowired
	private ShopUserService shopUserService;



	@ApiOperation("个人中心")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		ModelAndView view = new ModelAndView();
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer userId = wxUserSession.getUserId();//微信用户编号
		String qrcodeurl="";//二维码地址
		List<Object> orderlist = new ArrayList<Object>();//订单数量列表
		if (userId==0){ //游客
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getShareid(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,730);
		}else if(userId>0 && wxUserSession.getIsSubscribe()==0){ //跑路
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getUserId(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,730);
		}else{ //正常关注
			orderlist = orderService.queryOrderStatus(userId,wxUserSession.getShopId());//app端订单管理订单状态查询
		}
		//获取工程路径
		List<ProductGroupVo> groupList = productGroupService.queryGroupListByShopId(wxUserSession.getShopId()); //获取分组标签
		ModelMap modelMap = view.getModelMap();
		modelMap.put("productGroupList", groupList);
		request.setAttribute("orderlist", orderlist);
		request.setAttribute("qrcode", qrcodeurl);
		view.setViewName("/app/index/wxuser");
		return view;
	}

	@ApiOperation("下级微客页面")
	@RequestMapping(value = "/lastuser", method = RequestMethod.GET)
	public ModelAndView lastUser(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer userId = wxUserSession.getUserId();
		String qrcodeurl="";
		if (userId==0){ //游客
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getShareid(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else if(userId>0 && wxUserSession.getIsSubscribe()==0){ //跑路
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getUserId(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else{ //正常关注
			BrokerageSet set = brokerageSetService.getCacheBrokerageSet(wxUserSession.getShopId());
			if(set.getIsOpen()==1){
				int oneIsshow=0;
				int towIsshow=0;
				Object one = wxUserService.getWxUserUpperOneById(userId);
				Object two = wxUserService.getWxUserUpperTwoById(userId);
				if(set.getOne()>0){
					oneIsshow = 1;
				}
				if(set.getTwo()>0){
					towIsshow=1;
				}
				request.setAttribute("oneIsshow", oneIsshow);
				request.setAttribute("twoIsshow", towIsshow);
				request.setAttribute("one", one);
				request.setAttribute("two", two);
			}
		}
		ModelAndView view = new ModelAndView();
		request.setAttribute("qrcode", qrcodeurl);
		view.setViewName("/app/user/lastuser");
		return view;
	}

	@ApiOperation("一级微客数据")
	@RequestMapping(value = "/lastuserOne", method = RequestMethod.POST)
	public List<WxUser> lastUserOne(@ApiParam(hidden=true) HttpServletRequest request){
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		String openid = wxUserSession.getOpenid();
		String appid = wxUserSession.getAppid();
		WxUser wxuser=null;
		List<WxUser> one1 = new ArrayList<WxUser>();
		//获取微信用户
		if(openid!=null&&appid!=null){
			wxuser=wxUserService.findWxUserByAppidAndOpenid(appid, openid);
		}
		if(wxuser!=null){
			Integer wxuserid=wxuser.getUserId();
			List<WxUser> one = wxUserService.getWxUserUpperOneListById(wxuserid);
			for (WxUser wxUser2 : one) {
				wxUser2.setNickname(Base64Util.getFromBase64(wxUser2.getNickname()));
				one1.add(wxUser2);
			}
		}
		return one1;
	}

	@ApiOperation("二级微客数据")
	@RequestMapping(value = "/lastuserTwo", method = RequestMethod.POST)
	public List<WxUser> lastUserTwo(@ApiParam(hidden=true) HttpServletRequest request){
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		String openid = wxUserSession.getOpenid();
		String appid = wxUserSession.getAppid();
		WxUser wxuser=null;
		List<WxUser> two1 = new ArrayList<WxUser>();
		//获取微信用户
		if(openid!=null&&appid!=null){
			wxuser=wxUserService.findWxUserByAppidAndOpenid(appid, openid);
		}
		if(wxuser!=null){
			Integer wxuserid=wxuser.getUserId();
			List<WxUser> two = wxUserService.getWxUserUpperTwoListById(wxuserid);
			for (WxUser wxUser2 : two) {
				wxUser2.setNickname(Base64Util.getFromBase64(wxUser2.getNickname()));
				two1.add(wxUser2);
			}
		}
		return two1;
	}

	@ApiOperation("佣金下拉翻页数据")
	@RequestMapping(value = "/brokerage_page", method = RequestMethod.GET)
	public List<CommVo>  brokerage_page(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		List<CommVo>  comm = new ArrayList<CommVo>();
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		int userid =wxUserSession.getUserId();
		Integer curPage = Integer.valueOf(request.getParameter("curPage")) ;
		if (userid>0){
			comm=brokerageService.getCommTop20(userid,curPage*10);
		}
		return comm;
	}



	@ApiOperation("我的佣金/佣金明细   页面")
	@RequestMapping(value = "/brokerage", method = RequestMethod.GET)
	public ModelAndView brokerage(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		Integer userId = wxUserSession.getUserId();
		String qrcodeurl="";
		List<CommVo>  comm = new ArrayList<CommVo>();
		String shopName=wxUserSession.getShopName();
		WxUser wxuser=null;
		int br = 0;
		int send = 0;//已发佣金
		int balance = 0;//余额
		int totalCount = 0;//历史总佣金
		WxUserAccount userAccount = new WxUserAccount();
		if (userId==0){ //游客
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getShareid(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else if(userId>0 && wxUserSession.getIsSubscribe()==0){ //跑路
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getUserId(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else { //正常关注
			wxuser =  wxUserService.getWxUser(wxUserSession.getUserId());
			Object monthMoney = brokerageService.getNowMonthSumPrice(wxUserSession.getUserId());//本月佣金金额
			br=Toolkit.parseObjForInt(monthMoney);
			userAccount =  wxUserAccountRepository.findByUserIdAndAccountType(wxUserSession.getUserId(),1);//佣金账户
			if(userAccount!=null){
				send = userAccount.getTotalCount()-userAccount.getTotalBalance();//已发佣金
				balance = userAccount.getTotalBalance();
				totalCount = userAccount.getTotalCount();
			}
			comm=brokerageService.getCommTop20(wxuser.getUserId(),0);
		}
		ModelAndView view = new ModelAndView();
		request.setAttribute("send", send);
		request.setAttribute("balance", balance);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("nowMonthSumPrice", br);
		request.setAttribute("commList", comm);
		request.setAttribute("wxuser", wxuser);
		request.setAttribute("qrcode", qrcodeurl);
		view.setViewName("/app/user/brokerage");
		return view;
	}


	@ApiOperation("发放明细下拉翻页数据")
	@RequestMapping(value = "/brokerage_cash_data", method = RequestMethod.GET)
	public List<BrokerageRecord>  brokerageCashData(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		List<BrokerageRecord> brokerageRecords  = new ArrayList<>();
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		int userid =wxUserSession.getUserId();
		Integer curPage = Integer.valueOf(request.getParameter("curPage")) ;
		if (userid>0){
			brokerageRecords=brokerageRecordRepository.findRecordByUserIdLimit(userid,curPage*10);
		}
		return brokerageRecords;
	}


	@RequestMapping(value = "/brokerage_send", method = RequestMethod.GET)
	public ModelAndView brokerageSend(@ApiParam(hidden=true) HttpServletRequest request) throws Exception{
		ModelAndView view = new ModelAndView();
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		WxUserAccount cashAccount = new WxUserAccount();//现金账户
		WxUserAccount commAccount = new WxUserAccount();//佣金账户
		List<BrokerageRecord> brokerageRecords  = new ArrayList<>();
		String roleName="";
		int putType = 0;
		String qrcodeurl="";
		if (wxUserSession.getUserId()==0){//游客
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getShareid(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else if(wxUserSession.getUserId()>0 && wxUserSession.getIsSubscribe()==0){ //跑路
			qrcodeurl= wxUserService.getQrcodeUrl(wxUserSession.getUserId(),wxUserSession.getAppid());//分享人的二维码
			qrcodeurl = ImgUtil.appendUrl(qrcodeurl,720);
		}else { //正常关注
			roleName = "关注客户";
			PutSet putSet =  putSetRepository.findByShopIdAndPayType(wxUserSession.getShopId(),1);
			cashAccount = wxUserAccountRepository.findByUserIdAndAccountType(wxUserSession.getUserId(),0);
			commAccount = wxUserAccountRepository.findByUserIdAndAccountType(wxUserSession.getUserId(),1);
			//brokerageRecords=	brokerageRecordRepository.findByUserId(wxUserSession.getUserId());
			brokerageRecords = brokerageRecordRepository.findRecordByUserIdLimit(wxUserSession.getUserId(),0);
			WxUser wxUser =  wxUserService.findWxUserByUserId(wxUserSession.getUserId());
			if(wxUser.getIsBuy()!=0){
				roleName = "购买客户";
			}
			if(wxUser.getShopUserId()!=null){
				ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());
				if(shopUser!=null){
					if(shopUser.getAgentRole()==1){
						roleName = "代理商1档";
					}else if (shopUser.getAgentRole()==2){
						roleName = "代理商2档";
					}else if (shopUser.getAgentRole()==3){
						roleName = "代理商3档";
					}else if (shopUser.getAgentRole()==4){
						roleName = "代理商4档";
					}else if (shopUser.getAgentRole()==5){
						roleName = "分销商1档";
					}else if (shopUser.getAgentRole()==6){
						roleName = "分销商2档";
					}else if (shopUser.getAgentRole()==7){
						roleName = "分销商3档";
					}

				}
			}
			if(cashAccount==null){
				cashAccount = new WxUserAccount();
			}
			if(commAccount==null){
				commAccount = new WxUserAccount();
			}
			if(brokerageRecords==null){
				brokerageRecords = new ArrayList<>();
			}
			if(putSet!=null){
				putType = putSet.getPutType();
			}
		}

		request.setAttribute("roleName", roleName);
		request.setAttribute("putType", putType);
		request.setAttribute("qrcode", qrcodeurl);
		request.setAttribute("cashAccount",cashAccount);
		request.setAttribute("commAccount",commAccount);
		request.setAttribute("brokerageRecords",brokerageRecords);
		view.setViewName("/app/user/brokerage_cash");
		return view;
	}



	/*@RequestMapping(value = "/cash", method = RequestMethod.POST)
	public JmMessage cash(@ApiParam(hidden=true) HttpServletRequest request,@RequestBody @Valid BrokerageKitForCreateVo kitVo) throws Exception{
		WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
		WxUser wxUser = wxUserService.findWxUserByUserId(wxUserSession.getUserId());
		if(wxUser.getShopUserId() == null || wxUser.getShopUserId()<=0){
			return new JmMessage(2,"未注册店铺用户");
		}

		PutSet putSet =  putSetRepository.findByShopId(wxUserSession.getShopId());
		if(putSet!=null){
			//佣金账户
			WxUserAccount commAccount = wxUserAccountRepository.findByUserIdAndAccountType(wxUserSession.getUserId(),1);
			if(commAccount==null){
				return new JmMessage(1,"还没有佣金！");
			}
			kitVo.setKitMoney(kitVo.getKitMoney()*100);//提现金额
            if(kitVo.getKitMoney()>20000){
                return new JmMessage(1,"单笔提现金额不可超出200！");
            }
			if(kitVo.getKitMoney()<100){
				return new JmMessage(1,"提现金额不得低于1元！");
			}
			if((commAccount.getKitBalance()-kitVo.getKitMoney())<0){
				return new JmMessage(1,"可提现余额不足！");
			}
			int count = brokerageService.getKitCount(wxUserSession.getUserId(),1);//当天提现次数
			if(putSet.getAutoType()==4){//免审核
				if(putSet.getKitNum()!=0){
					if(putSet.getKitNum()-count<=0){//提现次数
						return new JmMessage(1,"当日提现次数超出");
					}
				}
				if(putSet.getMinMoney()!=0){
					if(putSet.getMinMoney()- kitVo.getKitMoney()>0){//每次提现金额
						return new JmMessage(1,"提现金额超出");
					}
				}
				int money =(int) kitVo.getKitMoney();
				RedSendParam redSendParam = brokerageRedService.getRedSendParam(request.getRemoteHost(),wxUserSession.getUserId(),wxUserSession.getShopId(),20000,2,4,money,null);
				brokerageRedService.brokerageRedSend(redSendParam);
				return new JmMessage(0,"提现成功");
			}
			if(putSet.getAutoType()==5){//需审核
				if(putSet.getKitNum()!=0){
					if(putSet.getKitNum()-count<=0){//提现次数
						return new JmMessage(1,"当日提现次数超出");
					}
				}
				if(putSet.getMinMoney()!=0){
					if(putSet.getMinMoney()- kitVo.getKitMoney()>0){//每次提现金额
						return new JmMessage(1,"提现金额超出");
					}
				}
				WxAccountKit kit = new WxAccountKit();//提现申请表
				kit.setShopId(wxUserSession.getShopId());
				kit.setUserId(wxUserSession.getUserId());
				kit.setPlatForm(0);
				kit.setType(1);
				kit.setKitDate(new Date());
				kit.setKitMoney((int)kitVo.getKitMoney());
				kit.setStatus(0);
				brokerageKitRepository.save(kit);
				commAccount.setKitBalance((int)(commAccount.getKitBalance()-kitVo.getKitMoney()));
				commAccount.setTotalBalance((int)(commAccount.getTotalBalance()-kitVo.getKitMoney()));
				wxUserAccountRepository.save(commAccount);
				return new JmMessage(0,"提现申请成功");
			}
			return new JmMessage(1,"商家未开启提现功能");
		}

		return new JmMessage(1,"提现失败");
	}*/


	@ApiOperation("总部系统 公众号列表")
	@RequestMapping(value = "/erpWxUsers", method = RequestMethod.POST)
	public PageItem<ErpWxUserRo> erpWxUsers(@RequestBody @Valid ErpWxUserQo qo) throws ParseException, IOException {
		PageItem<ErpWxUserRo>  ros = wxUserService.findErpWxUsers(qo);
		return ros;
	}


}
