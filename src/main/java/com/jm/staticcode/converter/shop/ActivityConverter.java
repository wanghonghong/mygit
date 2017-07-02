package com.jm.staticcode.converter.shop;

import com.jm.business.domain.wx.WxRedDo;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.activity.*;
import com.jm.mvc.vo.shop.integral.IntegralRecordVo;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.activity.*;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>活动转换器</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public class ActivityConverter {

    public static Activity v2p(ActivityCo activityCo) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityCo,activity);
        setStatusByTime(activity);
        return activity;
    }

    public static ActivityCondition toActivityCondition(Integer id, ActivityConditionCo activityConditionCo) {
        ActivityCondition activityCondition = new ActivityCondition();
        BeanUtils.copyProperties(activityConditionCo,activityCondition);
        activityCondition.setActivityId(id);
        return activityCondition;
    }

    public static List<ActivitySub> activitySubs(Integer id, List<ActivitySubCo> subActivityList) {
        List<ActivitySub> list = new ArrayList<>();
        for (ActivitySubCo subActivityCo : subActivityList){
            ActivitySub activitySub = new ActivitySub();
            BeanUtils.copyProperties(subActivityCo,activitySub);
            activitySub.setActivityId(id);
            list.add(activitySub);
        }
        return list;
    }

    public static void v2p(Activity activity, ActivityUo activityUo) {
        Toolkit.copyPropertiesIgnoreNull(activityUo,activity);
        setStatusByTime(activity);
    }

    /**
     * 根据时间设置活动状态
     * @param activity
     */
    private static void setStatusByTime(Activity activity){
        if (activity.getStartTime()!=null&&activity.getEndTime()!=null){
            long currentTime = System.currentTimeMillis();
            if (activity.getStartTime().getTime()<currentTime && activity.getEndTime().getTime()>currentTime){ //活动开始
                activity.setStatus(1);
            }else if (activity.getEndTime().getTime()<currentTime){
                activity.setStatus(2);
            }
        }
    }


    public static void v2ps(List<ActivitySub> activitySubs, List<ActivitySubUo> activitySubUos) {
        for (ActivitySubUo activitySubUo : activitySubUos){
            for (ActivitySub activitySub : activitySubs){
                if (activitySub.getId().equals(activitySubUo.getId())){
                    Toolkit.copyPropertiesIgnoreNull(activitySubUo,activitySub);
                }
            }
        }
    }

    public static void toActivityConditionUpdate(ActivityConditionUo activityConditionUo, ActivityCondition activityCondition) {
        if(activityConditionUo!=null){
            Toolkit.copyPropertiesIgnoreNull(activityConditionUo,activityCondition);
        }
    }

    public static PageItem<Activity> p2v(PageItem<Map<String, Object>> pageItemMap) throws IOException {
        PageItem<Activity> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<Activity> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            Activity obj = JsonMapper.map2Obj(map,Activity.class);
            list.add(obj);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

    public static List<SubscribePush> toSubscribePushList(SubscribePushCo subscribePushCo) {
        List<SubscribePush> list = new ArrayList<>();
        for (SubscribePushVo subscribePushVo :subscribePushCo.getSubscribePushVos()){
            SubscribePush subscribePush = new SubscribePush();
            BeanUtils.copyProperties(subscribePushVo,subscribePush);
            subscribePush.setShopId(subscribePushCo.getShopId());
            list.add(subscribePush);
        }
        return list;
    }

    public static SubscribePushCo toSubscribePushCo(List<SubscribePush> subscribePushList) {
        SubscribePushCo subscribePushCo = new SubscribePushCo();
        List<SubscribePushVo> list = new ArrayList<>();
        for (SubscribePush subscribePush :subscribePushList){
            SubscribePushVo subscribePushVo = new SubscribePushVo();
            BeanUtils.copyProperties(subscribePush,subscribePushVo);
            list.add(subscribePushVo);
        }
        subscribePushCo.setSubscribePushVos(list);
        return subscribePushCo;
    }

    public static WxRedDo toWxRedDo(Activity activity,WxUser wxUser) {
        WxRedDo wxRedDo = new WxRedDo();
        wxRedDo.setAppid(wxUser.getAppid());
        wxRedDo.setOpenid(wxUser.getOpenid());
        wxRedDo.setActName(activity.getActivityName());
        wxRedDo.setWishing(activity.getBlessings());
        wxRedDo.setRemark("关注送红包");
        wxRedDo.setClientIp("127.0.0.1");
        return wxRedDo;
    }

    public static List<ActivityCard> activityCards(Integer id, List<ActivityCardCo> activityCardList) {
        List<ActivityCard> list = new ArrayList<>();
        for (ActivityCardCo activityCardCo : activityCardList){
            ActivityCard activityCard = new ActivityCard();
            BeanUtils.copyProperties(activityCardCo,activityCard);
            activityCard.setActivityId(id);
            list.add(activityCard);
        }
        return list;

    }

    public static List<ActivityCard> activityCardsCopy(List<ActivityCard> activityCardList) {
        List<ActivityCard> list = new ArrayList<>();
        for (ActivityCard activityCard : activityCardList){
            ActivityCard aC = new ActivityCard();
            BeanUtils.copyProperties(activityCard,aC);
            list.add(aC);
        }
        return list;
    }

    public static void u2ps(List<ActivityCardUo> activityCardUos, List<ActivityCard> activityCards) {
        for (ActivityCardUo activityCardUo : activityCardUos){
            for (ActivityCard activityCard : activityCards){
                if (activityCard.getId().equals(activityCardUo.getId())){
                    Toolkit.copyPropertiesIgnoreNull(activityCardUo,activityCard);
                }
            }
        }
        for(ActivityCardUo activityCardUo : activityCardUos){
             //礼券修改---新增记录保存
            if (activityCardUo.getId()==null) {
                ActivityCard activityCardNew = new ActivityCard();
                Toolkit.copyPropertiesIgnoreNull(activityCardUo,activityCardNew);
                activityCards.add(activityCardNew);
            }
        }
    }

    public static PageItem<ActivityUserEo> p2e(PageItem<Map<String, Object>> pageItemMap) throws IOException {
        PageItem<ActivityUserEo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<ActivityUserEo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ActivityUserEo obj = JsonMapper.map2Obj(map,ActivityUserEo.class);
            list.add(obj);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        converterNickName(pageItem);
        return pageItem;
    }

    @ApiOperation("昵称解码")
    private static void converterNickName(PageItem page) {
        List<ActivityUserEo> list = page.getItems();
        for (ActivityUserEo ae:list) {
            ae.setNickname(Base64Util.getFromBase64(Toolkit.parseObjForStr(ae.getNickname())));
        }
    }
}
