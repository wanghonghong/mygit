package com.jm.business.service.shop.dataAnalysis;

import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>经营概况</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:40
 */
@Service
public class ManagerAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @ApiOperation("经营概况")
    public Map findManagerData(String appid, Long shopUid, Integer shopId, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        int platForm = paramsQo.getPlatForm();
        appid = appid == null ? "0" : appid;
        shopUid = shopUid == null ? 0L : shopUid;
        Map map = new HashMap();
        String sql1="",sql2="",sql3="",sql4="";
        if(1 == tabIndex.intValue()){ //今日数据
            if(1==platForm){  //微信
                sql1 = findMorderSql(appid,shopId,0,paramsQo); //订单信息
                sql2 = findMfunsSql(appid,shopId,paramsQo); //粉丝信息
                sql3 = findMproductSql(0,shopId,paramsQo); //商品信息
                sql4 = findMrpSqlT(0,shopId,paramsQo); //交易信息
            }else if(2==platForm){ //微博
                sql1 = findMorderSql(appid,shopId,1,paramsQo); //订单信息
                sql2 = findMfunsSqlWb(shopUid,shopId,paramsQo); //粉丝信息
                sql3 = findMproductSql(1,shopId,paramsQo); //商品信息
                sql4 = findMrpSqlWbT(1,shopId,paramsQo); //交易信息
            }
            map.put("orderInfo",jdbcTemplate.queryForMap(sql1));
            map.put("funsInfo",jdbcTemplate.queryForMap(sql2));
            map.put("productInfo",jdbcTemplate.queryForMap(sql3));
            map.put("rpInfo",jdbcTemplate.queryForMap(sql4));
        }else if(2 == tabIndex.intValue() || 3 == tabIndex.intValue()){//昨日数据 or 本月数据
            if(1==platForm){ //微信
                sql2 = findMorderSqlY(appid,shopId,0,paramsQo); //订单信息 ，发货信息
                sql3 = findMfunsSqlY(appid,shopId,paramsQo); //粉丝信息
                sql4 = findMproductSqlY(0,shopId,paramsQo); //商品信息
                sql1 = findMrpSqlY(0,shopId,paramsQo); //交易信息
            }else if(2==platForm){ //微博
                sql2 = findMorderSqlY(appid,shopId,1,paramsQo); //订单信息 ，发货信息
                sql3 = findMfunsSqlWbY(shopUid,shopId,paramsQo); //粉丝信息
                sql4 = findMproductSqlY(1,shopId,paramsQo); //商品信息
                sql1 = findMrpSqlWbY(1,shopId,paramsQo); //交易信息
            }
            map.put("rpInfo",jdbcTemplate.queryForMap(sql1));
            map.put("orderInfo",jdbcTemplate.queryForMap(sql2));
            map.put("funsInfo",jdbcTemplate.queryForMap(sql3));
            map.put("productInfo",jdbcTemplate.queryForMap(sql4));
        }
        return map;
    }
    @ApiOperation("经营概况-今日数据-交易信息")
    private String findMrpSqlT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        String sql1 = findMrSqlT(platForm,shopId,paramsQo);
        String sql2 = findMpSqlT(platForm,shopId,paramsQo);
        String sql = "select * from ("+sql1+") r," +
                "("+sql2+") p";
        return sql;
    }

    @ApiOperation("经营概况-今日数据-交易信息-wb")
    private String findMrpSqlWbT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        String sql1 = findMrSqlWbT(platForm,shopId,paramsQo);
        String sql2 = findMpSqlWbT(platForm,shopId,paramsQo);
        String sql = "select * from ("+sql1+") r," +
                "("+sql2+") p";
        return sql;
    }

    @ApiOperation("经营概况-今日数据-交易信息-收入")
    private String findMrSqlT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        sqlConditionO.append(" and date(a.pay_date) = curdate()");//今天

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));
        sqlConditionCzo.append(" and date(a.pay_date) = curdate()");//今天

        StringBuilder sqlConditionRed = new StringBuilder(); //活动红包退回
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));
        sqlConditionRed.append(" and date(c.refund_time) = curdate()");//今天

        StringBuilder sqlConditionYJRed = new StringBuilder(); //佣金退回红包
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.plat_form",platForm));
        sqlConditionYJRed.append(jdbcUtil.appendIn("c.status","6"));
        sqlConditionYJRed.append(" and date(c.refund_time) = curdate()");//今天

        StringBuilder sqlConditionTip = new StringBuilder(); //打赏订单
        sqlConditionTip.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionTip.append(JdbcUtil.appendAnd("a.plat_form",platForm));
        sqlConditionTip.append(" and date(b.pay_date) = curdate()");//今天

        String sql = "select sum(a.yesterday) as revenue from "+
                " (SELECT ifnull(sum("+
                "  ifnull(a.real_price,0)),0) as  yesterday " +
                "  from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " SELECT ifnull(sum("+
                " ifnull(a.money,0)),0) AS yesterday" +
                " FROM recharge_order a WHERE 1=1 " +sqlConditionCzo+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday  from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday" +
                " from brokerage_record b left join wx_red_record c" +
                " on b.red_pay_id = c.red_pay_id where 1=1 and b.red_pay_id is not null" +sqlConditionYJRed+
                " union" +
                " select ifnull(sum(b.pay_money),0) as yesterday" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1 "+sqlConditionTip+
                ")a";
        return sql;
    }
    @ApiOperation("经营概况-今日数据-交易信息-收入-wb")
    private String findMrSqlWbT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        sqlConditionO.append(" and date(a.pay_date) = curdate()");//今天

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));
        sqlConditionCzo.append(" and date(a.pay_date) = curdate()");//今天

        StringBuilder sqlConditionRed = new StringBuilder(); //活动红包退回
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));
        sqlConditionRed.append(" and date(c.refund_time) = curdate()");//今天

        StringBuilder sqlConditionYJRed = new StringBuilder(); //佣金退回红包
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.plat_form",platForm));
        sqlConditionYJRed.append(jdbcUtil.appendIn("c.status","6"));
        sqlConditionYJRed.append(" and date(c.refund_time) = curdate()");//今天

        StringBuilder sqlConditionTip = new StringBuilder(); //打赏订单
        sqlConditionTip.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionTip.append(JdbcUtil.appendAnd("a.plat_form",platForm));
        sqlConditionTip.append(" and date(b.pay_date) = curdate()");//今天

        String sql = "select sum(a.yesterday) as revenue from "+
                " (SELECT ifnull(sum("+
                "  ifnull(a.real_price,0)),0) as  yesterday " +
                "  from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " select ifnull(sum(b.pay_money),0) as yesterday" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1 "+sqlConditionTip+
                ")a";
        return sql;
    }
    @ApiOperation("经营概况-今日数据-交易信息-支出(订单和佣金和红包)")
    private String findMpSqlT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder();
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionYj.append(JdbcUtil.appendAnd("a.plat_form",platForm));
        sqlConditionYj.append(" and date(a.send_time) = curdate()");

        StringBuilder sqlConditionO = new StringBuilder();
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(" and date(a.agree_refund_date) = curdate()");

        StringBuilder sqlConditionRed = new StringBuilder(); //发送红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));
        sqlConditionRed.append(" and date(c.create_time) = curdate()");//今日
        String sql ="select sum(a.yesterday) as pay from " +
                " (SELECT ifnull(sum( " +
                " a.send_money ),0) as  yesterday" +
                " FROM brokerage_record a WHERE 1=1" +sqlConditionYj+
                " union" +
                " select ifnull(sum(" +
                " a.refund_money ),0) as  yesterday" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday  from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " ) a";
        return sql;
    }
    @ApiOperation("经营概况-今日数据-交易信息-支出(订单和佣金和红包) -wb")
    private String findMpSqlWbT(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder();
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionYj.append(JdbcUtil.appendAnd("a.plat_form",platForm));
        sqlConditionYj.append(" and date(a.send_time) = curdate()");

        StringBuilder sqlConditionO = new StringBuilder();
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(" and date(a.agree_refund_date) = curdate()");

        StringBuilder sqlConditionRed = new StringBuilder(); //发送红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));
        sqlConditionRed.append(" and date(c.create_time) = curdate()");//今日
        String sql ="select sum(a.yesterday) as pay from " +
                " (" +
                " select ifnull(sum(" +
                " a.refund_money ),0) as  yesterday" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " ) a";
        return sql;
    }

    @ApiOperation("经营概况-昨日数据-交易信息")
    private String findMrpSqlY(int platForm, Integer shopId,DataParamsQo paramsQo) {
        String sql1 = findMrSqlY(platForm,shopId,paramsQo);
        String sql2 = findMpSqlY(platForm,shopId,paramsQo);
        String sql = "select * from ("+sql1+") r," +
                "("+sql2+") p";
        return sql;
    }

    @ApiOperation("经营概况-昨日数据-交易信息-wb")
    private String findMrpSqlWbY(int platForm, Integer shopId,DataParamsQo paramsQo) {
        String sql1 = findMrSqlWbY(platForm,shopId,paramsQo);
        String sql2 = findMpSqlWbY(platForm,shopId,paramsQo);
        String sql = "select * from ("+sql1+") r," +
                "("+sql2+") p";
        return sql;
    }

    @ApiOperation("经营概况-昨日数据-交易信息-收入")
    private String findMrSqlY(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));

        StringBuilder sqlConditionRed = new StringBuilder(); //活动退回红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));

        StringBuilder sqlConditionYJRed = new StringBuilder(); //佣金退回红包
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.plat_form",platForm));
        sqlConditionYJRed.append(jdbcUtil.appendIn("c.status","6"));

        StringBuilder sqlConditionTip = new StringBuilder(); //打赏订单
        sqlConditionTip.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionTip.append(JdbcUtil.appendAnd("a.plat_form",platForm));

        String flag = paramsQo.getFlag();
        if("D".equals(flag)){//本月
            sqlConditionO.append(" and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionCzo.append(" and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionRed.append(" and date_format(c.refund_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionTip.append(" and date_format(b.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else{
            sqlConditionO.append(" and date(a.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionCzo.append(" and date(a.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionRed.append(" and date(c.refund_time) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionTip.append(" and date(b.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
        }
        String sql = "select sum(a.yesterday) as revenue from "+
                " (SELECT ifnull(sum("+
                "  ifnull(a.real_price,0)),0) as  yesterday " +
                "  from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " SELECT ifnull(sum("+
                " ifnull(a.money,0)),0) AS yesterday" +
                " FROM recharge_order a WHERE 1=1 " +sqlConditionCzo+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday  from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday" +
                " from brokerage_record b left join wx_red_record c" +
                " on b.red_pay_id = c.red_pay_id where 1=1 and b.red_pay_id is not null" +sqlConditionYJRed+
                " union" +
                " select ifnull(sum(b.pay_money),0) as yesterday" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1 "+sqlConditionTip+
                ")a";
        return sql;
    }
    @ApiOperation("经营概况-昨日数据-交易信息-支出(订单和佣金和红包)")
    private String findMpSqlY(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder(); //佣金
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionYj.append(JdbcUtil.appendAnd("a.plat_form",platForm));

        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("b.platform",platForm));

        StringBuilder sqlConditionRed = new StringBuilder(); //发送红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));

        String flag = paramsQo.getFlag();
        if("D".equals(flag)){//本月
            sqlConditionO.append(" and date_format(a.agree_refund_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionYj.append(" and date_format(a.send_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionRed.append(" and date_format(c.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else{
            sqlConditionYj.append(" and date(a.send_time) = date_sub(curdate(),interval 1 day)");
            sqlConditionO.append(" and date(a.agree_refund_date) = date_sub(curdate(),interval 1 day)");
            sqlConditionRed.append(" and date(c.create_time) = date_sub(curdate(),interval 1 day)");//昨日
        }
        String sql ="select sum(a.yesterday) as pay from " +
                " (SELECT ifnull(sum( " +
                " a.send_money ),0) as  yesterday" +
                " FROM brokerage_record a WHERE 1=1 " +sqlConditionYj+
                " union" +
                " select ifnull(sum(" +
                " a.refund_money ),0) as  yesterday" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " union" +
                " select ifnull(sum(c.amount),0) as yesterday  from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " ) a";
        return sql;
    }

    @ApiOperation("经营概况-昨日数据-交易信息-收入")
    private String findMrSqlWbY(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));

        StringBuilder sqlConditionRed = new StringBuilder(); //活动退回红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));

        StringBuilder sqlConditionYJRed = new StringBuilder(); //佣金退回红包
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionYJRed.append(JdbcUtil.appendAnd("b.plat_form",platForm));
        sqlConditionYJRed.append(jdbcUtil.appendIn("c.status","6"));

        StringBuilder sqlConditionTip = new StringBuilder(); //打赏订单
        sqlConditionTip.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionTip.append(JdbcUtil.appendAnd("a.plat_form",platForm));

        String flag = paramsQo.getFlag();
        if("D".equals(flag)){//本月
            sqlConditionO.append(" and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionCzo.append(" and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionRed.append(" and date_format(c.refund_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionTip.append(" and date_format(b.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else{
            sqlConditionO.append(" and date(a.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionCzo.append(" and date(a.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionRed.append(" and date(c.refund_time) = date_sub(curdate(),interval 1 day)");//昨日
            sqlConditionTip.append(" and date(b.pay_date) = date_sub(curdate(),interval 1 day)");//昨日
        }
        String sql = "select sum(a.yesterday) as revenue from "+
                " (SELECT ifnull(sum("+
                "  ifnull(a.real_price,0)),0) as  yesterday " +
                "  from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " select ifnull(sum(b.pay_money),0) as yesterday" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1 "+sqlConditionTip+
                ")a";
        return sql;
    }
    @ApiOperation("经营概况-昨日数据-交易信息-支出(订单和佣金和红包)")
    private String findMpSqlWbY(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder(); //佣金
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionYj.append(JdbcUtil.appendAnd("a.plat_form",platForm));

        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("b.platform",platForm));

        StringBuilder sqlConditionRed = new StringBuilder(); //发送红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(JdbcUtil.appendAnd("b.platform",platForm));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));

        String flag = paramsQo.getFlag();
        if("D".equals(flag)){//本月
            sqlConditionO.append(" and date_format(a.agree_refund_date,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionYj.append(" and date_format(a.send_time,'%Y-%m')=date_format(now(),'%Y-%m')");
            sqlConditionRed.append(" and date_format(c.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else{
            sqlConditionYj.append(" and date(a.send_time) = date_sub(curdate(),interval 1 day)");
            sqlConditionO.append(" and date(a.agree_refund_date) = date_sub(curdate(),interval 1 day)");
            sqlConditionRed.append(" and date(c.create_time) = date_sub(curdate(),interval 1 day)");//昨日
        }
        String sql ="select sum(a.yesterday) as pay from " +
                " (" +
                " select ifnull(sum(" +
                " a.refund_money ),0) as  yesterday" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " ) a";
        return sql;
    }

    @ApiOperation("经营概况-昨日数据-订单信息/发货信息")
    private String findMorderSqlY(String appid, Integer shopId, int platform ,DataParamsQo paramsQo) {
        String sql="";
        String flag = paramsQo.getFlag();
        if("D".equals(flag)){//本月
            sql = "select ifnull(sum(case when a.status !=4 and date_format(a.create_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as placeOrder," +
                    " ifnull(sum(case when a.status in(1,2,3,5) and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as payOrder," +
                    " ifnull(sum(case when a.status=5 and date_format(a.create_good_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as applyReturnOrder," +
                    " ifnull(sum(case when a.status=1 and date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as sendOrder," +
                    " ifnull(sum(case when a.status=2 and date_format(a.send_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as receiptOrder," +
                    " ifnull(sum(case when date_format(a.send_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as cumulateSendOrder," +
                    " ifnull(sum(case when date_format(a.take_date,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end),0) as cumulateReceiptOrder" +
                    " FROM order_info a where 1=1";
        }else{
            sql = "select ifnull(sum(case when a.status !=4 and date(a.create_date) = date_sub(curdate(),interval 1 day) then 1 end),0) as placeOrder," +
                    " ifnull(sum(case when a.status in(1,2,3,5) and date(a.pay_date) = date_sub(curdate(),interval 1 day) then 1 end),0) as payOrder," +
                    " ifnull(sum(case when a.status=5 and date(a.create_good_date) = date_sub(curdate(),interval 1 day) then 1 end),0) as applyReturnOrder," +
                    " ifnull(sum(case when a.status=1 and date(a.pay_date) = date_sub(curdate(),interval 1 day) then 1 end),0) as sendOrder," +
                    " ifnull(sum(case when a.status=2 and date(a.send_date) = date_sub(curdate(),interval 1 day) then 1 end),0) as receiptOrder" +
                    " FROM order_info a where 1=1";
        }
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",platform));
        return sql+sqlCondition;
    }
    @ApiOperation("经营概况-昨日数据-商品信息")
    private String findMproductSqlY(int platForm, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",platForm));
        String flag = paramsQo.getFlag();
        if("D".equals(flag)) {//本月
            sqlCondition.append(" and date_format(a.visit_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else{
            sqlCondition.append(" and date(a.visit_time) = date_sub(curdate(),interval 1 day)");
        }
        String sql =" select " +
                " count(distinct a.user_id ) as ucount,count(1) as pv" +
                " from (select a.* from  jm_visit a  left join product b" +
                " on a.pid = b.pid where 1=1 and ifnull(a.type,1) =1 "+sqlCondition+")a";
        return sql;
    }
    @ApiOperation("经营概况-昨日数据-粉丝信息")
    private String findMfunsSqlY(String appid, Integer shopId, DataParamsQo paramsQo) {
        String sql="";
        String flag = paramsQo.getFlag();
        if("D".equals(flag)) {//本月
            sql = "select IFNULL(sum(case when a.is_subscribe=1 and date_format(a.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as netCount," +
                    " IFNULL(sum(case when a.is_subscribe=0 and date_format(a.un_subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as offCount, " +
                    " IFNULL(sum(case when  date_format(a.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as addCount"+
                    " from wx_user a where 1=1";
        }else{
            sql = "select IFNULL(sum(case when a.is_subscribe=1 and date(a.subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as netCount," +
                    " IFNULL(sum(case when a.is_subscribe=0 and date(a.un_subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as offCount, " +
                    " IFNULL(sum(case when  date(a.subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as addCount"+
                    " from wx_user a where 1=1";
        }
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        return sql+sqlCondition;
    }

    @ApiOperation("经营概况-昨日数据-粉丝信息(微博)")
    private String findMfunsSqlWbY(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        String sql="";
        String flag = paramsQo.getFlag();
        if("D".equals(flag)) {//本月
            sql = "select IFNULL(sum(case when a.is_subscribe=1 and date_format(a.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as netCount," +
                    " IFNULL(sum(case when a.is_subscribe=0 and date_format(a.un_subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as offCount, " +
                    " IFNULL(sum(case when  date_format(a.subscribe_time,'%Y-%m')=date_format(now(),'%Y-%m') then 1 end ),0) as addCount"+
                    " from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1";
        }else{
            sql = "select IFNULL(sum(case when a.is_subscribe=1 and date(a.subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as netCount," +
                    " IFNULL(sum(case when a.is_subscribe=0 and date(a.un_subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as offCount, " +
                    " IFNULL(sum(case when  date(a.subscribe_time) = date_sub(curdate(),interval 1 day) then 1 end ),0) as addCount"+
                    " from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 ";
        }
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        return sql+sqlCondition;
    }


    @ApiOperation("经营概况-今日数据-粉丝信息")
    private String findMfunsSql(String appid, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.appid",appid));
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        sonSqlCondition1.append(" date(a.subscribe_time) = curdate()");
        sonSqlCondition2.append(" date(a.un_subscribe_time) = curdate()");
        String sql = "select IFNULL(sum(case when " +sonSqlCondition1+
                " then 1 end ),0) as addCount," +
                " IFNULL(sum(case when a.is_subscribe=1 and " +sonSqlCondition1+
                " then 1 end ),0) as netCount," +
                " IFNULL(sum(case when a.is_subscribe=0 and " +sonSqlCondition2+
                " then 1 end ),0) as offCount" +
                " from wx_user a where 1=1 " +sqlCondition;
        return sql;
    }

    @ApiOperation("经营概况-今日数据-粉丝信息（微博）")
    private String findMfunsSqlWb(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.pid",shopUid));
        StringBuilder sonSqlCondition1 = new StringBuilder(); //关注时间条件
        StringBuilder sonSqlCondition2 = new StringBuilder(); //跑路时间条件
        sonSqlCondition1.append(" date(a.subscribe_time) = curdate()");
        sonSqlCondition2.append(" date(a.un_subscribe_time) = curdate()");
        String sql = "select IFNULL(sum(case when " +sonSqlCondition1+
                " then 1 end ),0) as addCount," +
                " IFNULL(sum(case when a.is_subscribe=1 and " +sonSqlCondition1+
                " then 1 end ),0) as netCount," +
                " IFNULL(sum(case when a.is_subscribe=0 and " +sonSqlCondition2+
                " then 1 end ),0) as offCount" +
                " from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 " +sqlCondition;
        return sql;
    }
    @ApiOperation("经营概况-今日数据-订单信息")
    private String findMorderSql(String appid, Integer shopId,int platForm, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        StringBuilder sqlCondition1 = new StringBuilder();
        StringBuilder sqlCondition2 = new StringBuilder();
        StringBuilder sqlCondition3 = new StringBuilder();
        StringBuilder sqlCondition4 = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.platform",platForm));
        sqlCondition1.append(" and date(a.create_date) = curdate()");//新增订单
        sqlCondition2.append(" and date(a.pay_date) = curdate()");//付款订单/待发货
        sqlCondition3.append(" and date(a.send_date) = curdate()");//待收货订单
        sqlCondition4.append(" and date(a.create_good_date) = curdate()");//待退款订单
        //申请退单时间
        String sql = "SELECT IFNULL(sum(case when a.status=5 and a.agree_refund_date is null" +sqlCondition4+
                " then 1 end),0) as wreturnOrder," +
                " IFNULL(sum(case when a.status=1 " + sqlCondition2+
                "then 1 end),0) as sendOrder," +
                " IFNULL(sum(case when a.status=2 " +sqlCondition3+
                " then 1 end),0) as receiptOrder," +
                " IFNULL(sum(case when a.status !=4" +sqlCondition1+
                " then 1 end),0) as addOrder," +
                " IFNULL(sum(case when a.status in(1,2,3,5)" +sqlCondition2+
                " then 1 end),0) as payOrder," +
                " IFNULL(sum(case when a.status in(1,2,3,5)" +sqlCondition2+
                " then ifnull(a.real_price,0) end),0) as realMoney, " +
                " IFNULL(sum(case when a.status in(1,2,3,5)" +sqlCondition2+
                " then ifnull(a.total_price + a.send_fee,0) end),0) as money" +
                " from order_info a where 1=1 "+sqlCondition;
        return sql;
    }


    @ApiOperation("经营概况-今日数据-商品信息")
    private String findMproductSql(int platForm , Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("ifnull(a.plat_form,0)",platForm));
        sqlCondition.append(" and date(a.visit_time) = curdate()");
        String sql =" select " +
                " count(distinct a.user_id ) as ucount,count(1) as pv" +
                " from (select a.* from  jm_visit a left join product b" +
                " on a.pid = b.pid where 1=1 and ifnull(a.type,1) =1" +
                 sqlCondition+")a";
        return sql;
    }
}
