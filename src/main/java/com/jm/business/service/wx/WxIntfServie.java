package com.jm.business.service.wx;

import com.jm.business.service.product.ProductService;
import com.jm.business.service.shop.*;
import com.jm.business.service.system.WxAreaService;
import com.jm.business.service.user.ShopUserService;
import com.jm.mvc.vo.shop.activity.ActivityVo;
import com.jm.mvc.vo.wx.PicMsgArticle;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.jpa.product.ProductGroupRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductGroup;
import com.jm.repository.po.shop.*;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ImageTextType;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ApplicationContextUtil;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供給微信端接口服务层
 * Created by cj on 2016/6/28.
 */
@Slf4j
public class WxIntfServie {

    /**
     * 微信端点击事件触发红包活动，回调是否中奖
     * @param info
     * @param sendTarger
     * @param count
     * @throws IOException
     */
    /*@ApiOperation("微信/微博端点击事件触发红包活动，回调是否中奖")
    public void winActivityIng(WeixinActInfo info, Integer activityTypeId, Integer platform , String sendTarger, Integer count) throws Exception {
             *//*activityTypeId 活动类型 1:现金红包 2：优惠卷红包 3：红包墙
               platform  红包平台 / 1:微信商城 2：微博商城
               sendTarger发送目标  1:购买商品xx元 2：卡卷赠送xx张 3：收货奖励 4：首次关注的新粉
               0:活动未开始 1：活动进行中 2：活动暂停 3：活动已完结
             *//*
        List<Integer> status = new ArrayList<>();
        status.add(1);//正在进行中的
        ActivityVo activityVo  = activityService.findActivityByStatus(info.getAppid(),"1" ,activityTypeId, platform,sendTarger,status);
        Activity activity = activityVo.getActivity();
        List<SubActivity> subActivityList  = activityVo.getSubActivityList();
        if(activity != null && subActivityList.size()>0){
            long endTime = activity.getEndTime().getTime();
            long currentTime = System.currentTimeMillis();
            if(currentTime>endTime){
                activity.setStatus(3); //活动结束
                activityService.save(activity);
                return;
            }
            //开始跑红包配送活动
            if("1".equals(sendTarger) || "2".equals(sendTarger)){
                Integer condition = 0;//Toolkit.parseObjForInt(activity.getConditionText());
                if(Toolkit.parseObjForInt(count)>=Toolkit.parseObjForInt(condition)){
                    this.sendRedPack(activity,subActivityList,info);
                }
            }else if("4".equals(sendTarger)){ //首次关注
                List<ActivityOptions> activityOptionsList = activityVo.getActivityOptionsList();
                if(activityOptionsList.size()==0){
                    this.sendRedPack(activity,subActivityList,info);
                }else{
                    //高级设置
                    if(activityOptionsList.size()>0){
                        this.advancedNewUserOptions(activityOptionsList,info,activity,subActivityList);  //调用新用户关注高级设置
                    }else{
                        this.sendRedPack(activity,subActivityList,info);
                    }
                }
            }else{
                this.sendRedPack(activity,subActivityList,info);
            }
        }
    }*/

    /**
     * 新客户关注高级设置
     * @param activityOptionsList
     * @param info
     * @param activity
     * @param subActivityList
     */
    /*private void advancedNewUserOptions(List<ActivityOptions> activityOptionsList, WeixinActInfo info, Activity activity, List<SubActivity> subActivityList) throws Exception {
        boolean flag = true;
        String openId = Toolkit.parseObjForStr(info.getReOpenid());
        String appId = Toolkit.parseObjForStr(info.getAppid());
        WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(appId,openId);
        logger.info("---------获取微信端反馈的wxuser-----------WxUser=="+wxUser);
        for (ActivityOptions activityOptions : activityOptionsList) {
            Integer conditionId = activityOptions.getConditionId(); //1:性别限制 2：角色限定/指定该角色的下级客户得红包 4：地区限制
            String  optionIds = activityOptions.getConditionOptionIds();
            if(1==conditionId){//性别
                Integer sex = Toolkit.parseObjForInt(wxUser.getSex()); //1:男 2：女  0：未知
                if(Toolkit.parseObjForInt(optionIds)!=sex){
                    flag = false;
                    break;
                }
            }
            if(4==conditionId){ //地区
                String[] arr = optionIds.split(",");
                List optionIdsList = wxAreaService.findStaticByPareaIds(arr);
                String areaCode = Toolkit.parseObjForStr(wxUser.getAreaCode());
                if(!optionIdsList.contains(areaCode)){
                    flag = false;
                    break;
                }
            }
            if(2==conditionId){  //角色
                // 1:已经关注（value=1） 2:已购买（value=1） 分销商x档（1:A档 2:B档 3:C档 4:D档） 代理商x档（5:A档 6:B档 7:C档 8:D档）
                Integer uppperOneId =   Toolkit.parseObjForInt(wxUser.getUpperOne()); //上级
                Integer uppperTwoId =   Toolkit.parseObjForInt(wxUser.getUpperTwo()); //上级上级
                if("98".equals(optionIds)){ //已关注
                    if(uppperOneId!=0){
                          WxUser upperOneWxUser = wxUserService.getWxUser(uppperOneId);
                          Integer isSubscribe = Toolkit.parseObjForInt(upperOneWxUser.getIsSubscribe());
                        if(1!=isSubscribe){
                            if(uppperTwoId!=0){
                                WxUser upperTwoWxUser = wxUserService.getWxUser(uppperTwoId);
                                Integer isSubscribe2 = Toolkit.parseObjForInt(upperTwoWxUser.getIsSubscribe());
                                if(1!=isSubscribe2){
                                    flag = false;
                                }
                            }else{
                                flag = false;
                            }
                        }
                    }else{
                        flag = false;
                    }
                }else if("99".equals(optionIds)){//已购买
                    if(uppperOneId!=0){
                        WxUser upperOneWxUser = wxUserService.getWxUser(uppperOneId);
                        Integer isBuy = Toolkit.parseObjForInt(upperOneWxUser.getIsBuy());
                        if(1!=isBuy){
                            if(uppperTwoId!=0){
                                WxUser upperTwoWxUser = wxUserService.getWxUser(uppperTwoId);
                                Integer isBuy2 = Toolkit.parseObjForInt(upperTwoWxUser.getIsBuy());
                                if(1!=isBuy2){
                                    flag = false;
                                }
                            }else{
                                flag = false;
                            }
                        }
                    }else{
                        flag = false;
                    }
                }else if("1".equals(optionIds) || "2".equals(optionIds) ||  "3".equals(optionIds) || "4".equals(optionIds)){ //分销商x档（1:A档 2:B档 3:C档 4:D档）
                    if(uppperOneId!=0){
                        WxUser upperOneWxUser = wxUserService.getWxUser(uppperOneId);

                        if(upperOneWxUser.getShopUserId()!=null &&upperOneWxUser.getShopUserId()>0 ) {
                            ShopUser shopUser = shopUserService.findShopUser(upperOneWxUser.getShopUserId());
                            Integer distrRole = Toolkit.parseObjForInt(shopUser.getDistrRole());
                            if(distrRole!=Toolkit.parseObjForInt(optionIds)){
                                if(uppperTwoId!=0){
                                    WxUser upperTwoWxUser = wxUserService.getWxUser(uppperTwoId);
                                    Integer distributorRole2 = Toolkit.parseObjForInt(shopUser.getDistrRole());
                                    if(distributorRole2!=Toolkit.parseObjForInt(optionIds)){
                                        flag = false;
                                    }
                                }else{
                                    flag = false;
                                }
                            }

                        }else{
                            flag = false;
                        }

                    }else{
                        flag = false;
                    }
                }else if("5".equals(optionIds) || "6".equals(optionIds) ||  "7".equals(optionIds)|| "8".equals(optionIds)){//代理商x档（5:A档 6:B档 7:C档 8:D档）
                    if(uppperOneId!=0){
                        WxUser upperOneWxUser = wxUserService.getWxUser(uppperOneId);
                        if(upperOneWxUser.getShopUserId()!=null &&upperOneWxUser.getShopUserId()>0 ){
                             ShopUser shopUser =  shopUserService.findShopUser(upperOneWxUser.getShopUserId());
                            Integer agentRole = Toolkit.parseObjForInt(shopUser.getAgentRole());
                            if(agentRole!=Toolkit.parseObjForInt(optionIds)){
                                if(uppperTwoId!=0){
                                    WxUser upperTwoWxUser = wxUserService.getWxUser(uppperTwoId);
                                    Integer agentRole2 = Toolkit.parseObjForInt(shopUser.getAgentRole());
                                    if(agentRole2!=Toolkit.parseObjForInt(optionIds)){
                                        flag = false;
                                    }
                                }else{
                                    flag = false;
                                }
                            }
                        }else{
                            flag =false;
                        }


                    }else{
                        flag = false;
                    }
                }

            }
        }
        if(flag){
            this.sendRedPack(activity,subActivityList,info);
        }
    }*/

    /**
     * 5：已关注的老粉丝,触发红包活动
     */
    /*@ApiOperation("已关注的粉丝,触发现金红包活动")
    public  void  autoActivity() throws Exception, java.lang.Exception {
        // 发送目标  5：已关注的粉丝,触发红包活动
        // 0:活动未开始 1：活动进行中 2：活动暂停 3：活动已完结】
        boolean flag = false;
        List status = new ArrayList();
        status.add(1);
        //1元红包，现金红包，微信平台，已关注
        List<ActivityVo>  activityVos = activityService.findActivityByStatusList("1",1,1,"5",status);
        List<ActivityVo> newActivityVos = new ArrayList();
        if(activityVos.size()>0){
            //更新所有店铺+-的活动状态
            long currentTime = System.currentTimeMillis();
            for (ActivityVo actVo:activityVos) {
                Activity activity = actVo.getActivity();
                long endTime = activity.getEndTime().getTime();
                if(currentTime>endTime){
                    activity.setStatus(3);
                    activityService.save(activity);
                }else{
                    newActivityVos.add(actVo);
                }
            }

            activityVos = activityService.findActivityByStatusList("1",1,1,"5",status);
        }
        EntityManager entityManager = (EntityManager) ApplicationContextUtil.getApplicationContext().getBean("entityManagerPrimary");
        for (ActivityVo actVo:newActivityVos) {
            Activity activity =  actVo.getActivity();
            List<SubActivity> subActivityList = actVo.getSubActivityList();
            List<ActivityOptions> activityOptionsList = actVo.getActivityOptionsList();
            String appid = "";//Toolkit.parseObjForStr(activity.getAppid()); //店铺appid
            long funsCount = activity.getPreFunsCount();
            String querySql = activityService.getOlderUserSql(activityOptionsList,funsCount,appid);
            Query sql = entityManager.createNativeQuery(querySql);
            List wxusers = sql.getResultList();
            Integer count = wxusers.size(); //从微信用户中获取人数
            if(count!=0){
                flag =true;
            }
            for (Object object :wxusers) {
                Object[] obj = (Object[]) object;
                Integer userId = Toolkit.parseObjForInt(obj[0]);
                String  openId = Toolkit.parseObjForStr(obj[1]);
                String  appId = Toolkit.parseObjForStr(obj[2]);
                WeixinActInfo wxActInfo = getWxActInfo(userId,openId,appId);
                wxActInfo.setRemark("老用户送红包");
                sendRedPack(activity,subActivityList,wxActInfo);
            }
            if(flag){
                activity.setStatus(3);
                activityService.save(activity);
            }
        }
    }*/

   /* public WeixinActInfo getWxActInfo(Integer userId, String openId, String appid) {
        WxPubAccount pubAccount = wxpublicAccountService.findWxPubAccountByAppid(appid);
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = "127.0.0.1";//request.getRemoteHost();
        WeixinActInfo param = new WeixinActInfo();
        param.setWxappid(appid);
        param.setMchId(pubAccount.getMchId());
        param.setSendName(pubAccount.getNickName());
        param.setReOpenid(openId);//接收方
        param.setClientIp(clientIp);
        param.setAppkey(pubAccount.getAppKey());//商户key
        param.setTotalNum(1);
        param.setAppid(appid);
        return param;
    }*/

    /**
     * 红包活动流程派送机制
     * @param activity
     * @param subActivityList
     */
    /*@ApiOperation("红包活动派送机制")
    private void sendRedPack(Activity activity ,List<SubActivity> subActivityList,WeixinActInfo info) throws Exception {
        for (SubActivity subActivity : subActivityList) {
            long focusCount = subActivity.getFocusCount();
            int totalCount = subActivity.getWinCount()+ subActivity.getNoWinCount(); //中奖人数+未中奖人数
            if(totalCount==0){
                subActivity.setFocusCount(0);
                subActivity.setStatus(1);
                subActivityService.update(subActivity);
                //subactivity到最后一环节的时候，更新主表activity状态为3 (已完结)
                if(subActivityList.get(subActivityList.size()-1).getId()==subActivity.getId()){
                    activity.setStatus(3);//主活动结束
                    activityService.save(activity);
                }
            }else if( subActivity.getStatus()==0 && focusCount<totalCount){
                subActivity.setFocusCount(++focusCount);
                subActivityService.update(subActivity); // 更新关注人数
                if(focusCount==totalCount){
                    subActivity.setStatus(1);// 发送红包该规则已完结
                    subActivityService.update(subActivity); // 更新该红包方案已完结
                    //所有的活动流程都走完后更新活动主表activity 状态为3 (已完结)
                    if(subActivityList.get(subActivityList.size()-1).getId()==subActivity.getId()){
                        activity.setStatus(3);//主活动结束
                        activityService.save(activity);
                    }
                }
                int noWinScale = subActivity.getNoWinScale(); //中奖与不中奖比例 1:1 ; 1:2
                if(noWinScale==0){ // 全部中奖情况
                    int singleRedPacket = Toolkit.parseObjForInt(subActivity.getSingleRedPacket()); //红包金额(分)
                    info.setTotalAmount(singleRedPacket); // 红包金额
                    info.setActName(Toolkit.parseObjForStr(activity.getActivityName()));
                    info.setWishing(Toolkit.parseObjForStr(activity.getBlessings()));
                    info.setIsSuccess(1);
                    wxService.sendRed(info);
                    //发送红包
                }else{
                    long n  = (Long)(focusCount+noWinScale)%(noWinScale+1);
                    if(n==0){
                        // 关注后幸运得到红包
                        int singleRedPacket = Toolkit.parseObjForInt(subActivity.getSingleRedPacket()); //红包金额(分)
                        info.setTotalAmount(singleRedPacket); // 红包金额
                        info.setActName(Toolkit.parseObjForStr(activity.getActivityName()));
                        info.setWishing(Toolkit.parseObjForStr(activity.getBlessings()));
                        info.setIsSuccess(1);
                        wxService.sendRed(info); //调用红包接口
                        String  winInfo  = Toolkit.parseObjForStr(activity.getWinInfo()); //中奖推送语
                        String toUser = Toolkit.parseObjForStr(info.getAppid());
                        String accessToken = Toolkit.parseObjForStr(wxAuthService.getAuthAccessToken(toUser));
                        wxMessageService.sendMsg(info.getReOpenid(),accessToken,winInfo);
                        logger.info(focusCount+"----"+"成功!!!");
                    }else{
                        //关注后没抽中红包
                        String  noWinInfo  = Toolkit.parseObjForStr(activity.getNoWinInfo()); //未中奖推送语
                        String toUser = Toolkit.parseObjForStr(info.getAppid());
                        String accessToken = Toolkit.parseObjForStr(wxAuthService.getAuthAccessToken(toUser));
                        //发送推送语
                        wxMessageService.sendMsg(info.getReOpenid(),accessToken,noWinInfo);
                        logger.info(focusCount+"----"+"失败!!!");
                    }
                }
                break;
            }
        }
    }*/

    /**
     * 针对新关注的用户(走活动部署轴)
     */
    /*public void runActivityDeployAxis(WeixinActInfo info) throws InterruptedException {
            String appid = Toolkit.parseObjForStr(info.getAppid());
            Shop shop = shopService.findShopByAppId_(appid);
            Integer shopId = 0;
            if(shop != null){
                shopId = Toolkit.parseObjForInt(shop.getShopId());
            }
            List<ActivityDeployAxis> list = activityDeployAxisService.getDeployAxis(shopId);
            if(list.size()>0){
                this.runActDeployAxisThead(list,info);
            }
    }*/

    /**
     *活动部署轴流程
     * @param list
     * @param info
     */
    /*private void runActDeployAxisThead(List<ActivityDeployAxis> list, final WeixinActInfo info){
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (final ActivityDeployAxis actDeloyAxis:list) {
             long currentTime = System.currentTimeMillis(); //毫秒
             final long n = (long)Toolkit.parseObjForInt(actDeloyAxis.getIntervalSeconds())*1000;
             cachedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        pushWxMes(actDeloyAxis,info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            Thread.sleep(n);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
             });
        }
    }*/
    /**
     * 根据部署任务id给微信端用户推送对应信息
     * @param actDeloyAxis
     * @param info
     * @throws Exception
     */
    /*private void pushWxMes(ActivityDeployAxis actDeloyAxis, WeixinActInfo info) throws Exception {
        Integer deployAxisId = actDeloyAxis.getDeployAxisId();
        Integer taskId = Toolkit.parseObjForInt(actDeloyAxis.getPushTaskId());
        String toUser = Toolkit.parseObjForStr(info.getAppid());
        String accessToken = Toolkit.parseObjForStr(wxAuthService.getAuthAccessToken(toUser));
        if(1==deployAxisId){ // 1.首次回复关注语;
            String pushMess = Toolkit.parseObjForStr(actDeloyAxis.getPushMessage());
            wxMessageService.sendMsg(info.getReOpenid(),accessToken,pushMess);
        }else if(2==deployAxisId){//2.二维码海报;
              //等同品接口
        }else if(3==deployAxisId){//3.首次关注现金红包;
                this.winActivityIng(info,1,1,"4",null);
        }else if(4==deployAxisId){//4.礼券红包;
              //待开发中...
        }else if(5==deployAxisId){//5.商品分组列表;
                ProductGroup productGroup = productGroupRepository.findOne(taskId);
                String groupName = Toolkit.parseObjForStr(productGroup.getGroupName());
                String slogan = Toolkit.parseObjForStr(productGroup.getGroupSlogan());
                String imgUrl = Toolkit.parseObjForStr(productGroup.getGroupImagePath());
                PicMsgArticle pma = new PicMsgArticle();
                String path ="";//跳链接地址？？？？
                pma.setTitle(groupName);
                pma.setDescription(slogan);
                pma.setUrl(Constant.DOMAIN+path); //跳链接地址
                pma.setPicurl(ImgUtil.appendUrl(imgUrl,720)); //图片地址
                wxMessageService.sendPicMsg(toUser,accessToken,pma);
        }else if(6==deployAxisId){//6.商品详情;
                Product product = productService.getProduct(taskId);
                String imgUrl = Toolkit.parseObjForStr(product.getPicRectangle());
                String name = Toolkit.parseObjForStr(product.getName());
                String share = Toolkit.parseObjForStr(product.getShare());
                String path = ""; //跳链接地址？？？？
                PicMsgArticle pma = new PicMsgArticle();
                pma.setTitle(name);
                pma.setDescription(share);
                pma.setPicurl(ImgUtil.appendUrl(imgUrl,720)); //图片地址
                pma.setUrl(Constant.DOMAIN+path); //跳链接地址
                wxMessageService.sendPicMsg(toUser,accessToken,pma);
        }else if(7==deployAxisId){//7.图文分类列表;
             ImageTextType imageTextType = imageTextTypeService.findImageTextTypeById(taskId);
             if(imageTextType!=null){
                 String imgUrl =  ImgUtil.appendUrl(Toolkit.parseObjForStr(imageTextType.getImageUrl()));
                 String typeName = Toolkit.parseObjForStr(imageTextType.getTypeName());
                 String shareText = Toolkit.parseObjForStr(imageTextType.getShareText());
                 String path = "/shop/image_text_type/"+taskId; //跳链接地址
                 PicMsgArticle pma = new PicMsgArticle();
                 pma.setTitle(typeName);
                 pma.setDescription(shareText);
                 pma.setUrl(Constant.DOMAIN+path); //跳链接地址
                 pma.setPicurl(ImgUtil.appendUrl(imgUrl,720)); //图片地址
                 wxMessageService.sendPicMsg(toUser,accessToken,pma);
             }
        }else if(8==deployAxisId){//8.图文详情;
             ImageText imageText = imageTextService.findImageTextById(taskId);
             if(imageText!=null){
                 String imageTextTile =  Toolkit.parseObjForStr(imageText.getImageTextTile());
                 String imgUrl =  ImgUtil.appendUrl(Toolkit.parseObjForStr(imageText.getImageUrl()));
                 String shareText = Toolkit.parseObjForStr(Toolkit.parseObjForStr(imageText.getShareText()));
                 String createTime = Toolkit.parseObjForStr(Toolkit.parseObjForStr(imageText.getCreateTime()));
                 String authorName = Toolkit.parseObjForStr(Toolkit.parseObjForStr(imageText.getAuthorName()));
                 String path = "/imageText/"+taskId; //跳链接地址
                 PicMsgArticle pma = new PicMsgArticle();
                 pma.setTitle(imageTextTile);
                 pma.setDescription(shareText);
                 pma.setUrl(Constant.DOMAIN+path); //跳链接地址
                 pma.setPicurl(ImgUtil.appendUrl(imgUrl,720)); //图片地址
                 wxMessageService.sendPicMsg(toUser,accessToken,pma);
             }
        }else if(9==deployAxisId){//9.提示语
            String pushMess = Toolkit.parseObjForStr(actDeloyAxis.getPushMessage());
            if(!"".equals(pushMess)){
                wxMessageService.sendMsg(info.getReOpenid(),accessToken,pushMess);
            }
        }
    }*/
//    public static void main(String[] args) {
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//        for (int i = 0; i < 10; i++) {
//            final int index = i;
//            try {
//                Thread.sleep(index * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            cachedThreadPool.execute(new Runnable() {
//                public void run() {
//                    System.out.println("start");
//                    System.out.println("execute ende");
//                }
//            });
//        }
//    }
}
