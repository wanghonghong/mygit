package com.jm.business.service.shop.dataAnalysis;

import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>交易分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:41
 */
@Service
public class TradeAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation("交易分析")
    public Map findTradeData(String appid, long shopUid, Integer shopId, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        Map map = new HashMap();
        if(1 == tabIndex.intValue()){ //转化分析
            handleConver(shopId,paramsQo,map);
        }else if(2 == tabIndex.intValue()){ //客户分析
            handleUser(shopUid,shopId,paramsQo,map);
        }else if(3 == tabIndex.intValue()){ //线性分析
            handleLine(shopUid,appid,shopId,paramsQo,map);
        }
        return map;
    }

    @ApiOperation("客户分析")
    private void handleUser(long shopUid, Integer shopId, DataParamsQo paramsQo, Map map) {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        String sql = findTradeCustomerSql(shopUid,shopId,paramsQo); //新老客户
        map.put("customerInfo",jdbcTemplate.queryForMap(sql));
        sql = findTradeOrdersSql(shopId,paramsQo); // 金额区间订单数
        map.put("ordersInfo",jdbcTemplate.queryForMap(sql));
        sql = findTradeLevelSql(shopUid,shopId,paramsQo); //等级分布
        map.put("levelInfo",jdbcTemplate.queryForMap(sql));
        sql = findTradeBuyInfo(shopUid,shopId,paramsQo);//全国地区购买人数
        List buyList = jdbcUtil.queryList(sql);
        buyList.add(map1);
        buyList.add(map2);
        map.put("buyInfo",buyList);
        sql = findTradeVisitInfo(shopUid,shopId,paramsQo);//全国地区访问人数
        List visitList = jdbcUtil.queryList(sql);
        visitList.add(map1);
        visitList.add(map2);
        map.put("visitInfo",visitList);
        sql = findTradeBvInfo(shopUid,shopId,paramsQo);
        map.put("bvInfo",jdbcUtil.queryList(sql));
        /*sql = findUserRoleSql(appid,paramsQo);
        map.put("roleInfo",jdbcTemplate.queryForMap(sql));*/
    }

    @ApiOperation("转化分析")
    private void handleConver(Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql1 = findTradePlaceOrderSql(shopId,paramsQo); //下单(付款和代付款的都算)
        String sql2 = findTradeReturnOrderSql(shopId,paramsQo); //退单
        String sql3 = findTradePayOrderSql(shopId,paramsQo); //付款
        String sql4 = findTradeVisitSql(shopId,paramsQo); //浏览人数
        map.put("placeOrderInfo",jdbcTemplate.queryForMap(sql1));
        map.put("returnOrderInfo",jdbcTemplate.queryForMap(sql2));
        map.put("payOrderInfo",jdbcTemplate.queryForMap(sql3));
        map.put("visitUsers",jdbcTemplate.queryForMap(sql4));
    }

    @ApiOperation("地区分布--浏览和付款")
    private String findTradeBvInfo(long shopUid,Integer shopId, DataParamsQo paramsQo) {
        String sql = "select a.name,a.value as buyCount,b.value as visitCount," +
                " case when ifnull(b.value,0)=0 or ifnull(a.value,0)=0  then '0.00%' else concat(ROUND(a.value/b.value*100,2),'%') end  as percent" +
                " from (" +findTradeBuyInfo(shopUid,shopId,paramsQo)+
                " ) a," +
                " (" +findTradeVisitInfo(shopUid,shopId,paramsQo)+
                " ) b" +
                " where a.name = b.name" +
                " order by a.value desc";
        return sql;
    }
    @ApiOperation("全国访问人数")
    private String findTradeVisitInfo(long shopUid,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",paramsQo.getPlatForm()-1));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.visit_time) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql = "";
        if(1==paramsQo.getPlatForm()){  //wx
            sql =" select b.alias as name,ifnull(a.count,0) as value from " +
                    " (select a.area_code,count(1) as count from" +
                    " (select  a.user_id ,CONCAT(SUBSTR(b.area_code,1,2),'0000') as area_code" +
                    " from (SELECT distinct(a.user_id) as user_id" +
                    " FROM jm_visit a left join product b on a.pid = b.pid" +
                    " where 1=1 and ifnull(a.type,1) =1 " +sqlCondition+
                    " )a left join wx_user b on a.user_id = b.user_id" +
                    " where 1=1 and(b.area_code !=null or b.area_code!=0))a" +
                    " group by a.area_code)a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.area_code = b.area_id";
        }else if(2==paramsQo.getPlatForm()){  //wb
            sql =" select b.alias as name,ifnull(a.count,0) as value from " +
                    " (select a.area_code,count(1) as count from" +
                    " (select  a.user_id ,CONCAT(SUBSTR(b.area_code,1,2),'0000') as area_code" +
                    " from (SELECT distinct(a.user_id) as user_id" +
                    " FROM jm_visit a left join product b on a.pid = b.pid" +
                    " where 1=1 and ifnull(a.type,1) =1 " +sqlCondition+
                    " )a left join " +
                    " (select a.*,b.area_code from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 and a.pid=" +shopUid+
                    " ) b on a.user_id=b.uid" +
                    " where 1=1 and(b.area_code !=null or b.area_code!=0))a" +
                    " group by a.area_code)a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.area_code = b.area_id";
        }
        return sql;
    }
    @ApiOperation("全国购买人数")
    private String findTradeBuyInfo(long shopUid,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.pay_date) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql ="";
        if(1== paramsQo.getPlatForm()){ //wx
            sql = " select b.alias as name,ifnull(a.count,0) as value from " +
                    " (select a.area_code,count(1) as count from" +
                    " (select  a.user_id ,CONCAT(SUBSTR(b.area_code,1,2),'0000') as area_code" +
                    " from " +
                    " (SELECT distinct(a.user_id) as user_id FROM order_info a WHERE 1 = 1" +
                    " AND a. STATUS IN (1, 2, 3, 5)" +sqlCondition+
                    " )a left join wx_user b on a.user_id = b.user_id" +
                    " where 1=1 and(b.area_code !=null or b.area_code!=0))a " +
                    " group by a.area_code)a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.area_code = b.area_id";
        }else if (2==paramsQo.getPlatForm()){
            sql = " select b.alias as name,ifnull(a.count,0) as value from " +
                    " (select a.area_code,count(1) as count from" +
                    " (select  a.user_id ,CONCAT(SUBSTR(b.area_code,1,2),'0000') as area_code" +
                    " from " +
                    " (SELECT distinct(a.user_id) as user_id FROM order_info a WHERE 1 = 1" +
                    " AND a. STATUS IN (1, 2, 3, 5)" +sqlCondition+
                    " )a left join " +
                    " (select a.*,b.area_code from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 and a.pid=" +shopUid+
                    " ) b on a.user_id = b.uid where 1=1 and(b.area_code !=null or b.area_code!=0))a " +
                    " group by a.area_code)a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                    " on a.area_code = b.area_id";
        }

        return sql;
    }

    @ApiOperation("等级分布")
    private String findTradeLevelSql(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.pay_date) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql ="";
        if(1==paramsQo.getPlatForm()){ //wx
            sql = "select ifnull(sum(case when level_id =1 then 1 end),0) as gold," +
                    " ifnull(sum(case when level_id =2 then 1 end),0) as silver," +
                    " ifnull(sum(case when level_id =3 then 1 end),0) as copper, " +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 1 then 1 end),0)/count(1)*100,2),'%') end as goldPercent," +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 2 then 1 end),0)/count(1)*100,2),'%') end as silverPercent," +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 3 then 1 end),0)/count(1)*100,2),'%') end as copperPercent" +
                    " from " +
                    " (SELECT distinct(a.user_id) as user_id FROM order_info a WHERE 1 = 1" +
                    " AND a. STATUS IN (1, 2, 3, 5)"+sqlCondition+
                    " )a left join wx_user b on a.user_id = b.user_id";
        }else if(2==paramsQo.getPlatForm()){ //wb
            sql = "select ifnull(sum(case when level_id =1 then 1 end),0) as gold," +
                    " ifnull(sum(case when level_id =2 then 1 end),0) as silver," +
                    " ifnull(sum(case when level_id =3 then 1 end),0) as copper, " +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 1 then 1 end),0)/count(1)*100,2),'%') end as goldPercent," +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 2 then 1 end),0)/count(1)*100,2),'%') end as silverPercent," +
                    " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when level_id = 3 then 1 end),0)/count(1)*100,2),'%') end as copperPercent" +
                    " from " +
                    " (SELECT distinct(a.user_id) as user_id FROM order_info a WHERE 1 = 1" +
                    " AND a. STATUS IN (1, 2, 3, 5)"+sqlCondition+
                    " )a " +
                    " left join (select a.* from wb_user_rel a where 1=1 and a.pid=" +shopUid+
                    " ) b " +
                    "  on a.user_id = b.uid";
        }
        return sql;
    }

    @ApiOperation("金额区间订单数")
    private String findTradeOrdersSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.platform,0)",paramsQo.getPlatForm()-1));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.pay_date) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "select ifnull(sum(case when money<=1000 then 1 end),0) as count1, " +
                " ifnull(sum(case when money > 1000 and money<=5000 then 1 end),0) as count2, " +
                " ifnull(sum(case when money>5000 and money<=10000 then 1 end),0) as count3," +
                " ifnull(sum(case when money>10000 and money<=20000 then 1 end),0) as count4," +
                " ifnull(sum(case when money>20000 and money<=30000 then 1 end),0) as count5," +
                " ifnull(sum(case when money>30000 and money<=50000 then 1 end),0) as count6," +
                " ifnull(sum(case when money>50000 then 1 end),0) as count7" +
                " from (SELECT ifnull(a.real_price,0) as money FROM order_info a" +
                " WHERE 1=1 AND a. STATUS IN (1, 2, 3, 5)" +sqlCondition+
                " ) a";
        return sql;
    }

    @ApiOperation("新老客户")
    private String findTradeCustomerSql(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        StringBuilder sqlCondition1 = new StringBuilder();
        StringBuilder sqlCondition2 = new StringBuilder();
        StringBuilder sqlCondition3 = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            //sqlCondition1.append(" and date(a.payDate) = curdate()");
            sqlCondition2.append(" and date(b.subscribe_time) = curdate()");
            sqlCondition3.append(" and date(b.subscribe_time) != curdate()");
            sqlCondition.append(" and date(a.pay_date) = curdate()");
        }else {
            //sqlCondition1.append(JdbcUtil.appendAnd("a.payDate",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sqlCondition2.append(JdbcUtil.appendAnd("b.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sqlCondition3.append(JdbcUtil.appendDateOr("b.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String selectSql1 = "select ifnull(sum(case when 1=1" +sqlCondition2+
                " then 1 end ),0) as newCount," +
                " ifnull(sum(case when 1=1 " +sqlCondition2+
                " then a.money end),0) as newMoney," +
                " ifnull(sum(case when  1=1" +sqlCondition3+
                " then 1 end ),0) as oldCount," +
                " ifnull(sum(case when  1=1" +sqlCondition3+
                " then a.money end),0) as oldMoney";
        String selectSql2 = "select 0  as oldCount,0  as oldMoney, ifnull(count(1),0) as newCount,ifnull(sum(a.money),0) as newMoney";
        String fromSql="";
        if(1==paramsQo.getPlatForm()){ //wx
            fromSql =  " from (select a.user_id as userId,a.pay_date as payDate,ifnull(sum(a.real_price),0) as money " +
                    " from order_info a where 1=1 and a.status in(1,2,3,5) "+sqlCondition+
                    " group by a.user_id) a left join wx_user b on a.userId = b.user_id";
        }else if(2==paramsQo.getPlatForm()){ //wb
            fromSql =  " from (select a.user_id as userId,a.pay_date as payDate,ifnull(sum(a.real_price),0) as money " +
                    " from order_info a where 1=1 and a.status in(1,2,3,5) "+sqlCondition+
                    " group by a.user_id) a left join " +
                    " (select a.* from wb_user_rel a where 1=1 and a.pid=" +shopUid+
                    ") b " +
                    " on a.userId = b.uid";
        }

        if("B".equals(flag) ){
            return selectSql1+fromSql;
        }else if(paramsQo.getBeginTime()== null && paramsQo.getEndTime()==null){
            return selectSql2+fromSql;
        }
        return selectSql1+fromSql;
    }

    @ApiOperation("浏览人数")
    private String findTradeVisitSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",paramsQo.getPlatForm()-1));
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.visit_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql = " select count(distinct user_id) as count from  jm_visit a  left join product b" +
                " on a.pid = b.pid where 1=1 and ifnull(a.type,1) =1 "+sqlCondition;
        return sql;
    }


    @ApiOperation("交易分析，下单")
    private String findTradePlaceOrderSql(Integer shopId, DataParamsQo paramsQo) {

        String sql  = "SELECT count(distinct a.user_id) as userCount," +
                    " count(1) AS orderCount,ifnull(sum(a.real_price),0) as money" +
                    " from order_info a where 1=1 and a.status !=4 ";

        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.platform,0)",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.create_date) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.create_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        return sql+sqlCondition;
    }
    @ApiOperation("交易分析，退款")
    private String findTradeReturnOrderSql(Integer shopId, DataParamsQo paramsQo) {

        String  sql ="select count(distinct b.user_id) as userCount," +
                    " count(1) as returnCount, ifnull(sum(a.refund_money),0) as money," +
                    " ifnull(sum(c.count),0) as count from order_refund a left join order_info b" +
                    " on a.order_info_id = b.order_info_id left join order_detail c" +
                    " on a.order_info_id = c.order_info_id where 1=1 ";

        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(b.platform,0)",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("a.refund_status",1));
        if("B".equals(flag)){ //初始化当天数据
            sqlCondition.append(" and date(a.agree_refund_date) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.agree_refund_date", paramsQo.getBeginTime(), paramsQo.getEndTime()));
        }
        return sql+sqlCondition;
    }
    @ApiOperation("交易分析，付款")
    private String findTradePayOrderSql(Integer shopId, DataParamsQo paramsQo) {
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        sqlCondition.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        if("B".equals(flag)){ //初始化本月数据
            sqlCondition.append(" and date(a.pay_date) = curdate()");
        }else {
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_date", paramsQo.getBeginTime(), paramsQo.getEndTime()));
        }
        String  sql = "select *,case when ifnull(sum(a.count),0)=0 then 0 else b.money/a.count end as singleMoney from" +
                    " (SELECT count(DISTINCT a.user_id) as userCount,count(distinct a.order_num) as payCount," +
                    " ifnull(sum(c.count),0) as count " +
                    " FROM order_info a " +
                    " LEFT JOIN order_detail c ON a.order_info_id = c.order_info_id" +
                    " WHERE 1=1" +sqlCondition+
                    ") a,(" +
                    " select ifnull(sum(a.real_price),0) as money" +
                    " FROM order_info a WHERE 1=1 " +sqlCondition+
                    " )b" ;
        return sql;
    }
    @ApiOperation("线性分析")
    private void handleLine(long shopUid, String appid, Integer shopId, DataParamsQo paramsQo, Map map) {
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());//A:10 天 B：30天
        List dateList = new ArrayList<>();
        int nDay = 9;
        if("A".equals(flag)){
            for(int i=nDay;i>=0;i--){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, - i);
                Date day = c.getTime();
                String preDay = sdf.format(day);
                dateList.add(preDay);
            }
        }else if("B".equals(flag)){
            nDay = 27;
            for(int i=27;i>=0;i--){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, - i);
                Date day = c.getTime();
                String preDay = sdf.format(day);
                dateList.add(preDay);
            }
        }
        // 浏览人数sql
        List visiterList = findVisiterSql(paramsQo,shopId,dateList,nDay);
        // 付款人数sql
        List buyUsersList = findBuyUserSql(paramsQo,shopId,dateList,nDay);
        // 付款单数sql
        List buyOrdersList = findBuyOrdersSql(paramsQo,shopId,dateList,nDay);
        // 付款件数sql
        List buyCountList = findBuyCountSql(paramsQo,shopId,dateList,nDay);
        // 退单件数sql
        List backCountList = findBackCountSql(paramsQo,shopId,dateList,nDay);
        // 交易额
        List rMoneyList = findRMoneySql(paramsQo,shopId,dateList,nDay);
        //待付款额
        List rwMoneyList = findRwMoneySql(paramsQo,shopId,dateList,nDay);
        //待退款额
        List wbMoneyList = findWbMoneySql(paramsQo,shopId,dateList,nDay);
        //已退款额
        List bMoney = findBMoneySql(paramsQo,shopId,dateList,nDay);
        map.put("date",dateList);
        map.put("visiters",visiterList);
        map.put("buyUsers",buyUsersList);
        map.put("buyOrders",buyOrdersList);
        map.put("buyCount",buyCountList);
        map.put("backCount",backCountList);
        map.put("rMoney",rMoneyList);
        map.put("rwMoney",rwMoneyList);
        map.put("wbMoney",wbMoneyList);
        map.put("bMoney",bMoney);
    }
    @ApiOperation("线形图交易额")
    private List findRMoneySql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        sqlCondition.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        String sql = "select date_format(a.pay_date,'%Y-%m-%d') as date, ifnull(ROUND(sum(a.real_price)/100,2),0) as count " +
                " FROM order_info a WHERE 1=1 " +sqlCondition+
                " and date_format(a.pay_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.pay_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.pay_date,'%Y-%m-%d') order by date_format(a.pay_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }
    @ApiOperation("线形图待付款额")
    private List findRwMoneySql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        sqlCondition.append(jdbcUtil.appendAnd("a.status","0"));
        String sql = "select date_format(a.create_date,'%Y-%m-%d') as date, ifnull(ROUND(sum(a.real_price)/100,2),0) as count " +
                " FROM order_info a WHERE 1=1 " +sqlCondition+
                " and date_format(a.create_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.create_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.create_date,'%Y-%m-%d') order by date_format(a.create_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }
    @ApiOperation("线形图待退款额")
    private List findWbMoneySql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("b.platform",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("a.refund_status",0));
        String sql ="select date_format(a.agree_refund_date,'%Y-%m-%d') as date," +
                " ifnull(ROUND(sum(a.refund_money),2)/100,0) as count " +
                " from order_refund a left join order_info b on a.order_info_id = b.order_info_id where 1=1"+sqlCondition+
                " and date_format(a.agree_refund_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.agree_refund_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.agree_refund_date,'%Y-%m-%d') order by date_format(a.agree_refund_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }
    @ApiOperation("线形图已退款额")
    private List findBMoneySql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("b.platform",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("a.refund_status",1));
        String sql ="select date_format(a.agree_refund_date,'%Y-%m-%d') as date," +
                " ifnull(ROUND(sum(a.refund_money)/100,2),0) as count " +
                " from order_refund a left join order_info b on a.order_info_id = b.order_info_id where 1=1"+sqlCondition+
                " and date_format(a.agree_refund_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.agree_refund_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.agree_refund_date,'%Y-%m-%d') order by date_format(a.agree_refund_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("线形图退单件数")
    private List findBackCountSql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("b.platform",paramsQo.getPlatForm()-1));
        String sql = " SELECT date_format(a.agree_refund_date,'%Y-%m-%d') as date,ifnull(sum(c.count),0) as count " +
                " FROM order_refund a left join order_info b on a.order_info_id = b.order_info_id left join order_detail c" +
                " on a.order_info_id = c.order_info_id WHERE 1=1 " +
                " and a.refund_status=1 " +sqlCondition+
                " and date_format(a.agree_refund_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.agree_refund_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.agree_refund_date,'%Y-%m-%d') order by date_format(a.agree_refund_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("线形图付款件数")
    private List findBuyCountSql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendIn("a.status","1,2,3,5"));
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        String sql = " SELECT date_format(a.pay_date,'%Y-%m-%d') as date,ifnull(sum(c.count),0) as count " +
                " FROM order_info a LEFT JOIN order_detail c ON a.order_info_id = c.order_info_id WHERE 1=1 " +sqlCondition+
                " and date_format(a.pay_date,'%Y-%m-%d') <=CURDATE() ";
        sql +=" and date_format(a.pay_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.pay_date,'%Y-%m-%d') order by date_format(a.pay_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("线形图付款单数")
    private List findBuyOrdersSql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendIn("a.status","1,2,3,5"));
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        String sql = " SELECT date_format(a.pay_date,'%Y-%m-%d') as date,count(distinct a.order_num) as count " +
                " FROM order_info a  WHERE 1=1 " +sqlCondition+
                " and date_format(a.pay_date,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.pay_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.pay_date,'%Y-%m-%d') order by date_format(a.pay_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("线形图付款人数")
    private List findBuyUserSql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendIn("a.status","1,2,3,5"));
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",paramsQo.getPlatForm()-1));
        String sql = " SELECT date_format(a.pay_date,'%Y-%m-%d') as date,count(DISTINCT a.user_id) AS count " +
                " FROM order_info a  WHERE 1=1 " +sqlCondition+
                " and date_format(a.pay_date,'%Y-%m-%d') <= CURDATE()";
        sql +=" and date_format(a.pay_date,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.pay_date,'%Y-%m-%d') order by date_format(a.pay_date,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("线形图浏览人数")
    private List findVisiterSql(DataParamsQo paramsQo, Integer shopId, List dateList, int nDay) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",paramsQo.getPlatForm()-1));
        String sql = " SELECT date_format(a.visit_time,'%Y-%m-%d') as date," +
                " count(DISTINCT user_id) AS count" +
                " FROM jm_visit a LEFT JOIN product b ON a.pid = b.pid WHERE 1=1 and ifnull(a.type,1) =1" +sqlCondition+
                " and date_format(a.visit_time,'%Y-%m-%d') <=CURDATE() " ;
            sql +=" and date_format(a.visit_time,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
            sql+= " group by date_format(a.visit_time,'%Y-%m-%d') order by date_format(a.visit_time,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("处理线性数据")
    private List commonHandle(List<Map<String, Object>> oriList, List dateList) {
        List list = new ArrayList();
        boolean is = false;
        for (Object obj:dateList) {
            String key = obj.toString();
            for (Object object: oriList) {
                Map visiter = (Map)object;
                String keyName = visiter.get("date").toString();
                if(key.equals(keyName)){
                    String keyVal = visiter.get("count").toString();
                    list.add(keyVal);
                    is = true;
                    break;
                }
            }
            if(!is){
                list.add(0);
            }else{
                is = false;
            }

        }
        return list;
    }
        /*@ApiOperation("客户分析--角色")
    private String findUserRoleSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        StringBuilder sonSqlCondition3 = new StringBuilder();//代理分销新增时间条件
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_buy",1));
        sonSqlCondition1.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        sonSqlCondition2.append(JdbcUtil.appendAnd("a.un_subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        sonSqlCondition3.append(JdbcUtil.appendAnd("b.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        String sql = "select " +
                " IFNULL(count(1),0) as buyCount," +
                " IFNULL(sum(case when a.is_subscribe=0 " +sonSqlCondition2+
                " then 1 end ),0) as offCount ," +
                " IFNULL(sum(case when a.is_subscribe=1 " +sonSqlCondition1 +
                " then 1 end ),0) as netAdd," +
                " IFNULL(SUM(case when IFNULL(b.agent_role,0)>4 " +sonSqlCondition3+
                " then 1 end ),0) as distriCount," +
                " IFNULL(SUM(case when IFNULL(b.agent_role,0)>0 and IFNULL(b.agent_role,0)<=4 " +sonSqlCondition3+
                " then 1 end ),0) as agentCount," +
                " IFNULL(sum(case when b.agent_role = 8 " +sonSqlCondition3+
                " then 1 end),0) as d_d," +
                " IFNULL(sum(case when b.agent_role = 5 " +sonSqlCondition3+
                " then 1 end),0) as d_a," +
                " IFNULL(sum(case when b.agent_role = 6 " +sonSqlCondition3+
                " then 1 end),0) as d_b," +
                " IFNULL(sum(case when b.agent_role = 7 " +sonSqlCondition3+
                " then 1 end),0) as d_c," +
                " IFNULL(sum(case when b.agent_role = 1 " +sonSqlCondition3+
                " then 1 end),0) as a_a," +
                " IFNULL(sum(case when b.agent_role = 2 " +sonSqlCondition3+
                " then 1 end),0) as a_b," +
                " IFNULL(sum(case when b.agent_role = 3 " +sonSqlCondition3+
                " then 1 end),0) as a_c," +
                " IFNULL(sum(case when b.agent_role = 4 " +sonSqlCondition3+
                " then 1 end),0) as a_d  FROM wx_user a left join shop_user b" +
                " on a.shop_user_id = b.id WHERE 1=1" +
                sqlCondition ;
        return sql;
    }*/
}
