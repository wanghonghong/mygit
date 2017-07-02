package com.jm.business.service.shop.dataAnalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.WxMenuRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.csv.CsvToolUtil;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>菜单分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/17 10:29
 */
@Service
public class MenuAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CsvToolUtil csvToolUtil;

    @ApiOperation("菜单分析")
    public Map findMenuData(String appid, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        Map map = new HashMap();
        if(1 == tabIndex.intValue()){ //菜单概况
            handleMenuBasic(appid,paramsQo,map);
        }else if(2 == tabIndex.intValue()){ //线性分析
            Integer menuId = paramsQo.getMenuId();
            if(menuId != null){
                if(-1 == menuId){
                    return map;
                }
                handleLine(menuId,paramsQo,map);
            }
        }
        return map;
    }

    @ApiOperation("线性分析")
    private void handleLine(Integer menuId,DataParamsQo paramsQo, Map map) {
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
        map.put("date",dateList);
        // 菜单点击次数sql
        List menuList = findMenuCountSql(flag,menuId,dateList,nDay);
        // 菜单点击人数
        List menuUserList = findMenuCountUserSql(flag,menuId,dateList,nDay);
        //人均点击次数
        List perCapitaList = findPerCapitaSql(flag,menuId,dateList,nDay);
        map.put("menuOnclicks",menuList);
        map.put("menuOnclickUsers",menuUserList);
        map.put("perCapita",perCapitaList);

    }
    @ApiOperation("人均点击次数")
    private List findPerCapitaSql(String flag, Integer menuId, List dateList, int nDay) {
        String sql = " select date_format(a.create_time,'%Y-%m-%d') as date, " +
                " case when ifnull(count(distinct openid),0) = 0 then 0 else FORMAT(ifnull(count(1),0)/count(distinct openid),2) end AS count " +
                " from wx_menu_visit a " +
                " where 1=1 and a.url = (select url from wx_menu a where a.id=" +menuId+")" +
                " and date_format(a.create_time,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.create_time,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.create_time,'%Y-%m-%d') order by date_format(a.create_time,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }

    @ApiOperation("菜单点击次数")
    private List findMenuCountSql(String s, Integer menuId,List dateList, int nDay) {
        String sql = " select date_format(a.create_time,'%Y-%m-%d') as date,ifnull(count(1),0) AS count from wx_menu_visit a " +
                " where 1=1 and a.url = (select url from wx_menu a where a.id=" +menuId+")" +
                " and date_format(a.create_time,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.create_time,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.create_time,'%Y-%m-%d') order by date_format(a.create_time,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }
    @ApiOperation("菜单点击人数")
    private List findMenuCountUserSql(String s, Integer menuId,List dateList, int nDay) {
        String sql = " select date_format(a.create_time,'%Y-%m-%d') as date,ifnull(count(distinct openid),0) AS count from wx_menu_visit a " +
                " where 1=1 and a.url = (select url from wx_menu a where a.id=" +menuId+")" +
                " and date_format(a.create_time,'%Y-%m-%d') <=CURDATE() " ;
        sql +=" and date_format(a.create_time,'%Y-%m-%d')>=date_sub(CURDATE() ,interval "+nDay+" day)";
        sql+= " group by date_format(a.create_time,'%Y-%m-%d') order by date_format(a.create_time,'%Y-%m-%d')" ;
        List<Map<String,Object>> oriList = jdbcUtil.queryList(sql);
        return commonHandle(oriList,dateList);
    }
    @ApiOperation("微信昵称解码")
    private void converterNickName(PageItem page) {
        List<Map> list = page.getItems();
        for (Map map:list) {
            map.put("nickName",Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
        }
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
    @ApiOperation("菜单概况")
    private void handleMenuBasic(String appid, DataParamsQo paramsQo, Map map) {
        String sql = findMenuBasicSql(appid,paramsQo);
        map.put("menuAnalysisInfo",jdbcTemplate.queryForList(sql));
    }

    @ApiOperation("菜单访问统计列表")
    private String findMenuBasicSql(String appid, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        sqlCondition.append(JdbcUtil.appendAnd("appid",appid));
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(create_time) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(create_time,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史
        }
        String sql =" select a.name,a.pName,ifnull(b.count,0) as count , ifnull(b.userCount,0) as userCount," +
                " case when ifnull(b.userCount,0) = 0 then 0 else FORMAT(ifnull(b.count,0)/b.userCount,2) end as perCapita" +
                " FROM" +
                " (" +
                " select a.*,ifnull(b.pName,'无') as pName from " +
                " (select name,parent_id,url from wx_menu where appid='" +appid+ "' and url is not null and type='view') a" +
                " left join (select id,name as pName from wx_menu where appid='" +appid+ "') b" +
                " on a.parent_id = b.id" +
                " ) a" +
                " left join " +
                " (select url,ifnull(count(1),0) as count, ifnull(count(distinct openid),0) as userCount " +
                " from wx_menu_visit where 1= 1" +sqlCondition +
                " group by url)b" +
                " on a.url = b.url order by a.pName";
        return sql;
    }

    public void exportCsv(HttpServletRequest request, HttpServletResponse response, String appid) throws Exception {
        String json = request.getParameter("paramsQo").toString();
        String beginTime = request.getParameter("beginTime").toString();
        String endTime = request.getParameter("endTime").toString();
        ObjectMapper objectMapper=new ObjectMapper();
        DataParamsQo paramsQo =objectMapper.readValue(json, DataParamsQo.class);
        if(!"".equals(beginTime)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date beginDate = format.parse(beginTime);
            paramsQo.setBeginTime(beginDate);
        }
        if(!"".equals(endTime)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date endDate = format.parse(endTime);
            paramsQo.setEndTime(endDate);
        }
        String sql = findMenuBasicSql(appid,paramsQo);
        String title ="";
        String header  ="菜单,父类菜单,菜单点击次数,菜单点击人数,人均点击次数";
        /*if(StringUtil.isNotNull(beginTime)){
            title += beginTime;
        }
        if(StringUtil.isNotNull(endTime)){
            title += "-"+endTime;
        }*/
        title+="菜单分析";
        List<Map<String, Object>> list = jdbcUtil.queryList(sql);
        csvToolUtil.exportCsv(list,header,title,response);
    }
}
