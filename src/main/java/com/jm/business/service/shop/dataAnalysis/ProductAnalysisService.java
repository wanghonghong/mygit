package com.jm.business.service.shop.dataAnalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.util.ImgUtil;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>商品分析</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:41
 */
@Service
public class ProductAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CsvToolUtil csvToolUtil;

    @ApiOperation("商品分析")
    public Map findGoodData(Integer shopId, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        int platForm = paramsQo.getPlatForm();
        Map map = new HashMap();
        if(1 == tabIndex.intValue()){ //基础数据
            handleGoodBasic(shopId,paramsQo,map);
        }else if(2 == tabIndex.intValue()){ //效果分析
            handleEffect(shopId,paramsQo,map);
        }
        return map;
    }
    @ApiOperation("效果分析")
    private void handleEffect(Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql = findPageProductSql(shopId,paramsQo);
        String fromSql = findPageProductFromSql(shopId,paramsQo);
        PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
        List list = page.getItems();
        List newList = new ArrayList();
        for (Object obj : list) {
            Map product = (Map)obj;
            if(product.get("imgUrl")!= null){
                String imgUrl = ImgUtil.appendUrl(product.get("imgUrl").toString());
                product.put("imgUrl",imgUrl);
            }
            newList.add(product);
        }
        page.setItems(newList);
        map.put("productsAnalysisInfo",page);
    }

    @ApiOperation("基础信息")
    private void handleGoodBasic(Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql1 = findGoodBaseSql(shopId,paramsQo);//商品信息
        String sql2 = findGoodVisitSql(shopId,paramsQo);//访问数据
        map.put("productInfo",jdbcTemplate.queryForMap(sql1));
        map.put("pVisitInfo",jdbcTemplate.queryForMap(sql2));
    }

    @ApiOperation("分页商品效果分析")
    private String findPageProductSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        String sql  = "select a.pid ,a.specName,a.name,a.price,a.imgUrl,a.visiters,a.visitCount,a.cusCount,a.orderCount," +
                " case when a.visiters=0 then '0%' else concat(ROUND(a.cusCount/a.visiters*100,2),'%') end as percent," +
                " a.buyCount,a.pstatus "+findPageProductFromSql(shopId,paramsQo);
        return sql;
    }
    private String findPageProductFromSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        // sqlCondition.append(JdbcUtil.appendAnd("c.group_id",paramsQo.getGroupId()));
        sqlCondition.append(JdbcUtil.appendLike("m.name",paramsQo.getProductName()));
        int status = Toolkit.parseObjForInt(paramsQo.getStatus());
        if(1==status){ // 1在售 （在售商品，商品规格使用中）
            sqlCondition.append(JdbcUtil.appendAnd("m.status",0));
            sqlCondition.append(JdbcUtil.appendAnd("ifnull(n.STATUS,0)",0));
        }else if(2==status){ //  2停售 （在售商品，但是该规格已停用）
            sqlCondition.append(JdbcUtil.appendAnd("m.status",0));
            sqlCondition.append(JdbcUtil.appendAnd("n.status",1));
        }else if(3==status){ //  3下架 （下架商品，就不管规格停不停用吧，只要全部显示出来就好）
            sqlCondition.append(JdbcUtil.appendAnd("m.status",1));
        }else if(4==status){ // 4售完下架 （售完下架的商品，也不管规格停不停用，全部显示）
            sqlCondition.append(JdbcUtil.appendAnd("m.status",2));
        }else if(5==status){ //  5已删除 （已删除的，也是不管规格停不停用）
            sqlCondition.append(JdbcUtil.appendAnd("m.status",9));
        }
        String fromSql = " from ( select a.pid ,a.createTime,a.specName,a.product_spec_id," +
                " a.name,a.price,a.imgUrl,ifnull(c.visiters,0) as visiters,ifnull(c.visitCount,0) as visitCount," +
                " ifnull(count(distinct b.payUserId),0) as cusCount,"+
                " ifnull(count(distinct b.order_info_id),0) as orderCount," +
                " ifnull(sum(b.count),0) as buyCount,case when a.status = 0 and ifnull(a.specStatus,0) = 0  then '在售'" +
                " when a.status = 0 and ifnull(a.specStatus,0) = 1 then '停售'" +
                " when a.status = 1 then '下架'  when a.status = 2 then '售完下架'" +
                " when a.status = 9 then '已删除' end as pstatus from (" +
                " SELECT m.pid,CONCAT_ws('/',n.spec_value_one,n.spec_value_two,n.spec_value_three) as specName," +
                " ifnull(n.product_spec_id,-1) as product_spec_id,m.NAME,m.pic_square AS imgUrl,m.price,n.status as specStatus,m.status,m.create_time as createTime " +
                " FROM product m LEFT JOIN product_spec n ON m.pid = n.pid " +
                " WHERE 1=1 and m.shop_id = " +shopId+
                sqlCondition +
                " )a left join " +
                " ("+findOrderInfoSql(shopId,paramsQo)+
                " )b on a.pid = b.pid and a.product_spec_id = b.spec_id " +
                " left join ("+findJmVisitSql(shopId,paramsQo)+
                " ) c on a.pid = c.pid where 1=1 " +
                " group by a.pid,a.product_spec_id )a ";

         if(StringUtil.isNotNull(paramsQo.getSort())){
                fromSql +=" order by a."+paramsQo.getSort()+" desc";
         }
        return fromSql;
    }
    private String findOrderInfoSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(b.platform,0)",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("b.pay_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        String sql = "";
        if(2==paramsQo.getPlatForm()){ // 微博
            sql = "select a.*,ifnull(a.product_spec_id,-1) as spec_id,b.pay_date,b.uid as payUserId from order_detail a " +
                    " left JOIN order_info b on a.order_info_id = b.order_info_id" +
                    " left join product_spec c on a.product_spec_id = c.product_spec_id" +
                    " where 1=1 and b.status in(1,2,3,5)"+sqlCondition;
        }else if(1==paramsQo.getPlatForm()){
            sql = "select a.*,ifnull(a.product_spec_id,-1) as spec_id,b.pay_date,b.user_id as payUserId from order_detail a left " +
                    " JOIN order_info b on a.order_info_id = b.order_info_id" +
                    " left join product_spec c on a.product_spec_id = c.product_spec_id" +
                    " where 1=1 and b.status in(1,2,3,5)"+sqlCondition;
        }
        return sql;
    }
    private String findJmVisitSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",paramsQo.getPlatForm()-1));
        sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        String sql = "select  a.pid ,count(DISTINCT a.user_id) as visiters,count(a.pid) as visitCount" +
                " from jm_visit a left join product b on a.pid = b.pid " +
                " where 1=1 and ifnull(a.type,1) =1 "+sqlCondition+
                " group by a.pid  ";
        return sql;
    }
    @ApiOperation("商品分析，基础数据，访问数据")
    private String findGoodVisitSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)", paramsQo.getPlatForm() -1));
        sqlCondition.append(JdbcUtil.appendAnd("a.visit_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        String sql =" select count(distinct a.pid) as pcount," +
                " count(distinct a.user_id ) as ucount,count(1) as pv" +
                " from (select a.*  from  jm_visit a  left join product b" +
                " on a.pid = b.pid where 1=1 and ifnull(a.type,1) =1 "+sqlCondition+")a";
        return sql;
    }

    @ApiOperation("商品分析，基础数据，商品信息")
    private String findGoodBaseSql(Integer shopId, DataParamsQo paramsQo) {
        String sqlList = "SELECT ifnull(sum(case when a.status=0 then 1 end),0) as up," +
                " ifnull(SUM(case when a.status=1 or a.status=2 then 1 end ),0) as down," +
                " ifnull(SUM(case when a.status!=9 then 1 end ),0) as count from product a where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.create_time",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        return sqlList+sqlCondition;
    }
    @ApiOperation("商品分析导出")
    public void exportCsv(HttpServletRequest request, HttpServletResponse response, Integer shopId)  throws Exception {
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
        String sql  = "select a.name,case when a.specName ='' then '无' else a.specName end,a.price,a.visiters,a.visitCount,a.cusCount,a.orderCount," +
                " case when a.visiters=0 then '0%' else concat(ROUND(a.cusCount/a.visiters*100,2),'%') end as percent," +
                " a.buyCount,a.pstatus "+findPageProductFromSql(shopId,paramsQo);
        String header ="商品信息,规格,单价(分),浏览人数,浏览人次,销售人次,销售单次,单品转化率,销售数量,状态";
        String title = "商品分析";
        List<Map<String, Object>> list = jdbcUtil.queryList(sql);
        csvToolUtil.exportCsv(list,header,title,response);

    }
}
