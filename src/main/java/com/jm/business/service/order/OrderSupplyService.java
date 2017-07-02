package com.jm.business.service.order;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderDetailVo;
import com.jm.mvc.vo.order.OrderInfoForQueryVo;
import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.mvc.vo.order.SupplyForQueryVo;
import com.jm.mvc.vo.system.UserVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.converter.order.OrderConverter;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>地区供货下的订单</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/12/7
 */

@Service
public class OrderSupplyService {

    @Autowired
    protected JdbcUtil jdbcUtil;

    @Autowired
    private OrderService orderService;

    public PageItem<OrderInfoVo> queryOrderSupply(OrderInfoForQueryVo orderVo,int type) throws IOException {
        String orderInfoIds = "";
        PageItem<OrderInfoVo> pageItem = new PageItem<>();
        pageItem = queryOrderSupplyList(orderVo,type);
        List<OrderInfoVo> orderInfoVos = pageItem.getItems();
        for (OrderInfoVo orderInfoVo : orderInfoVos) {
            orderInfoIds += orderInfoVo.getOrderInfoId() + ",";
        }
        List<Map<String, Object>> orderDetails = new ArrayList<>();
        if(orderInfoIds.length()>1){
            orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
            orderDetails = queryOrderSupplyDetails(orderInfoIds);
        }
        PageItem<OrderInfoVo> pageItemRes = new PageItem<>();
        if (orderInfoVos == null || orderInfoVos.size() == 0) {
            pageItemRes.setCount(0);
            pageItemRes.setItems(orderInfoVos);
            return pageItemRes;
        }
        //合并订单列表跟 订单详情列表
        for (OrderInfoVo orderInfoVo : orderInfoVos) {
            for (Map<String, Object> map : orderDetails) {
                if (orderInfoVo.getOrderInfoId().equals(map.get("order_info_id"))) {
                    OrderDetailVo orderDetailVo = new OrderDetailVo();
                    orderDetailVo = orderService.getOrderInfoVo(map,orderInfoVo,orderDetailVo);
                    orderInfoVo.getOrderDetails().add(orderDetailVo);
                }
            }
        }
        pageItemRes.setCount(Integer.parseInt(pageItem.getCount() + ""));
        pageItemRes.setItems(orderInfoVos);
        return pageItemRes;
    }

    public PageItem<OrderInfoVo> queryOrderSupply(SupplyForQueryVo supplyForQueryVo,int type) throws IOException {
        String orderInfoIds = "";
        PageItem<OrderInfoVo> pageItem = new PageItem<>();
        pageItem = querySupplyConfigureList(supplyForQueryVo,type);
        List<OrderInfoVo> orderInfoVos = pageItem.getItems();
        for(OrderInfoVo orderInfoVo : orderInfoVos){
            orderInfoIds += orderInfoVo.getOrderInfoId() + ",";
        }
        List<Map<String, Object>> orderDetails = new ArrayList<>();
        if(orderInfoIds.length()>1){
            orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
            orderDetails = querySupplyConfigureDetails(supplyForQueryVo,orderInfoIds);
        }
        PageItem<OrderInfoVo> pageItemRes = new PageItem<>();
        if (orderInfoVos == null || orderInfoVos.size() == 0) {
            pageItemRes.setCount(0);
            pageItemRes.setItems(orderInfoVos);
            return pageItemRes;
        }
        //合并订单列表跟 订单详情列表
        for (OrderInfoVo orderInfoVo : orderInfoVos) {
            for (Map<String, Object> map : orderDetails) {
                if (orderInfoVo.getOrderInfoId().equals(map.get("order_info_id"))) {
                    OrderDetailVo orderDetailVo = new OrderDetailVo();
                    orderDetailVo = orderService.getOrderInfoVo(map,orderInfoVo,orderDetailVo);
                    orderInfoVo.getOrderDetails().add(orderDetailVo);
                }
            }
        }
        pageItemRes.setCount(Integer.parseInt(pageItem.getCount() + ""));
        pageItemRes.setItems(orderInfoVos);
        return pageItemRes;
    }

    private PageItem<OrderInfoVo> queryOrderSupplyList(OrderInfoForQueryVo orderVo,int type) throws IOException {
        String sqlList = "select oi.order_info_id,oi.order_num,oi.delivery_note,oi.create_date,oi.status,oi.shop_id,"
                       + "oi.remark,oi.user_id,oi.send_fee,oi.seller_note,oi.total_price,s.shop_name,wx.headimgurl,"
                       + "wx.nickname,rr.refund_id,pr.transaction_id,wxadd.user_name,wxadd.phone_number,"
                       + "org.good_status,wxadd.detail_address from order_info oi LEFT JOIN wx_user wx on wx.user_id=oi.user_id "
                       + "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
                       + "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
                       + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
                       + "LEFT JOIN shop s ON s.shop_id=oi.shop_id "
                       + "LEFT JOIN order_detail od ON od.order_info_id=oi.order_info_id "
                       + "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id "
                       + "where od.supply_user_id is not null and od.supply_user_id!=0";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("oi.user_id",orderVo.getUserId()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",orderVo.getShopId()));
        if(!StringUtils.isEmpty(orderVo.getStatus())){
            if (11!=orderVo.getStatus()) {
                sqlCondition.append(JdbcUtil.appendAnd("oi.status",orderVo.getStatus()));
            }else{
                if(type==2){
                    sqlCondition.append(JdbcUtil.appendNotAnd("oi.status",4));
                }
            }
        }
        sqlCondition.append(JdbcUtil.appendAnd("wxadd.phone_number",orderVo.getConsigneePhone()));
        sqlCondition.append(JdbcUtil.appendAnd("wxadd.user_name",orderVo.getConsigneeName()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.order_num",orderVo.getOrderNum()));
        sqlCondition.append(JdbcUtil.appendAnd("pr.transaction_id",orderVo.getTransactionId()));
        sqlCondition.append(JdbcUtil.appendAnd("rr.refund_id",orderVo.getRefundId()));
        if(!StringUtils.isEmpty(orderVo.getNickname())) {
            sqlCondition.append(JdbcUtil.appendAnd("wx.nickname", Base64Util.enCoding(orderVo.getNickname())));
        }
        sqlCondition.append(JdbcUtil.appendAnd("oi.create_date",orderVo.getOrderBeginDate(),orderVo.getOrderEndDate()));
        if (orderVo.getGoodStatus() == 1) {
            //退货管理条件查询
            sqlCondition.append(JdbcUtil.appendAnd("oi.good_status",orderVo.getGoodStatus()));
        }
        sqlCondition.append(JdbcUtil.appendGroupBy("oi.order_info_id"));
        sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,orderVo.getCurPage(),orderVo.getPageSize());
        return OrderConverter.p2v(pageItem);
    }

    private PageItem<OrderInfoVo> querySupplyConfigureList(SupplyForQueryVo supplyForQueryVo,int type) throws IOException {
        String sqlList = "select oi.order_info_id,oi.order_num,oi.status,oi.shop_id,oi.user_id,"
                + "oi.send_date,s.shop_name,rr.refund_id,org.good_status from order_info oi "
                + "LEFT JOIN order_detail od on od.order_info_id=oi.order_info_id "
                + "LEFT JOIN refund_recod rr on rr.order_id=oi.order_info_id "
                + "LEFT JOIN order_refund_goods org on org.order_info_id=oi.order_info_id "
                + "LEFT JOIN wx_user_address wxadd on wxadd.id=oi.user_addr_id "
                + "LEFT JOIN shop s ON s.shop_id=oi.shop_id "
                + "LEFT JOIN pay_record pr ON pr.pay_id=oi.pay_id "
                + "LEFT JOIN user u ON u.user_id=od.supply_user_id "
                + "LEFT JOIN product p ON p.pid = od.pid "
                + "where od.supply_user_id IS NOT NULL and od.supply_user_id!=0 "
                + "and oi.status!=4 and oi.status!=0";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("oi.shop_id",supplyForQueryVo.getShopId()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.status",supplyForQueryVo.getStatus()));
        sqlCondition.append(JdbcUtil.appendAnd("s.shop_name",supplyForQueryVo.getShopName()));
        sqlCondition.append(JdbcUtil.appendAnd("u.user_name",supplyForQueryVo.getShopUserName()));
        sqlCondition.append(JdbcUtil.appendAnd("p.name",supplyForQueryVo.getProductName()));
        sqlCondition.append(JdbcUtil.appendAnd("oi.send_date",supplyForQueryVo.getSupplySettlementBeginDate(),supplyForQueryVo.getSupplySettlementEndDate()));
        sqlCondition.append(JdbcUtil.appendGroupBy("oi.order_info_id"));
        sqlCondition.append(JdbcUtil.appendOrderBy("oi.order_info_id"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList + sqlCondition, supplyForQueryVo.getCurPage(), supplyForQueryVo.getPageSize());
        return OrderConverter.p2v(pageItem);
    }

    private List<Map<String, Object>> queryOrderSupplyDetails(String ids){
        String sqlList = "select od.order_info_id,p.pid,p.NAME,p.pic_square,od.price,od.count,ps.product_spec_id,"
                       + "ps.spec_pic,ps.spec_value_one,ps.spec_value_two,ps.spec_value_three "
                       + "FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
                       + "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
                       + "LEFT JOIN product_area_rel par ON par.user_id = od.supply_user_id "
                       + "WHERE od.supply_user_id IS NOT NULL and od.supply_user_id!=0 "
                       + "and od.order_info_id in("+ids+") group by od.order_detail_id";
        return jdbcUtil.queryList(sqlList);
    }

    private List<Map<String, Object>> querySupplyConfigureDetails(SupplyForQueryVo supplyForQueryVo,String ids){
        String sqlList = "select od.order_info_id,p.pid,p.NAME,p.pic_square,od.price,od.count,od.supply_price,"
                + "od.supply_user_id,ps.product_spec_id,ps.spec_pic,u.user_name as supply_user_name "
                + "FROM order_detail od LEFT JOIN product p ON p.pid = od.pid "
                + "LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
                + "LEFT JOIN product_area_rel par ON par.user_id = od.supply_user_id "
                + "LEFT JOIN user u ON u.user_id = od.supply_user_id "
                + "WHERE od.supply_user_id IS NOT NULL and od.supply_user_id!=0 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("p.NAME",supplyForQueryVo.getProductName()));
        sqlCondition.append(JdbcUtil.appendIn("od.order_info_id",ids));
        sqlCondition.append(JdbcUtil.appendGroupBy("od.order_detail_id"));
        return jdbcUtil.queryList(sqlList + sqlCondition);
    }

    public UserVo queryShop(Integer shopId) throws IOException {
        String sqlList = " select u.user_name,u.phone_number from shop s"
                       + " LEFT JOIN user_role ur ON ur.shop_id=s.shop_id"
                       + " LEFT JOIN user u ON u.user_id=ur.user_id"
                       + " where ur.role_id=2 and s.shop_id="+shopId;
        List<Map<String, Object>> maps = jdbcUtil.queryList(sqlList);
        UserVo userVo = new UserVo();
        userVo = m2v(maps,userVo);
        return userVo;
    }

    private UserVo m2v(List<Map<String, Object>> maps,UserVo userVo) throws IOException {
        for (Map<String,Object> map : maps){
            userVo = JsonMapper.map2Obj(map,UserVo.class);
        }
        return userVo;
    }
}
