package com.jm.business.service.shop.dataAnalysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.dataAnalysis.DataParamsQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.util.csv.CsvToolUtil;
import com.jm.staticcode.util.csv.CsvWriter;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>收支对账</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/8 16:40
 */
@Service
public class RpAnalysisService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CsvToolUtil csvToolUtil;
    @ApiOperation("收支分析")
    public Map findRpData(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        Integer tabIndex = paramsQo.getTabIndex();
        Map map = new HashMap();
        if(1 == tabIndex.intValue()){ //收支概况
            handleRpBasic(shopUid,shopId,paramsQo,map);
        }else if(2 == tabIndex.intValue()){ //收入对账
            handleRBasic(shopUid,shopId,paramsQo,map);
        }else if(3 == tabIndex.intValue()){ //支出对账
            handlePBasic(shopUid,shopId,paramsQo,map);
        }
        return map;
    }

    @ApiOperation("收支概况")
    private void handleRpBasic(long shopUid, Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql1 = findRSql(shopId,paramsQo); //wx收入（订单和充值订单和红包）
        String sql2 = findPSql(shopId,paramsQo); //wx支出 (发红包)
        String sqlwb1 = findRWbSql(shopUid,shopId,paramsQo); //wb收入（订单和充值订单和红包）
        String sqlwb2 = findPWbSql(shopUid,shopId,paramsQo); //wb支出 (发红包)
        map.put("revenue",jdbcTemplate.queryForMap(sql1));
        map.put("pay",jdbcTemplate.queryForMap(sql2));
        map.put("revenueWb",jdbcTemplate.queryForMap(sqlwb1));
        map.put("payWb",jdbcTemplate.queryForMap(sqlwb2));
    }

    @ApiOperation("收入对账")
    private void handleRBasic(long shopUid, Integer shopId, DataParamsQo paramsQo, Map map) {
        String showTotalMoney = Toolkit.parseObjForStr(paramsQo.getShowTotalMoney());
        if("Y".equals(showTotalMoney)){
            if(1==paramsQo.getPlatForm()){ //微信平台
                String moneySql =  findRTotalMoneySql(shopId,paramsQo);
                map.put("rTotalMoney",jdbcTemplate.queryForMap(moneySql));
            }else if(2==paramsQo.getPlatForm()){  // 微博平台
                String moneySql =  findRTotalMoneyWbSql(shopId,paramsQo);
                map.put("rTotalMoney",jdbcTemplate.queryForMap(moneySql));
            }
        }else if("N".equals(showTotalMoney)){
            if(1==paramsQo.getPlatForm()){ //微信平台
                String sql = findPageRSql(shopId,paramsQo);
                String fromSql = findPageRFromSql(shopId,paramsQo);
                PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
                converterNickName(page);
                map.put("rAnalysisInfo",page);
            }else if(2==paramsQo.getPlatForm()){  // 微博平台
                String sql = findPageRWbSql(shopUid,shopId,paramsQo);
                String fromSql = findPageRWbFromSql(shopUid,shopId,paramsQo);
                PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
                map.put("rAnalysisInfo",page);
            }
        }
    }

    @ApiOperation("支出对账")
    private void handlePBasic(long shopUid, Integer shopId, DataParamsQo paramsQo, Map map) {
        String sql = "";
        String fromSql = "";
        String showTotalMoney = Toolkit.parseObjForStr(paramsQo.getShowTotalMoney());
        if("Y".equals(showTotalMoney)){
            if(1==paramsQo.getPlatForm()) { //微信平台
                String moneySql =  findPTotalMoneySql(shopId,paramsQo);
                map.put("pTotalMoney",jdbcTemplate.queryForMap(moneySql));
            }else if(2==paramsQo.getPlatForm()){//微博平台
                String moneySql =  findPTotalMoneyWbSql(shopUid,shopId,paramsQo);
                map.put("pTotalMoney",jdbcTemplate.queryForMap(moneySql));
            }
        }else if("N".equals(showTotalMoney)){
            if(1==paramsQo.getPlatForm()) { //微信平台
                sql = findPagePSql(shopId,paramsQo);
                fromSql = findPagePFromSql(shopId,paramsQo);
                PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
                converterNickName(page);
                map.put("pAnalysisInfo",page);
            }else if(2==paramsQo.getPlatForm()){//微博平台
                sql = findPagePWbSql(shopUid,shopId,paramsQo);
                fromSql = findPagePFromSql(shopId,paramsQo);
                PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,paramsQo.getCurPage(),paramsQo.getPageSize());
                map.put("pAnalysisInfo",page);
            }
        }
    }
    @ApiOperation("支出总金额")
    private String findPTotalMoneySql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.orderNum",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transactId",paramsQo.getTransactionId()));
        int pTypeId = Toolkit.parseObjForInt(paramsQo.getPayTypeId());
        // 订单状态 -1 商品退款订单  -2 佣金  -3 活动现金红包
        if(pTypeId==0){ //全部
        }else if(pTypeId==1){ //退款商品订单
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
        }else if(pTypeId==2){ //活动现金红包
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-3));
        }else if(pTypeId==3){ //商家发佣
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendIn("a.type","1,4"));
        }else if(pTypeId==4){ //佣金提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",2));
        }else if(pTypeId==5){ //积分提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",3));
        }
        String sql = "select ifnull(sum(a.money),0) as totalMoney from (" +
                " select c.user_id as userId ,c.order_num as orderNum,b.refund_id as transactId,-1 as orderStatus," +
                " b.refund_Fee as money,b.re_fun_time as date,-1 as type from order_refund a " +
                " left join refund_recod b on a.order_info_id = b.order_id" +
                " left join order_info c on a.order_info_id = c.order_info_id" +
                " where 1=1 and ifnull(a.platform,0) =0 and a.refund_status=1 and c.shop_id=" +shopId+
                " UNION" +
                " select a.user_id,a.send_num,b.detail_id,-2,b.amount,b.create_time,a.put_type FROM brokerage_record a " +
                " left join wx_red_record b on a.red_pay_id = b.red_pay_id " +
                " where 1=1 and ifnull(a.plat_form,0) = 0 and a.red_pay_id is not null and b.status  in(2,4,6) and a.shop_id="+shopId+
                " UNION" +
                " select a.user_id,c.mch_billno,c.detail_id,-3,c.amount,c.create_time,b.type " +
                " from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id " +
                " where 1=1 and a.red_pay_id is not null and c.status in(2,4,6) and b.shop_id="+shopId+
                " )a  where 1=1";
        return sql+sqlCondition;
    }

    @ApiOperation("支出总金额")
    private String findPTotalMoneyWbSql(long shopUid ,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.orderNum",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transactId",paramsQo.getTransactionId()));
        int pTypeId = Toolkit.parseObjForInt(paramsQo.getPayTypeId());
        // 订单状态 -1 商品退款订单  -2 佣金  -3 活动现金红包
        if(pTypeId==0){ //全部
        }else if(pTypeId==1){ //退款商品订单
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
        }else if(pTypeId==2){ //活动现金红包
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-3));
        }else if(pTypeId==3){ //商家发佣
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendIn("a.type","1,4"));
        }else if(pTypeId==4){ //佣金提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",2));
        }else if(pTypeId==5){ //积分提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",3));
        }
        String sql = "select ifnull(sum(a.money),0) as totalMoney from (" +
                " select c.user_id as userId ,c.order_num as orderNum,b.refund_id as transactId,-1 as orderStatus," +
                " b.refund_Fee as money,b.re_fun_time as date,-1 as type from order_refund a " +
                " left join refund_recod b on a.order_info_id = b.order_id" +
                " left join order_info c on a.order_info_id = c.order_info_id" +
                " where 1=1 and a.platform = 1 and a.refund_status=1 and c.shop_id=" +shopId+
                " )a  where 1=1";
        return sql+sqlCondition;
    }

    @ApiOperation("支出分页")
    private String findPagePFromSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String sql = " from ( " +
                " select c.user_id as userId ,c.order_num as orderNum,b.refund_id as transactId,-1 as orderStatus," +
                " b.refund_Fee as money,b.re_fun_time as date,-1 as type from order_refund a " +
                " left join refund_recod b on a.order_info_id = b.order_id" +
                " left join order_info c on a.order_info_id = c.order_info_id" +
                " where 1=1 and ifnull(a.platform,0) = 0 and a.refund_status=1 and c.shop_id=" +shopId+
                " UNION" +
                " select a.user_id,a.send_num,b.detail_id,-2,b.amount,b.create_time,a.put_type FROM brokerage_record a " +
                " left join wx_red_record b on a.red_pay_id = b.red_pay_id " +
                " where 1=1 and ifnull(a.plat_form,0)=0 and a.red_pay_id is not null and b.status  in(2,4,6) and a.shop_id="+shopId+
                " UNION" +
                " select a.user_id,c.mch_billno,c.detail_id,-3,c.amount,c.create_time,b.type " +
                " from activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id " +
                " where 1=1 and a.red_pay_id is not null and c.status in(2,4,6) and b.shop_id="+shopId+
                ")a " +
                " left join wx_user c on a.userId = c.user_id" +
                " left join shop_user d on c.shop_user_id = d.id " +
                " where 1=1 ";
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.orderNum",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transactId",paramsQo.getTransactionId()));
        int pTypeId = Toolkit.parseObjForInt(paramsQo.getPayTypeId());
        // 订单状态 -1 商品退款订单  -2 佣金  -3 活动现金红包
        if(pTypeId==0){ //全部
        }else if(pTypeId==1){ //退款商品订单
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
        }else if(pTypeId==2){ //活动现金红包
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-3));
        }else if(pTypeId==3){ //商家发佣
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendIn("a.type","1,4"));
        }else if(pTypeId==4){ //佣金提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",2));
        }else if(pTypeId==5){ //积分提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",3));
        }
        return sql+sqlCondition+" order by a.date desc";
    }

    @ApiOperation("支出分页-wb")
    private String findPagePWbFromSql(long shopUid,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String sql = " from ( " +
                " select c.user_id as userId ,c.order_num as orderNum,b.refund_id as transactId,-1 as orderStatus," +
                " b.refund_Fee as money,b.re_fun_time as date,-1 as type from order_refund a " +
                " left join refund_recod b on a.order_info_id = b.order_id" +
                " left join order_info c on a.order_info_id = c.order_info_id" +
                " where 1=1 and a.platform = 1 and a.refund_status=1 and c.shop_id=" +shopId+
                ")a " +
                " left join (select a.*,b.area_code,b.nickname from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 and a.pid=" +shopUid+
                " ) c on a.userId = c.uid" +
                " left join shop_user d on c.shop_user_id = d.id " +
                " where 1=1 ";
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.orderNum",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transactId",paramsQo.getTransactionId()));
        int pTypeId = Toolkit.parseObjForInt(paramsQo.getPayTypeId());
        // 订单状态 -1 商品退款订单  -2 佣金  -3 活动现金红包
        if(pTypeId==0){ //全部
        }else if(pTypeId==1){ //退款商品订单
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
        }else if(pTypeId==2){ //活动现金红包
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-3));
        }else if(pTypeId==3){ //商家发佣
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendIn("a.type","1,4"));
        }else if(pTypeId==4){ //佣金提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",2));
        }else if(pTypeId==5){ //积分提现
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-2));
            sqlCondition.append(JdbcUtil.appendAnd("a.type",3));
        }
        return sql+sqlCondition+" order by a.date desc";
    }

    @ApiOperation("支出明细")
    private String findPagePSql(Integer shopId, DataParamsQo paramsQo) {
        String sql ="select a.date," +
                " case when a.orderStatus =-1 then '订单退款' " +
                " when a.orderStatus =-2 and a.type in(1,4) then '商家发佣' " +
                " when a.orderStatus =-2 and a.type = 2 then '佣金提现' " +
                " when a.orderStatus =-2 and a.type = 3 then '积分提现' " +
                " when a.orderStatus =-3 and a.type = 1 then '现金红包活动' " +
                " when a.orderStatus =-3 and a.type = 2 then '卡券红包活动' " +
                " when a.orderStatus =-3 and a.type = 3 then '金券红包活动' " +
                " when a.orderStatus =-3 and a.type = 4 then '天天红包活动' " +
                " end as typeName," +
                " a.orderNum," +
                " a.transactId,'微信' as plat," +
                " '微信支付' as payWay,a.money," +
                " c.nickname as nickName,d.user_name as userName,d.phone_number as phoneNum," +
                " case when a.orderStatus in (-1,-2,-3) then '成功'" +
                " end as evenName"+findPagePFromSql(shopId,paramsQo);
        return sql;
    }
    @ApiOperation("支出明细 -wb")
    private String findPagePWbSql(long shopUid ,Integer shopId, DataParamsQo paramsQo) {
        String sql ="select a.date," +
                " case when a.orderStatus =-1 then '订单退款' " +
                " end as typeName," +
                " a.orderNum," +
                " a.transactId,'微博' as plat," +
                " '微博支付' as payWay,a.money," +
                " c.nickname as nickName,d.user_name as userName,d.phone_number as phoneNum," +
                " case when a.orderStatus in (-1,-2,-3) then '成功'" +
                " end as evenName"+findPagePWbFromSql(shopUid,shopId,paramsQo);
        return sql;
    }



    @ApiOperation("收入明细")
    private String findPageRSql(Integer shopId, DataParamsQo paramsQo) {
        // b.pay_id as payId
        String sql ="select a.pay_end_date as payEndDate," +
                " case when a.orderStatus not in(-1,-2,-3,-4) then '商品订单' " +
                " when a.orderStatus =-1 and a.typeId =1 then '积分充值' " +
                " when a.orderStatus =-1 and a.agentRole in(1,2,3,4) then '代理收费'" +
                " when a.orderStatus =-1 and a.agentRole in(5,6,7,8) then '分销收费' " +
                " when a.orderStatus =-1 and a.agentRole =9 then '我的小店收费'" +
                " when a.orderStatus =-2 and a.typeId in (1,4) then '商家发佣红包' " +
                " when a.orderStatus =-2 and a.typeId = 2 then '佣金提现红包' " +
                " when a.orderStatus =-2 and a.typeId = 3 then '积分提现红包' " +
                " when a.orderStatus =-3 and a.typeId = 1 then '现金红包活动' " +
                " when a.orderStatus =-3 and a.typeId = 2 then '卡券红包活动' " +
                " when a.orderStatus =-3 and a.typeId = 3 then '金券红包活动' " +
                " when a.orderStatus =-3 and a.typeId = 4 then '天天红包活动' " +
                " when a.orderStatus =-4 then '图文打赏' " +
                " end as typeName," +
                " a.order_num as orderNum," +
                " a.transaction_id as transactionId,case when a.pay_type=1 then '微信' end as plat," +
                " case when a.pay_type=1 then '微信支付' end as payWay,a.totalMoney,ifnull((a.totalMoney - a.pay_money),0),a.pay_money as payMoney," +
                " c.nickname as nickName,d.user_name as userName,d.phone_number as phoneNum," +
                " case when a.orderStatus in(-1,-4) then '成功'" +
                " when a.orderStatus in (-2,-3) then '红包未领'" +
                " when a.orderStatus =1 then '待发货'" +
                " when a.orderStatus =2 then '待收货' when a.orderStatus =3 then '已收货'" +
                " when a.orderStatus =4 or a.orderStatus=0 or a.orderStatus is null then '异常单' when a.orderStatus =5 and a.refundStatus=0 then '待退款'" +
                " when a.orderStatus =5 and a.refundStatus=2 then '拒绝退款' when a.orderStatus =5 and a.refundStatus=1 then '已退货/退款'" +
                " end as evenName"+findPageRFromSql(shopId,paramsQo);
        // ,a.orderStatus,a.refundStatus,a.agentRole
        return sql;
    }

    @ApiOperation("收入明细-wb")
    private String findPageRWbSql(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        // b.pay_id as payId
        String sql ="select a.pay_end_date as payEndDate," +
                " case when a.orderStatus not in(-1,-2,-3,-4) then '商品订单' " +
                " when a.orderStatus =-4 then '图文打赏' " +
                " end as typeName," +
                " a.order_num as orderNum," +
                " a.transaction_id as transactionId,case when a.pay_type=2 then '微博' end as plat," +
                " case when a.pay_type=2 then '微博支付' end as payWay,a.totalMoney," +
                " ifnull((a.totalMoney - a.pay_money),0),a.pay_money as payMoney," +
                " c.nickname as nickName,d.user_name as userName,d.phone_number as phoneNum," +
                " case when a.orderStatus in(-1,-4) then '成功'" +
                " when a.orderStatus in (-2,-3) then '红包未领'" +
                " when a.orderStatus =1 then '待发货'" +
                " when a.orderStatus =2 then '待收货' when a.orderStatus =3 then '已收货'" +
                " when a.orderStatus =4 or a.orderStatus=0 or a.orderStatus is null then '异常单' when a.orderStatus =5 and a.refundStatus=0 then '待退款'" +
                " when a.orderStatus =5 and a.refundStatus=2 then '拒绝退款' when a.orderStatus =5 and a.refundStatus=1 then '已退货/退款'" +
                " end as evenName"+findPageRWbFromSql(shopUid,shopId,paramsQo);
        // ,a.orderStatus,a.refundStatus,a.agentRole
        return sql;
    }
    @ApiOperation("收入分页")
    private String findPageRFromSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String sql = " from ( " +
                " select a.pay_id,a.order_num,a.status as orderStatus,b.refund_status as refundStatus," +
                " -1 as agentRole,a.user_id as payUserId,-1 as typeId,ifnull(a.total_price + a.send_fee,0) as totalMoney," +
                " c.pay_money,c.pay_end_date,c.transaction_id,c.pay_type" +
                " from  order_info a left join order_refund b on " +
                " a.order_info_id = b.order_info_id left join pay_record c  on a.pay_id = c.pay_id" +
                " where 1=1 and ifnull(a.platform,0)=0 and a.pay_id is not null and c.pay_status=1 and a.shop_id=" +shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-1,-1,a.agent_role as agentRole,a.user_id as payUserId,a.type as typeId,a.money as totalMoney, " +
                " c.pay_money,c.pay_end_date,c.transaction_id,c.pay_type" +
                " from recharge_order a" +
                " left join pay_record c  on a.pay_id = c.pay_id where 1=1 and ifnull(a.platform,0)=0 and a.pay_id is not null and c.pay_status=1 and a.shop_id="+shopId+
                " UNION" +
                " select c.red_pay_id, c.mch_billno,-3,-1,-1,a.user_id,b.type,c.amount,c.amount,c.refund_time,c.detail_id,1 FROM activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null and c.status=6 and b.shop_id =" +shopId+
                " UNION" +
                " select a.red_pay_id,a.send_num,-2,-1,-1,a.user_id,a.put_type,b.amount,b.amount,b.refund_time,b.detail_id ,1" +
                " from brokerage_record a left join wx_red_record b" +
                " on a.red_pay_id = b.red_pay_id where 1=1 and ifnull(a.plat_form,0) = 0 and a.red_pay_id is not null" +
                " and b.status =6 and a.shop_id =" +shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-4,-1,-1,a.user_id,-1,b.pay_money,b.pay_money,b.pay_end_date,b.transaction_id,b.pay_type " +
                " from image_text_tip a left join pay_record b ON a.pay_id = b.pay_id" +
                " where 1=1 and a.plat_form =0 and a.pay_id IS NOT NULL and b.pay_status = 1 and a.shop_id="+shopId+
                ")a " +
                " left join wx_user c on a.payUserId = c.user_id" +
                " left join shop_user d on c.shop_user_id = d.id " +
                " where 1=1 ";
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_end_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.pay_end_date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.pay_end_date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.pay_end_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史
        }
        sqlCondition.append(JdbcUtil.appendLike("a.order_num",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transaction_id",paramsQo.getTransactionId()));
        int rTypeId = Toolkit.parseObjForInt(paramsQo.getRevenueTypeId());
        if(rTypeId==0){ //全部
        }else if(rTypeId==1){ //商品订单
            sqlCondition.append(JdbcUtil.appendNotIn("a.orderStatus","-1,-2,-3,-4"));
            int orderStatus = Toolkit.parseObjForInt(paramsQo.getOrderStatus());
            if(0==orderStatus){
            }else if(1==orderStatus || 2==orderStatus || 3==orderStatus){ //待发货,待收货,已收货
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",orderStatus));
            }else if(4==orderStatus){ //待退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",0));
            }else if(5==orderStatus){ //拒绝退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",2));
            }else if(6==orderStatus){ //已退货/退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",1));
            }else if(7==orderStatus){//异常单
                sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","4,0"));
            }
        }else if(rTypeId==2){ //我的小店收费 9
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendAnd("a.agentRole",9));
        }else if(rTypeId==3){ //分销收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","5,6,7,8"));
        }else if(rTypeId==4){ //代理收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","1,2,3,4"));
        }else if(rTypeId==5){ //红包回款
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-2,-3"));
        }else if(rTypeId==6){ //积分充值
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-1"));
            sqlCondition.append(JdbcUtil.appendIn("a.typeId","1"));
        }else if(rTypeId==7){ //图文打赏
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-4"));
        }
        return sql+sqlCondition+" order by a.pay_end_date desc";
    }

    @ApiOperation("收入分页-wb")
    private String findPageRWbFromSql(long shopUid, Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String sql = " from ( " +
                " select a.pay_id,a.order_num,a.status as orderStatus,b.refund_status as refundStatus," +
                " -1 as agentRole,a.user_id as payUserId,-1 as typeId,ifnull(a.total_price + a.send_fee,0) as totalMoney," +
                " c.pay_money,c.pay_end_date,c.transaction_id,c.pay_type" +
                " from  order_info a left join order_refund b on " +
                " a.order_info_id = b.order_info_id left join pay_record c  on a.pay_id = c.pay_id" +
                " where 1=1 and ifnull(a.platform,0)=1 and a.pay_id is not null and c.pay_status=1 and a.shop_id=" +shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-4,-1,-1,a.user_id,-1,b.pay_money,b.pay_money,b.pay_end_date,b.transaction_id,b.pay_type " +
                " from image_text_tip a left join pay_record b ON a.pay_id = b.pay_id" +
                " where 1=1 and a.plat_form =1 and a.pay_id IS NOT NULL and b.pay_status = 1 and a.shop_id="+shopId+
                ")a " +
                " left join (select a.*,b.area_code,b.nickname from wb_user_rel a left join  wb_user b on a.uid = b.id where 1=1 and a.pid=" +shopUid+
                " ) c on a.payUserId = c.uid" +
                " left join shop_user d on c.shop_user_id = d.id " +
                " where 1=1 ";
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_end_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.pay_end_date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.pay_end_date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.pay_end_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史
        }
        sqlCondition.append(JdbcUtil.appendLike("a.order_num",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transaction_id",paramsQo.getTransactionId()));
        int rTypeId = Toolkit.parseObjForInt(paramsQo.getRevenueTypeId());
        if(rTypeId==0){ //全部
        }else if(rTypeId==1){ //商品订单
            sqlCondition.append(JdbcUtil.appendNotIn("a.orderStatus","-1,-2,-3,-4"));
            int orderStatus = Toolkit.parseObjForInt(paramsQo.getOrderStatus());
            if(0==orderStatus){
            }else if(1==orderStatus || 2==orderStatus || 3==orderStatus){ //待发货,待收货,已收货
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",orderStatus));
            }else if(4==orderStatus){ //待退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",0));
            }else if(5==orderStatus){ //拒绝退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",2));
            }else if(6==orderStatus){ //已退货/退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",1));
            }else if(7==orderStatus){//异常单
                sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","4,0"));
            }
        }else if(rTypeId==2){ //我的小店收费 9
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendAnd("a.agentRole",9));
        }else if(rTypeId==3){ //分销收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","5,6,7,8"));
        }else if(rTypeId==4){ //代理收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","1,2,3,4"));
        }else if(rTypeId==5){ //红包回款
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-2,-3"));
        }else if(rTypeId==6){ //积分充值
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-1"));
            sqlCondition.append(JdbcUtil.appendIn("a.typeId","1"));
        }else if(rTypeId==7){ //图文打赏
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-4"));
        }
        return sql+sqlCondition+" order by a.pay_end_date desc";
    }

    @ApiOperation("收入总金额")
    private String findRTotalMoneySql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_end_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.pay_end_date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.pay_end_date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.pay_end_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.order_num",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transaction_id",paramsQo.getTransactionId()));
        int rTypeId = Toolkit.parseObjForInt(paramsQo.getRevenueTypeId());
        if(rTypeId==0){ //全部
        }else if(rTypeId==1){ //商品订单
            // 订单状态《 -1： 充值订单  -2 ：手动发佣金和提现订单24小时未领红包 -3 ：活动订单24小时未领红包 -4 ：图文打赏订单》
            sqlCondition.append(JdbcUtil.appendNotIn("a.orderStatus","-1,-2,-3,-4"));
            int orderStatus = Toolkit.parseObjForInt(paramsQo.getOrderStatus());
            if(0==orderStatus){
            }else if(1==orderStatus || 2==orderStatus || 3==orderStatus){ //待发货,待收货,已收货
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",orderStatus));
            }else if(4==orderStatus){ //待退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",0));
            }else if(5==orderStatus){ //拒绝退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",2));
            }else if(6==orderStatus){ //已退货/退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",1));
            }else if(7==orderStatus){//异常单
                sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","4,0"));
            }
        }else if(rTypeId==2){ //我的小店收费 9
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendAnd("a.agentRole",9));
        }else if(rTypeId==3){ //分销收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","5,6,7,8"));
        }else if(rTypeId==4){ //代理收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","1,2,3,4"));
        }else if(rTypeId==5){ //红包回款
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-2,-3"));
        }else if(rTypeId==6){ //积分充值
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-1"));
            sqlCondition.append(JdbcUtil.appendIn("a.typeId","1"));
        }else if(rTypeId==7){ //图文打赏
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus","-4"));
        }
        // 订单状态《 -1： 充值订单  -2 ：手动发佣金和提现订单24小时未领红包 -3 ：活动订单24小时未领红包 -4 ：图文打赏订单》
        String sql = "select ifnull(sum(a.payMoney),0) as totalMoney from (" +
                " select a.pay_id,a.order_num,a.status as orderStatus,b.refund_status as refundStatus," +
                " -1 as agentRole,a.user_id as payUserId,-1 as typeId,c.pay_money as payMoney,c.pay_end_date,c.transaction_id" +
                " from  order_info a left join order_refund b on " +
                " a.order_info_id = b.order_info_id left join pay_record c  on a.pay_id = c.pay_id " +
                " where 1=1 and a.pay_id is not null and ifnull(a.platform,0) = 0 and c.pay_status=1 and a.shop_id=" +shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-1,-1,a.agent_role as agentRole,a.user_id as payUserId,a.type as typeId,b.pay_money," +
                " b.pay_end_date,b.transaction_id from recharge_order a " +
                " left join pay_record b  on a.pay_id = b.pay_id where 1=1 and ifnull(a.platform,0)=0 and a.pay_id is not null and b.pay_status=1 and shop_id="+shopId+
                " UNION" +
                " select c.red_pay_id, c.mch_billno,-3,-1,-1,a.user_id,b.type,c.amount,c.refund_time,c.detail_id FROM activity_user a " +
                " left join activity b on a.activity_id = b.id  left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null and c.status=6 and b.shop_id="+shopId+
                " UNION" +
                " select a.red_pay_id,a.send_num,-2,-1,-1,user_id,put_type,b.amount,b.refund_time,b.detail_id " +
                " from brokerage_record a left join wx_red_record b on a.red_pay_id = b.red_pay_id" +
                " where 1=1 and a.plat_form = 0 and a.red_pay_id is not null and b.status =6 and a.shop_id="+shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-4,-1,-1,a.user_id,-1,b.pay_money,b.pay_end_date,b.transaction_id " +
                " from image_text_tip a left join pay_record b ON a.pay_id = b.pay_id" +
                " where 1=1 and a.plat_form =0 and a.pay_id IS NOT NULL and b.pay_status = 1 and a.shop_id="+shopId+
                " )a  where 1=1";
        return sql+sqlCondition;
    }
    @ApiOperation("收入总金额")
    private String findRTotalMoneyWbSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlCondition = new StringBuilder();
        String flag = Toolkit.parseObjForStr(paramsQo.getFlag());
        if("A".equals(flag)){ //查询
            sqlCondition.append(JdbcUtil.appendAnd("a.pay_end_date",paramsQo.getBeginTime(),paramsQo.getEndTime()));
        }else if("B".equals(flag)){ //当日
            sqlCondition.append(" and date(a.pay_end_date) = curdate()");
        }else if("C".equals(flag)){ //这周
            sqlCondition.append(" and YEARWEEK(date_format(a.pay_end_date,'%Y-%m-%d')) = YEARWEEK(now())");
        }else if("D".equals(flag)){ //本月
            sqlCondition.append(" and date_format(a.pay_end_date,'%Y-%m')=date_format(now(),'%Y-%m')");
        }else if("F".equals(flag)){ //历史

        }
        sqlCondition.append(JdbcUtil.appendLike("a.order_num",paramsQo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendLike("a.transaction_id",paramsQo.getTransactionId()));
        int rTypeId = Toolkit.parseObjForInt(paramsQo.getRevenueTypeId());
        if(rTypeId==0){ //全部
        }else if(rTypeId==1){ //商品订单
            // 订单状态《 -1： 充值订单  -2 ：手动发佣金和提现订单24小时未领红包 -3 ：活动订单24小时未领红包 -4 ：图文打赏订单》
            sqlCondition.append(JdbcUtil.appendNotIn("a.orderStatus","-1,-2,-3,-4"));
            int orderStatus = Toolkit.parseObjForInt(paramsQo.getOrderStatus());
            if(0==orderStatus){
            }else if(1==orderStatus || 2==orderStatus || 3==orderStatus){ //待发货,待收货,已收货
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",orderStatus));
            }else if(4==orderStatus){ //待退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",0));
            }else if(5==orderStatus){ //拒绝退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",2));
            }else if(6==orderStatus){ //已退货/退款
                sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",5));
                sqlCondition.append(JdbcUtil.appendAnd("a.refundStatus",1));
            }else if(7==orderStatus){//异常单
                sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","4,0"));
            }
        }else if(rTypeId==2){ //我的小店收费 9
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendAnd("a.agentRole",9));
        }else if(rTypeId==3){ //分销收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","5,6,7,8"));
        }else if(rTypeId==4){ //代理收费
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus",-1));
            sqlCondition.append(JdbcUtil.appendIn("a.agentRole","1,2,3,4"));
        }else if(rTypeId==5){ //红包回款
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-2,-3"));
        }else if(rTypeId==6){ //积分充值
            sqlCondition.append(JdbcUtil.appendIn("a.orderStatus","-1"));
            sqlCondition.append(JdbcUtil.appendIn("a.typeId","1"));
        }else if(rTypeId==7){ //图文打赏
            sqlCondition.append(JdbcUtil.appendAnd("a.orderStatus","-4"));
        }
        // 订单状态《 -1： 充值订单  -2 ：手动发佣金和提现订单24小时未领红包 -3 ：活动订单24小时未领红包 -4 ：图文打赏订单》
        String sql = "select ifnull(sum(a.payMoney),0) as totalMoney from (" +
                " select a.pay_id,a.order_num,a.status as orderStatus,b.refund_status as refundStatus," +
                " -1 as agentRole,a.user_id as payUserId,-1 as typeId,c.pay_money as payMoney,c.pay_end_date,c.transaction_id" +
                " from  order_info a left join order_refund b on " +
                " a.order_info_id = b.order_info_id left join pay_record c  on a.pay_id = c.pay_id " +
                " where 1=1 and ifnull(a.platform,0) = 1 and a.pay_id is not null and c.pay_status=1 and a.shop_id=" +shopId+
                " UNION" +
                " select a.pay_id,a.order_num,-4,-1,-1,a.user_id,-1,b.pay_money,b.pay_end_date,b.transaction_id " +
                " from image_text_tip a left join pay_record b ON a.pay_id = b.pay_id" +
                " where 1=1 and a.plat_form = 1 and a.pay_id IS NOT NULL and b.pay_status = 1 and a.shop_id="+shopId+
                " )a  where 1=1";
        return sql+sqlCondition;
    }

    @ApiOperation("收入")
    private String findRSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("ifnull(a.platform,0)",0));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        StringBuilder sqlConditionO1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionO2 = new StringBuilder(); //7日
        StringBuilder sqlConditionO3 = new StringBuilder(); //本月
        sqlConditionO1.append(" date(a.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionO2.append(" YEARWEEK(date_format(a.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionO3.append(" date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(JdbcUtil.appendAnd("ifnull(a.platform,0)",0));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));
        StringBuilder sqlConditionCzo1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionCzo2 = new StringBuilder(); //7日
        StringBuilder sqlConditionCzo3 = new StringBuilder(); //本月
        sqlConditionCzo1.append(" date(a.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionCzo2.append(" YEARWEEK(date_format(a.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionCzo3.append(" date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionRed = new StringBuilder(); //退回红包 分2部分 ，一部分是佣金没领退回，第二部分是活动红包没领退回
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));
        StringBuilder sqlConditionRed1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionRed2 = new StringBuilder(); //7日
        StringBuilder sqlConditionRed3 = new StringBuilder(); //本月
        sqlConditionRed1.append(" date(c.refund_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionRed2.append(" YEARWEEK(date_format(c.refund_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionRed3.append(" date_format(c.refund_time,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionDS = new StringBuilder(); //打赏订单
        sqlConditionDS.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionDS.append(JdbcUtil.appendAnd("a.plat_form",0));
        StringBuilder sqlConditionDS1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionDS2 = new StringBuilder(); //7日
        StringBuilder sqlConditionDS3 = new StringBuilder(); //本月
        sqlConditionDS1.append(" date(b.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionDS2.append(" YEARWEEK(date_format(b.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionDS3.append(" date_format(b.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        String sql = "select sum(a.yesterday) as yesterday,sum(a.week) as week,sum(a.month) as month,sum(a.ls)as ls from "+
                " (SELECT ifnull(sum(case when "+sqlConditionO1+
                " then a.real_price end ),0) as  yesterday," +
                " ifnull(sum(case when "+sqlConditionO2+
                " then a.real_price end ),0) as  week," +
                " ifnull(sum(case when "+sqlConditionO3 +
                " then a.real_price end ),0) as  month," +
                " ifnull(sum(a.real_price),0) as ls from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionCzo1+
                " THEN a.money END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionCzo2+
                " THEN a.money END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionCzo3 +
                " THEN a.money END ),0) AS MONTH," +
                " ifnull(sum(a.money),0) AS ls" +
                " FROM recharge_order a WHERE 1=1 and pay_id is not null" +sqlConditionCzo+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionRed1+
                " THEN c.amount END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionRed2+
                " THEN c.amount END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionRed3 +
                " THEN c.amount END ),0) AS MONTH," +
                " ifnull(sum(c.amount),0) AS ls" +
                " FROM activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionRed1+
                " THEN c.amount END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionRed2+
                " THEN c.amount END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionRed3 +
                " THEN c.amount END ),0) AS MONTH," +
                " ifnull(sum(c.amount),0) AS ls" +
                " from brokerage_record b left join wx_red_record c" +
                " on b.red_pay_id = c.red_pay_id" +
                " where 1=1 and b.red_pay_id is not null and ifnull(b.plat_form,0) = 0" +sqlConditionRed+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionDS1+
                " THEN b.pay_money END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionDS2+
                " THEN b.pay_money END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionDS3 +
                " THEN b.pay_money END ),0) AS MONTH," +
                " ifnull(sum(b.pay_money),0) AS ls" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1 "+sqlConditionDS+
                ")a";
        return sql;
    }
    @ApiOperation("支出 --佣金和退款和发送红包")
    private String findPSql(Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder(); // 佣金和提现
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        StringBuilder sqlConditionYj1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionYj2 = new StringBuilder(); //本周日
        StringBuilder sqlConditionYj3 = new StringBuilder(); //本月
        sqlConditionYj1.append(" date(a.send_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionYj2.append(" YEARWEEK(date_format(a.send_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionYj3.append(" date_format(a.send_time,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionO = new StringBuilder(); //订单退款
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        StringBuilder sqlConditionO1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionO2 = new StringBuilder(); //本周日
        StringBuilder sqlConditionO3 = new StringBuilder(); //本月
        sqlConditionO1.append(" date(a.agree_refund_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionO2.append(" YEARWEEK(date_format(a.agree_refund_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionO3.append(" date_format(a.agree_refund_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionRed = new StringBuilder(); //发放红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));
        StringBuilder sqlConditionRed1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionRed2 = new StringBuilder(); //7日
        StringBuilder sqlConditionRed3 = new StringBuilder(); //本月
        sqlConditionRed1.append(" date(c.create_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionRed2.append(" YEARWEEK(date_format(c.create_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionRed3.append(" date_format(c.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        String sql ="select sum(a.yesterday) as yesterday,sum(a.week) as week,sum(a.month) as month,sum(a.ls)as ls from " +
                " (SELECT ifnull(sum(case when " +sqlConditionYj1+
                " then a.send_money end),0) as  yesterday," +
                " ifnull(sum(case when " + sqlConditionYj2+
                " then a.send_money end ),0) as  week," +
                " ifnull(sum(case when " +sqlConditionYj3+
                " then a.send_money end ),0) as  month," +
                " ifnull(sum(a.send_money),0) as ls FROM brokerage_record a WHERE a.plat_form = 0" +sqlConditionYj+
                " union" +
                " select ifnull(sum(case when " +sqlConditionO1+
                " then a.refund_money end),0) as  yesterday," +
                " ifnull(sum(case when " +sqlConditionO2+
                " then a.refund_money end ),0) as  week," +
                " ifnull(sum(case when  " +sqlConditionO3+
                " then a.refund_money end ),0) as  month," +
                " ifnull(sum(a.refund_money),0) as ls" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionRed1+
                " THEN c.amount END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionRed2+
                " THEN c.amount END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionRed3 +
                " THEN c.amount END ),0) AS MONTH," +
                " ifnull(sum(c.amount),0) AS ls" +
                " FROM activity_user a " +
                " left join activity b on a.activity_id = b.id " +
                " left join wx_red_record c on a.red_pay_id = c.red_pay_id" +
                " where 1=1 and a.red_pay_id is not null " +sqlConditionRed+
                " ) a";
        return sql;
    }
    @ApiOperation("收入-wb")
    private String findRWbSql(long shopUid ,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionO = new StringBuilder(); //订单
        sqlConditionO.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("ifnull(a.platform,0)",1));
        sqlConditionO.append(jdbcUtil.appendIn("a.status","1,2,3,5"));
        StringBuilder sqlConditionO1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionO2 = new StringBuilder(); //7日
        StringBuilder sqlConditionO3 = new StringBuilder(); //本月
        sqlConditionO1.append(" date(a.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionO2.append(" YEARWEEK(date_format(a.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionO3.append(" date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionCzo = new StringBuilder(); //充值订单
        sqlConditionCzo.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionCzo.append(jdbcUtil.appendIn("a.status","1"));
        StringBuilder sqlConditionCzo1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionCzo2 = new StringBuilder(); //7日
        StringBuilder sqlConditionCzo3 = new StringBuilder(); //本月
        sqlConditionCzo1.append(" date(a.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionCzo2.append(" YEARWEEK(date_format(a.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionCzo3.append(" date_format(a.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionRed = new StringBuilder(); //退回红包 分2部分 ，一部分是佣金没领退回，第二部分是活动红包没领退回
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","6"));
        StringBuilder sqlConditionRed1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionRed2 = new StringBuilder(); //7日
        StringBuilder sqlConditionRed3 = new StringBuilder(); //本月
        sqlConditionRed1.append(" date(c.refund_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionRed2.append(" YEARWEEK(date_format(c.refund_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionRed3.append(" date_format(c.refund_time,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionDS = new StringBuilder(); //打赏订单
        sqlConditionDS.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlConditionDS.append(JdbcUtil.appendAnd("a.plat_form",1));
        StringBuilder sqlConditionDS1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionDS2 = new StringBuilder(); //7日
        StringBuilder sqlConditionDS3 = new StringBuilder(); //本月
        sqlConditionDS1.append(" date(b.pay_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionDS2.append(" YEARWEEK(date_format(b.pay_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionDS3.append(" date_format(b.pay_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        String sql = "select sum(a.yesterday) as yesterday,sum(a.week) as week,sum(a.month) as month,sum(a.ls)as ls from "+
                " (SELECT ifnull(sum(case when "+sqlConditionO1+
                " then a.real_price end ),0) as  yesterday," +
                " ifnull(sum(case when "+sqlConditionO2+
                " then a.real_price end ),0) as  week," +
                " ifnull(sum(case when "+sqlConditionO3 +
                " then a.real_price end ),0) as  month," +
                " ifnull(sum(a.real_price),0) as ls from order_info a where 1=1 "+sqlConditionO+
                " union" +
                " SELECT ifnull(sum(case when "+sqlConditionDS1+
                " THEN b.pay_money END),0) AS yesterday," +
                " ifnull(sum(case when "+sqlConditionDS2+
                " THEN b.pay_money END),0) AS WEEK," +
                " ifnull(sum(case when "+sqlConditionDS3 +
                " THEN b.pay_money END ),0) AS MONTH," +
                " ifnull(sum(b.pay_money),0) AS ls" +
                " from image_text_tip a left join pay_record b " +
                " ON a.pay_id = b.pay_id" +
                " where 1=1 and a.pay_id is not null and b.pay_status = 1  "+sqlConditionDS+
                ")a";
        return sql;
    }
    @ApiOperation("支出 wb --佣金和退款和发送红包")
    private String findPWbSql(long shopUid ,Integer shopId, DataParamsQo paramsQo) {
        StringBuilder sqlConditionYj = new StringBuilder(); // 佣金和提现
        sqlConditionYj.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        StringBuilder sqlConditionYj1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionYj2 = new StringBuilder(); //本周日
        StringBuilder sqlConditionYj3 = new StringBuilder(); //本月
        sqlConditionYj1.append(" date(a.send_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionYj2.append(" YEARWEEK(date_format(a.send_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionYj3.append(" date_format(a.send_time,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionO = new StringBuilder(); //订单退款
        sqlConditionO.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionO.append(JdbcUtil.appendAnd("ifnull(b.platform,0)",1));
        StringBuilder sqlConditionO1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionO2 = new StringBuilder(); //本周日
        StringBuilder sqlConditionO3 = new StringBuilder(); //本月
        sqlConditionO1.append(" date(a.agree_refund_date) = date_sub(curdate(),interval 1 day)");
        sqlConditionO2.append(" YEARWEEK(date_format(a.agree_refund_date,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionO3.append(" date_format(a.agree_refund_date,'%Y-%m')=date_format(now(),'%Y-%m')");

        StringBuilder sqlConditionRed = new StringBuilder(); //发放红包
        sqlConditionRed.append(JdbcUtil.appendAnd("b.shop_id",shopId));
        sqlConditionRed.append(jdbcUtil.appendIn("c.status","2,4,6"));
        StringBuilder sqlConditionRed1 = new StringBuilder(); //昨日
        StringBuilder sqlConditionRed2 = new StringBuilder(); //7日
        StringBuilder sqlConditionRed3 = new StringBuilder(); //本月
        sqlConditionRed1.append(" date(c.create_time) = date_sub(curdate(),interval 1 day)");
        sqlConditionRed2.append(" YEARWEEK(date_format(c.create_time,'%Y-%m-%d')) = YEARWEEK(now())");
        sqlConditionRed3.append(" date_format(c.create_time,'%Y-%m')=date_format(now(),'%Y-%m')");
        String sql =
                " select sum(a.yesterday) as yesterday,sum(a.week) as week,sum(a.month) as month,sum(a.ls)as ls from " +
                " (" +
                " select ifnull(sum(case when " +sqlConditionO1+
                " then a.refund_money end),0) as  yesterday," +
                " ifnull(sum(case when " +sqlConditionO2+
                " then a.refund_money end ),0) as  week," +
                " ifnull(sum(case when  " +sqlConditionO3+
                " then a.refund_money end ),0) as  month," +
                " ifnull(sum(a.refund_money),0) as ls" +
                " from order_refund a left join order_info b" +
                " on a.order_info_id = b.order_info_id where 1=1 and a.refund_status=1" +sqlConditionO+
                " ) a";
        return sql;
    }

    @ApiOperation("收入支出对账导出")
    public void exportCsv(HttpServletRequest request, HttpServletResponse response, Integer shopId, long shopUid) throws Exception {
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
            Integer tabIndex = Toolkit.parseObjForInt(paramsQo.getTabIndex());
            String sql = "";
            String header ="";
            String title ="";
            List<Map<String, Object>> list = new ArrayList<>();
            if(2==tabIndex.intValue()){
                header ="时间,收入分类,订单编号,收款流水号,交易平台,支付方式,订单金额(分),优惠金额(分),实收金额(分),昵称,购买人,联系方式,状态";
                if(1==paramsQo.getPlatForm()){ //wx
                    sql = findPageRSql(shopId,paramsQo);
                    list = jdbcUtil.queryList(sql);
                    for (Map map:list) {
                        map.put("nickName",Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
                    }
                }else if(2==paramsQo.getPlatForm()){ //wb
                    sql = findPageRWbSql(shopUid,shopId,paramsQo);
                    list = jdbcUtil.queryList(sql);
                }
                title = "收入对账";
            }else if(3==tabIndex.intValue()){
                header ="时间,支出分类,订单编号,付款流水号,支付平台,支付方式,金额(分),昵称,收款人,联系方式,状态";

                if(1==paramsQo.getPlatForm()){ //wx
                    sql = findPagePSql(shopId,paramsQo);
                    list = jdbcUtil.queryList(sql);
                    for (Map map:list) {
                        map.put("nickName",Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
                    }
                }else if(2==paramsQo.getPlatForm()){ //wb
                    sql = findPagePWbSql(shopUid,shopId,paramsQo);
                    list = jdbcUtil.queryList(sql);
                }
                title = "支出对账";
            }
            csvToolUtil.exportCsv(list,header,title,response);
    }

    @ApiOperation("微信昵称解码")
    private void converterNickName(PageItem page) {
        List<Map> list = page.getItems();
        for (Map map:list) {
            map.put("nickName",Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
        }
    }


}
