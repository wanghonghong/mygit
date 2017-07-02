//package com.jm.business.service.shop;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import com.jm.mvc.vo.shop.activity.*;
//import com.jm.mvc.vo.wx.PicMsgArticle;
//import com.jm.repository.jpa.shop.ShopRepository;
//import com.jm.repository.client.dto.ResultMsg;
//import com.jm.staticcode.util.ImgUtil;
//
//import lombok.extern.log4j.Log4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//import com.jm.business.domain.shop.ActivityRedDo;
//import com.jm.business.domain.shop.SubscribePushDo;
//import com.jm.business.domain.wx.SendContextDo;
//import com.jm.business.domain.wx.WxRedDo;
//import com.jm.business.domain.wx.WxTemplateDo;
//import com.jm.business.service.system.UserAccountService;
//import com.jm.business.service.wx.WxAuthService;
//import com.jm.business.service.wx.WxMessageService;
//import com.jm.business.service.wx.WxService;
//import com.jm.business.service.wx.WxUserService;
//import com.jm.mvc.vo.JmMessage;
//import com.jm.mvc.vo.PageItem;
//import com.jm.mvc.vo.wx.wxred.RedResultParam;
//import com.jm.repository.client.WxClient;
//import com.jm.repository.jpa.JdbcUtil;
//import com.jm.repository.jpa.shop.activity.ActivityCardRepository;
//import com.jm.repository.jpa.shop.activity.ActivityConditionRepository;
//import com.jm.repository.jpa.shop.activity.ActivityRepository;
//import com.jm.repository.jpa.shop.activity.ActivitySubRepository;
//import com.jm.repository.jpa.shop.activity.ActivityUserRepository;
//import com.jm.repository.jpa.shop.activity.SubscribePushRepository;
//import com.jm.repository.jpa.wx.WxUserCardRepository;
//import com.jm.repository.po.shop.Shop;
//import com.jm.repository.po.shop.activity.Activity;
//import com.jm.repository.po.shop.activity.ActivityCard;
//import com.jm.repository.po.shop.activity.ActivityCondition;
//import com.jm.repository.po.shop.activity.ActivitySub;
//import com.jm.repository.po.shop.activity.ActivityUser;
//import com.jm.repository.po.shop.activity.SubscribePush;
//import com.jm.repository.po.system.user.UserAccount;
//import com.jm.repository.po.wx.WxUser;
//import com.jm.repository.po.wx.WxUserCard;
//import com.jm.staticcode.constant.Constant;
//import com.jm.staticcode.converter.shop.ActivityConverter;
//import com.jm.staticcode.util.JsonMapper;
//import com.jm.staticcode.util.Toolkit;
//import com.jm.staticcode.util.wx.Base64Util;
//
//
///**
// * <p>活动服务</p>
// *
// * @author wukf
// * @version latest
// * @date 2016/10/27
// */
//@Log4j
//@Service
//public class ActivityService {
//    @Autowired
//    private ActivityRepository activityRepository;
//    @Autowired
//    private ActivitySubRepository activitySubRepository;
//    @Autowired
//    private ActivityConditionRepository activityConditionRepository;
//    @Autowired
//    private SubscribePushRepository subscribePushRepository;
//    @Autowired
//    private ActivityCardRepository activityCardRepository;
//    @Autowired
//    private WxUserCardRepository wxUserCardRepository;
//    @Autowired
//    private ShopService shopService;
//    @Autowired
//    private WxService wxService;
//    @Autowired
//    private WxUserService wxUserService;
//    @Autowired
//    private WxAuthService wxAuthService;
//    @Autowired
//    private WxMessageService wxMessageService;
//    @Autowired
//    private ActivityUserRepository activityUserRepository;
//    @Autowired
//    private ShopRepository shopRepository;
// 	@Autowired
// 	private WxClient wxClient;
// 	@Autowired
// 	private UserAccountService userAccountService;
//
//    @Autowired
//    private JdbcUtil jdbcUtil;
//
//    /**
//     * 活动新增
//     * @param activityCo
//     */
//    @Transactional
//    public JmMessage addActivity(ActivityCo activityCo,Integer userId) throws Exception{
//
//    	if(activityCo.getType()==5){
//    		UserAccount  account = userAccountService.findByUserId(userId,0);
//            int	counterFee = (activityCo.getTotalMoney()*3)/100;  //平台手续费 定为百分之3
//            int money = activityCo.getTotalMoney()+counterFee;//创建活动需要暂时冻结预计支出金额加平台手续费
//    		if(money>account.getBalance()){
//   			 return	new JmMessage(1, "账户余额不足以支付本次活动支出，请充值后再操作！");
//   			}
//    		 int activityCount = activityRepository.countActivityByAppid(activityCo.getAppId(), activityCo.getType(), activityCo.getSubType(), activityCo.getStartTime(), activityCo.getEndTime());
//    		if(activityCount > 0){
//    		return new JmMessage(1, "该时间段已经有同类型活动了，新增活动失败，请认真核实!!!");
//    		}
//    	}
//
//        if(activityCo.getSubType()!=2) {
//            if (activityCo.getStartTime().getTime() >= activityCo.getEndTime().getTime()) {
//                return new JmMessage(1, "该活动的开始时间大于等于结束时间，新增活动失败，请认真核实!!!");
//            }
//            if(activityCo.getType()!=5){
//            	 int activityCount = activityRepository.countActivity(activityCo.getShopId(), activityCo.getType(), activityCo.getSubType(), activityCo.getStartTime(), activityCo.getEndTime());
//                 if (activityCount > 0) {
//                     return new JmMessage(1, "该时间段已经有同类型活动了，新增活动失败，请认真核实!!!");
//                 }
//                 int activityCountAll = activityRepository.countActivity(activityCo.getShopId(), activityCo.getSubType(), activityCo.getStartTime(), activityCo.getEndTime());
//                 String oprState = activityCo.getOperationState();
//                 if (activityCountAll > 0 && (oprState == "" || oprState == null)) {
//                     return new JmMessage(2, "该时间段已经有同类型的红包活动了，是否仍执行该操作!!!");
//                 }
//            }
//
//        }else{
//            int activityOldCount = activityRepository.countOldActivity(activityCo.getShopId(), activityCo.getType(), activityCo.getSubType(), activityCo.getStartTime());
//            if (activityOldCount > 0) {
//                return new JmMessage(1, "该时间段已经有同类型活动了，新增活动失败，请认真核实!!!");
//            }
//            int activityOldCountAll = activityRepository.countOldActivity(activityCo.getShopId(), activityCo.getSubType(), activityCo.getStartTime());
//            String oprOldState = activityCo.getOperationState();
//            if (activityOldCountAll > 0 && (oprOldState == "" || oprOldState == null)) {
//                return new JmMessage(2, "该时间段已经有同类型的红包活动了，是否仍执行该操作!!!");
//            }
//        }
//        List<ActivitySub> subListNew = null;
//        List<ActivityCard> cardListNew = null;
//        List<ActivityVo> activityVos = new ArrayList<>();
//        ActivityVo activityVo = new ActivityVo();
//
//        Activity activity = ActivityConverter.v2p(activityCo);
//        Activity activityNew = activityRepository.save(activity);
//        ActivityCondition activityCondition = ActivityConverter.toActivityCondition(activityNew.getId(),activityCo.getActivityConditionCo());
//        ActivityCondition activityConditionNew = activityConditionRepository.save(activityCondition);
//        activityVo.setActivityCondition(activityConditionNew);
//        if (activityNew.getType()==1){ //现金红包
//            List<ActivitySub> list = ActivityConverter.activitySubs(activityNew.getId(),activityCo.getSubActivityList());
//            subListNew = activitySubRepository.save(list);
//            activityVo.setActivitySubList(subListNew);
//        }else if (activityNew.getType()==2){ //卡卷红包
//            List<ActivityCard> list = ActivityConverter.activityCards(activityNew.getId(),activityCo.getActivityCardList());
//            cardListNew = activityCardRepository.save(list);
//            activityVo.setActivityCardList(cardListNew);
//        }else if(activityNew.getType()==3){ //金券红包
//            List<ActivitySub> subList = ActivityConverter.activitySubs(activityNew.getId(),activityCo.getSubActivityList());
//            subListNew = activitySubRepository.save(subList);
//            List<ActivityCard> cardList = ActivityConverter.activityCards(activityNew.getId(),activityCo.getActivityCardList());
//            cardListNew = activityCardRepository.save(cardList);
//            activityVo.setActivitySubList(subListNew);
//            activityVo.setActivityCardList(cardListNew);
//        }else if(activityNew.getType()==5){ //聚客红包
//            List<ActivitySub> list = ActivityConverter.activitySubs(activityNew.getId(),activityCo.getSubActivityList());
//            UserAccount  account = userAccountService.findByUserId(userId,0);
//            account.setBalance(account.getBalance()-activityCo.getTotalMoney());
//            userAccountService.saveAccount(account);//新增活动成功 需要把账户上的钱扣掉
//            activitySubRepository.save(list);
//        }
//        String msg = "活动新增成功!!"+compareTime(activityCo.getStartTime());
//        if(activityCo.getSubType()==2) {
//            Calendar curTime = Calendar.getInstance();
//            Date current = curTime.getTime();
//            if (activityCo.getStartTime().getTime() <= current.getTime()) {
//                activityNew.setStatus(3);
//                activityRepository.save(activityNew);
//                activityVo.setActivity(activityNew);
//                activityVos.add(activityVo);
//                sendActivityRedByOldFans(activityVos,new ActivityRedDo(null, null, 2));
//                msg = "活动操作成功!!";
//            }
//        }
//        return new JmMessage(0,msg);
//    }
//
//    /**
//     * 删除活动
//     * @param id
//     * @param shopId
//     * @return
//     * @throws Exception
//     */
//    @Transactional
//    public JmMessage deleteActivity(Integer id, Integer shopId) throws Exception {
//        Activity activity = activityRepository.findOne(id);
//        if (!activity.getShopId().equals(shopId)){
//            throw new Exception("无权限删除");
//        }
//        activity.setStatus(9);
//        activityRepository.save(activity);
//        return new JmMessage(0,"活动删除成功!!");
//    }
//
//    /**
//     * 结束活动
//     * @param id
//     * @param appid
//     * @return
//     * @throws Exception
//     */
//    public JmMessage stopActivity(Integer id,String appid) throws Exception{
//    	Activity activity = activityRepository.findOne(id);
//    	if(!activity.getAppId().equals(appid)){
//    		throw new Exception("无权限操作");
//    	}
//    	activity.setStatus(3);
//        activityRepository.save(activity);
//        return new JmMessage(0,"操作成功");
//
//    }
//
//    /**
//     * 活动修改
//     * @param activityUo
//     * @return
//     */
//    @Transactional
//    public JmMessage updateActivity(ActivityUo activityUo) throws Exception{
//        if(activityUo.getSubType()!=2) {
//            if (activityUo.getStartTime().getTime() >= activityUo.getEndTime().getTime()) {
//                return new JmMessage(1, "该活动的开始时间大于等于结束时间，新增活动失败，请认真核实!!!");
//            }
//
//            int activityCount = activityRepository.countActivity(activityUo.getShopId(), activityUo.getType(), activityUo.getSubType(), activityUo.getStartTime(), activityUo.getEndTime(), activityUo.getId());
//            if (activityCount > 0) {
//                return new JmMessage(1, "该时间段已经有同类型活动了，修改活动失败，请认真核实!!!");
//            }
//            int activityCountAll = activityRepository.countActivity(activityUo.getShopId(), activityUo.getSubType(), activityUo.getStartTime(), activityUo.getEndTime(), activityUo.getId());
//            String oprState = activityUo.getOperationState();
//            if (activityCountAll > 0 && (oprState == "" || oprState == null)) {
//                return new JmMessage(2, "该时间段已经有同类型的红包活动了，是否仍执行该操作!!!");
//            }
//        }else{
//            int activityOldCount = activityRepository.countOldActivity(activityUo.getShopId(), activityUo.getType(), activityUo.getSubType(), activityUo.getStartTime(), activityUo.getId());
//            if (activityOldCount > 0) {
//                return new JmMessage(1, "该时间段已经有同类型活动了，新增活动失败，请认真核实!!!");
//            }
//            int activityOldCountAll = activityRepository.countOldActivity(activityUo.getShopId(), activityUo.getSubType(), activityUo.getStartTime(), activityUo.getId());
//            String oprOldState = activityUo.getOperationState();
//            if (activityOldCountAll > 0 && (oprOldState == "" || oprOldState == null)) {
//                return new JmMessage(2, "该时间段已经有同类型的红包活动了，是否仍执行该操作!!!");
//            }
//        }
//        List<ActivitySub> subListNew = null;
//        List<ActivityCard> cardListNew = null;
//        List<ActivityVo> activityVos = new ArrayList<>();
//        ActivityVo activityVo = new ActivityVo();
//        //修改活动主表
//        Activity activity = activityRepository.findOne(activityUo.getId());
//        ActivityConverter.v2p(activity,activityUo);
//        Activity activityNew = activityRepository.save(activity);
//        //修改活动配置表
//        ActivityCondition activityCondition = activityConditionRepository.findByActivityId(activityUo.getId());
//        ActivityConverter.toActivityConditionUpdate(activityUo.getActivityConditionUo(),activityCondition);
//        ActivityCondition activityConditionNew = activityConditionRepository.save(activityCondition);
//        activityVo.setActivityCondition(activityConditionNew);
//        //修改活动子表
//        if (activity.getType()==1){ //现金红包
//            List<ActivitySubUo> activitySubUos = activityUo.getActivitySubList();
//            List<ActivitySub> activitySubs = activitySubRepository.findByActivityId(activityUo.getId());
//            ActivityConverter.v2ps(activitySubs,activitySubUos);
//            subListNew = activitySubRepository.save(activitySubs);
//            activityVo.setActivitySubList(subListNew);
//        }else if (activity.getType()==2){ //卡卷红包
//            String delId = activityUo.getDelIds();
//            if(delId!=""&&delId!=null) {
//                String[] pid = delId.split(",");
//                for (int i = 0; i < pid.length; i++) {
//                    activityCardRepository.delete(Integer.parseInt(pid[i]));
//                }
//            }
//            List<ActivityCardUo> activityCardUos = activityUo.getActivityCardList();
//            List<ActivityCard> activityCards = activityCardRepository.findByActivityId(activityUo.getId());
//            ActivityConverter.u2ps(activityCardUos,activityCards);
//            for(ActivityCard activityCard:activityCards){
//                activityCard.setActivityId(activity.getId());
//            }
//            cardListNew =  activityCardRepository.save(activityCards);
//            activityVo.setActivityCardList(cardListNew);
//        }else if(activity.getType()==3){ //金券红包
//            //金额设定
//            List<ActivitySubUo> activitySubUos = activityUo.getActivitySubList();
//            List<ActivitySub> activitySubs = activitySubRepository.findByActivityId(activityUo.getId());
//            ActivityConverter.v2ps(activitySubs,activitySubUos);
//            subListNew = activitySubRepository.save(activitySubs);
//            //礼券调用
//            String delId = activityUo.getDelIds();
//            if(delId!=""&&delId!=null) {
//                String[] pid = delId.split(",");
//                for (int i = 0; i < pid.length; i++) {
//                    activityCardRepository.delete(Integer.parseInt(pid[i]));
//                }
//            }
//
//            List<ActivityCardUo> activityCardUos = activityUo.getActivityCardList();
//            List<ActivityCard> activityCards = activityCardRepository.findByActivityId(activityUo.getId());
//            ActivityConverter.u2ps(activityCardUos,activityCards);
//            for(ActivityCard activityCard:activityCards){
//                activityCard.setActivityId(activity.getId());
//            }
//            cardListNew = activityCardRepository.save(activityCards);
//            activityVo.setActivitySubList(subListNew);
//            activityVo.setActivityCardList(cardListNew);
//        }
//        String msg = "活动编辑成功!!"+compareTime(activityUo.getStartTime());
//        if(activityUo.getSubType()==2) {
//            Calendar curTime = Calendar.getInstance();
//            Date current = curTime.getTime();
//            if (activityUo.getStartTime().getTime() <= current.getTime()) {
//                activityNew.setStatus(3);
//                activityRepository.save(activityNew);
//                activityVo.setActivity(activityNew);
//                activityVos.add(activityVo);
//                sendActivityRedByOldFans(activityVos,new ActivityRedDo(null, null, 2));
//                msg = "活动操作成功!!";
//            }
//        }
//        return new JmMessage(0,msg);
//    }
//
//    /**
//     * 延迟五分钟提示
//     * @param startTime
//     * @return
//     */
//    public String compareTime(Date startTime){
//        String msg = "";
//        Calendar beforeTime = Calendar.getInstance();
//       // beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
//        Date beforeD = beforeTime.getTime();
//
//        Calendar afterTime = Calendar.getInstance();
//        afterTime.add(Calendar.MINUTE, +5);// 5分钟之后的时间
//        Date afterD = afterTime.getTime();
//        if(beforeD.getTime()<startTime.getTime()&&afterD.getTime()>startTime.getTime()){
//            msg ="该活动将在五分钟内进行,请稍候!!!";
//        }
//        return msg;
//    }
//
//    /**
//     * 查询活动列表
//     * @param qo
//     * @return
//     * @throws IOException
//     */
//    public PageItem<Activity> queryActivity(ActivityQo qo) throws IOException {
//        String sqlList = "select * from activity a where a.platform=1 ";
//        StringBuilder sqlCondition = new StringBuilder();
//        if(null==qo.getShopId()){//店铺id为空代表是聚客红包的根据appid查询
//        	sqlCondition.append(JdbcUtil.appendAnd("a.app_id",qo.getAppid()));
//        }else{
//        	sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",qo.getShopId()));
//        }
//        sqlCondition.append(JdbcUtil.appendIn("a.status",qo.getStatus()));
//        sqlCondition.append(JdbcUtil.appendAnd("a.type",qo.getType()));
//        sqlCondition.append(JdbcUtil.appendOrderBy("a.create_time"));
//        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
//        return ActivityConverter.p2v(pageItem);
//    }
//
//    /**
//     * 获取活动详情
//     * @param id
//     * @param shopId
//     * @return
//     * @throws Exception
//     */
//    public ActivityRo getActivity(Integer id,Integer shopId,String appid) throws Exception {
//        ActivityRo activityRo = new ActivityRo();
//        Activity activity =  activityRepository.findOne(id);
//        if(activity.getType()==5){
//        	if(!appid.equals(activity.getAppId())){
//        		throw new Exception("无权限查看");
//        	}
//        }else{
//        	if (!activity.getShopId().equals(shopId)){
//                throw new Exception("无权限查看");
//            }
//        }
//
//        BeanUtils.copyProperties(activity,activityRo);
//        ActivityCondition activityCondition = activityConditionRepository.findByActivityId(id);
//        activityRo.setActivityCondition(activityCondition);
//        if (activity.getType()==1){ //现金红包
//            List<ActivitySub> activitySubs  = activitySubRepository.findByActivityId(id);
//            activityRo.setActivitySubList(activitySubs);
//        }else if (activity.getType()==2){ //卡卷红包
//            List<ActivityCard> activityCards = activityCardRepository.findByActivityId(id);
//            activityRo.setActivityCardList(activityCards);
//        }else if(activity.getType()==3){ //金券红包
//            List<ActivitySub> activitySubs  = activitySubRepository.findByActivityId(id);
//            activityRo.setActivitySubList(activitySubs);
//            List<ActivityCard> activityCards = activityCardRepository.findByActivityId(id);
//            activityRo.setActivityCardList(activityCards);
//        }else if(activity.getType()==5){//聚客红包
//        	List<ActivitySub> activitySubs  = activitySubRepository.findByActivityId(id);
//            activityRo.setActivitySubList(activitySubs);
//        }
//        return activityRo;
//    }
//
//    /**
//     * 暂停活动
//     * @param id
//     * @param shopId
//     * @return
//     * @throws Exception
//     */
//    public JmMessage pause(Integer id, Integer shopId,String appid) throws Exception {
//        Activity activity = activityRepository.findOne(id);
//        if (activity==null){
//            throw new Exception("该活动不存在");
//        }
//        if(activity.getType()==5){
//        	 if(!appid.equals(activity.getAppId())){
//        		 throw new Exception("无权限修改");
//        	 }
//        }else{
//        	if (!activity.getShopId().equals(shopId)){
//                throw new Exception("无权限修改");
//            }
//        }
//
//        Integer status = activity.getStatus();
//        if(status==1){
//            activity.setStatus(2);
//            activityRepository.save(activity);
//            return new JmMessage(0,"活动暂停成功!!");
//        } else if(status==2){
//            activity.setStatus(1);
//            activityRepository.save(activity);
//            return new JmMessage(0,"活动开启成功!!");
//        }
//        return new JmMessage(1,"该活动已经结束!!");
//    }
//
//    /**
//     * 校验该用户是否可以获取红包
//     * @param activityRedDo
//     * @param activityCondition
//     * @return
//     */
//    private boolean checkActivityCondition(ActivityRedDo activityRedDo,ActivityCondition activityCondition){
//        WxUser wxUser = activityRedDo.getWxUser();
//        int subType = activityRedDo.getSubType();
//        if (activityCondition.getSex()>0 && activityCondition.getSex()!=wxUser.getSex()){ //不满足性别条件
//            return false;
//        }
//        if (activityCondition.getAreaLimit()==1){ //不满足地区条件
//            String areaIds = activityCondition.getAreaIds();
//            if (!StringUtils.isEmpty(areaIds)){
//                String[] areaId = areaIds.split(",");
//                List<String> areaIdList = Arrays.asList(areaId);
//                if (wxUser.getAreaCode()==null){
//                    return false;
//                }else if (!areaIdList.contains(wxUser.getAreaCode().toString())){
//                    return false;
//                }
//            }
//        }
//        if (activityCondition.getDownRoleLimit()==1 && !StringUtils.isEmpty(activityCondition.getDownRoles())){ //不满足角色条件
//            if (wxUser.getUpperOne()==null)  return false;
//            String userIds = "";
//            if (subType==1){  //首次关注
//                userIds = wxUser.getUpperOne().toString();
//                if (wxUser.getUpperTwo()!=null ){
//                    userIds+=","+wxUser.getUpperTwo();
//                }
//            }else{
//                userIds = wxUser.getUserId().toString();
//            }
//            String sqlCount = "select count(*) from wx_user w,shop_user s where w.shop_user_id=s.id and w.user_id in("+
//                    userIds+") and s.agent_role in("+activityCondition.getDownRoles()+")";
//            int count = jdbcUtil.queryCount(sqlCount);
//            if (count==0){
//                return false;
//            }
//        }
//        if (activityCondition.getRoleLimit()==1 && !StringUtils.isEmpty(activityCondition.getRoles())){ //不满足角色条件
//            String sqlCount = "select count(*) from wx_user w,shop_user s where w.shop_user_id=s.id and w.user_id ="+
//                    wxUser.getUserId()+" and s.agent_role in("+activityCondition.getRoleLimit()+")";
//            int count = jdbcUtil.queryCount(sqlCount);
//            if (count==0){
//                return false;
//            }
//        }
//        if (subType==3){
//            if (activityRedDo.getBuyMoney()==null||activityRedDo.getBuyMoney()< activityCondition.getBuyMoney()){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 发送活动红包
//     * @param activityRedDo
//     */
//    public void sendActivityRed(ActivityRedDo activityRedDo) throws Exception {
//        log.info("====================ActivityRedDo groupid!=1==================="+activityRedDo.getWxUser().getGroupid());
//    	log.info("====================activityType==================="+activityRedDo.getType());
//        if(activityRedDo.getWxUser().getGroupid()==1) return; //黑名单粉丝
//        List<ActivityVo> activityVos = getActivityIng(activityRedDo);
//        if(activityVos==null || activityVos.size()==0) return;
//        for (ActivityVo activityVo : activityVos){
//            ActivityCondition activityCondition = activityVo.getActivityCondition();
//            if(!checkActivityCondition(activityRedDo,activityCondition)){ continue; } //不满足条件
//            //满足条件，计算红包并且发放
//            Activity activity = activityVo.getActivity();
//            int subType = activity.getSubType();
//            int type = activity.getType();
//            WxUser wxUser = activityRedDo.getWxUser();
//            String appId = wxUser.getAppid();
//            String token = wxAuthService.getAuthAccessToken(appId);
//            log.info("redmoney::::start-----");
//            if (type==1){ //现金红包
//                Integer winRedMoney = winRedMoney(activityVo); //计算是否中奖
//                if (winRedMoney>0){ //红包金额大于零表示中奖
//                    if(subType==1){
//                        ActivityUser actUser =  findActUserByUserId(wxUser.getUserId());
//                        if(actUser==null){//用户没有领取过红包
//                            ActivityUser activityUser  = new ActivityUser();
//                            activityUser.setUserId(wxUser.getUserId());
//                            activityUser.setActivityId(activity.getId());
//                            activityUser.setMoney(winRedMoney);
//                            activityUserRepository.save(activityUser);
//                            //有资格领取，发送消息提示
//                            String accessToken  = wxAuthService.getAuthAccessToken(wxUser.getAppid());
//                            //WxReply wxReply = wxReplyRepository.findByAppidAndReplyType(wxUser.getAppid(), 1);
//                            String url = wxClient.long2short(accessToken, Constant.DOMAIN+"/card/card_page?appid="+wxUser.getAppid()+"&userId="+wxUser.getUserId()+"&activityId="+activity.getId()+"&winRedMoney="+winRedMoney);
//                            List<PicMsgArticle> articles = new ArrayList<>();
//                            PicMsgArticle article = new PicMsgArticle();
//                            article.setTitle("恭喜您获得现金红包！");
//                            article.setDescription("手气暴棚，立即点击领取！");
//                            article.setPicurl("https://mmbiz.qlogo.cn/mmbiz_jpg/GYicbGWHu9Tws1KNBYicdyj5nlc9htrj6mYLtPh3KUzjuUQSic2Iprt51vK5yMgkibdGx26XZpbOUYo3iaLib5PjqkkA/0?wx_fmt=jpeg");
//                            article.setUrl(url);
//                            articles.add(article);
//                            wxMessageService.sendImgTextMsg(wxUser.getOpenid(),accessToken,articles);
//                        }
//                    }else{
//                        sendRed(activity,wxUser,winRedMoney,token);
//                    }
//                }else {
//                    wxMessageService.sendMsg(wxUser.getOpenid(),token,activity.getNoWinInfo(),appId);
//                }
//                log.info("redmoney::::money-----");
//            }else if (type==2){ //卡卷红包
//                if(subType==1){
//                    //有资格领取，发送消息提示
//                    String accessToken  = wxAuthService.getAuthAccessToken(wxUser.getAppid());
//                    //WxReply wxReply = wxReplyRepository.findByAppidAndReplyType(wxUser.getAppid(), 1);
//                    String url = wxClient.long2short(accessToken, Constant.DOMAIN+"/card/card_page?appid="+wxUser.getAppid()+"&userId="+wxUser.getUserId()+"&activityId="+activity.getId()+"&winRedMoney=0");
//                    List<PicMsgArticle> articles = new ArrayList<>();
//                    PicMsgArticle article = new PicMsgArticle();
//                    article.setTitle("恭喜您获得优惠券！");
//                    article.setDescription("手气暴棚，点击领取优惠券！");
//                    article.setPicurl("https://mmbiz.qlogo.cn/mmbiz_jpg/GYicbGWHu9Tws1KNBYicdyj5nlc9htrj6mYLtPh3KUzjuUQSic2Iprt51vK5yMgkibdGx26XZpbOUYo3iaLib5PjqkkA/0?wx_fmt=jpeg");
//                    article.setUrl(url);
//                    articles.add(article);
//                    wxMessageService.sendImgTextMsg(wxUser.getOpenid(),accessToken,articles);
//                }else{
//                    sendCard(activityVo,wxUser, token);  //发送礼卷
//                    log.info("redmoney::::gift-----");
//                }
//            }else if (type==3) { //金券红包
//                Integer winRedMoney = winRedMoney(activityVo); //计算是否中奖
//                if (subType==1 || subType==3 || subType==4 || subType==5){
//                    String accessToken  = wxAuthService.getAuthAccessToken(wxUser.getAppid());
//                    if (winRedMoney>0){ //红包金额大于零表示中奖
//                        if(subType==1){//首次关注才推送图文
//                            ActivityUser actUser =  findActUserByUserId(wxUser.getUserId());
//                            if(actUser==null){//有资格领取
//                                ActivityUser activityUser  = new ActivityUser();
//                                activityUser.setUserId(wxUser.getUserId());
//                                activityUser.setActivityId(activity.getId());
//                                activityUser.setMoney(winRedMoney);
//                                activityUserRepository.save(activityUser);
//                            }
//                            String url = wxClient.long2short(accessToken, Constant.DOMAIN+"/card/card_page?appid="+wxUser.getAppid()+"&userId="+wxUser.getUserId()+"&activityId="+activity.getId()+"&winRedMoney="+winRedMoney);
//                            List<PicMsgArticle> articles = new ArrayList<>();
//                            PicMsgArticle article = new PicMsgArticle();
//                            article.setTitle("恭喜您获得金券红包！");
//                            article.setDescription("手气暴棚，点击领取金券！");
//                            article.setPicurl("https://mmbiz.qlogo.cn/mmbiz_jpg/GYicbGWHu9Tws1KNBYicdyj5nlc9htrj6mYLtPh3KUzjuUQSic2Iprt51vK5yMgkibdGx26XZpbOUYo3iaLib5PjqkkA/0?wx_fmt=jpeg");
//                            article.setUrl(url);
//                            articles.add(article);
//                            wxMessageService.sendImgTextMsg(wxUser.getOpenid(),accessToken,articles);
//                        }else{
//                            sendRed(activity,wxUser,winRedMoney,token);
//                        }
//                    }else {
//                        if(subType==1){
//                            String url = wxClient.long2short(accessToken, Constant.DOMAIN+"/card/card_page?appid="+wxUser.getAppid()+"&userId="+wxUser.getUserId()+"&activityId="+activity.getId()+"&winRedMoney="+winRedMoney);
//                            List<PicMsgArticle> articles = new ArrayList<>();
//                            PicMsgArticle article = new PicMsgArticle();
//                            article.setTitle("恭喜您获得金券红包！");
//                            article.setDescription("手气暴棚，点击领取金券！");
//                            article.setPicurl("https://mmbiz.qlogo.cn/mmbiz_jpg/GYicbGWHu9Tws1KNBYicdyj5nlc9htrj6mYLtPh3KUzjuUQSic2Iprt51vK5yMgkibdGx26XZpbOUYo3iaLib5PjqkkA/0?wx_fmt=jpeg");
//                            article.setUrl(url);
//                            articles.add(article);
//                            wxMessageService.sendImgTextMsg(wxUser.getOpenid(),accessToken,articles);
//                        }else{
//                            sendCard(activityVo,wxUser,token);//发送礼卷
//                        }
//                    }
//
//                }
//                log.info("redmoney::::gold-----");
//            }else if (type==5) { //聚客红包
//            	log.info("======================in juke red=======================");
//                Integer winRedMoney = winRedMoney(activityVo); //计算是否中奖
//                if (subType == 1) {
//                    if (winRedMoney > 0) { //红包金额大于零表示中奖
//                    	ActivityUser actUser =  findActUserByUserId(wxUser.getUserId());
//                    	if(actUser==null){//用户在该活动未发送过红包
//                    		   ActivityUser activityUser  = new ActivityUser();
//                               activityUser.setUserId(wxUser.getUserId());
//                               activityUser.setActivityId(activity.getId());
//                               activityUser.setMoney(winRedMoney);
//                               activityUserRepository.save(activityUser);
//                               //有资格领取，发送消息提示
//                               String accessToken  = wxAuthService.getAuthAccessToken(wxUser.getAppid());
//                               //WxReply wxReply = wxReplyRepository.findByAppidAndReplyType(wxUser.getAppid(), 1);
//                               String url = wxClient.long2short(accessToken, Constant.DOMAIN+"/jk/send_red?appid="+wxUser.getAppid()+"&userId="+wxUser.getUserId()+"&activityId="+activity.getId());
//                                    List<PicMsgArticle> articles = new ArrayList<>();
//                                    PicMsgArticle article = new PicMsgArticle();
//                                    article.setTitle("摇一摇 抢现金红包！");
//                                    article.setDescription("手气暴棚，立即点击领取！");
//                                    article.setPicurl("https://mmbiz.qlogo.cn/mmbiz_jpg/GYicbGWHu9Tws1KNBYicdyj5nlc9htrj6mYLtPh3KUzjuUQSic2Iprt51vK5yMgkibdGx26XZpbOUYo3iaLib5PjqkkA/0?wx_fmt=jpeg");
//                                    article.setUrl(url);
//                                    articles.add(article);
//                                 wxMessageService.sendImgTextMsg(wxUser.getOpenid(),accessToken,articles);
//                              // String content  = "点击链接领取红包\n"+url;
//                               //wxMessageService.sendMsg(wxUser.getOpenid(), accessToken,content, wxUser.getAppid());
//                    	}
//
//                    }
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 已关注粉丝发送活动红包
//     * @param activityRedDo
//     */
//    public void sendActivityRedByOldFans(List<ActivityVo> activityVos,ActivityRedDo activityRedDo) throws Exception {
//        if(activityVos==null || activityVos.size()==0) {
//            activityVos = getActivityIng(activityRedDo);
//        }
//        if(activityVos==null || activityVos.size()==0) return;
//        for (ActivityVo activityVo : activityVos) {
//            ActivityCondition activityCondition = activityVo.getActivityCondition();
//            List<WxUser> userList = getUserList(activityCondition);
//            //满足条件，计算红包并且发放
//            Activity activity = activityVo.getActivity();
//            if(activity.getStatus()!=3) {   //改变活动状态 金券 红包数量大于预计粉丝数
//                activity.setStatus(3);
//                activityRepository.save(activity);
//            }
//            int type = activity.getType();
//            log.info("redmoney::::start-----oldFans-----");
//            for (WxUser wxUser : userList) { //给已关注粉丝发红包
//                log.info("====================ActivityRedDo groupid!=1==================="+wxUser.getGroupid());
//                if(wxUser.getGroupid()==1) { continue; }; //黑名单粉丝
//                String appId = wxUser.getAppid();
//                String token = wxAuthService.getAuthAccessToken(appId);
//                if (type == 1) { //现金红包
//                    Integer winRedMoney = winRedMoney(activityVo); //计算是否中奖
//                    if (winRedMoney > 0) { //红包金额大于零表示中奖
//                        sendRed(activity, wxUser, winRedMoney, token);
//                    } else {
//                        wxMessageService.sendMsg(wxUser.getOpenid(), token, activity.getNoWinInfo(), appId);
//                    }
//                } else if (type == 2) { //卡卷红包
//                    sendCard(activityVo, wxUser, token);  //发送礼卷
//                } else if (type == 3) { //金券红包
//                    Integer winRedMoney = winRedMoney(activityVo); //计算是否中奖
//                    if (winRedMoney > 0) { //红包金额大于零表示中奖
//                        sendRed(activity, wxUser, winRedMoney, token);
//                    } else {
//                        sendCard(activityVo, wxUser, token);//发送礼卷
//                    }
//                }
//            }
//        }
//    }
//
//    public List<WxUser> getUserList(ActivityCondition ac) throws Exception{
//        String sql = "select wu.user_id,wu.headimgurl,wu.appid,wu.phone_number,wu.user_name,wu.openid,wu.union_id,wu.subscribe_time,wu.un_subscribe_time," ;
//        sql += "wu.sex,wu.area_code,wu.nickname,wu.upper_one,wu.upper_two,wu.remark,wu.groupid,wu.is_subscribe,wu.language,wu.city,wu.province,wu.country,wu.level_id,";
//        sql +=" wu.is_my_shop,wu.is_buy,wu.shop_user_id from wx_user wu LEFT JOIN shop_user su on wu.shop_user_id = su.id where wu.is_subscribe=1 and groupid!=1";
//        StringBuilder sqlCondition = new StringBuilder();
//        Integer activityId = ac.getActivityId();
//        sql +=" and wu.appid=(select b.app_id from activity a  left join shop b on a.shop_id = b.shop_id where 1=1 and a.id =" +activityId+
//                " )";
//        if(Toolkit.parseObjForInt(ac.getSex())!=0){
//            sqlCondition.append(JdbcUtil.appendAnd("wu.sex",ac.getSex()));
//        }
//        if(Toolkit.parseObjForInt(ac.getRoleLimit())==1 ){
//                if(!ac.getRoles().equals("")){
//                    sqlCondition.append(JdbcUtil.appendIn("su.agent_role",ac.getRoles()));
//                }
//                //cj 2017-01-20  【现金红包 — 已关注粉丝】角色限定，增加一个可选角色：购买粉丝；
//                if(Toolkit.parseObjForInt(ac.getIsBuy())==99 ){
//                    sqlCondition.append(JdbcUtil.appendAnd("wu.is_buy",1));
//                }
//
//        }
//        if(Toolkit.parseObjForInt(ac.getAreaLimit())==1 ){
//            if(!ac.getAreaIds().equals("")){
//                sqlCondition.append(JdbcUtil.appendIn("wu.area_code",ac.getAreaIds()));
//            }
//        }
//        List<WxUser> wxUserList = jdbcUtil.queryList(sql + sqlCondition+" order by RAND()",WxUser.class);
//        return wxUserList;
//    }
//
//    private void sendCard(ActivityVo activityVo,WxUser wxUser,String token) throws Exception {
//        int useCount = 0;
//        int totalCount = 0;
//        Activity activity = activityVo.getActivity();
//        List<ActivityCard> activityCardList = activityVo.getActivityCardList();
//        if (activityCardList==null || activityCardList.size()==0) return;
//        if (activity.getCardType()==1) { //一张
//            ActivityCard activityCard = activityCardList.get(0);
//            log.info("sendCard::::UseCount---"+activityCard.getUseCount()+"-----TotalCount-----"+activityCard.getTotalCount());
//            if (activityCard.getUseCount()>=activityCard.getTotalCount()){ //礼券消耗完
//                activity.setStatus(3);
//                activityRepository.save(activity);
//                // sendShopUserMsg(activity);//发送手机短信
//                return;
//            }
//            activityCard.setUseCount(activityCard.getUseCount()+1); //使用量加一张
//            activityCardRepository.save(activityCard);
//            //发送卡卷到微信用户
//            sendCardToWxUser(activity,activityCard ,wxUser, token);
//            if (activityCard.getUseCount()>=activityCard.getTotalCount()){ //礼券消耗完
//                activity.setStatus(3);
//                activityRepository.save(activity);
//                // sendShopUserMsg(activity);//发送手机短信
//            }
//        }else if(activity.getCardType()==2){// 随机
//           /* List<ActivityCard> activityCardListCopy =ActivityConverter.activityCardsCopy(activityCardList);
//            List<ActivityCard> delList = new ArrayList();
//            for(ActivityCard activityCard : activityCardListCopy){
//                if (activityCard.getUseCount()>=activityCard.getTotalCount()){
//                    delList.add(activityCard);
//                }
//            }
//            activityCardListCopy.removeAll(delList);//移除已发完卡券
//            if(activityCardListCopy.size()<=0){
//                activity.setStatus(3);
//                activityRepository.save(activity);
//                return;
//            }
//
//            int number =  (int) (Math.random()*(activityCardListCopy.size()));//产生随机数
//            ActivityCard activityCardRandom = activityCardListCopy.get(number);
//            activityCardRandom.setUseCount(activityCardRandom.getUseCount()+1); //使用量加一张
//            activityCardRepository.save(activityCardRandom);
//            sendCardToWxUser(activity,activityCardRandom ,wxUser);
//            if(activityCardListCopy.size()==1){
//               if(activityCardRandom.getUseCount()>=activityCardRandom.getTotalCount()){//礼券消耗完
//                   activity.setStatus(3);
//                   activityRepository.save(activity);
//               }
//            }*/
//        }else if(activity.getCardType()==3){ //礼包
//          /*  ActivityCard activityCardbag = activityCardList.get(0);
//            activityCardbag.setUseCount(activityCardbag.getUseCount()+1); //使用量加一张
//            activityCardRepository.save(activityCardbag);
//            //发送卡卷到微信用户
//           // sendCardToWxUser(activity,activityCardbag ,wxUser);
//            if (activityCardbag.getUseCount()>=activityCardbag.getTotalCount()){ //礼券消耗完
//                activity.setStatus(3);
//                activityRepository.save(activity);
//            }*/
//        }
//    }
//
//
//    /**
//     * 发送卡卷到微信用户
//     * @param activityCard
//     * @param wxUser
//     * @throws Exception
//     */
//    private void sendCardToWxUser(Activity activity,ActivityCard activityCard ,WxUser wxUser,String token) throws Exception {
//        //保存用户卡卷信息
//        WxUserCard wxUserCard = new WxUserCard();
//        wxUserCard.setActivityId(activity.getId());
//        wxUserCard.setUserId(wxUser.getUserId());
//        wxUserCard.setStatus(0);
//        wxUserCard.setCardId(activityCard.getCardId());
//        wxUserCard = wxUserCardRepository.save(wxUserCard);
//        //发送模板消息
//        WxTemplateDo tdo = new WxTemplateDo();
//        //Shop shop = shopService.getCacheShopByAppId(wxUser.getAppid());
//        tdo.setTouser(wxUser.getOpenid());
//        tdo.setUrl(Constant.DOMAIN+"/card/user_card/"+wxUserCard.getId()+"?shopId="+activity.getShopId());
//        tdo.setAppid(wxUser.getAppid());
//        tdo.setType(6);
//        tdo.setFirst("收到礼券");
//        tdo.setRemark("点击查看详情》》");
//        tdo.setWaitForTask("恭喜！您收到一份礼券啦！");
//        tdo.setDelayTask("暂无任务");
//        ResultMsg resultMsg = wxMessageService.sendTemplate(tdo);
//        if(resultMsg.getErrcode()==0) {
//            wxMessageService.sendMsg(wxUser.getOpenid(), token, activity.getNoWinInfo(), wxUser.getAppid());
//        }
//    }
//
//
//
//    /**
//     * 发送红包
//     * @param activity
//     * @param wxUser
//     * @param winRedMoney
//     * @param token
//     * @throws Exception
//     */
//    private void sendRed(Activity activity,WxUser wxUser,int winRedMoney,String token) throws Exception {
//        WxRedDo wxRedDo = ActivityConverter.toWxRedDo(activity,wxUser);
//        Shop shop = shopService.getCacheShop(activity.getShopId());
//        wxRedDo.setShopName(shop.getShopName());
//        wxRedDo.setTotalAmount(winRedMoney);
//        RedResultParam redResultParam = wxService.sendRed(wxRedDo);
//        saveActivityUser(activity,wxUser,winRedMoney,redResultParam);
//        if("SUCCESS".equals(redResultParam.getResultCode())){
//            wxMessageService.sendMsg(wxUser.getOpenid(),token,activity.getWinInfo(),wxUser.getAppid());
//        }
//        log.info(JsonMapper.toJson(redResultParam));
//    }
//
//    //保存发送红包记录
//    private void saveActivityUser(Activity activity,WxUser wxUser,int winRedMoney,RedResultParam redResultParam){
//        ActivityUser activityUser  = new ActivityUser();
//        activityUser.setRedPayId(redResultParam.getRedPayId());
//        activityUser.setUserId(wxUser.getUserId());
//        activityUser.setActivityId(activity.getId());
//        activityUser.setMoney(winRedMoney);
//        activityUser.setStatus(1);
//        activityUserRepository.save(activityUser);
//    }
//
//
//    /**
//     * 获取正在执行的活动
//     * @param activityRedDo
//     * @return
//     */
//    private List<ActivityVo> getActivityIng(ActivityRedDo activityRedDo) throws Exception{
//        String appId = null;
//        List<Activity> activityList = null;
//        if(activityRedDo.getSubType()!=2) {
//            appId = activityRedDo.getWxUser().getAppid();
//            activityList = activityRepository.findActivityIng(activityRedDo.getPlatform(), appId, activityRedDo.getSubType());
//        }else{
//            activityList = activityRepository.findActivityIngByOldFans(activityRedDo.getPlatform(), activityRedDo.getSubType());
//        }
//        if (activityList==null || activityList.size()==0 ) return null;
//        List<ActivityVo> activityVos = new ArrayList<>();
//        for (Activity activity : activityList){
//            if (activity.getStatus()==0){ //未开始
//                activity.setStatus(1);
//                activityRepository.save(activity);
//            }
//            ActivityVo activityVo = new ActivityVo();
//            if (activity.getType()==1){ //现金红包
//                List<ActivitySub> activitySubs = activitySubRepository.findByActivityId(activity.getId());
//                if (activitySubs==null || activitySubs.size()==0) return null;
//                activityVo.setActivitySubList(activitySubs);
//            }else if (activity.getType()==2) { //卡卷红包
//                List<ActivityCard> activityCardList = activityCardRepository.findByActivityId(activity.getId());
//                if (activityCardList==null || activityCardList.size()==0) return null;
//                activityVo.setActivityCardList(activityCardList);
//            }else if (activity.getType()==3) { //金券红包
//                List<ActivitySub> activitySubs = activitySubRepository.findByActivityId(activity.getId());
//                if (activitySubs==null || activitySubs.size()==0) return null;
//                List<ActivityCard> activityCardList = activityCardRepository.findByActivityId(activity.getId());
//                if (activityCardList==null || activityCardList.size()==0) return null;
//                activityVo.setActivitySubList(activitySubs);
//                activityVo.setActivityCardList(activityCardList);
//            }else if (activity.getType()==5) { //聚客红包活动
//                List<ActivitySub> activitySubs = activitySubRepository.findByActivityId(activity.getId());
//                if (activitySubs==null || activitySubs.size()==0) return null;
//                activityVo.setActivitySubList(activitySubs);
//            }
//            activityVo.setActivity(activity);
//            activityVo.setActivityCondition(getCacheActivityCondition(activity.getId()));
//            activityVos.add(activityVo);
//        }
//        return activityVos;
//    }
//
//    @Cacheable(value ="ActivityConditionCache", key="#id")
//    private ActivityCondition getCacheActivityCondition(int id){
//        return activityConditionRepository.findByActivityId(id);
//    }
//
//    /**
//     * 计算获取红包金额,并更新活动状态
//     * @param activityVo
//     * @return
//     */
//    private int winRedMoney(ActivityVo activityVo){
//        int redMoney = 0;
//        for (ActivitySub activitySub : activityVo.getActivitySubList()) {
//            if( activitySub.getStatus()==1){ // 该规则已发完红包
//                continue;
//            }
//            int totalCount = activitySub.getWinCount()+ activitySub.getNoWinCount(); //中奖人数+未中奖人数
//            int fansCount = activitySub.getFansCount();
//            if (totalCount==0||fansCount>=totalCount){
//                updateActivity(activityVo,activitySub);
//                continue;
//            }
//            activitySub.setFansCount(++fansCount);
//            if (fansCount==totalCount){ //一个阶段结束
//                updateActivity(activityVo,activitySub);
//            }else {
//                activitySubRepository.save(activitySub);
//            }
//            int noWinScale = activitySub.getNoWinScale(); //中奖与不中奖比例 1:1 ; 1:2
//            if(noWinScale==0){ // 全部中奖情况
//                    redMoney = activitySub.getRedMoney();
//            }else{
//                int n  =(fansCount+noWinScale)%(noWinScale+1);
//                if(n==0){  // 关注后幸运得到红包
//                        redMoney = activitySub.getRedMoney();
//                }
//                log.info("money:"+n+"-----"+redMoney);
//            }
//
//            break;
//        }
//        return redMoney;
//    }
//
//    /**
//     * 根据关注任务更新任务表
//     * @param activityVo
//     * @param activitySub
//     */
//    private void updateActivity(ActivityVo activityVo,ActivitySub activitySub){
//        Activity activity = activityVo.getActivity();
//        List<ActivitySub> activitySubs = activityVo.getActivitySubList();
//        activitySub.setStatus(1);
//        activitySubRepository.save(activitySub);
//        if(activitySubs.get(activitySubs.size()-1).getId()==activitySub.getId()){ //子任务到最后一环节的时候，更新主表activity状态为3 (已完结)
//            activity.setStatus(3);//主活动结束
//            activityRepository.save(activity);
//            // sendShopUserMsg(activity);//发送手机短信
//        }
//    }
//
//    private void sendShopUserMsg(Activity activity){
//        Integer shopId = activity.getShopId();
//        Shop shop = shopRepository.findShopByShopId(shopId);
//        SMSUtil.sendMsg("【聚米为谷】您的店铺:"+shop.getShopName()+"，【"+getRedType(activity.getType())+"--"+getRedSubType(activity.getSubType())+"】活动已结束，请尽快登录聚米为谷系统部署新活动！", shop.getPhoneNumber());
//    }
//
//    public String getRedType(Integer typeVal){
//        String type ="";
//        switch(typeVal)
//        {
//            case 1:
//                type="现金红包";
//                break;
//            case 2:
//                type="卡券红包";
//                break;
//            case 3:
//                type="金券红包";
//                break;
//        }
//        return  type;
//    }
//    public String getRedSubType(Integer subTypeVal){
//        String type ="";
//        switch(subTypeVal)
//        {
//            case 1:
//                type="首次关注发红包";
//                break;
//            case 2:
//                type="已关注粉丝发红包";
//                break;
//            case 3:
//                type="购买商品发红包";
//                break;
//            case 4:
//                type="确认收货发红包";
//                break;
//            case 5:
//                type="卡卷转赠发红包";
//                break;
//        }
//        return  type;
//    }
//
//    @Transactional
//    public void addPush(SubscribePushCo subscribePushCo) {
//        subscribePushRepository.updateSubscribePush(subscribePushCo.getIsOpen(),subscribePushCo.getShopId());
//        List<SubscribePush> subscribePushList = ActivityConverter.toSubscribePushList(subscribePushCo);
//        subscribePushRepository.save(subscribePushList);
//    }
//
//    @Transactional
//    public void updatePush(SubscribePushCo subscribePushCo) {
//        subscribePushRepository.updateSubscribePush(subscribePushCo.getIsOpen(),subscribePushCo.getShopId());
//        subscribePushRepository.deleteByShopId(subscribePushCo.getShopId());
//        List<SubscribePush> subscribePushList = ActivityConverter.toSubscribePushList(subscribePushCo);
//        subscribePushRepository.save(subscribePushList);
//    }
//
//    public SubscribePushCo getPush(Integer shopId) {
//        List<SubscribePush> subscribePushList = subscribePushRepository.findByShopId(shopId);
//        SubscribePushCo subscribePushCo = ActivityConverter.toSubscribePushCo(subscribePushList);
//        Shop shop = shopService.getShop(shopId);
//        subscribePushCo.setIsOpen(shop.getIsSubscribePush());
//        subscribePushCo.setShopId(shopId);
//        return subscribePushCo;
//    }
//
//    @Transactional
//    public void updateActivityTask() throws Exception  {
//        activityRepository.updateActivityTaskIng();
//        activityRepository.updateActivityTaskOver();
//        // 发送短信  cj 2017-02-06
//        List<Activity> activityList = activityRepository.findActivityTaskOver(); // 已结束还未发短信的活动
//        List ids = new ArrayList();
//        for (Activity activity:activityList) {
//            sendShopUserMsg(activity);
//            ids.add(activity.getId());
//        }
//        if(ids.size()>0){
//            activityRepository.updateActivityTaskOverSms(ids); //更新 已发短信
//        }
//        // ----end
//    }
//
//    @Transactional
//    public void updateActivityOldFansTask() throws Exception  {
//        activityRepository.updateActivityOldFansTaskIng();
//        activityRepository.updateActivityOldFansTaskOver();
//    }
//
//    /**
//     * 关注时加入到待推送列表
//     * @param newUser
//     * @param remoteHost
//     * @param flag
//     */
//    public void addPushList(WxUser newUser,String remoteHost,boolean flag) throws Exception {
//        if(newUser==null) return;
//        Shop shop = shopService.getShopByAppId(newUser.getAppid());
//        if (shop==null || shop.getIsSubscribePush()==null || shop.getIsSubscribePush()==0 ) return;//未开启关注推送
//        //删除未发送的关注推送
//        List<SubscribePushDo> delList = new ArrayList<>();
//        for (SubscribePushDo subscribePushDo : Constant.SUBSCRIBE_PUSH_LIST){
//            if (subscribePushDo.getUserId().equals(newUser.getUserId())){
//                delList.add(subscribePushDo);
//            }
//        }
//        Constant.SUBSCRIBE_PUSH_LIST.removeAll(delList);
//
//        //加入到代发列表
//        List<SubscribePush> subscribePushList = subscribePushRepository.findByShopId(shop.getShopId());
//        for (SubscribePush subscribePush : subscribePushList){
//            SubscribePushDo subscribePushDo = new SubscribePushDo();
//            subscribePushDo.setPushType(subscribePush.getPushType());
//            subscribePushDo.setPushContext(subscribePush.getPushContext());
//            subscribePushDo.setUserId(newUser.getUserId());
//            subscribePushDo.setNewUser(flag);
//            subscribePushDo.setClientIp(remoteHost);
//            if (subscribePush.getTotalSecond()==0){
//                List<SubscribePushDo> pushList = new ArrayList<>();
//                pushList.add(subscribePushDo);
//                pushInfo(pushList);
//            }else {
//                subscribePushDo.setPushTime((int) (System.currentTimeMillis()/1000 + subscribePush.getTotalSecond()));
//                Constant.SUBSCRIBE_PUSH_LIST.add(subscribePushDo);
//            }
//        }
//    }
//
//    /**
//     * 定时推送
//     * @throws Exception
//     */
//    public void subscribePush() throws Exception {
//        final List<SubscribePushDo> pushList = new ArrayList<>();
//        for (SubscribePushDo subscribePushDo : Constant.SUBSCRIBE_PUSH_LIST){
//            if (subscribePushDo.getPushTime()<=(System.currentTimeMillis()/1000)){ //可以发送
//                pushList.add(subscribePushDo);
//            }
//        }
//        if(pushList.size()==0) return;
//        Constant.SUBSCRIBE_PUSH_LIST.removeAll(pushList);
//
//        //启动线程
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        cachedThreadPool.execute(new Runnable() {
//            public void run() {
//                try {
//                    pushInfo(pushList);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error(e.getMessage());
//                }
//            }
//        });
//    }
//        /**
//         * 具体推送
//         * @param pushList
//         * @throws Exception
//         */
//    private void pushInfo(List<SubscribePushDo> pushList) throws Exception {
//        for (SubscribePushDo subscribePushDo : pushList){
//            Integer pushType = subscribePushDo.getPushType();
//            String context =subscribePushDo.getPushContext();
//            WxUser wxUser = wxUserService.getWxUser(subscribePushDo.getUserId());
//            String accessToken = wxAuthService.getAuthAccessToken(wxUser.getAppid());
//            if (0==pushType){ //关注回复
//                wxService.autoReply(wxUser.getAppid(),wxUser.getOpenid(), accessToken, 1);
//            }else if (1==pushType) { //二维码海报
//                wxService.sendUserQrcode(accessToken,wxUser,context);
//            }else if (2==pushType) { //现金红包
//                if (subscribePushDo.isNewUser()){
//                    sendActivityRed(new ActivityRedDo(wxUser,subscribePushDo.getClientIp(),1));
//                }
//            }else if (4==pushType || 5==pushType  || 6==pushType  || 7==pushType || 8==pushType|| 10==pushType ) { //商品详情
//                SendContextDo sendContextDo = new SendContextDo();
//                sendContextDo.setAccessToken(accessToken);
//                sendContextDo.setType(pushType);
//                sendContextDo.setOpenid(wxUser.getOpenid());
//                sendContextDo.setAppid(wxUser.getAppid());
//                sendContextDo.setUserId(wxUser.getUserId());
//                if (8==pushType){ //商城首页
//                    context = "1";
//                }else{
//                    if (StringUtils.isEmpty(context)){
//                        return;
//                    }
//                }
//                sendContextDo.setContextId(Integer.valueOf(context));
//                wxMessageService.sendContext(sendContextDo);
//            }else if (pushType==9) { //关注语
//                wxMessageService.sendMsg(wxUser.getOpenid(),accessToken,subscribePushDo.getPushContext(),wxUser.getAppid());
//            }
//        }
//    }
//
//    /**
//     * 根据活动条件获取用户数量
//     */
//    public int userCount(ActivityConditionQo qo, String appid){
//        String sql = "select count(*) from wx_user wu LEFT JOIN shop_user su on wu.shop_user_id = su.id " +
//                "where 1=1 and wu.appid='" +appid+"'"+
//                " and wu.is_subscribe=1 and groupid!=1";
//        StringBuilder sqlCondition = new StringBuilder();
//        if(Toolkit.parseObjForInt(qo.getSex())!=0){
//            sqlCondition.append(JdbcUtil.appendAnd("wu.sex",qo.getSex()));
//        }
//        if(Toolkit.parseObjForInt(qo.getRoleLimit())==1 ){
//                if(!qo.getRoles().equals("")){
//                    sqlCondition.append(JdbcUtil.appendIn("su.agent_role",qo.getRoles()));
//                }
//                //cj 2017-01-20  【现金红包 — 已关注粉丝】角色限定，增加一个可选角色：购买粉丝；
//                if(Toolkit.parseObjForInt(qo.getIsBuy())==99 ){
//                    sqlCondition.append(JdbcUtil.appendAnd("wu.is_buy",1));
//                }
//        }
//
//        if(Toolkit.parseObjForInt(qo.getAreaLimit())==1 ){
//            if(!qo.getAreaIds().equals("")){
//                sqlCondition.append(JdbcUtil.appendIn("wu.area_code",qo.getAreaIds()));
//            }
//        }
//        int count = jdbcUtil.queryCount(sql + sqlCondition);
//        return count;
//    }
//
//    /**
//     * 查询聚客红包已发送记录
//     * @param activityUserQo
//     * @return
//     * @throws Exception
//     */
//    public PageItem<ActivityUserVo> findJkSendRecord(ActivityUserQo activityUserQo) throws Exception{
//    	String sqlList = " select u.headimgurl,u.nickname,p.nick_name pub_nick_name,ac.sex,ac.area_ids,ac.area_names,"
//    			+ "  asub.red_money,au.create_time,au.status,au.id as activity_user_id from activity_user au"
//    			+ "  left join wx_user u on au.user_id = u.user_id"
//    			+ "  left join activity a on au.activity_id = a.id"
//    			+ "  left join wx_pub_account p on a.app_id=p.appid	"
//    			+ "  left join activity_condition ac on a.id = ac.activity_id"
//    			+ "  left join activity_sub asub on asub.activity_id = au.activity_id"
//    			+ "  where a.type=5 and p.user_id="+activityUserQo.getUserId()+"";
//
//    	 StringBuilder sqlCondition = new StringBuilder();
//    	 if(activityUserQo.getPlatform()>0){
//    		 sqlCondition.append(JdbcUtil.appendIn("a.platform",activityUserQo.getPlatform()+""));
//    	 }
//    	 int status = activityUserQo.getStatus();
//    	 if(status>-1){
//    		 sqlCondition.append(JdbcUtil.appendIn("au.status",activityUserQo.getStatus()+""));
//    	 }
//    	sqlCondition.append(JdbcUtil.appendAnd("au.create_time",activityUserQo.getStartDate(),activityUserQo.getEndtDate()));
//    	sqlCondition.append(JdbcUtil.appendLike("p.nick_name",activityUserQo.getPubNickName()));
//    	sqlCondition.append(" group by au.id");
//    	sqlCondition.append(JdbcUtil.appendOrderBy("au.create_time"));
//    	PageItem<ActivityUserVo> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, activityUserQo.getCurPage(), activityUserQo.getPageSize(), ActivityUserVo.class);
//
//    	for (ActivityUserVo p : pageItem.getItems()) {
//    		p.setNickname(Base64Util.getFromBase64(p.getNickname()));
//		}
//    	return pageItem;
//    }
//    /**
//     * 根据微信用户id查询活动用户表
//     * @param userId
//     * @return
//     */
//    public ActivityUser findActUserByUserId(Integer userId){
//    	return activityUserRepository.findByUserId(userId);
//    }
//
//    public Activity findByActivityId(Integer activityId){
//    	return activityRepository.findOne(activityId);
//    }
//
//    public void saveActivityUser(ActivityUser activityUser){
//    	activityUserRepository.save(activityUser);
//    }
//
//    public List<Activity> findActByAppid(String appid){
//    	return activityRepository.findByAppId(appid);
//    }
//
//    public void saveActivity(Activity activity){
//    	activityRepository.save(activity);
//    }
//
//   /**
//     * 根据活动ID获取统计后的红包记录
//     */
//    public ActivityEo getActivityCount(Integer id) throws  Exception{
//        String sql = "select a.id as activity_id,a.total_money,a.pre_fans_count,a.sub_type,sum(case when a.id is not null then 1 end) as fans_count,SUM(a.money) as money, a.card_total_count, a.card_use_count,a.start_time,a.end_time from  " +
//                "( select distinct au.id,au.money,au.user_id,au.red_pay_id,a.total_money,a.pre_fans_count,a.sub_type,a.start_time,a.end_time,acard.card_total_count,acard.card_use_count " +
//                " from activity a " ;
//        sql+=" left join activity_user au on a.id=au.activity_id " ;
//        sql+=" left join  ";
//        sql+=" (SELECT ac.activity_id as activity_id, SUM(ac.total_count) as card_total_count,SUM(ac.use_count) as card_use_count from  activity_card ac where ac.activity_id ="+id+") acard " +
//                " on acard.activity_id=a.id";
//        sql+=" where 1=1 ";
//        StringBuilder sqlCondition = new StringBuilder();
//        sqlCondition.append(JdbcUtil.appendAnd("a.id",id));
//        sql+=sqlCondition+")a";
//        List<ActivityEo> list = jdbcUtil.queryList(sql,ActivityEo.class);
//        if(list!=null&&list.size()>0){
//            return list.get(0);
//        }
//        return  null;
//    }
//
//    /**
//     * 查询活动列表
//     * @param eo
//     * @param flag
//     * @return
//     * @throws IOException
//     */
//    public PageItem<ActivityUserEo> queryActivityUserList(ActivityUserEo eo, Integer flag) throws Exception {
//        Activity activity = activityRepository.findOne(eo.getActivityId());
//        Integer type = Toolkit.parseObjForInt(activity.getType());
//        PageItem<ActivityUserEo> pageItem = new PageItem<>();
//        List<ActivityUserEo> data = new ArrayList<>();
//        if(1==type.intValue()){ // 现金红包
//            StringBuffer sqlBuffer = createMoneySql(eo);
//            data = jdbcUtil.queryList(sqlBuffer.toString(),ActivityUserEo.class);
//            for(ActivityUserEo userEo :data){
//                userEo.setNickname(Base64Util.getFromBase64(userEo.getNickname()));
//                userEo.setHeadimgurl(ImgUtil.appendUrl(userEo.getHeadimgurl()));
//            }
//            String countSql = "SELECT ifnull(count(au.user_id),0) FROM activity_user au LEFT JOIN activity a ON a.id = au.activity_id " +
//                              "  WHERE a.id = "+eo.getActivityId();
//            int count = jdbcUtil.queryCount(countSql);
//            pageItem.setCount(count);
//        }else if(2==type.intValue()){
//            StringBuffer sqlBuffer = createCardSql(eo);
//            data = jdbcUtil.queryList(sqlBuffer.toString(),ActivityUserEo.class);
//            for(ActivityUserEo userEo :data){
//                userEo.setNickname(Base64Util.getFromBase64(userEo.getNickname()));
//                userEo.setHeadimgurl(ImgUtil.appendUrl(userEo.getHeadimgurl()));
//            }
//            String countSql = "SELECT ifnull(count(wuc.user_id),0) FROM wx_user_card wuc " +
//                    " LEFT JOIN activity a ON a.id = wuc.activity_id " +
//                    " WHERE a.id = "+eo.getActivityId();
//            int count = jdbcUtil.queryCount(countSql);
//            pageItem.setCount(count);
//        } else if(3==type.intValue()){
//            if(1==flag.intValue()){
//                StringBuffer sqlBuffer = createCardSql(eo);
//                data = jdbcUtil.queryList(sqlBuffer.toString(),ActivityUserEo.class);
//                for(ActivityUserEo userEo :data){
//                    userEo.setNickname(Base64Util.getFromBase64(userEo.getNickname()));
//                    userEo.setHeadimgurl(ImgUtil.appendUrl(userEo.getHeadimgurl()));
//                }
//                String countSql = "SELECT ifnull(count(au.user_id),0) FROM activity_user au LEFT JOIN activity a ON a.id = au.activity_id " +
//                        "  WHERE a.id = "+eo.getActivityId();
//                int count = jdbcUtil.queryCount(countSql);
//                pageItem.setCount(count);
//            }else if(2==flag.intValue()){
//                StringBuffer sqlBuffer = createMoneySql(eo);
//                data = jdbcUtil.queryList(sqlBuffer.toString(),ActivityUserEo.class);
//                for(ActivityUserEo userEo :data){
//                    userEo.setNickname(Base64Util.getFromBase64(userEo.getNickname()));
//                    userEo.setHeadimgurl(ImgUtil.appendUrl(userEo.getHeadimgurl()));
//                }
//                String countSql = "SELECT ifnull(count(wuc.user_id),0) FROM wx_user_card wuc " +
//                        " LEFT JOIN activity a ON a.id = wuc.activity_id " +
//                        " WHERE a.id = "+eo.getActivityId();
//                int count = jdbcUtil.queryCount(countSql);
//                pageItem.setCount(count);
//            }
//        }
//        pageItem.setItems(data);
//        return pageItem;
//    }
//
//    /**
//     * <p>卡券用户领取</p>
//     *
//     * @Author cj
//     * @version latest
//     * @Date 2017/5/15 17:34
//     */
//    private StringBuffer createCardSql(ActivityUserEo eo) {
//        StringBuffer sqlBuffer = new StringBuffer("SELECT " +
//                " sc.card_name,sc.card_money,wuc.STATUS AS card_status,");
//        sqlBuffer.append(" wuc.card_id,wuc.create_time AS card_create_time,wuc.user_id,b.nickname,b.headimgurl FROM wx_user_card wuc ");
//        sqlBuffer.append(" LEFT JOIN  activity a ON a.id = wuc.activity_id LEFT JOIN shop_card sc ON wuc.card_id = sc.id ");
//        sqlBuffer.append(" LEFT JOIN wx_user b ON wuc.user_id = b.user_id");
//        sqlBuffer.append(" WHERE a.id ="+eo.getActivityId());
//        int start = eo.getCurPage()*eo.getPageSize();
//        sqlBuffer.append(" LIMIT "+start+" , "+eo.getPageSize());
//        return sqlBuffer;
//    }
//
//    /**
//     * <p>现金活动用户领取</p>
//     *
//     * @Author cj
//     * @version latest
//     * @Date 2017/5/15 17:35
//     */
//    private StringBuffer createMoneySql(ActivityUserEo eo) {
//        StringBuffer sqlBuffer = new StringBuffer("SELECT  au.id AS activity_user_id," +
//                " au.create_time,au.money, au.STATUS AS money_status,");
//        sqlBuffer.append(" b.nickname,b.headimgurl FROM activity_user au");
//        sqlBuffer.append(" LEFT JOIN activity a  ON a.id = au.activity_id ");
//        sqlBuffer.append(" LEFT JOIN wx_user b ON au.user_id = b.user_id");
//        sqlBuffer.append(" WHERE a.id ="+eo.getActivityId());
//        int start = eo.getCurPage()*eo.getPageSize();
//        sqlBuffer.append(" LIMIT "+start+" , "+eo.getPageSize());
//        return sqlBuffer;
//    }
//
//    /**
//     * 卡券明细
//     * @param eo
//     * @return
//     * @throws IOException
//     * @author cj
//     */
//
//    public PageItem<ActivityUserEo> queryGoldActivityUserList(ActivityUserEo eo) throws IOException {
//        String sql =" select a.* from (SELECT a.id as activity_id,wu.headimgurl,wu.nickname," +
//                " sc.card_name, sc.card_money,wuc.status as card_status,wuc.card_id," +
//                "wuc.create_time as card_create_time,wu.user_id from activity a ";
//        sql+=" left join wx_user_card wuc on a.id=wuc.activity_id ";
//        sql+=" left join wx_user wu on  ( wuc.user_id=wu.user_id)";
//        sql+=" left join shop_card sc on wuc.card_id =sc.id";
//        sql+=" where 1=1 ";
//        StringBuilder sqlCondition = new StringBuilder();
//        sqlCondition.append(JdbcUtil.appendAnd("a.id",eo.getActivityId()));
//        sqlCondition.append(JdbcUtil.appendOrderBy("a.create_time"));
//        sql+=sqlCondition+" )a where 1=1 and  a.user_id is not null";
//        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql,eo.getCurPage(),eo.getPageSize());
//        return ActivityConverter.p2e(pageItem);
//    }
//
//    /**
//     * 给活动报表查询条件使用
//     * @param shopId
//     * @param type
//     * @return
//     * @author cj 2017-02-09
//     */
//    public List<Activity> queryActivityName(Integer shopId, Integer type) {
//        List typeIdList = new ArrayList();
//        if(0 == type.intValue()){
//            typeIdList.add(1);
//            typeIdList.add(2);
//            typeIdList.add(3);
//        }else{
//            typeIdList.add(type);
//        }
//        List subTypeList = new ArrayList();
//        subTypeList.add(1); // 首次关注
//        subTypeList.add(3); // 购买
//        // subTypeList.add(5); // 转赠
//        return activityRepository.queryActivityName(shopId,typeIdList,subTypeList);
//    }
//
//
//
//    /**
//     * 查询聚客红包已发送记录(聚客管理平台使用)
//     * @param activityUserQo
//     * @return
//     * @throws Exception
//     */
//    public PageItem<ActivityUserVo> findJkSendRecordToManage(ActivityUserQo activityUserQo) throws Exception{
//        String sqlList = " select u.headimgurl,u.nickname,p.nick_name pub_nick_name,ac.sex,ac.area_ids,ac.area_names,"
//                + "  asub.red_money,au.create_time,au.status,au.id as activity_user_id from activity_user au"
//                + "  left join wx_user u on au.user_id = u.user_id"
//                + "  left join activity a on au.activity_id = a.id"
//                + "  left join wx_pub_account p on a.app_id=p.appid	"
//                + "  left join activity_condition ac on a.id = ac.activity_id"
//                + "  left join activity_sub asub on asub.activity_id = au.activity_id"
//                + "  where a.type=5 and p.appid='"+activityUserQo.getAppid()+"'";
//
//        StringBuilder sqlCondition = new StringBuilder();
//        if(activityUserQo.getPlatform()>0){
//            sqlCondition.append(JdbcUtil.appendIn("a.platform",activityUserQo.getPlatform()+""));
//        }
//        int status = activityUserQo.getStatus();
//        if(status>-1){
//            sqlCondition.append(JdbcUtil.appendIn("au.status",activityUserQo.getStatus()+""));
//        }
//        sqlCondition.append(JdbcUtil.appendAnd("au.create_time",activityUserQo.getStartDate(),activityUserQo.getEndtDate()));
//        sqlCondition.append(JdbcUtil.appendLike("p.nick_name",activityUserQo.getPubNickName()));
//        sqlCondition.append(" group by au.id");
//        sqlCondition.append(JdbcUtil.appendOrderBy("au.create_time"));
//        PageItem<ActivityUserVo> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, activityUserQo.getCurPage(), activityUserQo.getPageSize(), ActivityUserVo.class);
//
//        for (ActivityUserVo p : pageItem.getItems()) {
//            p.setNickname(Base64Util.getFromBase64(p.getNickname()));
//        }
//        return pageItem;
//    }
//
//
//
//}