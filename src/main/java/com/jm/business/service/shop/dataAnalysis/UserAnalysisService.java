package com.jm.business.service.shop.dataAnalysis;

import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>用户分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:41
 */
@Service
public class UserAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation("用户分析")
    public Map findUserData(Integer shopId, String appid, Long shopUid, DataParamsQo paramsQo) throws Exception {
        Integer tabIndex = paramsQo.getTabIndex();
        String sql ="";
        Map map = new HashMap();
        int platForm = paramsQo.getPlatForm();
        if(1 == tabIndex.intValue()){ //日常增减
            appid = appid == null ? "0" : appid;
            shopUid = shopUid == null ? 0L : shopUid;
            List userBaseList = new ArrayList();
            if(1 == platForm){ //微信
                sql = findUserBaseSql(appid,paramsQo);
                List wx = jdbcUtil.queryList(sql);
                userBaseList.add(wx);
            }else if( 2== platForm){
                sql = findUserBaseSqlWb(shopUid,paramsQo);
                List wb = jdbcUtil.queryList(sql);
                userBaseList.add(wb);
            }else{
                sql = findUserBaseSql(appid,paramsQo);
                List wx = jdbcUtil.queryList(sql);
                sql = findUserBaseSqlWb(shopUid,paramsQo);
                List wb = jdbcUtil.queryList(sql);
                userBaseList.add(wx);
                userBaseList.add(wb);
            }
            map.put("userBaseInfo",userBaseList);
        } else if(2 == tabIndex.intValue()){ //分类资料
            if(1 == platForm){ //微信
                handleClassify(appid,paramsQo,map);
            }else if(2 == platForm){ //微博
                handleClassifyWb(shopUid,paramsQo,map);
            }
        } else if(3 == tabIndex.intValue()){ //访问行为
            if(1 == platForm){ //微信
                handleVisitAction(shopId,appid,paramsQo,map);
            }else if(2 == platForm){ //微博
                handleVisitActionWb(shopId,shopUid,paramsQo,map);
            }
        }
        return map;
    }
    @ApiOperation("分类资料-微博")
    private void handleClassifyWb(Long shopUid, DataParamsQo paramsQo, Map map) {
        String sql ="";
        sql = findUserSexSqlWb(shopUid,paramsQo);
        Map sexMap = jdbcTemplate.queryForMap(sql);
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
        map.put("userSexInfo",sexList);
        sql = findUserLevelSqlWb(shopUid,paramsQo);
        map.put("levelInfo",jdbcTemplate.queryForMap(sql));
        sql = findUserFunsSqlWb(shopUid,paramsQo);
        List list = jdbcUtil.queryList(sql);
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        list.add(map1);
        list.add(map2);
        map.put("funsInfo",list);
        sql = findUserRoleSqlWb(shopUid,paramsQo);
        map.put("roleInfo",jdbcTemplate.queryForMap(sql));

    }
    @ApiOperation("访问行为")
    private void handleVisitAction(Integer shopId, String appid, DataParamsQo paramsQo, Map map) {
        String sql ="";
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        sql = findVistersSql(shopId,appid,paramsQo);
        List list = jdbcUtil.queryList(sql);
        list.add(map1);
        list.add(map2);
        map.put("visitersInfo",list);
        List initVisisterInfo = new ArrayList();
        for (Object obj: list) {
            Map visiter = (Map)obj;
            String areaName = visiter.get("name").toString();
            //'福建','山东','浙江','江苏','重庆','广东'
            if("福建".equals(areaName) || "山东".equals(areaName) || "浙江".equals(areaName) || "江苏".equals(areaName) ||"重庆".equals(areaName)
                    || "广东".equals(areaName)){
                initVisisterInfo.add(obj);
            }
        }
        map.put("initVisitersInfo",initVisisterInfo);

    }

    @ApiOperation("访问行为-(微博)")
    private void handleVisitActionWb(Integer shopId, Long shopUid, DataParamsQo paramsQo, Map map) {
        String sql ="";
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        sql = findVistersSqlWb(shopId,shopUid,paramsQo);
        List list = jdbcUtil.queryList(sql);
        list.add(map1);
        list.add(map2);
        map.put("visitersInfo",list);
        List initVisisterInfo = new ArrayList();
        for (Object obj: list) {
            Map visiter = (Map)obj;
            String areaName = visiter.get("name").toString();
            //'福建','山东','浙江','江苏','重庆','广东'
            if("福建".equals(areaName) || "山东".equals(areaName) || "浙江".equals(areaName) || "江苏".equals(areaName) ||"重庆".equals(areaName)
                    || "广东".equals(areaName)){
                initVisisterInfo.add(obj);
            }
        }
        map.put("initVisitersInfo",initVisisterInfo);

    }

    @ApiOperation("分类资料")
    private void handleClassify(String appid, DataParamsQo paramsQo, Map map) {
        String sql ="";
        sql = findUserSexSql(appid,paramsQo);
        Map sexMap = jdbcTemplate.queryForMap(sql);
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
        map.put("userSexInfo",sexList);
        sql = findUserLevelSql(appid,paramsQo);
        map.put("levelInfo",jdbcTemplate.queryForMap(sql));
        sql = findUserFunsSql(appid,paramsQo);
        List list = jdbcUtil.queryList(sql);
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","台湾");
        map1.put("value","0");
        map2.put("name","南海诸岛");
        map2.put("value","0");
        list.add(map1);
        list.add(map2);
        map.put("funsInfo",list);
        sql = findUserRoleSql(appid,paramsQo);
        map.put("roleInfo",jdbcTemplate.queryForMap(sql));

    }

    @ApiOperation("分类资料--角色")
    private String findUserRoleSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        StringBuilder sonSqlCondition3 = new StringBuilder();//代理分销新增时间条件
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sonSqlCondition1.append(" and date(a.subscribe_time) = curdate()");
            sonSqlCondition2.append(" and date(a.un_subscribe_time) = curdate()");
            sonSqlCondition3.append(" and date(b.create_time) = curdate()");
        }else {
            sonSqlCondition1.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition2.append(JdbcUtil.appendAnd("a.un_subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition3.append(JdbcUtil.appendAnd("b.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "select " +
                " IFNULL(count(1),0) as count," +
                " IFNULL(sum(case when 1=1 " +sonSqlCondition1 +
                " then 1 end ),0) as addCount," +
                " IFNULL(sum(case when a.is_subscribe=0 " +sonSqlCondition2+
                " then 1 end ),0) as offCount ," +
                " IFNULL(sum(case when a.is_subscribe=1 " +sonSqlCondition1 +
                " then 1 end ),0) as netAdd," +
                " IFNULL(SUM(case when a.is_buy =1 " +sonSqlCondition1 +
                " then 1 end ),0) as buyCount," +
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
    }

    @ApiOperation("分类资料--角色(微博)")
    private String findUserRoleSqlWb(Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        StringBuilder sonSqlCondition3 = new StringBuilder();//代理分销新增时间条件
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sonSqlCondition1.append(" and date(a.subscribe_time) = curdate()");
            sonSqlCondition2.append(" and date(a.un_subscribe_time) = curdate()");
            sonSqlCondition3.append(" and date(b.create_time) = curdate()");
        }else {
            sonSqlCondition1.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition2.append(JdbcUtil.appendAnd("a.un_subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition3.append(JdbcUtil.appendAnd("b.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "select " +
                " IFNULL(count(1),0) as count," +
                " IFNULL(sum(case when 1=1 " +sonSqlCondition1 +
                " then 1 end ),0) as addCount," +
                " IFNULL(sum(case when a.is_subscribe=0 " +sonSqlCondition2+
                " then 1 end ),0) as offCount ," +
                " IFNULL(sum(case when a.is_subscribe=1 " +sonSqlCondition1 +
                " then 1 end ),0) as netAdd," +
                " IFNULL(SUM(case when a.is_buy =1 " +sonSqlCondition1 +
                " then 1 end ),0) as buyCount," +
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
                " then 1 end),0) as a_d  FROM wb_user_rel a " +
                " left join  wb_user c on a.uid = c.id " +
                " left join shop_user b on a.shop_user_id = b.id WHERE 1=1" +
                sqlCondition ;
        return sql;
    }
    @ApiOperation("分类资料--全国粉丝数")
    private String findUserFunsSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql ="select a.alias as name,IFNULL(a.count,0) as value from " +
                " (select b.alias ,a.count from " +
                " (select a.area_code as areaCode,count(1) as count" +
                " FROM" +
                " (select a.user_id,a.appid,a.is_subscribe,a.un_subscribe_time,a.subscribe_time," +
                " CONCAT(SUBSTR(a.area_code,1,2),'0000') as area_code," +
                " b.area_name from wx_user a left join wx_area b" +
                " on a.area_code=b.area_id where 1=1 and a.area_code !=0 " +sqlCondition+
                " ) a group by a.area_code)a right join (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                " on a.areaCode = b.area_id)a order by count desc";
        return sql;
    }

    @ApiOperation("分类资料--全国粉丝数(微博)")
    private String findUserFunsSqlWb(Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql ="select a.alias as name,IFNULL(a.count,0) as value from " +
                " (select b.alias ,a.count from " +
                " (select a.area_code as areaCode,count(1) as count" +
                " FROM" +
                " (select a.id as user_id,a.pid,a.is_subscribe,a.un_subscribe_time,a.subscribe_time," +
                " CONCAT(SUBSTR(c.area_code,1,2),'0000') as area_code," +
                " b.area_name from wb_user_rel a left join  wb_user c on a.uid = c.id left join wx_area b" +
                " on c.area_code=b.area_id where 1=1  and c.area_code !=0 " +sqlCondition+
                " ) a group by a.area_code)a right join (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                " on a.areaCode = b.area_id)a order by count desc";
        return sql;
    }

    @ApiOperation("分类资料--等级")
    private String findUserLevelSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "SELECT ifnull(sum(case when a.level_id = 1 then 1 end),0) as gold," +
                " ifnull(sum(case when a.level_id = 2 then 1 end),0) as silver," +
                " ifnull(sum(case when a.level_id = 3 then 1 end),0) as copper," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 1 then 1 end),0)/count(1)*100,2),'%') end as goldPercent," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 2 then 1 end),0)/count(1)*100,2),'%') end as silverPercent," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 3 then 1 end),0)/count(1)*100,2),'%') end as copperPercent" +
                " from wx_user a where 1=1 "+sqlCondition;
        return sql;
    }

    @ApiOperation("分类资料--等级(微博)")
    private String findUserLevelSqlWb(Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "SELECT ifnull(sum(case when a.level_id = 1 then 1 end),0) as gold," +
                " ifnull(sum(case when a.level_id = 2 then 1 end),0) as silver," +
                " ifnull(sum(case when a.level_id = 3 then 1 end),0) as copper," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 1 then 1 end),0)/count(1)*100,2),'%') end as goldPercent," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 2 then 1 end),0)/count(1)*100,2),'%') end as silverPercent," +
                " case when ifnull(count(1),0)=0 then '0%' else concat(ROUND(ifnull(sum(case when a.level_id = 3 then 1 end),0)/count(1)*100,2),'%') end as copperPercent" +
                " from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 "+sqlCondition;
        return sql;
    }

    @ApiOperation("分类资料--性别")
    private String findUserSexSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "select  " +
                " ifnull(sum(case WHEN a.sex = 1 THEN 1 end),0) as m," +
                " ifnull(sum(case WHEN a.sex = 2 THEN 1 end),0) as w," +
                " ifnull(sum(case when a.sex = 0 THEN 1 end),0) as t" +
                " from wx_user a where 1=1 " +sqlCondition;
        return sql;
    }

    @ApiOperation("分类资料--性别(微博)")
    private String findUserSexSqlWb(Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        sqlCondition.append(JdbcUtil.appendAnd("a.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.subscribe_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = "select  " +
                " ifnull(sum(case WHEN b.sex = 1 THEN 1 end),0) as m," +
                " ifnull(sum(case WHEN b.sex = 2 THEN 1 end),0) as w," +
                " ifnull(sum(case when b.sex = 0 THEN 1 end),0) as t" +
                " from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 " +sqlCondition;
        return sql;
    }

    @ApiOperation("用户分析--日常增减")
    private String findUserBaseSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        StringBuilder sonSqlCondition3 = new StringBuilder();//代理分销新增时间条件
        String flag = paramsQo.getFlag();
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        if("A".equals(flag)){ //查询
            sonSqlCondition1.append(JdbcUtil.appendAnd("m.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition2.append(JdbcUtil.appendAnd("m.un_subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition3.append(JdbcUtil.appendAnd("m.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sonSqlCondition1.append(" and date(m.subscribe_time) = curdate()");
            sonSqlCondition2.append(" and date(m.un_subscribe_time) = curdate()");
            sonSqlCondition3.append(" and TO_DAYS(m.create_time) = TO_DAYS(NOW())");
        }else if("C".equals(flag)){ //这周
            sonSqlCondition1.append(" and YEARWEEK(date_format(m.subscribe_time,'%Y-%m-%d')) = YEARWEEK(now())");
            sonSqlCondition2.append(" and YEARWEEK(date_format(m.un_subscribe_time,'%Y-%m-%d')) = YEARWEEK(now())");
            sonSqlCondition3.append(" and YEARWEEK(date_format(m.create_time,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sonSqlCondition1.append(" and date_format(m.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sonSqlCondition2.append(" and date_format(m.un_subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sonSqlCondition3.append(" and date_format(m.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        String sqlList = "select '微信'as name," +
                " IFNULL(sum(case when 1=1 " + sonSqlCondition1 +
                " then 1 end ),0) as count," +
                " IFNULL(sum(case when m.is_subscribe=0 " +sonSqlCondition2+
                " then 1 end ),0) as offCount ," +
                " IFNULL(sum(case when m.is_subscribe=1 " +sonSqlCondition1 +
                " then 1 end ),0) as netAdd," +
                " IFNULL(SUM(case when m.is_buy=1 " +sonSqlCondition1 +
                " then 1 end ),0) as buyCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)=8 " +sonSqlCondition3+
                " then 1 end ),0) as shareCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)>4 and IFNULL(m.agent_role,0) <=7 " +sonSqlCondition3+
                " then 1 end ),0) as distriCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)>0 and IFNULL(m.agent_role,0)<=4 " +sonSqlCondition3+
                " then 1 end ),0) as agentCount" +
                " from (SELECT a.is_subscribe,a.is_buy,a.subscribe_time,a.un_subscribe_time,a.appid," +
                " b.create_time,b.agent_role FROM wx_user a LEFT JOIN " +
                " (select * from shop_user where 1=1 "+
                " )b ON a.shop_user_id = b.id where 1=1 " +sqlCondition+ ") m" ;
        return sqlList;
    }

    @ApiOperation("wb用户分析--日常增减")
    private String findUserBaseSqlWb(Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();//appid条件
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        StringBuilder sonSqlCondition3 = new StringBuilder();//代理分销新增时间条件
        String flag = paramsQo.getFlag();
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        if("A".equals(flag)){ //查询
            sonSqlCondition1.append(JdbcUtil.appendAnd("m.subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition2.append(JdbcUtil.appendAnd("m.un_subscribe_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
            sonSqlCondition3.append(JdbcUtil.appendAnd("m.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sonSqlCondition1.append(" and date(m.subscribe_time) = curdate()");
            sonSqlCondition2.append(" and date(m.un_subscribe_time) = curdate()");
            sonSqlCondition3.append(" and TO_DAYS(m.create_time) = TO_DAYS(NOW())");
        }else if("C".equals(flag)){ //这周
            sonSqlCondition1.append(" and YEARWEEK(date_format(m.subscribe_time,'%Y-%m-%d')) = YEARWEEK(now())");
            sonSqlCondition2.append(" and YEARWEEK(date_format(m.un_subscribe_time,'%Y-%m-%d')) = YEARWEEK(now())");
            sonSqlCondition3.append(" and YEARWEEK(date_format(m.create_time,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sonSqlCondition1.append(" and date_format(m.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sonSqlCondition2.append(" and date_format(m.un_subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sonSqlCondition3.append(" and date_format(m.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        String sqlList = "select '微博'as name," +
                " IFNULL(sum(case when 1=1 " + sonSqlCondition1 +
                " then 1 end ),0) as count," +
                " IFNULL(sum(case when m.is_subscribe=0 " +sonSqlCondition2+
                " then 1 end ),0) as offCount ," +
                " IFNULL(sum(case when m.is_subscribe=1 " +sonSqlCondition1 +
                " then 1 end ),0) as netAdd," +
                " IFNULL(SUM(case when m.is_buy=1 " +sonSqlCondition1 +
                " then 1 end ),0) as buyCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)=8 " +sonSqlCondition3+
                " then 1 end ),0) as shareCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)>4 and IFNULL(m.agent_role,0) <=7 " +sonSqlCondition3+
                " then 1 end ),0) as distriCount," +
                " IFNULL(SUM(case when IFNULL(m.agent_role,0)>0 and IFNULL(m.agent_role,0)<=4 " +sonSqlCondition3+
                " then 1 end ),0) as agentCount" +
                " from (SELECT a.is_subscribe,a.is_buy,a.subscribe_time,a.un_subscribe_time," +
                " b.create_time,b.agent_role FROM wb_user_rel a LEFT JOIN " +
                " (select * from shop_user where 1=1 "+
                " )b ON a.shop_user_id = b.id where 1=1 " +sqlCondition+ ") m" ;
        return sqlList;
    }
    @ApiOperation("访问行为--访问过店铺的粉丝数")
    private String findVistersSql(Integer shopId, String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("c.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.visit_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }
        String sql = " select b.alias as name ,ifnull(a.count,0) as value from " +
                " (select count(1) as count, a.areaCode from " +
                " (select distinct a.user_id as userId,CONCAT(SUBSTR(c.area_code,1,2),'0000') as areaCode from  jm_visit a  left join product b " +
                " on a.pid = b.pid  left join wx_user c on a.user_id=c.user_id" +
                " where 1=1 and ifnull(a.plat_form,0) = 0 " +sqlCondition+
                " )a group by a.areaCode) a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                " on a.areaCode = b.area_id";
        return sql;
    }

    @ApiOperation("访问行为--访问过店铺的粉丝数")
    private String findVistersSqlWb(Integer shopId, Long shopUid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("c.is_subscribe",1));
        String flag = paramsQo.getFlag();
        if("B".equals(flag)){
            sqlCondition.append(" and date(a.visit_time) = curdate()");
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }

        String sql = " select b.alias as name ,ifnull(a.count,0) as value from " +
                " (select count(1) as count, a.areaCode from " +
                " (select distinct a.user_id as userId,CONCAT(SUBSTR(c.area_code,1,2),'0000') as areaCode from  jm_visit a  left join product b " +
                " on a.pid = b.pid  left join " +
                " (select a.*,b.area_code from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 and a.pid=" +shopUid+
                " ) c on a.user_id=c.uid" +
                " where 1=1 and ifnull(a.plat_form,0) = 1 " +sqlCondition+
                " )a group by a.areaCode) a RIGHT JOIN (select area_id,alias from wx_area where substr(area_id,3)='0000')b" +
                " on a.areaCode = b.area_id";
        return sql;
    }

    public void callWxUserProc(String value){
        jdbcTemplate.execute(value);
    }
}
