package com.jm.business.task;

import com.jm.business.domain.shop.ActivityRedDo;
import com.jm.business.service.order.OrderService;
import com.jm.business.service.order.ShoppingCartService;
import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.VoteService;
import com.jm.business.service.shop.dataAnalysis.UserAnalysisService;
import com.jm.business.service.shop.distribution.BrokerageRedService;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.shop.enrolmentActivity.EnrolmentActivityService;
import com.jm.business.service.system.VisitService;
import com.jm.business.service.wb.WbUserService;
import com.jm.business.service.wx.JkRedService;
import com.jm.business.service.wx.WxMessageService;
import com.jm.staticcode.util.FortyEightUserUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.net.InetAddress;

/**
 * <p>定时执行任务</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/6/29
 */
@Component
@Configurable
@EnableScheduling
@Log4j
@Service
public class JmTask {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private BrokerageService brokerageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrokerageRedService brokerageRedService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private WxMessageService wxMessageService;

    @Autowired
    private JkRedService jkRedService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private EnrolmentActivityService enrolmentActivityService;

    @Autowired
    private WbUserService wbUserService;

    @Autowired
    private UserAnalysisService userAnalysisService;

    /**
     * 更新活动状态
     * @throws Exception
     *
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public  void  updateActivityTask() throws Exception {
        activityService.updateActivityTask();
    }

    /**
     * 更新活动状态(已关注粉丝发送红包)
     * @throws Exception
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public synchronized void  updateActivityOldFansTask() throws Exception {
        activityService.updateActivityOldFansTask();
        activityService.sendActivityRedByOldFans(null,new ActivityRedDo(null, null, 2));
    }

    /**
     * 商家发放佣金（满200、定期、满额）
     * @throws Exception
     */
    @Scheduled(fixedRate = 1000*60*60)
    public void  sendBrokerage() throws Exception {
        brokerageRedService.autoSendBrokerage(InetAddress.getLocalHost().getHostAddress());
    }

    /**
     * 商家定时发放佣金（某月的某天10点）
     * @throws Exception
     */
    @Scheduled(cron = "0 0 10 * * ?")//每天中午10点触发
    public void  timeAutoBrokerage() throws Exception {
        brokerageRedService.timeAutoBrokerage(InetAddress.getLocalHost().getHostAddress());
    }
//
//    /**
//     * 佣金回流
//     * @throws Exception
//     */
//    @Scheduled(fixedRate = 1000*60*10)//一个小时刷新一次红包未领取的佣金
//    public void  brokerageBack() throws Exception {
//        brokerageRedService.brokerageBack();
//    }


    /**
     * 刷新pv和uv
     * @throws Exception
     */
    @Scheduled(cron = "0 0 3 * * *  ")//每天凌晨三点
    public synchronized void  productUvAndPv() throws Exception {
        visitService.updateProductUvAndPv();
    }

    /**
     *更新收货7天以后的状态
     * @throws Exception
     */
    @Scheduled(fixedRate = 1000 * 60*5)
    public synchronized void  updateBrokerage() throws Exception {
        brokerageService.updateBrokerage();
    }

    /**
     * 关闭订单
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void  closeOrder() {
        orderService.closeOrder();
    }

    /**
     * 订单确认收货
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void  confimOrder() {
        orderService.confimOrder();
    }

    /**
     * 删除兴趣单
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void  closeShoppingCart() {
        shoppingCartService.deleteShoppingCart();
    }

    /**
     * 定时发送图文消息和精推消息
     * @throws Exception 
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void sendMsg() throws Exception{
    	wxMessageService.sendTask();
    }

    /**
     * 更新招呼用户
     * */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void updateCallIngUser() throws Exception{
        FortyEightUserUtil.isCalling();
    }

    /**
     * 查询红包状态
     * */
    @Scheduled(fixedRate = 1000*60*60 )
    public void queryRedStatus() throws  Exception{
    	jkRedService.queryRedStatus();
    }
    
    /**
     * 聚客红包活动结算
     * */
    @Scheduled(fixedRate = 1000*60*60 )
    public void jkActivitySettlement() throws  Exception{
    	jkRedService.activitySettlement();
    }

    /**
     * 修改投票主题状态
     * @throws Exception
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void  updateVoteThemeStatus() throws Exception {
          voteService.updateVoteThemeStatus();

    }

    /**
     * 拉取所有商家粉丝列表，10分钟拉取
     * @throws Exception
     *@author whh
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public  void  getWbUserInfoTask() throws Exception {
        wbUserService.getWbUserInfoTask();
    }

    /**
     * 拉取所有商家分组，5分钟拉取
     * @throws Exception
     *@author whh
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public  void  getWbUserGroupTask() throws Exception {
        wbUserService.getWbUserGroupTask();
    }

    /**
     * 拉取所有商家粉丝列表
     * @throws Exception
     *@author whh
     */
    @Scheduled(cron = "0 0 0 * * *  ")//每天十二点更新
    public  void  updateWbUserInfoTask() throws Exception {
        wbUserService.updateWbUserInfoTask();
    }


    /**
     * 拉取所有商家粉丝分组
     * @throws Exception
     *@author whh
     */
    @Scheduled(cron = "0 0 0 * * *  ")//每天十二点更新
    public  void  updateWbUserGroupTask() throws Exception {
        wbUserService.updateWbUserGroupTask();
    }

/*    *//**
     * 关注推送
     *//*
    @Scheduled(fixedRate = 1000 * 5 )
    public void subscribePush() throws Exception {
        activityService.subscribePush();
    }*/
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public synchronized void callWxUserProc(){
        userAnalysisService.callWxUserProc("call wxUserProc()");
    }

}
