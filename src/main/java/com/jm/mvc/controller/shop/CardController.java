package com.jm.mvc.controller.shop;

import com.jm.business.service.order.OrderFavorableService;
import com.jm.business.service.shop.CardService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.activity.*;
import com.jm.mvc.vo.wx.wxuser.WxUserCardCo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardQo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardVo;
import com.jm.repository.po.wx.WxUserCard;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>卡卷管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
@RestController
@RequestMapping(value = "/card")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private OrderFavorableService orderFavorableService;

    @ApiOperation("新增卡卷")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage addCard(@ApiParam("新增卡卷VO") @RequestBody @Valid ShopCardCo shopCardCo,
                            @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopCardCo.setShopId(user.getShopId());
        return cardService.addCard(shopCardCo);
    }

    @ApiOperation("编辑卡卷")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JmMessage updateCard(@ApiParam("编辑活动VO") @RequestBody @Valid ShopCardUo shopCardUo,
                                    @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopCardUo.setShopId(user.getShopId());
        return cardService.updateCard(shopCardUo);
    }

    @ApiOperation("查询卡卷详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ShopCardVo findCardById(@ApiParam("活动id") @PathVariable("id") Integer id,
                                       @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return cardService.getCard(id,user.getShopId());
    }

    @ApiOperation("删除卡卷")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteCard(@ApiParam("活动id") @PathVariable("id") Integer id,
                                @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return cardService.deleteCard(id,user.getShopId());
    }

    @ApiOperation("卡卷列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public PageItem<ShopCardVo> findCardList(@RequestBody @Valid ShopCardQo shopCardQo,
                                                  @ApiParam(hidden=true) HttpServletRequest request) throws IOException, InstantiationException, IllegalAccessException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopCardQo.setShopId(user.getShopId());
        return cardService.queryCardList(shopCardQo);
    }

    @ApiOperation("有效期内的卡卷列表")
    @RequestMapping(value = "/byDateList", method = RequestMethod.POST)
    public PageItem<ShopCardVo> findCardListByTime(@RequestBody @Valid ShopCardQo shopCardQo,
                                                   @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        shopCardQo.setShopId(user.getShopId());
        return cardService.getCardList(shopCardQo);
    }

    /**
     * 获取该用户底下的可用的优惠券
     * */
    @RequestMapping(value="/queryUserCards" , method = RequestMethod.POST)
    public PageItem<WxUserCardVo> queryUserCards(@ApiParam(hidden=true) HttpServletRequest request,WxUserCardQo wxUserCardQo)
            throws IllegalAccessException, IOException, InstantiationException {
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Integer userId = wxUserSession.getUserId();
        wxUserCardQo.setUserId(userId);
        //wxUserCardQo.setShopId(20);
        wxUserCardQo.setShopId(wxUserSession.getShopId());
        wxUserCardQo.setStatus(0);
        wxUserCardQo.setStartTime(new Date());
        wxUserCardQo.setEndTime(new Date());
        return orderFavorableService.getUserCardPage(wxUserCardQo,wxUserCardQo.getCurPage(),wxUserCardQo.getPageSize());
    }

    /**
     * 获取用户优惠券片段
     * */
    @RequestMapping(value="/cards" , method = RequestMethod.GET)
    public ModelAndView userCards(@ApiParam(hidden=true) HttpServletRequest request)
            throws IllegalAccessException, IOException, InstantiationException {
        ModelAndView view = new ModelAndView();
        ModelMap model = view.getModelMap();
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Integer userId = wxUserSession.getUserId();
        Integer shopId = wxUserSession.getShopId();
        List<WxUserCardVo> cards = orderFavorableService.findWxUserCardByUid(userId,shopId);
        model.put("cards",cards);
        view.setViewName("/app/user/ticket");
        return view;
    }

    /**
     * ajax方式获取用户的优惠券 不分页
     * */
    @RequestMapping(value="/cardList",method = RequestMethod.GET)
    public List<WxUserCardVo> userCardList(@ApiParam(hidden=true) HttpServletRequest request) throws IllegalAccessException, IOException, InstantiationException {
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        Integer userId =wxUserSession.getUserId();
        Integer shopId = wxUserSession.getShopId();
        List<WxUserCardVo> cards = orderFavorableService.findWxUserCardByUid(userId,shopId);
        return cards;
    }


    /**
     * 领取卡券
     * */
    @RequestMapping(value="/user_card/{id}", method = RequestMethod.GET)
    public ModelAndView getCard(@ApiParam(hidden=true) HttpServletRequest request, @PathVariable("id") Integer id) {
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        WxUserCardVo card = null;
        ModelAndView view = new ModelAndView("/app/user/received_card");
        ModelMap model = view.getModelMap();
        try {
            Map<String,Object> result = orderFavorableService.receiveCard(id,wxUserSession.getUserId());
            card = (WxUserCardVo) result.get("card");
            ShopCardVo shopCardVo = cardService.getShopCard(card.getCardId());
            BeanUtils.copyProperties(shopCardVo,card);
            model.put("result",result);
            model.put("shopCard",card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  view;
    }
    /**
     *卡券分享
     * */
    @RequestMapping(value="/share/{card_id}" , method = RequestMethod.GET)
    public ModelAndView shareCard(@ApiParam(hidden=true) HttpServletRequest request, @PathVariable("card_id") int card_id){
        WxUserSession wxUserSession = (WxUserSession)request.getSession().getAttribute(Constant.SESSION_WX_USER);
        ModelAndView view = new ModelAndView();
        WxUserCardVo cardVo = orderFavorableService.findCardVoById(card_id);
        ModelMap model = view.getModelMap();
        if(cardVo.getUserId().equals(wxUserSession.getUserId())){
            model.put("cardDetail",cardVo);
            //分享页面
            view.setViewName("/");
        }else{
            //分享成功页面
            WxUserCardCo CardCo = new WxUserCardCo();
            Integer userId = wxUserSession.getUserId();
            WxUserCard shareCard =  orderFavorableService.shareUserCard(cardVo,userId);

            model.put("shareCard",shareCard);
            model.put("cardDetail",cardVo);
            view.setViewName("/");
        }
        return view;
    }



}
