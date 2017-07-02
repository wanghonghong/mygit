package com.jm.business.service.shop.distribution;

import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxMessageService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.wx.wxred.RedResultParam;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.jpa.shop.distribution.BrokerageOrderRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.BrokerageOrder;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.BrokerageAgent;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.Toolkit;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>佣金</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/12
 */

@Log4j
@Service
public class BrokerageBuildService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private BrokerageSetService brokerageSetService;

    @Autowired
    private BrokerageOrderRepository brokerageOrderRepository;

    @Autowired
    private WxMessageService wxMessageService;

    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    private BrokerageRedService brokerageRedService;
    /**
     * 准佣金生成
     *
     * @param
     * @return
     * @throws Exception
     */
    @Transactional
    public void addBrokerageOrder(WxUser wxuser,OrderInfo orderInfo) throws Exception {
        Integer shopId = orderInfo.getShopId();
        BrokerageSet brokerageSet = brokerageSetService.getCacheBrokerageSet(shopId);
        //订单信息为空 || 店铺设置为空 || 用户为空
        if (null == orderInfo || null == brokerageSet || null == wxuser) {
            log.info("------order=null--or--brokerageSet=null--or--user=null--");
            return;
        }
        //店铺设置未启用
        else if (brokerageSet.getIsOpen() != 1) {
            log.info("----------------Comm seting no open--------------");
            return;
        }
        log.info("----------------addBrokerageOrder start--------------");
        log.info("----------------brokerageSet.getType()--------------" + brokerageSet.getType());
        //1-2级分销

        //0表示普通1级分销商  1表示普通2级分销商
        //2 表示1级升级分销商 3表示2级升级分销商
        //4表示1级代理商与自己购买代理商  5表示2级代理商
        switch (brokerageSet.getType()) {
            case 1://1-2级分销
                if (brokerageSet.getOne() != 0) {//一级分销是否为空
                    if (null != wxuser.getUpperOne() && wxuser.getUpperOne() > 0) {
                        WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
                        BrokerageOrder brokerageOrder = getBrokerageOrder(oneUser, orderInfo, shopId);
                        saveNormalBrokerage(brokerageSet, brokerageOrder, orderInfo, wxuser, oneUser, BrokerageAgent.NORMAL_USER);//一级分销
                    }
                }
                if (brokerageSet.getTwo() != 0) {//二级分销是否为空
                    if (null != wxuser.getUpperTwo() && wxuser.getUpperTwo() > 0) {
                        WxUser twoUser = wxUserService.getWxUser(wxuser.getUpperTwo());
                        BrokerageOrder brokerageOrder = getBrokerageOrder(twoUser, orderInfo, shopId);
                        saveNormalBrokerage(brokerageSet, brokerageOrder, orderInfo, wxuser, twoUser, BrokerageAgent.NORMAL_TWO_USER);//二级分销
                    }
                }
                break;
            case 2://1-4档代理
                if (null != wxuser.getShopUserId()) {
                ShopUser shopUser = shopUserService.findShopUser(wxuser.getShopUserId());
                BrokerageOrder brokerageOrder = getBrokerageOrder(wxuser, orderInfo, shopId);
                if (brokerageSet.getSettleType() == 2) {
                    WxUser fxUser = wxuser;//自己购买自己拿佣金
                    saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, fxUser, brokerageSet, BrokerageAgent.ONE_AGENT);//4表示1级代理商与自己购买代理商
                }
            }
                break;
            case 3://分销系统+佣金升级
                if (brokerageSet.getOne() != 0) {//一级分销是否为空
                    if (null != wxuser.getUpperOne() && wxuser.getUpperOne() > 0) {//判断1级用户是否存在
                        WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());//获取一级用户
                        BrokerageOrder brokerageOrder = getBrokerageOrder(oneUser, orderInfo, shopId);
                        if (null != oneUser.getShopUserId()) {//保存一级分销
                            ShopUser shopUser = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
                            if (shopUser.getAgentRole() == 0 || shopUser.getAgentRole()==8) {//普通角色
                                saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, oneUser, brokerageSet, BrokerageAgent.NORMAL_USER);//0表示普通1级分销商
                            } else  if(shopUser.getAgentRole()>=5 && shopUser.getAgentRole()<8){
                                saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, oneUser, brokerageSet, BrokerageAgent.UP_ONE_USER);//2 表示1级升级分销商
                            }
                        } else {//普通用户
                            saveNormalBrokerage(brokerageSet,brokerageOrder, orderInfo,wxuser, oneUser ,BrokerageAgent.NORMAL_USER);//一级分销
                        }
                    }
                }
                if (brokerageSet.getTwo() != 0) {//二级分销是否为空
                    if (null != wxuser.getUpperTwo() && wxuser.getUpperTwo() > 0) {//判断2级用户是否存在
                        WxUser twoUser = wxUserService.getWxUser(wxuser.getUpperTwo());//获取二级用户
                        BrokerageOrder brokerageOrder = getBrokerageOrder(twoUser, orderInfo, shopId);
                        if (null != twoUser.getShopUserId()) {//保存二级分销
                            ShopUser shopUser = shopUserService.findShopUser(twoUser.getShopUserId());//获取升级用户的信息
                            if (shopUser.getAgentRole() == 0 || shopUser.getAgentRole()==8) {//普通角色
                                saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.NORMAL_TWO_USER);//1表示普通2级分销商
                            } else if(shopUser.getAgentRole()>=5 && shopUser.getAgentRole()<8){
                                saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.UP_TWO_USER);//3 表示2级升级分销商
                            }
                        } else {//普通用户
                            saveNormalBrokerage(brokerageSet,brokerageOrder, orderInfo,wxuser, twoUser ,BrokerageAgent.NORMAL_TWO_USER);//二级分销
                        }
                    }
                }
                break;
//            case 6://动销代理
//            case 4://分销代理
//                //0表示普通1级分销商  1表示普通2级分销商
//                //2 表示1级升级分销商 3表示2级升级分销商
//                //4表示1级代理商与自己购买代理商  5表示2级代理商
//                if (null != wxuser.getShopUserId()) {//自己是否有角色
//                    ShopUser shopUser = shopUserService.findShopUser(wxuser.getShopUserId());
//                    if(shopUser.getAgentRole()==0 || shopUser.getAgentRole()>=5){//分销商处理
//                        saveNormalAgentBrokerage(brokerageSet,wxuser,orderInfo,shopId);
//                    }else if(shopUser.getAgentRole() >= 1 && shopUser.getAgentRole() <= 4){
//                        BrokerageOrder brokerageOrder = getBrokerageOrder(wxuser, orderInfo, shopId);
//                        if (brokerageSet.getSettleType() == 2) {
//                            WxUser fxUser = wxuser;//自己购买自己拿佣金
//                            saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, fxUser, brokerageSet, BrokerageAgent.ONE_AGENT);//4表示1级代理商与自己购买代理商
//                        }
//                    }
//                }else {//不是代理商
//                    saveNormalAgentBrokerage(brokerageSet,wxuser,orderInfo,shopId);
//                }
//                break;
//            case 7://动销代理+佣金升级
            case 5://分销代理+佣金升级
                //0表示普通1级分销商  1表示普通2级分销商
                //2 表示1级升级分销商 3表示2级升级分销商
                //4表示1级代理商与自己购买代理商  5表示2级代理商0
                if (null != wxuser.getShopUserId()) {//自己是否有角色
                    ShopUser shopUser = shopUserService.findShopUser(wxuser.getShopUserId());
                    if (shopUser.getAgentRole() == 0 || shopUser.getAgentRole() >= 5) {//分销商处理
                        saveNormalAgentUpBrokerage(brokerageSet, wxuser, orderInfo, shopId);
                    } else if (shopUser.getAgentRole() >= 1 && shopUser.getAgentRole() <= 4) {
                        BrokerageOrder brokerageOrder = getBrokerageOrder(wxuser, orderInfo, shopId);
                        if (brokerageSet.getSettleType() == 2) {
                            WxUser fxUser = wxuser;//自己购买自己拿佣金
                            saveAgentBrokerage(shopUser, brokerageOrder, orderInfo, wxuser, fxUser, brokerageSet, BrokerageAgent.ONE_AGENT);//2表示1级代理商与自己购买代理商
                        }
                    }
                } else {
                    saveNormalAgentUpBrokerage(brokerageSet, wxuser, orderInfo, shopId);
                }
                break;
        }
        brokerageRedService.sendBrokerageRedByPid(wxuser,orderInfo);
        log.info("----------------addBrokerageOrder end --------------");
    }


//    private void saveNormalAgentBrokerage(BrokerageSet brokerageSet, WxUser wxuser, OrderInfo orderInfo, Integer shopId) throws Exception {
//        if (brokerageSet.getOne() != 0) {//一级分销是否为空
//            if (null != wxuser.getUpperOne() && wxuser.getUpperOne() > 0) {
//                WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
//                BrokerageOrder brokerageOrder = getBrokerageOrder(oneUser, orderInfo, shopId);
//                if (null != oneUser.getShopUserId()) {
//                    ShopUser shopUserOne = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
//                    if(shopUserOne.getAgentRole()==0 || shopUserOne.getAgentRole()==8){//普通角色
//                        saveAgentBrokerage(shopUserOne,brokerageOrder, orderInfo, wxuser,oneUser, brokerageSet,BrokerageAgent.NORMAL_USER);//0表示普通1级分销商
//                    } else if(shopUserOne.getAgentRole()>=1 && shopUserOne.getAgentRole()<=4){//代理商角色
//                        saveAgentBrokerage(shopUserOne,brokerageOrder, orderInfo, wxuser,oneUser, brokerageSet,BrokerageAgent.ONE_AGENT);//4表示1级代理商与自己购买代理商
//                    }
//                } else {//普通角色
//                    saveNormalBrokerage(brokerageSet,brokerageOrder, orderInfo,wxuser, oneUser ,BrokerageAgent.NORMAL_USER);//一级分销
//                }
//            }
//        }
//        if (brokerageSet.getTwo() != 0) {//二级分销是否为空
//            if (null != wxuser.getUpperTwo() && wxuser.getUpperTwo() > 0) {
//                WxUser twoUser = wxUserService.getWxUser(wxuser.getUpperTwo());
//                BrokerageOrder brokerageOrder = getBrokerageOrder(twoUser, orderInfo, shopId);
//                if (null != twoUser.getShopUserId()) {//保存二级分销
//                    ShopUser shopUserTwo = shopUserService.findShopUser(twoUser.getShopUserId());//获取升级用户的信息
//                    if (shopUserTwo.getAgentRole() == 0) {//普通角色
//                        saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.NORMAL_TWO_USER);//1表示普通2级分销商
//                    } else if(shopUserTwo.getAgentRole()>=1 && shopUserTwo.getAgentRole()<=4){//代理商角色
//                        //判断1级是不是代理商，如果1级是代理商，则不生成佣金
//                        WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
//                        ShopUser shopUserOne = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
//                        if(shopUserOne.getAgentRole()>=1 && shopUserOne.getAgentRole()<=4){
//                            return;
//                        }else if(shopUserOne.getAgentRole()==0||shopUserOne.getAgentRole()==8){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT);//5表示2级代理商
//                        }else if(shopUserOne.getAgentRole()==5){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_A);//a级升级分销二级代理商
//                        }else if(shopUserOne.getAgentRole()==6){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_B);//b级升级分销二级代理商
//                        }else if(shopUserOne.getAgentRole()==7){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_C);//c级升级分销二级代理商
//                        }
//                    }
//                } else {//普通角色
//                    saveNormalBrokerage(brokerageSet,brokerageOrder, orderInfo,wxuser, twoUser ,BrokerageAgent.NORMAL_TWO_USER);//二级分销
//                }
//            }
//        }
//    }

    /**
     * 普通角色佣金生成
     *
     * @param brokerageSet
     * @param wxuser
     * @param orderInfo
     * @param shopId
     * @throws Exception
     */
    private void saveNormalAgentUpBrokerage(BrokerageSet brokerageSet, WxUser wxuser, OrderInfo orderInfo, Integer shopId) throws Exception {
        if (brokerageSet.getOne() != 0) {//一级分销是否为空
            if (null != wxuser.getUpperOne() && wxuser.getUpperOne() > 0) {
                WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
                BrokerageOrder brokerageOrder = getBrokerageOrder(oneUser, orderInfo, shopId);

                if (null != oneUser.getShopUserId()) {
                    ShopUser shopUserOne = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
                    if (shopUserOne.getAgentRole() == 0 || shopUserOne.getAgentRole() == 8) {//普通角色
                        saveAgentBrokerage(shopUserOne, brokerageOrder, orderInfo, wxuser, oneUser, brokerageSet, BrokerageAgent.NORMAL_USER);//0表示普通1级分销商
                    } else if (shopUserOne.getAgentRole() >= 1 && shopUserOne.getAgentRole() <= 4) {//代理商角色
                        saveAgentBrokerage(shopUserOne, brokerageOrder, orderInfo, wxuser, oneUser, brokerageSet, BrokerageAgent.ONE_AGENT);//4表示1级代理商与自己购买代理商
                    } else if (shopUserOne.getAgentRole() >= 5) {
                        saveAgentBrokerage(shopUserOne, brokerageOrder, orderInfo, wxuser, oneUser, brokerageSet, BrokerageAgent.UP_ONE_USER);//2表示1级升级分销商
                    }
                } else {//普通角色
                    saveNormalBrokerage(brokerageSet, brokerageOrder, orderInfo, wxuser, oneUser, BrokerageAgent.NORMAL_USER);//一级分销
                }
            }
        }
        if (brokerageSet.getTwo() != 0) {//二级分销是否为空
            if (null != wxuser.getUpperTwo() && wxuser.getUpperTwo() > 0) {
                WxUser twoUser = wxUserService.getWxUser(wxuser.getUpperTwo());
                BrokerageOrder brokerageOrder = getBrokerageOrder(twoUser, orderInfo, shopId);
                if (null != twoUser.getShopUserId()) {//保存二级分销
                    ShopUser shopUserTwo = shopUserService.findShopUser(twoUser.getShopUserId());//获取升级用户的信息
                    //判断1级是不是代理商，如果1级是代理商，则不生成佣金
                    WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());

                    if(oneUser.getShopUserId()!=null){
                        ShopUser shopUserOne = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
                        if (shopUserOne.getAgentRole() >= 1 && shopUserOne.getAgentRole() <= 4) {
                            return;
                        } else {
                            if (shopUserTwo.getAgentRole() == 0 || shopUserTwo.getAgentRole() == 8) {//普通角色
                                saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.NORMAL_TWO_USER);//1表示普通2级分销商
                            } else if (shopUserTwo.getAgentRole() >= 1 && shopUserTwo.getAgentRole() <= 4) {//代理商
                                if (shopUserOne.getAgentRole() == 0 || shopUserOne.getAgentRole() == 8) {
                                    saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT);//5表示普通2级代理商
                                } else if (shopUserOne.getAgentRole() == 5) {
                                    saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_A);//a级升级分销二级代理商
                                } else if (shopUserOne.getAgentRole() == 6) {
                                    saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_B);//b级升级分销二级代理商
                                } else if (shopUserOne.getAgentRole() == 7) {
                                    saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_C);//c级升级分销二级代理商
                                }
                            } else if (shopUserTwo.getAgentRole() >= 5) {
                                saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.UP_TWO_USER);//3表示2级升级分销商
                            }
                        }
                    }else {
                        if (shopUserTwo.getAgentRole() == 0 || shopUserTwo.getAgentRole() == 8) {//普通角色
                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.NORMAL_TWO_USER);//1表示普通2级分销商
                        } else if (shopUserTwo.getAgentRole() >= 1 && shopUserTwo.getAgentRole() <= 4) {//代理商
                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT);//5表示普通2级代理商
                        } else if (shopUserTwo.getAgentRole() >= 5) {
                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.UP_TWO_USER);//3表示2级升级分销商
                        }
                    }

                } else {//普通角色
                    saveNormalBrokerage(brokerageSet, brokerageOrder, orderInfo, wxuser, twoUser, BrokerageAgent.NORMAL_TWO_USER);//二级分销
                }

//                if (null != twoUser.getShopUserId()) {//保存二级分销
//                    ShopUser shopUserTwo = shopUserService.findShopUser(twoUser.getShopUserId());//获取升级用户的信息
//                    if (shopUserTwo.getAgentRole() == 0 || shopUserTwo.getAgentRole()==8) {//普通角色
//                        saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.NORMAL_TWO_USER);//1表示普通2级分销商
//                    } else if (shopUserTwo.getAgentRole() >= 1 && shopUserTwo.getAgentRole() <= 4) {//代理商角色
//                        //判断1级是不是代理商，如果1级是代理商，则不生成佣金
//                        WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
//                        ShopUser shopUserOne = shopUserService.findShopUser(oneUser.getShopUserId());//获取升级用户的信息
//                        if(shopUserOne.getAgentRole()>=1 && shopUserOne.getAgentRole()<=4){
//                            return;
//                        }else if(shopUserOne.getAgentRole()==0||shopUserOne.getAgentRole()==8){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT);//5表示2级代理商
//                        }else if(shopUserOne.getAgentRole()==5){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_A);//a级升级分销二级代理商
//                        }else if(shopUserOne.getAgentRole()==6){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_B);//b级升级分销二级代理商
//                        }else if(shopUserOne.getAgentRole()==7){
//                            saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.TWO_AGENT_C);//c级升级分销二级代理商
//                        }
//                    } else if (shopUserTwo.getAgentRole() >= 5) {
//                        saveAgentBrokerage(shopUserTwo, brokerageOrder, orderInfo, wxuser, twoUser, brokerageSet, BrokerageAgent.UP_TWO_USER);//3表示2级升级分销商
//                    }
//                } else {//普通角色
//                    saveNormalBrokerage(brokerageSet,brokerageOrder, orderInfo,wxuser, twoUser ,BrokerageAgent.NORMAL_TWO_USER);//二级分销
//                }
            }
        }

    }

    /**
     * 普通分销商一二级佣金生成
     *
     * @param brokerageSet
     * @param brokerageOrder
     * @param orderInfo
     * @param wxuser
     * @param fxUser
     * @param type           1表示1级分销商  2表示2级分销商
     * @throws Exception
     */
    private void saveNormalBrokerage(BrokerageSet brokerageSet, BrokerageOrder brokerageOrder, OrderInfo orderInfo, WxUser wxuser, WxUser fxUser, int type) throws Exception {
        int brokerage = 0;
        switch (type) {
            case 0://一级分销
                brokerage = brokerageSet.getOne();
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 1://二级分销
                brokerage = brokerageSet.getTwo();
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
        }
    }

    /**
     * 代理商与升级分销商佣金生成（升级分销商一级、自购）
     *
     * @param shopUser
     * @param wxuser
     * @param orderInfo
     * @param brokerageSet
     * @throws Exception
     */
    private void saveAgentBrokerage(ShopUser shopUser, BrokerageOrder brokerageOrder, OrderInfo orderInfo, WxUser wxuser, WxUser fxUser, BrokerageSet brokerageSet, int type) throws Exception {
        //type  :0表示普通用户   1表示1级代理代理商  2表示1级代理代理商
        int brokerage = 0;
        switch (shopUser.getAgentRole()) {
            case 0:
            case 8:
                if (wxuser == fxUser) {//如果是代理商
                    return;
                } else if (type == 0) {//普通1级分销商
                    brokerage = brokerageSet.getOne();
                } else if (type == 1) {//普通2级分销商
                    brokerage = brokerageSet.getTwo();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 1://A档代理
                if (brokerageSet.getAgentA() != 0) {
                    if (type == 4) {//4表示1级代理商与自己购买代理商
                        brokerage = 10000 - brokerageSet.getAgentA() * 10;
                    }
                    if (type == 5) {//5表示2级代理商
                        brokerage = 10000 - brokerageSet.getAgentA() * 10 - brokerageSet.getOne();
                    }
                    if (type == 6) {//a级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentA() * 10 - brokerageSet.getOneAup();
                    }
                    if (type == 7) {//b级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentA() * 10 - brokerageSet.getOneBup();
                    }
                    if (type == 8) {//c级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentA() * 10 - brokerageSet.getOneCup();
                    }
                } else {
                    return;
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 2://B档代理
                if (brokerageSet.getAgentB() != 0) {
                    if (type == 4) {//4表示1级代理商与自己购买代理商
                        brokerage = 10000 - brokerageSet.getAgentB() * 10;
                    }
                    if (type == 5) {//5表示2级代理商
                        brokerage = 10000 - brokerageSet.getAgentB() * 10 - brokerageSet.getOne();
                    }
                    if (type == 6) {//a级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentB() * 10 - brokerageSet.getOneAup();
                    }
                    if (type == 7) {//b级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentB() * 10 - brokerageSet.getOneBup();
                    }
                    if (type == 8) {//c级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentB() * 10 - brokerageSet.getOneCup();
                    }
                } else {
                    return;
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 3://C档代理
                if (brokerageSet.getAgentC() != 0) {
                    if (type == 4) {//4表示1级代理商与自己购买代理商
                        brokerage = 10000 - brokerageSet.getAgentC() * 10;
                    }
                    if (type == 5) {//5表示2级代理商
                        brokerage = 10000 - brokerageSet.getAgentC() * 10 - brokerageSet.getOne();
                    }
                    if (type == 6) {//a级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentC() * 10 - brokerageSet.getOneAup();
                    }
                    if (type == 7) {//b级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentC() * 10 - brokerageSet.getOneBup();
                    }
                    if (type == 8) {//c级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentC() * 10 - brokerageSet.getOneCup();
                    }
                } else {
                    return;
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 4://D档代理
                if (brokerageSet.getAgentD() != 0) {
                    if (type == 4) {//4表示1级代理商与自己购买代理商
                        brokerage = 10000 - brokerageSet.getAgentD() * 10;
                    }
                    if (type == 5) {//5表示2级代理商
                        brokerage = 10000 - brokerageSet.getAgentD() * 10 - brokerageSet.getOne();
                    }
                    if (type == 6) {//a级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentD() * 10 - brokerageSet.getOneAup();
                    }
                    if (type == 7) {//b级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentD() * 10 - brokerageSet.getOneBup();
                    }
                    if (type == 8) {//c级升级分销二级代理商
                        brokerage = 10000 - brokerageSet.getAgentD() * 10 - brokerageSet.getOneCup();
                    }
                } else {
                    return;
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                //2 表示1级升级分销商 3表示2级升级分销商
                break;
            case 5://A档升级

                if (type == 2) {//表示1级升级分销商
                    if (brokerageSet.getOneAup() != 0) {
                        brokerage = brokerageSet.getOneAup();//a档1级升级
                    } else {
                        return;
                    }
                }
                if (type == 3) {//表示2级升级分销商
                    if (brokerageSet.getTwoAup() != 0) {
                        brokerage = brokerageSet.getTwoAup();//a档2级升级
                    } else {
                        return;
                    }
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 6://B档升级
                if (type == 2) {//表示1级升级分销商
                    if (brokerageSet.getOneBup() != 0) {
                        brokerage = brokerageSet.getOneBup();//b档1级升级
                    } else {
                        return;
                    }
                }
                if (type == 3) {//表示2级升级分销商
                    if (brokerageSet.getTwoBup() != 0) {
                        brokerage = brokerageSet.getTwoBup();//a档2级升级
                    } else {
                        return;
                    }
                }

                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 7://C档升级
                if (type == 2) {//表示1级升级分销商
                    if (brokerageSet.getOneCup() != 0) {
                        brokerage = brokerageSet.getOneCup();//b档1级升级
                    } else {
                        return;
                    }
                }
                if (type == 3) {//表示2级升级分销商
                    if (brokerageSet.getTwoCup() != 0) {
                        brokerage = brokerageSet.getTwoCup();//a档2级升级
                    } else {
                        return;
                    }
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;

        }
    }
    //0表示普通1级分销商  1表示普通2级分销商
    //2 表示1级升级分销商 3表示2级升级分销商
    //4表示1级代理商与自己购买代理商  5表示2级代理商
    /*private void saveAgentBrokerage(ShopUser shopUser, BrokerageOrder brokerageOrder, OrderInfo orderInfo, WxUser wxuser, WxUser fxUser, BrokerageSet brokerageSet, int type) throws Exception {
        //type  :0表示普通用户   1表示1级代理代理商  2表示1级代理代理商
        int brokerage = 0;
        switch (shopUser.getAgentRole()) {
            case 0:
            case 8:
                if (wxuser == fxUser) {//如果是代理商
                    return;
                } else if (type == 0) {//普通1级分销商
                    brokerage = brokerageSet.getOne();
                } else if (type == 1) {//普通2级分销商
                    brokerage = brokerageSet.getTwo();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 1://A档代理
                if (type == 4) {//4表示1级代理商与自己购买代理商
                    brokerage = 1000 - brokerageSet.getAgentA();
                }
                if (type == 5) {//5表示2级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne();
                }
                if (type == 6) {//a级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoAup();
                }
                if (type == 7) {//b级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoBup();
                }
                if (type == 8) {//c级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoCup();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 2://B档代理
                if (type == 4) {//4表示1级代理商与自己购买代理商
                    brokerage = 1000 - brokerageSet.getAgentB();
                }
                if (type == 5) {//5表示2级代理商
                    brokerage = 1000 - brokerageSet.getAgentB() - brokerageSet.getOne();
                }
                if (type == 6) {//a级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoAup();
                }
                if (type == 7) {//b级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoBup();
                }
                if (type == 8) {//c级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoCup();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 3://C档代理
                if (type == 4) {//4表示1级代理商与自己购买代理商
                    brokerage = 1000 - brokerageSet.getAgentC();
                }
                if (type == 5) {//5表示2级代理商
                    brokerage = 1000 - brokerageSet.getAgentC() - brokerageSet.getOne();
                }
                if (type == 6) {//a级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoAup();
                }
                if (type == 7) {//b级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoBup();
                }
                if (type == 8) {//c级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoCup();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 4://D档代理
                if (type == 4) {//4表示1级代理商与自己购买代理商
                    brokerage = 1000 - brokerageSet.getAgentD();
                }
                if (type == 5) {//5表示2级代理商
                    brokerage = 1000 - brokerageSet.getAgentD() - brokerageSet.getOne();
                }
                if (type == 6) {//a级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoAup();
                }
                if (type == 7) {//b级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoBup();
                }
                if (type == 8) {//c级升级分销二级代理商
                    brokerage = 1000 - brokerageSet.getAgentA() - brokerageSet.getOne()-brokerageSet.getTwoCup();
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                //2 表示1级升级分销商 3表示2级升级分销商
                break;
            case 5://A档升级
                if (type == 2) {//表示1级升级分销商
                    brokerage = brokerageSet.getOne() + brokerageSet.getOneAup();//a档1级升级
                }
                if (type == 3) {//表示2级升级分销商
                    brokerage = brokerageSet.getTwo() + brokerageSet.getTwoAup();//a档2级升级
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 6://B档升级
                if (type == 2) {//表示1级升级分销商
                    brokerage = brokerageSet.getOne() + brokerageSet.getOneBup();//b档1级升级
                }
                if (type == 3) {//表示2级升级分销商
                    brokerage = brokerageSet.getTwo() + brokerageSet.getTwoBup();//a档2级升级
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;
            case 7://C档升级
                if (type == 2) {//表示1级升级分销商
                    brokerage = brokerageSet.getOne() + brokerageSet.getOneCup();//b档1级升级
                }
                if (type == 3) {//表示2级升级分销商
                    brokerage = brokerageSet.getTwo() + brokerageSet.getTwoCup();//a档2级升级
                }
                brokerageOrder.setBrokerage(brokerage);
                saveAndSendBrokerage(brokerage, brokerageOrder, orderInfo, wxuser, fxUser);
                break;

        }
    }*/


    /**
     * 保存用户佣金
     *
     * @param brokerage
     * @param brokerageOrder
     * @param orderInfo
     * @param user
     * @throws Exception
     */
    private void saveAndSendBrokerage(int brokerage, BrokerageOrder brokerageOrder, OrderInfo orderInfo, WxUser user, WxUser fxUser) throws Exception {
        brokerageOrder.setBrokerage(brokerage);
        brokerageOrder.setCommissionPrice(brokerageOrder.getTotalPrice() * brokerage / 10000);
        if (brokerageOrder.getCommissionPrice() >= 1) {
            //保存订单佣金表信息
            saveBrokerageOrder(brokerageOrder);
            saveShopUser(fxUser, brokerageOrder.getShopId());
            pushBrokerageWxMsg(user, brokerageOrder, fxUser);
//            pushBrokerageHxMsg(user,brokerageOrder);
        }
    }

    /**
     * 保存普通用户信息
     *
     * @param fxUser
     * @param shopId
     */
    public ShopUser saveShopUser(WxUser fxUser, Integer shopId) {
        log.info("-------------user.getShopUserId()--------------" + fxUser.getShopUserId());
        if (fxUser.getShopUserId() == null) {
            ShopUser shopUser = new ShopUser();
            shopUser.setShopId(shopId);
            //shopUser.setUserName(user.getUserName());
            shopUser.setAgentRole(8);
            shopUser.setCreateTime(new Date());
            log.info("-------------shopUser--------------" + shopUser);
            shopUser = shopUserService.save(shopUser);
            fxUser.setShopUserId(shopUser.getId());
            wxUserService.saveUser(fxUser);
            return shopUser;
        } else {
            ShopUser shopUser = shopUserService.findShopUser(fxUser.getShopUserId());
            //如果存在shopUser并且角色为0的话，改为分销角色
            if (shopUser.getAgentRole() == 0) {
                shopUser.setAgentRole(8);
                shopUser.setUpdateTime(new Date());
                shopUser = shopUserService.save(shopUser);
            }
            return shopUser;
        }
    }

    /**
     * 环信接收佣金信息
     * @param user
     * @param brokerageOrder
     * @throws Exception
     */
//    private void pushBrokerageHxMsg(WxUser user, BrokerageOrder brokerageOrder) throws Exception {
//        double commissionPrice = brokerageOrder.getCommissionPrice();
//        String content = "分享分销佣金:)" + commissionPrice / 100 + "元";
//        pushHxMsg(user,content);
//    }

//    private void pushHxMsg(WxUser user,String content) throws Exception{
//        Shop shop = shopService.getShopByAppId(user.getAppid());
//        List<User> users = shopService.findUserByAppid(user.getAppid());
//        //String[] hxuser = orderService.getHxUser(users);
//        String fromUser = null;
//        for(User shopUser :users){
//            fromUser = shopUser.getHxAccount();
//            if(fromUser!=null&&!fromUser.equals("")){
//                break;
//            }
//        }
//        HxUserVo hxUserVo= hxUserService.getHxUserByUserId(user.getUserId());
//        if(hxUserVo!=null){
//            String[] toUser = new String[]{hxUserVo.getHxAccount()};
//            wxService.postDateToHx(fromUser, shop.getShopName(), content, toUser,user.getOpenid(),user.getAppid());
//        }
//    }


    /**
     * 推送佣金信息
     *
     * @param user
     * @param brokerageOrder
     * @param fxUser
     * @throws Exception
     */
    private void pushBrokerageWxMsg(WxUser user, BrokerageOrder brokerageOrder, WxUser fxUser) throws Exception {
        WxTemplateDo wxTemplateDo = getWxTemplateDo(user, brokerageOrder, fxUser);
        wxMessageService.sendTemplate(wxTemplateDo);
    }

    private WxTemplateDo getWxTemplateDo(WxUser user, BrokerageOrder brokerageOrder, WxUser fxUser) throws Exception {
        WxTemplateDo wxTemplateDo = new WxTemplateDo();
        wxTemplateDo.setTouser(fxUser.getOpenid());
        wxTemplateDo.setUrl(Constant.DOMAIN + "/wxuser/brokerage?shopId=" + brokerageOrder.getShopId());
        wxTemplateDo.setAppid(user.getAppid());
        wxTemplateDo.setType(5);
        double commissionPrice = Double.valueOf(brokerageOrder.getCommissionPrice()) / 100;
        wxTemplateDo.setIncomeMoney(String.valueOf(commissionPrice));
        wxTemplateDo.setFirst("佣金又产生了");
        wxTemplateDo.setRemark("叮咚" + "\r\n" + "你又产生了一笔佣金" + commissionPrice + "等待购买客户确认收货后便可发放：分享多多，票票多多" + "\r\n" + "点击查看");
        wxTemplateDo.setIncomeType("佣金发放");
        wxTemplateDo.setIncomeTime(Toolkit.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return wxTemplateDo;
    }

    /**
     * 保存订单佣金表信息
     *
     * @param brokerageOrder
     */
    private void saveBrokerageOrder(BrokerageOrder brokerageOrder) {
        brokerageOrderRepository.save(brokerageOrder);
    }

    /**
     * 封装佣金订单清单
     *
     * @param wxUser
     * @param orderInfo
     * @param shopId
     * @return
     */
    private BrokerageOrder getBrokerageOrder(WxUser wxUser, OrderInfo orderInfo, Integer shopId) {
        BrokerageOrder brokerageOrder = new BrokerageOrder();
        brokerageOrder.setShopId(shopId);//店铺id
        brokerageOrder.setType(1);//店铺id
        brokerageOrder.setUserId(wxUser.getUserId());//用户id
        brokerageOrder.setOrderInfoId(orderInfo.getOrderInfoId());//订单id
        brokerageOrder.setOrderDate(orderInfo.getCreateDate());//订单生成时间
        if(orderInfo.getRealPrice()-orderInfo.getSendFee()>=0){
            brokerageOrder.setTotalPrice(orderInfo.getRealPrice()-orderInfo.getSendFee());//商品总额
        }else{
            brokerageOrder.setTotalPrice(0);
        }
        brokerageOrder.setPlatForm(0);
        brokerageOrder.setStatus(0);
        return brokerageOrder;
    }

}
