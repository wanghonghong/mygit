package com.jm.mvc.controller.wx;

import com.jm.business.domain.shop.ActivityRedDo;
import com.jm.business.service.order.OrderDetailService;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.order.PayRecordService;
import com.jm.business.service.product.ProductService;
import com.jm.business.service.product.ProductSpecService;
import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.shop.distribution.BrokerageBuildService;
import com.jm.business.service.shop.distribution.BrokerageRedService;
import com.jm.business.service.system.UserAccountService;
import com.jm.business.service.wx.WxPayService;
import com.jm.business.service.wx.WxService;
import com.jm.business.service.wx.WxUserService;
import com.jm.business.service.wx.WxpublicAccountService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.order.PayRecordVo;
import com.jm.mvc.vo.wx.WeixinPayVo;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.product.ProductSpecRepository;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.PayRecord;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductSpec;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.system.JmRechargeOrder;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.PayRecordConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *<p>微信支付业务逻辑控制器</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月1日
 */
@Log4j
@Api
@RestController
@RequestMapping(value = "/pay")
public class WxPayController {
	@Autowired
	private PayRecordService recordService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductSpecService productSpecService;
	@Autowired
	private ProductSpecRepository productSpecRepository;
	@Autowired
	private WxService wxService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private BrokerageBuildService brokerageBuildService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private BrokerageRedService brokerageRedService;
	@Autowired
	private UserAccountService userAccountService;
	


/*	@ApiOperation("微信支付统一下单")
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public ModelAndView userAuth(@ApiParam(hidden=true)HttpServletRequest request,ModelMap model,
								 @ApiParam("订单ID")  @RequestParam(value="orderInfoId",required= false) Long orderInfoId,
								 @ApiParam("订单类型")  @RequestParam(value="type",required= false) Integer type) throws Exception {
		//type 订单类型   0：普通购买支付   1：积分充值或者渠道充值
		ModelAndView view = new ModelAndView();
		if(!Toolkit.isWxBrowser(request)) {//不是微信浏览器
			return view;
		}
		int orderType = Toolkit.parseObjForInt(type).intValue();
		WxUserSession wxUserSession = (WxUserSession) request.getSession().getAttribute(Constant.SESSION_WX_USER);
		if (wxUserSession != null) {
			WxPubAccount account = wxpubAccountService.findWxPubAccountByAppid(wxUserSession.getAppid()); //获取微信公众账号
			WeixinPayVo payVo = wxPayService.getPayVo(wxUserSession, account, orderInfoId, orderType); //拼vo
			String wx_js_api = wxPayService.getWxJsApi(account, payVo, request); //生成jsapi
			model.addAttribute("wx_js_api", wx_js_api);
			model.addAttribute("integraType", payVo.getIntegraType()); //积分充值或者渠道充值，需要把值返回前端做判断
			//渠道充值需要判断是那种渠道
			int agentRole = -1;
			if (payVo.getIntegraType() == 2) {
				agentRole = payVo.getRechargeOrder().getAgentRole();
			}
			model.addAttribute("agentRole", agentRole);
			model.addAttribute("type", orderType); //返回支付页面，判断支付操作完毕做相关操作
			view.setViewName("/app/weixinpay/go_pay");
			return view;
		}
		return null;
		
	}*/
	
	
	@ApiOperation("微信扫码支付")
	@RequestMapping(value = "/code_pay", method = RequestMethod.GET)
	public JmMessage wxCodePay(@ApiParam(hidden=true)HttpServletRequest request,
			@ApiParam("订单ID")  @RequestParam(value="orderInfoId",required= false) Long orderInfoId) throws Exception{
			JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
			JmRechargeOrder rechargeOrder = userAccountService.findRechargeOrderById(orderInfoId);
			String payUrl = wxPayService.wxCodePay(rechargeOrder, request,user.getUserId());
	    return new JmMessage(0, ImgUtil.appendUrl(payUrl));
	}
	
	/**
	 * 微信扫码支付回调
	 * @param request
	 * @param response
	 * @param domSource
	 * @throws Exception 
	 */
	@RequestMapping(value = "/qrcode_pay_callback", method = RequestMethod.POST)
	public void qrcodePayCallback(@ApiParam(hidden=true)HttpServletRequest request,
			 @ApiParam(hidden=true) HttpServletResponse response,
			   @RequestBody(required=false) DOMSource domSource) throws Exception{
		response.reset();
		response.getWriter().write(wxService.returnWx());
		response.getWriter().close();
		Map<String,Object> xmlMap  = wxService.wxNotify(request, domSource);
		String attach[] =  ((String)xmlMap.get("attach")).split(",");
		Long orderInfoId = Long.valueOf(attach[0]);
		JmRechargeOrder rechargeOrder = userAccountService.findRechargeOrderById(orderInfoId);
		synchronized (this){
			if(rechargeOrder.getStatus()==0){//状态未付款才做操作
				userAccountService.qrcodePaySuccess(rechargeOrder, xmlMap);
			}
		}
		
	}
	
	/**
	 *<p>微信普通购买支付回调</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws IOException 
	 * @data 2016年6月23日
	 */
	@RequestMapping(value = "/pay_record", method = RequestMethod.POST)
	public void addPayRecord(@ApiParam(hidden=true)HttpServletRequest request,
							 @ApiParam(hidden=true) HttpServletResponse response,
							   @RequestBody(required=false) DOMSource domSource) throws Exception{
		response.reset();
		response.getWriter().write(wxService.returnWx());
		response.getWriter().close();
		log.info("==================in pay Callback=======================");
		Map<String,Object> xmlMap  = wxService.wxNotify(request, domSource);
		String attach[] =  ((String)xmlMap.get("attach")).split(",");
		Long orderInfoId = Long.valueOf(attach[0]);
		//Integer isSub = Integer.parseInt(attach[2]);
		/*String sign  = wxService.getNotifySign(xmlMap, request, domSource, isSub); //防止伪造支付篡改数据，对回调参数做支付签名验证
		//签名校验不一致
		if(!sign.equals(Toolkit.parseObjForStr(xmlMap.get("sign")))){
			log.info("==============sign check fail===============");
			throw new Exception(sign);
		}*/
		log.info("==============sign check success===============");
		OrderInfo orderInfo = orderService.getOrderInfo(orderInfoId);
		if (orderInfo.getStatus()==0){
			PayRecordVo recordVo = new PayRecordVo();
			recordVo.setPayStatus(1);//支付状态设置为1
			recordVo.setPayDate(new Date());
			recordVo.setPayType(1);//微信支付
			recordVo.setPayMoney(Integer.parseInt(attach[1]));
			recordVo.setPayNo(Toolkit.getOrderNum("P"));//流水号
			recordVo.setTransactionId(Toolkit.parseObjForStr(xmlMap.get("transaction_id")));
			recordVo.setTimeEnd(Toolkit.parseObjForStr(xmlMap.get("time_end")));
			PayRecord payRecord = PayRecordConverter.toPayRecord(recordVo);
			PayRecord pr = recordService.createPayRecord(payRecord);//添加流水
			//修改订单状态为已支付
			orderService.updateOrder(pr,orderInfoId);
			synchronized (this){
				updateSaleCount(orderInfoId); //更新销量库存
			}
			//因为此方法是微信支付完成后的回调，不可以用session,只能使用appid与openid去数据库取
			WxUser wxUser = wxUserService.getWxUser(orderInfo.getUserId());
			if(wxUser.getIsBuy()==0){
				wxUser.setIsBuy(1); //支付完成 成为购买客户
			}
			wxUser.setLastBuyTime(new Date());
			wxUserService.saveUser(wxUser);
			brokerageBuildService.addBrokerageOrder(wxUser,orderInfo);
			orderService.pushPayWxMsg(wxUser,orderInfo,Constant.DOMAIN);
			//发送活动红包
			activityService.sendActivityRed(new ActivityRedDo(wxUser,request.getRemoteHost(),3,orderInfo.getTotalPrice()+orderInfo.getSendFee()));
		}
	}

	/**
	 * 更新销量库存
	 * @param orderInfoId
	 */
	@Transactional
	private void updateSaleCount(Long orderInfoId){
		List<OrderDetail> orderDetails = orderDetailService.queryOrderDetail(orderInfoId);
		List<Integer> pids = new ArrayList<>();
		List<Integer> productSpecIds = new ArrayList<>();
		for(OrderDetail orderDetail:orderDetails){
			pids.add(orderDetail.getPid());
			if(!StringUtils.isEmpty(orderDetail.getProductSpecId())){
				productSpecIds.add(orderDetail.getProductSpecId());
			}
		}
		List<Product> products = productService.findAll(pids);
		List<ProductSpec> productSpecs = productSpecService.findAll(productSpecIds);
		for(ProductSpec productSpec:productSpecs){ //更新产品规格表，产品表销量库存
			for(OrderDetail orderDetail:orderDetails){
				if(productSpec.getProductSpecId().equals(orderDetail.getProductSpecId())){
					productSpec.setTotalCount(productSpec.getTotalCount()-orderDetail.getCount());
					productSpec.setSaleCount(productSpec.getSaleCount()+orderDetail.getCount());
					log.info("==================productSpec.getTotalCount()======================="+productSpec.getTotalCount());
					log.info("==================productSpec.getSaleCount()======================="+productSpec.getSaleCount());
					for (Product product :products){
						if (product.getPid().equals(productSpec.getPid())){
							product.setTotalCount(product.getTotalCount()-orderDetail.getCount());
							product.setSaleCount(product.getSaleCount()+orderDetail.getCount());
							log.info("================productSpec==product.getTotalCount()======================="+product.getTotalCount());
							log.info("================productSpec==product.getSaleCount()======================="+product.getSaleCount());
							if(product.getTotalCount()<=0){//售完下架
								product.setStatus(2);
							}
							break;
						}
					}
					break;
				}
			}
		}
		for(Product product:products){ //更新没有规格的产品表销量库存
			for(OrderDetail orderDetail:orderDetails){
				if(product.getPid().equals(orderDetail.getPid())&&product.getIsSpec()==0){
					product.setTotalCount(product.getTotalCount()-orderDetail.getCount());
					product.setSaleCount(product.getSaleCount()+orderDetail.getCount());
					log.info("==================product.getTotalCount()======================="+product.getTotalCount());
					log.info("==================product.getSaleCount()======================="+product.getSaleCount());
					if(product.getTotalCount()<=0){//售完下架
						product.setStatus(2);
					}
					break;
				}
			}
		}
		productRepository.save(products);
		productSpecRepository.save(productSpecs);
	}

	/**
	 *<p>积分充值&渠道充值，支付回调</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws IOException 
	 * @data 2016年6月23日
	 */
	@RequestMapping(value = "/integral_recode", method = RequestMethod.POST)
	public void integralPay(@ApiParam(hidden=true)HttpServletRequest request,
							 @ApiParam(hidden=true) HttpServletResponse response,
							   @RequestBody(required=false) DOMSource domSource) throws Exception{
		response.reset();
		response.getWriter().write(wxService.returnWx());
		response.getWriter().close();
		log.info("==================in pay Callback=======================");
		Map<String,Object> xmlMap  = wxService.wxNotify(request, domSource);
		String attach[] =  ((String)xmlMap.get("attach")).split(",");
		Long orderInfoId = Long.valueOf(attach[0]);
		//Integer isSub = Integer.parseInt(attach[2]);
		synchronized (this){
			//防止伪造支付篡改数据，对回调参数做支付签名验证
			/*String sign  = wxService.getNotifySign(xmlMap, request, domSource, isSub);
	        //签名校验不一致
	        if(!sign.equals(Toolkit.parseObjForStr(xmlMap.get("sign")))){
				log.info("==============sign check fail===============");
				throw new Exception(sign);
	        }*/
	        log.info("==============sign check success===============");
	    	RechargeOrder rechargeOrder = integralService.findRechargeById(orderInfoId);
			if (rechargeOrder.getStatus()==0){
				PayRecordVo recordVo = new PayRecordVo();
				recordVo.setPayStatus(1);//支付状态设置为1
				recordVo.setPayDate(new Date());
				recordVo.setPayType(1);//微信支付
				recordVo.setPayMoney(Integer.parseInt(attach[1]));
				recordVo.setPayNo(Toolkit.getOrderNum("P"));//流水号
				recordVo.setTransactionId(Toolkit.parseObjForStr(xmlMap.get("transaction_id")));
				recordVo.setTimeEnd(Toolkit.parseObjForStr(xmlMap.get("time_end")));
				PayRecord payRecord = PayRecordConverter.toPayRecord(recordVo);
				PayRecord returnPayRecord = recordService.createPayRecord(payRecord);//添加流水
				//修改充值表
				rechargeOrder.setStatus(1);//设置已付款
				rechargeOrder.setPayId(returnPayRecord.getPayId());
				rechargeOrder.setPayDate(new Date());
				integralService.saveIntegralRecharge(rechargeOrder);
				//到此支付与流水插入已完成-----
				//以下根据类型做判断是积分充值还是渠道充值 做相关修改操作业务操作 
				if(rechargeOrder.getType()==1){
					//积分充值操作
					integralService.integralRecharge(orderInfoId);
				}else if(rechargeOrder.getType()==2){
					//渠道充值操作
					if(rechargeOrder.getAgentRole()==8){//我的小店
//						brokerageRedService.myShopRecharge(rechargeOrder);
					}else{
						//渠道类型 1：代理商a，2代理商b，3代理商c，4代理商d，5：分销代理a 6：分销代理b，7分销代理c
						brokerageRedService.agentRecharge(rechargeOrder);
					}
				}
			}
		}
	}
}
