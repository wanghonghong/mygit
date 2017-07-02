package com.jm.mvc.controller.order;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.business.service.order.OrderDetailService;
import com.jm.business.service.order.OrderFavorableService;
import com.jm.business.service.order.ShoppingCartService;
import com.jm.business.service.product.ProductTransPriceService;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxAuthService;
import com.jm.business.service.wx.WxUserAccountService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.order.OrderConfirmVo;
import com.jm.mvc.vo.order.OrderDetailCreateVo;
import com.jm.mvc.vo.qo.ShoppingCartQo;
import com.jm.mvc.vo.shop.integral.IntegralSetVo;
import com.jm.mvc.vo.wx.WxUserAddressVo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardVo;
import com.jm.repository.client.dto.auth.AccessToken;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.product.ProductTrans;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.repository.po.wx.WxUserAddress;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * <p>订单明细控制层</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月14日
 */
@Api
@Log4j
@RestController
@RequestMapping(value = "/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private ProductTransPriceService productTransPriceService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private IntegralService integralService;
    @Autowired
    private OrderFavorableService orderFavorableService;
    @Autowired
    private WxUserAccountService wxUserAccountService;
    @Autowired
    private BrokerageSetService brokerageSetService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private ShopService shopService;

    /**
     * <p>商品详情点击立即购买时跳转页面</p>
     *
     * @throws Exception
     * @author liangrs
     * @version latest
     * @data 2016年5月14日
     */
    @ApiOperation("订单确认")
    @RequestMapping(value = "/{order_id}", method = RequestMethod.GET)
    public ModelAndView orderConfirm(@ApiParam(hidden = true) HttpServletRequest request,
                                     @ApiParam(hidden = true) HttpServletResponse response,
                                     @ApiParam("订单标识") @PathVariable("order_id") Long orderId,
                                     @ApiParam("订单提交界面") @RequestParam(value = "param", required = false) String param) throws Exception {
        ModelAndView view = new ModelAndView();
        if (!Toolkit.isWxBrowser(request)) {//不是微信浏览器
            return view;
        }
        long start = System.currentTimeMillis();
        WxUserSession wxUserSession = (WxUserSession) request.getSession().getAttribute(Constant.SESSION_WX_USER);
        if (wxUserSession == null) {
            return view;
        }
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (code != null && "1".equals(state)) {
            AccessToken accessToken = wxAuthService.getTokenByCode(code, wxUserSession.getAppid());
            wxUserSession.setAccessToken(accessToken.getAccessToken());
            wxUserSession.setRefreshToken(accessToken.getRefreshToken());
        } else {
            wxAuthService.sendRedirect(request, response, wxUserSession.getAppid());
            return view;
        }
        ModelMap modelMap = view.getModelMap();
        List<OrderConfirmVo> orderConfirmVoList = new ArrayList<>();
        List<Integer> pids = new ArrayList<>();
        if (orderId > 0) { //待付款去支付
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailByOrderInfoId(orderId);
            for (OrderDetail orderDetail : orderDetails) {
                OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
                orderConfirmVo.setProductId(orderDetail.getPid());
                pids.add(orderDetail.getPid());
                orderConfirmVo.setProductSpecId(orderDetail.getProductSpecId());
                orderConfirmVo.setCount(orderDetail.getCount());
                orderConfirmVo.setPrice(orderDetail.getPrice());
                orderConfirmVoList.add(orderConfirmVo);
            }
        } else {
            OrderConfirmVo[] orderConfirmVos = new ObjectMapper().readValue(param, OrderConfirmVo[].class);
            orderConfirmVoList = Arrays.asList(orderConfirmVos);
        }
        //商品价格数量计算
        List<ShoppingCartQo> shoppingCartQos = shoppingCartService.getShoppingCart(orderConfirmVoList);
        Integer totalprice = 0;
        Integer totalCount = 0;
        List<OrderDetailCreateVo> orderDetailCreateVos = new ArrayList<>();
        for (ShoppingCartQo shoppingCartQo : shoppingCartQos) {
            OrderDetailCreateVo orderDetailCreateVo = new OrderDetailCreateVo();
            orderDetailCreateVo.setPid(shoppingCartQo.getProductId());
            orderDetailCreateVo.setProductSpecId(shoppingCartQo.getProductSpecId());
            orderDetailCreateVo.setPrice(shoppingCartQo.getPrice());
            orderDetailCreateVo.setCount(shoppingCartQo.getCount());
            orderDetailCreateVos.add(orderDetailCreateVo);
            pids.add(shoppingCartQo.getProductId());
            Integer s = shoppingCartQo.getPrice() * shoppingCartQo.getCount();
            totalprice = totalprice + s;
            totalCount = totalCount + shoppingCartQo.getCount();
        }

        //地址获取
        WxUserAddress addr = wxUserService.findAddressByUserIdAndDefaultShow(wxUserSession.getUserId(), 1);
        if (addr != null) {
            modelMap.addAttribute("userAddrId", addr.getId());
            modelMap.addAttribute("areaCode", addr.getAreaCode());
        } else {
            modelMap.addAttribute("userAddrId", 0);
            modelMap.addAttribute("areaCode", "350100");
        }

        //封装运费计算类
        //List<ProductTrans> productTranses = orderDetailService.getProductTrans(shoppingCartQos,addr);
        //运费计算
        //Integer transFree  = productTransPriceService.getTransPrice(productTranses);
        long middle = System.currentTimeMillis();
        //计算分销商折扣
        //BrokerageSet brokerageSet = brokerageSetService.getCacheBrokerageSet(wxUserSession.getShopId());
        WxUser wxUser = wxUserService.getWxUser(wxUserSession.getUserId());
        int broDiscount = getBrokegralDiscount(wxUserSession.getShopId(), wxUser);
        if (broDiscount > 0) {
            modelMap.put("broDiscount", broDiscount);
        } else {
            //获取用户底下所有卡券
            List<WxUserCardVo> cards = orderFavorableService.findWxUserCardByUid(wxUserSession.getUserId(), wxUserSession.getShopId(), orderConfirmVoList, totalprice);
            //获取最优卡券
            //WxUserCardVo wxUserCardVo = orderFavorableService.getBestCard(cards,orderDetailCreateVos);
            //modelMap.put("card",wxUserCardVo);
            modelMap.put("cards", cards);
            String cardsJson = new ObjectMapper().writeValueAsString(cards);
            modelMap.put("cardJson", cardsJson);
        }
        // 获取积分抵扣价格
        usePoint(wxUserSession.getShopId(),wxUserSession.getUserId(),modelMap,orderDetailCreateVos);
        long end = System.currentTimeMillis();
        log.info("start time" + (middle - start));
        log.info("middle time" + (end - middle));
        log.info("end - start" + (end - start));
        String json = new ObjectMapper().writeValueAsString(orderDetailCreateVos);
        modelMap.addAttribute("shopName", wxUserSession.getShopName());
        modelMap.addAttribute("shoppingCartQos", shoppingCartQos);
        //String products = JsonMapper.toJson(shoppingCartQos);
        //modelMap.put("products",products);
        modelMap.addAttribute("qoJson", json);
        modelMap.addAttribute("addr", addr);
        modelMap.addAttribute("totalprice", totalprice);
        modelMap.addAttribute("totalCount", totalCount);
        modelMap.addAttribute("nonceStr", UUID.randomUUID().toString().replace("-", ""));//获取随机字符串
        modelMap.addAttribute("timeStamp", System.currentTimeMillis());
        modelMap.addAttribute("addressToken", wxUserSession.getAccessToken());
        modelMap.addAttribute("appid", wxUserSession.getAppid());
        //modelMap.addAttribute("transFee",transFree);

        view.setViewName("/app/order/orderConfirm");
        return view;
    }


    /**
     * <p>app端待付款跳转结算页面</p>
     *
     * @throws Exception
     * @author liangrs
     * @version latest
     * @data 2016年5月14日
     */
    @ApiOperation("买家待付款跳转结算页面")
    @RequestMapping(value = "/customerPay", method = RequestMethod.GET)
    public ModelAndView customerPay(@ApiParam(hidden = true) HttpServletRequest request,
                                    @ApiParam(hidden = true) HttpServletResponse response, @ApiParam("订单明细") String ids) throws Exception {
        ModelAndView view = new ModelAndView();
        if (!Toolkit.isWxBrowser(request)) {//不是微信浏览器
            return view;
        }
        WxUserSession wxUserSession = (WxUserSession) request.getSession().getAttribute(Constant.SESSION_WX_USER);
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String accessTokens = "";
        if (code != null && "1".equals(state)) {
            AccessToken accessToken = wxAuthService.getTokenByCode(code, wxUserSession.getAppid());
            accessTokens = accessToken.getAccessToken();
            wxUserSession.setAccessToken(accessTokens);
        } else {
            wxAuthService.sendRedirect(request, response, wxUserSession.getAppid());
            return view;
        }
        ModelMap modelMap = view.getModelMap();
        PageItem<Map<String, Object>> orderlist = orderDetailService.getOrderDetails(ids);
        Collection<Map<String, Object>> lst = orderlist.getItems();
        Integer totalPrice = 0;
        Integer totalCount = 0;
        Integer sendFee = 0;
        Integer status1 = 0;
        Integer realPrice = 0;
        for (Map<String, Object> map : lst) {
            totalPrice = Integer.parseInt(String.valueOf(map.get("total_price")));
            totalCount = totalCount + Integer.parseInt(String.valueOf(map.get("count")));
            sendFee = Integer.parseInt(String.valueOf(map.get("send_fee")));
            status1 = Integer.parseInt(String.valueOf(map.get("status")));
            realPrice = Integer.parseInt(String.valueOf(map.get("real_price")));
            map.put("pic_square", ImgUtil.appendUrl(String.valueOf(map.get("pic_square")), 100));
            if (!StringUtils.isEmpty(map.get("spec_pic"))) {
                map.put("spec_pic", ImgUtil.appendUrl(String.valueOf(map.get("spec_pic")), 100));
            }
        }
        WxUserAddress addr = wxUserService.findAddressByUserIdAndDefaultShow(wxUserSession.getUserId(), 1);
        modelMap.addAttribute("shopName", wxUserSession.getShopName());
        modelMap.addAttribute("nonceStr", UUID.randomUUID().toString().replace("-", ""));//获取随机字符串
        modelMap.addAttribute("timeStamp", System.currentTimeMillis());
        modelMap.addAttribute("addressToken", wxUserSession.getAccessToken());
        modelMap.addAttribute("appid", wxUserSession.getAppid());
        modelMap.put("sendFee", sendFee);
        modelMap.put("orderlist", orderlist.getItems());
        modelMap.put("totalPrice", totalPrice + sendFee);
        modelMap.put("realPrice", realPrice);
        modelMap.put("totalCount", totalCount);
        modelMap.put("status1", status1);
        modelMap.addAttribute("addr", addr);
        view.setViewName("/app/order/orderSettlement");
        return view;
    }

    /**
     * <p>保存用户地址</p>
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @author chenyy
     * @version latest
     * @data 2016年7月11日
     */
    @RequestMapping(value = "/wx_user_address", method = RequestMethod.POST)
    public WxUserAddressVo saveAddress(@ApiParam("新增用户地址") @RequestBody WxUserAddress wxUserAddress,
                                       @ApiParam("订单id和数量") @RequestParam(value = "param", required = false) String param,
                                       @ApiParam(hidden = true) HttpServletRequest request) throws Exception {
        WxUserSession wxUserSession = (WxUserSession) request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Shop shop = shopService.getCacheShop(wxUserSession.getShopId());
        WxUserAddress returnAddr = wxUserService.saveAddress(wxUserSession, wxUserAddress);//保存地址
        //计算运费
        OrderConfirmVo[] orderConfirmVos = new ObjectMapper().readValue(param, OrderConfirmVo[].class);
        List<OrderConfirmVo> orderConfirmVoList = Arrays.asList(orderConfirmVos);
        List<ShoppingCartQo> shoppingCartQos = shoppingCartService.getShoppingCart(orderConfirmVoList);
        //运费计算
        List<ProductTrans> productTranses = orderDetailService.getProductTrans(shoppingCartQos, wxUserAddress);
        Integer transFree = productTransPriceService.getTransPrice(productTranses,shop);
        WxUserAddressVo vo = new WxUserAddressVo();
        vo.setTransFree(transFree);
        vo.setUserAddrId(returnAddr.getId());
        return vo;
    }

    /**
     * 获取订单运费
     * */
    @RequestMapping(value = "/order_trans_fare", method = RequestMethod.POST)
    public WxUserAddressVo getOrderFree(@RequestBody List<ProductTrans> productTranses,
                                        @ApiParam(hidden = true) HttpServletRequest request){
        WxUserSession wxUserSession = (WxUserSession) request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Shop shop = shopService.getCacheShop(wxUserSession.getShopId());
        Integer transFree = productTransPriceService.getTransPrice(productTranses,shop);
        WxUserAddressVo vo = new WxUserAddressVo();
        vo.setTransFree(transFree);
        return vo;
    }

    private int getBrokegralDiscount(Integer shopId, WxUser wxUser) {
        BrokerageSet brokerageSet = brokerageSetService.getCacheBrokerageSet(shopId);
        int broDiscount = 0;
        if (brokerageSetService.isBrokerage(brokerageSet)) {
            Integer shopUserId = wxUser.getShopUserId();
            if (shopUserId != null) {
                ShopUser shopUser = shopUserService.findShopUser(shopUserId);
                if (shopUserService.isBrokerageUserRole(shopUser)) {
                    broDiscount = brokerageSetService.getUserDiscount(shopUser, brokerageSet);
                    return broDiscount;
                }
            }
        }
        return broDiscount;
    }

    private void usePoint(Integer shopId,Integer userId,ModelMap modelMap,List<OrderDetailCreateVo> orderDetailCreateVos) throws Exception {
        IntegralSetVo setVo = integralService.getIntegralSet(shopId);
        //获取积分账号
        if(setVo!=null&&setVo.getIsOpen()==1&&setVo.getIsExchange()==1){
            WxUserAccount wxUserAccount = wxUserAccountService.findWxUserAccountById(userId,2);
            if(wxUserAccount!=null&&wxUserAccount.getTotalBalance()>0){
                Integer userPoint = wxUserAccount.getTotalBalance();
                Integer point = orderFavorableService.getPoint(orderDetailCreateVos);
                Integer unit = setVo.getUnit();
                if(userPoint<point){
                    point = userPoint;
                }
                if(point>0){
                    modelMap.addAttribute("point",point);
                    modelMap.addAttribute("unit",unit);
                }
            }
        }

    }
}
