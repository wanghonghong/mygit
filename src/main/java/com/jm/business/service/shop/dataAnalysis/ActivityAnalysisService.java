package com.jm.business.service.shop.dataAnalysis;

import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.*;

/**
 * <p>活动分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:41
 */
@Service
public class ActivityAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ShopService shopService;

    @ApiOperation("活动分析")
    public Map findActivityData(Integer shopId, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        Map map = new HashMap();
        if(1 == tabIndex.intValue()){ //活动概况
            handleActivityBasic(shopId,paramsQo,map);
        }else if(2 == tabIndex.intValue()){ //效果分析
            Integer activityId = paramsQo.getActivityId();
            if(activityId != null){
                if(-1 == activityId){
                    return map;
                }
                Activity activity = activityService.findByActivityId(activityId);
                handlEffect(activity,map);
            }
        }
        return map;
    }

    private void handlEffect(Activity activity, Map map) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        Integer subType = activity.getSubType(); // 1:首次关注发红包  3：购买商品发红包  5：卡卷转赠发红包
        Integer type = activity.getType(); //1:现金红包 2：卡券红包 3：金券红包
        Integer shopId = activity.getShopId();
        Shop shop = shopService.getShop(shopId);
        Date startTime = activity.getStartTime();
        Date endTime = activity.getEndTime();
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        if(1==subType.intValue()){
            sqlCondition.append(JdbcUtil.appendAnd("a.appid",shop.getAppId()));
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",startTime,endTime));
            String sexSql = "select  " +
                    " ifnull(sum(case WHEN a.sex = 1 THEN 1 end),0) as m," +
                    " ifnull(sum(case WHEN a.sex = 2 THEN 1 end),0) as w," +
                    " ifnull(sum(case when a.sex = 0 THEN 1 end),0) as t" +
                    " from wx_user a where 1=1 " +sqlCondition;

            String addUserSql ="select a.alias as name,IFNULL(a.count,0) as value from " +
                    " (select b.alias ,a.count from " +
                    " (select a.area_code as areaCode,count(1) as count" +
                    " FROM" +
                    " (select a.user_id,a.appid,a.is_subscribe,a.un_subscribe_time,a.subscribe_time," +
                    " CONCAT(SUBSTR(a.area_code,1,2),'0000') as area_code," +
                    " b.area_name from wx_user a left join wx_area b" +
                    " on a.area_code=b.area_id where 1=1 and a.area_code !=0 " +sqlCondition+
                    " ) a group by a.area_code)a right join (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.areaCode = b.area_id)a order by count desc";

            String buyUserSql = "select a.alias as name,IFNULL(a.count,0) as value from " +
                    " (select b.alias ,a.count from " +
                    " (select a.area_code as areaCode,count(1) as count" +
                    " FROM" +
                    " (select a.user_id,a.appid,a.is_subscribe,a.un_subscribe_time,a.subscribe_time," +
                    " CONCAT(SUBSTR(a.area_code,1,2),'0000') as area_code," +
                    " b.area_name from wx_user a left join wx_area b" +
                    " on a.area_code=b.area_id where 1=1 and a.area_code !=0 and a.is_buy = 1" +sqlCondition+
                    " ) a group by a.area_code)a right join (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.areaCode = b.area_id)a order by count desc";

            String sql ="SELECT a.alias AS name,IFNULL(a.addCount, 0) AS addCount,IFNULL(a.buyCount,0) as buyCount," +
                    " case when IFNULL(a.addCount, 0)=0 then '0.00%' else concat(ROUND(IFNULL(a.buyCount,0)/IFNULL(a.addCount, 0)*100,2),'%') end as percent" +
                    " from (select b.alias ,a.addCount,a.buyCount from " +
                    " (select a.area_code as areaCode,count(1) as addCount,ifnull(sum(case when a.is_buy =1 then 1 end),0) AS buyCount" +
                    " FROM" +
                    " (select a.user_id,a.is_buy,a.appid,a.is_subscribe,a.un_subscribe_time,a.subscribe_time," +
                    " CONCAT(SUBSTR(a.area_code,1,2),'0000') as area_code," +
                    " b.area_name from wx_user a left join wx_area b" +
                    " on a.area_code=b.area_id where 1=1 and a.area_code !=0 " +sqlCondition+
                    " ) a group by a.area_code)a right join (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.areaCode = b.area_id)a order by addCount desc";
            Map sexMap = jdbcTemplate.queryForMap(sexSql);
            map.put("sexInfo",converterSex(sexMap));
            List addUserList = jdbcTemplate.queryForList(addUserSql);
            List buyUserList = jdbcTemplate.queryForList(buyUserSql);
            addUserList.add(map1);
            addUserList.add(map2);
            buyUserList.add(map1);
            buyUserList.add(map2);
            map.put("addUserInfo",addUserList);
            map.put("buyUserInfo",buyUserList);
            map.put("activityAnalysisInfo",jdbcTemplate.queryForList(sql));
        } else if(3==subType.intValue()){
            sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date",startTime,endTime));
            String sexSql = "select  " +
                    " ifnull(sum(case WHEN b.sex = 1 THEN 1 end),0) as m," +
                    " ifnull(sum(case WHEN b.sex = 2 THEN 1 end),0) as w," +
                    " ifnull(sum(case when b.sex = 0 THEN 1 end),0) as t" +
                    " from order_info a left join  wx_user b  on  b.user_id = a.user_id where 1=1 and a.status in (1,2,3,5)" +sqlCondition;
            String orderSql = " SELECT b.alias as name,ifnull(a.addOrders,0) as value" +
                    " FROM (" +
                    " SELECT a.area_code AS areaCode,count(1) AS addOrders,sum(a.orderMoney) as orderMoney FROM (" +
                    " SELECT a.order_info_id as orderInfoId,ifnull(a.real_price,0) as orderMoney,CONCAT(SUBSTR(c.area_code, 1, 2),'0000') AS area_code" +
                    " FROM order_info a LEFT JOIN wx_user c on a.user_id = c.user_id WHERE 1 = 1 AND c.area_code != 0 and a.status in(1,2,3,5) " + sqlCondition+
                    " ) a GROUP BY a.area_code ) a RIGHT JOIN (SELECT area_id,alias FROM wx_area WHERE  substr(area_id, 3) = '0000' ) b ON a.areaCode = b.area_id " +
                    " ORDER BY a.addOrders DESC ";
            String orderMoneySql = " SELECT b.alias as name,ifnull(a.orderMoney,0) as value" +
                    " FROM (" +
                    " SELECT a.area_code AS areaCode,count(1) AS addOrders,round(sum(a.orderMoney)/100,2) as orderMoney FROM (" +
                    " SELECT a.order_info_id as orderInfoId,ifnull(a.real_price,0) as orderMoney,CONCAT(SUBSTR(c.area_code, 1, 2),'0000') AS area_code" +
                    " FROM order_info a LEFT JOIN wx_user c on a.user_id = c.user_id WHERE 1 = 1 AND c.area_code != 0 and a.status in(1,2,3,5) " + sqlCondition+
                    " ) a GROUP BY a.area_code ) a RIGHT JOIN (SELECT area_id,alias FROM wx_area WHERE  substr(area_id, 3) = '0000' ) b ON a.areaCode = b.area_id " +
                    " ORDER BY a.addOrders DESC ";
            String sql = " SELECT b.alias as name,ifnull(a.addOrders,0) as addOrders,ifnull(a.orderMoney,0) as orderMoney" +
                    " FROM (" +
                    " SELECT a.area_code AS areaCode,count(1) AS addOrders,round(sum(a.orderMoney)/100,2) as orderMoney FROM (" +
                    " SELECT a.order_info_id as orderInfoId,ifnull(a.real_price,0) as orderMoney,CONCAT(SUBSTR(c.area_code, 1, 2),'0000') AS area_code" +
                    " FROM order_info a LEFT JOIN wx_user c on a.user_id = c.user_id WHERE 1 = 1 AND c.area_code != 0 and a.status in(1,2,3,5) " + sqlCondition+
                    " ) a GROUP BY a.area_code ) a RIGHT JOIN (SELECT area_id,alias FROM wx_area WHERE  substr(area_id, 3) = '0000' ) b ON a.areaCode = b.area_id " +
                    " ORDER BY a.addOrders DESC ";
            Map sexMap = jdbcTemplate.queryForMap(sexSql);
            map.put("sexInfo",converterSex(sexMap));
            List orderList = jdbcTemplate.queryForList(orderSql);
            List orderMoneyList = jdbcTemplate.queryForList(orderMoneySql);
            orderList.add(map1);
            orderList.add(map2);
            orderMoneyList.add(map1);
            orderMoneyList.add(map2);
            map.put("orderInfo",orderList);
            map.put("orderMoneyInfo",orderMoneyList);
            map.put("activityAnalysisInfo",jdbcTemplate.queryForList(sql));
        }else if(5==subType.intValue()){

        }
        map.put("activityInfo",activity);
       /* if(type.intValue()==1){

        }else{
            String sql = " select a.activity_name as activityName,a.status,a.total_money as totalMoney,b.total_count as totalCount,a.sub_type as subType," +
                    " a.start_time as startTime ,a.end_time as endTime,a.type" +
                    "  from  activity a LEFT JOIN  activity_card b" +
                    " on a.id = b.activity_id where 1=1 "+sqlCondition;
        }*/

    }

    private List converterSex(Map sexMap) {
        List sexList = new ArrayList();
        Map mapM = new HashMap(); //男
        mapM.put("name","男性");
        mapM.put("value",sexMap.get("m"));
        Map mapW = new HashMap(); //女
        mapW.put("name","女性");
        mapW.put("value",sexMap.get("w"));
        Map mapT = new HashMap(); //未知
        mapT.put("name","未知");
        mapT.put("value",sexMap.get("t"));
        sexList.add(mapM);sexList.add(mapW);sexList.add(mapT);
        return sexList;
    }

    @ApiOperation("活动概况")
    private void handleActivityBasic(Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql = findPageActivityBasicSql(shopId,paramsQo);
        String fromSql = findPageActivityBasicFromSql(shopId,paramsQo);
        PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
        map.put("activityAnalysisInfo",page);

    }

    @ApiOperation("活动列表")
    private String findPageActivityBasicSql(Integer shopId, DataParamsQo paramsQo) {
        String sql ="select a.id,a.activity_name as activityName,a.start_time as startTime,a.end_time as endTime," +
                " case when a.type = 1 then '现金红包' when a.type = 2 then '礼券红包' when a.type = 3 then '金券红包' end as typeName," +
                " case when a.sub_type = 1 then '首次关注发红包' when a.sub_type = 3 then '购买商品发红包' when a.sub_type = 5 then '卡卷转赠发红包' end as subTypeName," +
                " a.sub_type as subType,case when a.status = 1 then '进行中' when a.status = 2 then '暂停' " +
                " when a.status = 3 then '已结束' end as status " +findPageActivityBasicFromSql(shopId,paramsQo);
        return sql;
    }

    /**
     * 活动概况分页 from语句
     * @param shopId
     * @param paramsQo
     * @return
     */
    private String findPageActivityBasicFromSql(Integer shopId, DataParamsQo paramsQo) {
        String sql = " from activity a " +
                " where 1=1 and a.shop_id= " +shopId+
                " and a.platform=1 and a.sub_type in (1,3)";
        StringBuilder sqlCondition = new StringBuilder();
        int status = Toolkit.parseObjForInt(paramsQo.getActivityStatus());//活动状态
        if(0==status){ // 0 代表1,2,3   1 ：进行中 2：暂停 3：结束
            sqlCondition.append(JdbcUtil.appendIn("a.status","1,2,3"));
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.status",status));
        }
        int type = Toolkit.parseObjForInt(paramsQo.getActivityTypeId());//活动类型
        if(0==type){ // 0 代表1,2,3   1:现金红包 2：卡券红包 3：金券红包
            sqlCondition.append(JdbcUtil.appendIn("a.type","1,2,3"));
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.type",type));
        }
        sqlCondition.append(JdbcUtil.appendLike("a.activity_name",paramsQo.getActivityName()));
        sqlCondition.append(JdbcUtil.appendAnd("a.start_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        sqlCondition.append(JdbcUtil.appendAnd("a.end_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        return sql+sqlCondition+" order by a.create_time desc";
    }
    @ApiOperation("微信昵称解码")
    private void converterNickName(PageItem page) {
        List<Map> list = page.getItems();
        for (Map map:list) {
            map.put("nickName",Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
        }
    }


}
