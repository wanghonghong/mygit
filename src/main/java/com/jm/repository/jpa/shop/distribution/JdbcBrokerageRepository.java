package com.jm.repository.jpa.shop.distribution;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.shop.ChannelRecordQo;
import com.jm.mvc.vo.shop.distribution.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.shop.brokerage.Brokerage;
import com.jm.staticcode.converter.order.OrderConverter;
import com.jm.staticcode.converter.shop.distribution.BrokerageConverter;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>JDBC佣金模块查询</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Slf4j
@Repository
public class JdbcBrokerageRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcUtil jdbcUtil;

    /**
     * 商家发放New
     * @param brokerageQo
     * @param shopId
     * @return
     */
    public PageItem<WxAccountVo> getWxAccountList(BrokerageQo brokerageQo, Integer shopId) throws IOException {
        String sqlList ="SELECT w.user_id, w.headimgurl, w.nickname, s.user_name, s.phone_number, u.total_count, u.kit_balance, u.total_balance, s.agent_role, s.send_times FROM wx_user_account u RIGHT JOIN wx_user w ON u.user_id = w.user_id LEFT JOIN shop_user s ON s.id = w.shop_user_id WHERE 1=1  ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("u.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("s.phone_number",brokerageQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",brokerageQo.getUserName()));
        if(StringUtil.isNotNull(brokerageQo.getNickname())){
            sqlCondition.append(JdbcUtil.appendLike("w.nickname",Base64Util.enCoding(brokerageQo.getNickname())));
        }
        if(brokerageQo.getType()==0){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",1));
            sqlCondition.append(JdbcUtil.appendAnd("u.kit_balance",0));
        }
        if(brokerageQo.getType()==1){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",1));
            sqlCondition.append(JdbcUtil.appendMore("u.kit_balance",0));
        }
        if(brokerageQo.getType()==2){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",0));
        }
        sqlCondition.append(JdbcUtil.appendAnd("u.account_type",brokerageQo.getAccountType()));
        sqlCondition.append(JdbcUtil.appendOrderBy("u.kit_balance"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageQo.getCurPage(),brokerageQo.getPageSize());
        return BrokerageConverter.WxAccount2v(pageItem);

    }

    /**
     * 微博待发放
     * @param brokerageQo
     * @param shopId
     * @return
     */
    public PageItem<WxAccountVo> getwbAccountList(BrokerageQo brokerageQo, Integer shopId) throws IOException {
        String sqlList ="SELECT w.user_id, w.headimgurl, w.nickname, s.user_name, s.phone_number, u.total_count, u.kit_balance, u.total_balance, s.agent_role, s.send_times FROM wx_user_account u RIGHT JOIN wx_user w ON u.user_id = w.user_id LEFT JOIN shop_user s ON s.id = w.shop_user_id WHERE 1=1  ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("u.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("s.phone_number",brokerageQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",brokerageQo.getUserName()));
        if(StringUtil.isNotNull(brokerageQo.getNickname())){
            sqlCondition.append(JdbcUtil.appendLike("w.nickname",Base64Util.enCoding(brokerageQo.getNickname())));
        }
        if(brokerageQo.getType()==0){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",1));
            sqlCondition.append(JdbcUtil.appendAnd("u.kit_balance",0));
        }
        if(brokerageQo.getType()==1){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",1));
            sqlCondition.append(JdbcUtil.appendMore("u.kit_balance",0));
        }
        if(brokerageQo.getType()==2){
            sqlCondition.append(JdbcUtil.appendAnd("w.is_subscribe",0));
        }
        sqlCondition.append(JdbcUtil.appendAnd("u.account_type",brokerageQo.getAccountType()));
        sqlCondition.append(JdbcUtil.appendOrderBy("u.kit_balance"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageQo.getCurPage(),brokerageQo.getPageSize());
        return BrokerageConverter.WxAccount2v(pageItem);

    }

    /**
     * 用户提现New
     * @param brokerageQo
     * @param shopId
     * @return
     */
    public PageItem<WxAccountKitVo> getWxAccountKitList(BrokerageQo brokerageQo, Integer shopId)  throws IOException {
        String sqlList ="SELECT k.id, w.user_id, w.headimgurl, w.nickname, s.user_name, s.phone_number,s.agent_role, a.total_count, a.total_balance, a.kit_balance, k.plat_form, k.kit_date, k.kit_money, k.status FROM wx_account_kit k LEFT JOIN wx_user w ON k.user_id = w.user_id LEFT JOIN shop_user s ON w.shop_user_id = s.id LEFT JOIN wx_user_account a ON k.user_id = a.user_id AND a.account_type = 1 WHERE 1 = 1  ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("k.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("s.phone_number",brokerageQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",brokerageQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendAnd("k.type",brokerageQo.getAccountType()));
        if(StringUtil.isNotNull(brokerageQo.getNickname())){
            sqlCondition.append(JdbcUtil.appendLike("w.nickname",Base64Util.enCoding(brokerageQo.getNickname())));
        }
        if(brokerageQo.getPlatForm() != -1){
            sqlCondition.append(JdbcUtil.appendAnd("k.plat_form",brokerageQo.getPlatForm()));
        }
        sqlCondition.append(JdbcUtil.appendDateOr("k.kit_date",brokerageQo.getStartDate(),brokerageQo.getEndDate()));
        sqlCondition.append(JdbcUtil.appendOrderBy("k.kit_date"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageQo.getCurPage(),brokerageQo.getPageSize());
        return BrokerageConverter.WxAccountKit2v(pageItem);
    }



    /**
     * 佣金流水
     * @param brokerageRecordQo
     * @param shopId
     * @return
     */
    public PageItem<BrokerageRecordVo> getBrokerageRecord(BrokerageRecordQo brokerageRecordQo, Integer shopId) throws IOException {
        String sqlList = " SELECT r.id, w.user_id, r.send_num, w.headimgurl, w.nickname, s.user_name, s.phone_number, s.agent_role, r.send_time, r.send_money, r.plat_form, r.put_type, r.auto_type, wr.status, r.shop_id FROM brokerage_record r LEFT JOIN wx_user w ON r.user_id = w.user_id LEFT JOIN shop_user s ON s.id = w.shop_user_id LEFT JOIN wx_red_record wr ON wr.red_pay_id = r.red_pay_id WHERE 1 = 1   ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("r.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("s.phone_number",brokerageRecordQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("r.send_num",brokerageRecordQo.getSendNum()));
        if(brokerageRecordQo.getPlatForm() != -1){
            sqlCondition.append(JdbcUtil.appendAnd("r.plat_form",brokerageRecordQo.getPlatForm()));
        }
        if(StringUtil.isNotNull(brokerageRecordQo.getNickname())){
            sqlCondition.append(JdbcUtil.appendLike("w.nickname",Base64Util.enCoding(brokerageRecordQo.getNickname())));
        }
        if(brokerageRecordQo.getPutType() != -1) {
            sqlCondition.append(JdbcUtil.appendAnd("r.put_type", brokerageRecordQo.getPutType()));
        }
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",brokerageRecordQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendDateOr("r.send_time",brokerageRecordQo.getStartDate(),brokerageRecordQo.getEndDate()));
        if(StringUtil.isNotNull(brokerageRecordQo.getUserName())){
            sqlCondition.append(JdbcUtil.appendLike("s.user_name",Base64Util.enCoding(brokerageRecordQo.getUserName())));
        }
        if(brokerageRecordQo.getStatus() != -1){
            sqlCondition.append(JdbcUtil.appendAnd("wr.status",brokerageRecordQo.getStatus()));
        }
        sqlCondition.append(JdbcUtil.appendOrderBy("r.send_time "));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageRecordQo.getCurPage(),brokerageRecordQo.getPageSize());
        return BrokerageConverter.brokerageRecord2v(pageItem);
    }

    /**
     * 基于订单的佣金清单new
     * @param brokerageOrderQo
     * @param shopId
     * @return
     */
    public PageItem<BrokerageOrderVo> queryBrokerageOrderList(BrokerageOrderQo brokerageOrderQo, Integer shopId) throws IOException {
        String sqlList ="SELECT w.user_id, r.id, o.order_num, r.order_info_id, w.headimgurl, w.nickname, s.user_name, s.phone_number,s.agent_role, r.total_price, r.brokerage, r.commission_price, r.plat_form, r.order_date, r.status FROM brokerage_order r LEFT JOIN wx_user w ON r.user_id = w.user_id LEFT JOIN order_info o ON o.order_info_id = r.order_info_id LEFT JOIN shop_user s ON s.id = w.shop_user_id WHERE 1 = 1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("r.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendLike("s.phone_number",brokerageOrderQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("o.order_num",brokerageOrderQo.getOrderNum()));
        if(brokerageOrderQo.getPlatForm() != -1){
            sqlCondition.append(JdbcUtil.appendAnd("r.plat_form",brokerageOrderQo.getPlatForm()));
        }
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",brokerageOrderQo.getUserName()));
        if(StringUtil.isNotNull(brokerageOrderQo.getNickname())){
            sqlCondition.append(JdbcUtil.appendLike("w.nickname",Base64Util.enCoding(brokerageOrderQo.getNickname())));
        }
        if(brokerageOrderQo.getStatus()==1){
            sqlCondition.append(JdbcUtil.appendIn("r.status","0,1"));
        }
        if(brokerageOrderQo.getStatus()==2){
            sqlCondition.append(JdbcUtil.appendIn("r.status","2,3"));
        }
        sqlCondition.append(JdbcUtil.appendAnd("r.type",brokerageOrderQo.getType()));
        sqlCondition.append(JdbcUtil.appendDateOr("r.order_date",brokerageOrderQo.getStartDate(),brokerageOrderQo.getEndDate()));
        sqlCondition.append(JdbcUtil.appendOrderBy(" r.order_date  "));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageOrderQo.getCurPage(),brokerageOrderQo.getPageSize());
        return BrokerageConverter.brokerageOrder2v(pageItem);
    }

    /**
     * 获取佣金商品列表
     * @param brokerageProductQo
     * @param shopId
     * @return
     */
    public PageItem<BrokerageProductVo> queryBrokerageProductList(BrokerageProductQo brokerageProductQo, Integer shopId) throws IOException{
        String sqlList ="select b.id,p.pid,p.name,b.one_brokerage,b.two_brokerage,b.exclude_role from product p LEFT JOIN brokerage_product b on p.pid=b.pid where p.gift=1  ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("p.shop_id",shopId));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageProductQo.getCurPage(),brokerageProductQo.getPageSize());
        return BrokerageConverter.brokerageProduct2v(pageItem);
    }


    /***
     * 用户当天提现次数
     * @param userId
     * @return
     */
    public int getKitCount(Integer userId,int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String strToday = sdf.format(today);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        String strTomday = sdf.format(calendar.getTime());
        String sql = "select k.user_id,k.kit_date from wx_account_kit k WHERE 1=1  " +
                " and k.kit_date  BETWEEN '"+strToday+"' and '"+strTomday+"'"+
                " and k.user_id="+userId +
                " and k.type="+type;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        int count = list.size();
        return count;
    }

    public PageItem<ChannelRecordVo> getChannelRecordList(ChannelRecordQo channelRecordQo, Integer shopId) throws IOException {
        String sqlList ="SELECT r.order_info_id, r.order_num, r.user_id, w.headimgurl, w.nickname, s.user_name,s.phone_number,s.agent_role, r.agent_role, r.money, r.pay_date FROM recharge_order r LEFT JOIN wx_user w ON r.user_id = w.user_id LEFT JOIN shop_user s ON s.id = w.shop_user_id WHERE 1 = 1 AND STATUS = 1 and type = 2 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("r.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendLike("s.user_name",channelRecordQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("s.phone_number",channelRecordQo.getPhoneNumber()));
        if(channelRecordQo.getAgentRole() != -1){
            sqlCondition.append(JdbcUtil.appendAnd("r.agent_role",channelRecordQo.getAgentRole()));
        }
        sqlCondition.append(JdbcUtil.appendAnd("w.nickname",channelRecordQo.getNickname()));
        sqlCondition.append(JdbcUtil.appendAnd("r.pay_date",channelRecordQo.getStartDate(),channelRecordQo.getEndDate()));
        sqlCondition.append(JdbcUtil.appendOrderBy("r.pay_date "));
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,channelRecordQo.getCurPage(),channelRecordQo.getPageSize());
        //"select * from order_detail d LEFT JOIN order_info o on d.order_info_id=o.order_info_id where pid=27 and o.status >0 and o.`status` !=4"
        return BrokerageConverter.ChannelRecord2v(pageItem);
    }

    public PageItemExt<BrokerageDetailListVo,Integer> getBrokerageDetailList(BrokerageDetailListQo brokerageDetailListQo) throws IOException {
        String sqlList ="SELECT b.order_info_id, b.take_date, b.brokerage, b.total_price, b.commission_price,o.order_num FROM brokerage_order b LEFT JOIN order_info o on b.order_info_id=o.order_info_id where 1=1 ";
        String sqlList2="select  sum(b.commission_price) as commissionPrice from brokerage_order b where 1=1 ";
        Integer commissionPrice=0;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("b.user_id",brokerageDetailListQo.getUserId()));
        sqlCondition.append(JdbcUtil.appendAnd("b.status",brokerageDetailListQo.getStatus()));
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,brokerageDetailListQo.getCurPage(),brokerageDetailListQo.getPageSize());
        Map<String,Object> map = jdbcTemplate.queryForMap(sqlList2+sqlCondition);
        if ("null" != String.valueOf(map.get("commissionPrice"))){
//			count = (int) map.get("count");
            commissionPrice =Integer.parseInt(map.get("commissionPrice").toString());
            log.info("------------commissionPrice--------------"+commissionPrice);
        }
        return BrokerageConverter.BrokerageDetail2v(pageItem,commissionPrice);
    }


}
