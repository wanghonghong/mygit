package com.jm.business.service.shop.distribution;

import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.wx.WxMessageService;
import com.jm.business.service.wx.WxService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.shop.distribution.BrokerageRecordVo;
import com.jm.mvc.vo.shop.recharge.AgentRechargeOrderCo;
import com.jm.mvc.vo.wx.RedSendParam;
import com.jm.mvc.vo.wx.wxred.RedResultParam;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.jpa.order.OrderDetailRepository;
import com.jm.repository.jpa.shop.RechargeOrderRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageKitRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageRecordRepository;
import com.jm.repository.jpa.user.ShopUserRepository;
import com.jm.repository.jpa.wx.WxUserAccountRepository;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.*;
import com.jm.repository.po.shop.integral.IntegralSet;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.staticcode.constant.BrokerageAgent;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.RedStatus;
import com.jm.staticcode.converter.shop.distribution.BrokerageConverter;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>佣金</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/12
 */

@Log4j
@Service
public class BrokerageRedService {

    @Autowired
    private BrokerageSetService brokerageSetService;

    @Autowired
    private WxUserAccountRepository wxUserAccountRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private WxPubAccountService wxPubAccountService;

    @Autowired
    private WxService wxService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private BrokerageRecordRepository brokerageRecordRepository;

    @Autowired
    private BrokerageKitRepository brokerageKitRepository;

    @Autowired
    private RechargeOrderRepository rechargeOrderRepository;

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private BrokerageBuildService brokerageBuildService;

    @Autowired
    private IntegralService integralService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private WxMessageService wxMessageService;
    /**
     * 全额发放:可传每次发放限额，循环发放
     * @param redSendParam
     * @return
     * @throws Exception
     */
    @Transactional
    public String brokerageRedSend(RedSendParam redSendParam) throws Exception {
        WxUser wxUser = wxUserService.findWxUserByUserId(redSendParam.getUserId());
        Shop shop = shopService.findShopById(redSendParam.getShopId());
//        if(!wxUser.getAppid().equals(shop.getAppId())){
//            return "该用户为其他公众号下的粉丝";
//        }
        String result = null;
        //取出用户账户
        if(shop.getIsOpen()==0){
            log.info("========no---openBrokerage============");
            return "请先开通分销系统";
        }
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(wxUser.getUserId(), 1);
        if (wxUserAccount != null) {
            int balance = 0;//取出余额，判断余额是否超过200
            if(redSendParam.getSendMoney() == 0){
                balance=wxUserAccount.getTotalBalance();//全额
            }else{
                balance = redSendParam.getSendMoney();//自由发放金额
            }

            ShopUser shopUser = brokerageBuildService.saveShopUser(wxUser,shop.getShopId());
            //推送通知用户注册
//            if(StringUtil.isNull(shopUser.getPhoneNumber())){
//                int sendTimes= shopUser.getSendTimes();
//                if(sendTimes==0){
//                    shopUser.setSendTimes(sendTimes+1);
//                    pushRegisterMsg(wxUser,balance,shop);
//                    return "该粉丝尚未注册，无法发送红包佣金！!<br>系统已通知该粉丝注册！<br>请稍后再尝试佣金发放！";
//                }else{
//                    return "该粉丝尚未注册，无法发送红包佣金！!<br>系统已通知该粉丝注册！<br>请稍后再尝试佣金发放！";
//                }
//            }

            int sendMoney = 0;//已发送成功金额
            int extra = balance;//发送剩余金额
            log.info("==========balance===========" + balance);
            //每次发放金额
            int send = redSendParam.getEverySend();
            //20005
            int tempExtra = 0;
            int tempSendMoney = 0;
            for (; balance >= 100; balance = balance - send) {//余额大于200，分多次发放
                WeixinActInfo requestParam;
                if (balance < send && balance >= 100) {
                    requestParam = getWeixinParam(redSendParam.getIp(), wxUser, shop, balance);
                    tempExtra = 0;
                    tempSendMoney += balance;//20105
                } else {
                    requestParam = getWeixinParam(redSendParam.getIp(), wxUser, shop, send);
                    tempExtra = balance - send;//105
                    tempSendMoney += send;//20000
                }
                result = sendBrokerageRed(requestParam, wxUser, shop,redSendParam);
                if (!result.matches("[0-9]+")) {
                    if(redSendParam.getPutType()==2 || redSendParam.getPutType()==3){//提现
                        return "提现失败：失败原因：" + result + "，本次成功提现 ￥" + (Double.valueOf(sendMoney)) / 100 + " 未提现 ￥" + (Double.valueOf(extra)) / 100 + "佣金!";
                    }else{
                        return "发放失败：失败原因：" + result + "!<br>本次成功发放 ￥" + (Double.valueOf(sendMoney)) / 100 + " 佣金!<br>未发放 ￥" + (Double.valueOf(extra)) / 100 + "佣金!";
                    }
                } else {
                    extra = tempExtra;//105
                    sendMoney = tempSendMoney;
                }
            }
            if(null != redSendParam.getKitId()){
                WxAccountKit brokerageKit = getBrokerageKit(redSendParam.getKitId());
                brokerageKit.setStatus(2);
                brokerageKitRepository.save(brokerageKit);
            }
            if(redSendParam.getPutType()==2 || redSendParam.getPutType()==3) {//提现
                result = "本次成功提现 ￥" + (Double.valueOf(sendMoney)) / 100 + "，未提现 ￥" + (Double.valueOf(extra)) / 100 + "!";
            }else{
                result = "本次成功发放 ￥" + (Double.valueOf(sendMoney)) / 100 + " 佣金!<br>未发放 ￥" + (Double.valueOf(extra)) / 100 + "佣金!";
            }

        }
        return result;
    }

    /**
     * 推送注册通知信息
     * @param wxUser
     * @param balance
     * @param shop
     * @throws Exception
     */
//    private void pushRegisterMsg(WxUser wxUser,Integer balance,Shop shop) throws Exception {
//        WxTemplateDo wxTemplateDo = getWxTemplateDo(wxUser,balance,shop);
//        wxMessageService.sendTemplate(wxTemplateDo);
//    }

    private WxTemplateDo getWxTemplateDo(WxUser wxUser,Integer balance,Shop shop) {
        WxTemplateDo wxTemplateDo = new WxTemplateDo();
        wxTemplateDo.setTouser(wxUser.getOpenid());
        wxTemplateDo.setUrl( Constant.DOMAIN + "/user_manage/app/0?shopId=" + shop.getShopId());
        wxTemplateDo.setAppid(wxUser.getAppid());
        wxTemplateDo.setType(9);
        double commissionPrice = Double.valueOf(balance)/100;
        wxTemplateDo.setIncomeMoney(String.valueOf(commissionPrice));
        wxTemplateDo.setFirst("佣金发放——用户注册");
        wxTemplateDo.setRegisterPerson(Base64Util.getFromBase64(wxUser.getNickname()));
        wxTemplateDo.setRegisterNum("您的手机号码");
        wxTemplateDo.setRegisterTime(Toolkit.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
        return wxTemplateDo;
    }

    /**
     * 给购买兄弟体验装的用户的上两级每人发1块钱佣金
     * @param wxuser
     * @param orderInfo
     * @throws Exception
     */
    public void sendBrokerageRedByPid(WxUser wxuser,OrderInfo orderInfo)throws Exception {
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderInfoId(orderInfo.getOrderInfoId());
        log.info("-------------orderDetails-----------------"+orderDetails.size());
        Shop shop = shopService.findShopById(orderInfo.getShopId());
        if(orderDetails!=null && orderDetails.size()>0){
            for(OrderDetail orderDetail:orderDetails){
                log.info("-------------orderDetail.getPid().equals(174)-----------------"+orderDetail.getPid().equals(174));
                if(orderDetail.getPid().equals(169)){
                    if (wxuser.getUpperOne() != null && wxuser.getUpperOne() > 0) {
                        WxUser oneUser = wxUserService.getWxUser(wxuser.getUpperOne());
                        WeixinActInfo requestParam = getWeixinParam(null, oneUser, shop, 100);
                        RedResultParam redResult = wxService.sendRed(requestParam);
                        log.info("-------------redResult-----------------" + redResult);
                        log.info("-------------redResult.getErrCode()-----------------" + redResult.getErrCode());
                    }
                    if (wxuser.getUpperTwo() != null && wxuser.getUpperTwo() > 0) {
                        WxUser twoUser = wxUserService.getWxUser(wxuser.getUpperTwo());
                        WeixinActInfo requestParam = getWeixinParam(null, twoUser, shop, 100);
                        RedResultParam redResult = wxService.sendRed(requestParam);
                        log.info("-------------redResult-----------------" + redResult);
                        log.info("-------------redResult.getErrCode()-----------------" + redResult.getErrCode());
                    }
                }
            }

        }

    }
    /**
     * 发放红包
     * @param requestParam
     */
    private String sendBrokerageRed(WeixinActInfo requestParam, WxUser wxUser, Shop shop,RedSendParam redSendParam) throws Exception {
        RedResultParam redResult = wxService.sendRed(requestParam);
        log.info("-------------redResult-----------------" + redResult);
        log.info("-------------redResult.getErrCode()-----------------" + redResult.getErrCode());
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(wxUser.getUserId(),1);
        if (redResult.getResultCode().equals("SUCCESS")) {
            //发放成功---修改表
            BrokerageRecord brokerageRecord = new BrokerageRecord();
            brokerageRecord.setShopId(shop.getShopId());
            brokerageRecord.setUserId(wxUser.getUserId());
            brokerageRecord.setSendNum(Toolkit.getOrderNum("Y"));
            brokerageRecord.setSendMoney(requestParam.getTotalAmount());
            brokerageRecord.setSendTime(new Date());
            brokerageRecord.setPlatForm(0);
            brokerageRecord.setPutType(redSendParam.getPutType());
            brokerageRecord.setAutoType(redSendParam.getAutoType());
            brokerageRecord.setRedPayId(redResult.getRedPayId());
            brokerageRecordRepository.save(brokerageRecord);
            if(redSendParam.getAutoType()!=5 && redSendParam.getAutoType()!=6 ){//5需审核、积分提现
                wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance()-requestParam.getTotalAmount());//减佣金账户
                wxUserAccount.setKitBalance(wxUserAccount.getKitBalance()-requestParam.getTotalAmount());//减佣金账户
            }
            wxUserAccount.setUpdateTime(new Date());
            wxUserAccountRepository.save(wxUserAccount);
        } else {
            if(redSendParam.getAutoType()==5){//5需审核---失败
                wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance()+requestParam.getTotalAmount());//加佣金账户
                wxUserAccount.setKitBalance(wxUserAccount.getKitBalance()+requestParam.getTotalAmount());//加佣金账户
                wxUserAccount.setUpdateTime(new Date());
                wxUserAccountRepository.save(wxUserAccount);
            }else if(redSendParam.getAutoType()==6){//积分

            }
            for (RedStatus redStatus : RedStatus.values()) {
                if (redResult.getErrCode().equals(redStatus.toString())) {
                    return redStatus.getName()+"错误描述:"+redResult.getErrCodeDes();
                }
            }
            return redResult.getErrCode();
        }
        return String.valueOf(requestParam.getTotalAmount());
    }

    /**
     * 分装微信红包请求信息
     * @param wxUser
     * @param shop
     * @param balance
     * @return
     */
    public WeixinActInfo getWeixinParam(String ip, WxUser wxUser, Shop shop, int balance) {
        WeixinActInfo requestParam = new WeixinActInfo();
        //获取微信公众账号信息
        WxPubAccount wxPubAccount = wxPubAccountService.findWxPubAccountByAppid(wxUser.getAppid());
        //获取店铺信息
        requestParam.setWxappid(wxPubAccount.getAppid());
        requestParam.setMchId(wxPubAccount.getMchId());
        requestParam.setAppkey(wxPubAccount.getAppKey());
        requestParam.setSendName(shop.getShopName());//店铺名称
        requestParam.setReOpenid(wxUser.getOpenid());
        requestParam.setTotalAmount(balance);
        if(ip==null){
            requestParam.setClientIp("127.0.0.1");//调用接口的机器Ip地址
        }else{
            requestParam.setClientIp(ip);//调用接口的机器Ip地址
        }
        requestParam.setTotalNum(1);
        requestParam.setWishing("每日分享、赚满幸福");
        requestParam.setActName("佣金发放");
        requestParam.setRemark("佣金发放");
        return requestParam;
    }

    /**
     * 自动发放
     * @param hostAddress
     * @throws Exception
     */
    public void autoSendBrokerage(String hostAddress) throws Exception {
        //获取所有店铺的配置
        List<PutSet> putSetList = brokerageSetService.findPutSetList(1);
        int everyMoney = 0;//自动发放的金额
        for(PutSet putSet:putSetList){
            if(putSet.getAutoType()==1){//满200自动发放
                if(putSet.getCashKit()==1){
                    everyMoney=20000;
                    List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),1,everyMoney);
                    for(WxUserAccount wxUserAccount : wxUserAccounts ){
                        //发放满200的红包
                        RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,1,everyMoney,null);
                        brokerageRedSend(redSendParam);
                    }
                }
//                if(putSet.getIntegralKit()==1){//积分金额比例转换（待修正）
//                    IntegralSet integralSet=integralService.getCacheIntegralSet(putSet.getShopId());
//                    Integer integral=200*integralSet.getUnit();
//                    List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),1,integral);
//                    for(WxUserAccount wxUserAccount : wxUserAccounts ){
//                        //发放满200的红包
//                        RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,1,everyMoney,null);
//                        brokerageRedSend(redSendParam);
//                    }
//                }
            }
            if(putSet.getAutoType()==3){//满额自动发放
                if(putSet.getCashKit()==1){
                    everyMoney=putSet.getFullPut();
                    List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),1,everyMoney);
                    for(WxUserAccount wxUserAccount : wxUserAccounts ){
                        //满额自动发放
                        RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,3,0,null);
                        brokerageRedSend(redSendParam);
                    }
                }
//                if(putSet.getIntegralKit()==1){//积分金额比例转换（待修正）
//                    everyMoney=putSet.getFullPut();
//                    List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),1,everyMoney);
//                    for(WxUserAccount wxUserAccount : wxUserAccounts ){
//                        //满额自动发放
//                        RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,3,0,null);
//                        brokerageRedSend(redSendParam);
//                    }
//                }
            }
        }
    }

    /**
     * 定时发送
     * @param hostAddress
     * @throws Exception
     */
    public void timeAutoBrokerage(String hostAddress) throws Exception {
        //获取所有店铺的配置
        List<PutSet> putSetList = brokerageSetService.findPutSetList(1);
        int everyMoney = 0;//自动发放的金额
        for(PutSet putSet:putSetList){
            if(putSet.getAutoType()==2){//满200自动发放
                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                Date today = new Date();
                String strToday = sdf.format(today);
                String sendTime = sdf.format(putSet.getSendTime());
                if(sendTime.equals(strToday)){
                    if(putSet.getCashKit()==1){
                        everyMoney=20000;
                        List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),1,everyMoney);
                        for(WxUserAccount wxUserAccount : wxUserAccounts ){
                            //发放满200的红包
                            RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,1,0,null);
                            brokerageRedSend(redSendParam);
                        }
                    }
//                    if(putSet.getIntegralKit()==1){//金额换算（待修正）
//                        everyMoney=20000;
//                        List<WxUserAccount> wxUserAccounts = wxUserAccountRepository.findByShopIdAndAccountType(putSet.getShopId(),2,everyMoney);
//                        for(WxUserAccount wxUserAccount : wxUserAccounts ){
//                            //发放满200的红包
//                            RedSendParam redSendParam = getRedSendParam(hostAddress,wxUserAccount.getUserId(),wxUserAccount.getShopId(),everyMoney,1,1,0,null);
//                            brokerageRedSend(redSendParam);
//                        }
//                    }
                }
            }
        }
    }

    public RedSendParam getRedSendParam(String remoteHost, Integer userId, Integer shopId, int everySend, int putType, int autoType,int sendMoney,Integer kitId) {
        RedSendParam redSendParam = new RedSendParam();
        redSendParam.setIp(remoteHost);
        redSendParam.setUserId(userId);
        redSendParam.setShopId(shopId);
        redSendParam.setEverySend(everySend);
        redSendParam.setPutType(putType);
        redSendParam.setAutoType(autoType);
//        sendMoney=(sendMoney==20000)?100:sendMoney;
        redSendParam.setSendMoney(sendMoney);
        redSendParam.setKitId(kitId);
        return redSendParam;
    }

    public WxAccountKit getBrokerageKit(Integer id) {
        return brokerageKitRepository.findOne(id);
    }

    public RechargeOrder agentRecharge(AgentRechargeOrderCo agentRechargeOrderCo) {
//        RechargeOrder rechargeOrder = IntegralConverter.toRechargeOrder(agentRechargeOrderCo);
        RechargeOrder rechargeOrder = BrokerageConverter.toRechargeOrder(agentRechargeOrderCo);

        return rechargeOrderRepository.save(rechargeOrder);
    }

    /**
     * 代理商购买回调
     * @param rechargeOrder
     */
    @Transactional
    public void agentRecharge(RechargeOrder rechargeOrder) {
        WxUser wxUser = wxUserService.findWxUserByUserId(rechargeOrder.getUserId());
        ShopUser shopUser = shopUserRepository.findOne(wxUser.getShopUserId());
        shopUser.setAgentRole(rechargeOrder.getAgentRole());
        //购买成为代理商清除上级用户
        if(rechargeOrder.getAgentRole()==1||rechargeOrder.getAgentRole()==2||rechargeOrder.getAgentRole()==3||rechargeOrder.getAgentRole()==4){
            wxUser.setIsMyShop(3);
        }else if(rechargeOrder.getAgentRole()==5 || rechargeOrder.getAgentRole()==6 || rechargeOrder.getAgentRole()==7){
            wxUser.setIsMyShop(4);
        }
        wxUserService.saveUser(wxUser);
        shopUserRepository.save(shopUser);
    }

    /**
     * 佣金回流
     * @throws Exception
     */
    public void brokerageBack() {
        //查询过期红包
//        BrokerageRecordVo brokerageRecord =
        String sql = "";

    }

    /**
     * 我的小店回调
     * @param rechargeOrder
     */
//    public void myShopRecharge(RechargeOrder rechargeOrder) {
//        WxUser wxUser = wxUserService.findWxUserByUserId(rechargeOrder.getUserId());
//        wxUser.setIsMyShop(2);
//        wxUserService.saveUser(wxUser);
//        ShopSmall shopSmall = smallShopRepository.findByShopId(rechargeOrder.getShopId());
//        shopSmall.setFeeCount(shopSmall.getFeeCount()-1);
//        smallShopRepository.save(shopSmall);
//    }

}
