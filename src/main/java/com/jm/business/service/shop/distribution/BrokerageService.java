package com.jm.business.service.shop.distribution;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.shop.ChannelRecordQo;
import com.jm.mvc.vo.shop.CommVo;
import com.jm.mvc.vo.shop.distribution.*;
import com.jm.repository.jpa.order.OrderDetailRepository;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.product.ProductSpecRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageKitRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageOrderRepository;
import com.jm.repository.jpa.shop.distribution.JdbcBrokerageRepository;
import com.jm.repository.jpa.wb.WbUserAccountRepository;
import com.jm.repository.jpa.wx.WxUserAccountRepository;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductSpec;
import com.jm.repository.po.shop.brokerage.BrokerageOrder;
import com.jm.repository.po.shop.brokerage.WxAccountKit;
import com.jm.repository.po.wb.WbUserAccount;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * <p>佣金</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/12
 */

@Log4j
@Service
public class BrokerageService {

    @Autowired
    private JdbcBrokerageRepository jdbcBrokerageRepository;

    @Autowired
    private BrokerageOrderRepository brokerageOrderRepository;

    @Autowired
    private WxUserAccountRepository wxUserAccountRepository;

    @Autowired
    private WbUserAccountRepository wbUserAccountRepository;


    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSpecRepository productSpecRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private BrokerageKitRepository brokerageKitRepository;

    /**
     * 商家发放
     *
     * @param brokerageQo
     * @param shopId
     * @return
     */
    public PageItem<WxAccountVo> getWxAccountList(BrokerageQo brokerageQo, Integer shopId) throws IOException {
        return jdbcBrokerageRepository.getWxAccountList(brokerageQo, shopId);
    }

    public PageItem<WxAccountVo> getwbAccountList(BrokerageQo brokerageQo, Integer shopId) throws IOException {
        return jdbcBrokerageRepository.getwbAccountList(brokerageQo, shopId);
    }

    /**
     * 别的客户
     *
     * @param prokerageQo
     * @param shopId
     * @return
     */
//    public PageItem<Map<String, Object>> getBrokerageOtherList(BrokerageQo prokerageQo, Integer shopId) {
//        return jdbcBrokerageRepository.getBrokerageOtherList(prokerageQo, shopId);
//    }

    /**
     * 用户提现
     *
     * @param prokerageQo
     * @param shopId
     * @return
     */
    public PageItem<WxAccountKitVo> getWxAccountKitList(BrokerageQo prokerageQo, Integer shopId) throws IOException  {
        return jdbcBrokerageRepository.getWxAccountKitList(prokerageQo, shopId);
    }


    /**
     * 佣金流水
     *
     * @param brokerageRecordQo
     * @param shopId
     * @return
     */
    public PageItem<BrokerageRecordVo> getBrokerageRecord(BrokerageRecordQo brokerageRecordQo, Integer shopId)  throws IOException {
        return jdbcBrokerageRepository.getBrokerageRecord(brokerageRecordQo, shopId);
    }

    /**
     * 基于订单的佣金清单
     *
     * @param brokerageOrderQo
     * @param shopId
     * @return
     */
    public PageItem<BrokerageOrderVo> getBrokerageOrderList(BrokerageOrderQo brokerageOrderQo, Integer shopId)  throws IOException {
        return jdbcBrokerageRepository.queryBrokerageOrderList(brokerageOrderQo, shopId);
    }

    public PageItem<BrokerageProductVo> getBrokerageProductList(BrokerageProductQo brokerageProductQo, Integer shopId)  throws IOException{
        return jdbcBrokerageRepository.queryBrokerageProductList(brokerageProductQo, shopId);
    }

    /**
     * 确认收货时候修改佣金状态
     *
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateBrokerageOrder(Long orderInfoId) throws Exception {
        List<BrokerageOrder> list = brokerageOrderRepository.findByOrderInfoId(orderInfoId);
        List<BrokerageOrder> brokerageOrderList = new ArrayList<BrokerageOrder>();
        if (list != null && list.size() > 0) {
            for (BrokerageOrder brokerageOrder : list) {
                if (brokerageOrder.getStatus()==0){
                    brokerageOrder.setStatus(1);
                    brokerageOrder.setTakeDate(new Date());
                    brokerageOrderList.add(brokerageOrder);
                }
            }
            brokerageOrderRepository.save(brokerageOrderList);
        }
    }

    /**
     * 退款退货完成以后清除该订单佣金
     *
     * @param orderInfoId
     * @throws Exception
     */
    @Transactional
    public void refundBrokerageOrder(Long orderInfoId) throws Exception {
        List<BrokerageOrder> list = brokerageOrderRepository.findByOrderInfoId(orderInfoId);
        List<BrokerageOrder> brokerageOrderList = new ArrayList<BrokerageOrder>();
        if (list != null && list.size() > 0) {
            for (BrokerageOrder brokerageOrder : list) {
                brokerageOrder.setStatus(3);
                brokerageOrderList.add(brokerageOrder);
            }
            brokerageOrderRepository.save(brokerageOrderList);
        }
    }

    @Transactional
    public void refuse(Long orderInfoId) {
        //获取7天前的佣金订单
        List<BrokerageOrder> list = brokerageOrderRepository.findByOrderInfoId(orderInfoId);
        if (list != null && list.size() > 0) {
            for (BrokerageOrder brokerageOrder : list) {
                if (brokerageOrder.getStatus()==1) {
                    brokerageOrder.setStatus(2);
                    brokerageOrderRepository.save(brokerageOrder);// 修改用户佣金状态
                    saveWxUserAccount(brokerageOrder);//保存用户佣金账户金额
                }
            }
        }
    }

    /**
     * 收货15天后更新佣金状态
     */
    @Transactional
    public void updateBrokerage() {
//        Date dNow = new Date();   //当前时间
//        Calendar calendar = Calendar.getInstance(); //得到日历
//        calendar.setTime(dNow);//把当前时间赋给日历
//        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为前7天
//        Date dBefore = calendar.getTime();   //得到15天前的时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
//        String defaultStartDate = sdf.format(dBefore);    //格式化7天前
//        log.info("======defaultStartDate==========" + defaultStartDate);
        //获取7天前的佣金订单
        List<BrokerageOrder> list = brokerageOrderRepository.findByTakeDate();
        if (list != null && list.size() > 0) {
            for (BrokerageOrder brokerageOrder : list) {
                if (brokerageOrder.getStatus()==1){
                    if (brokerageOrder.getPlatForm()==0){
                        brokerageOrder.setStatus(2);
                        brokerageOrderRepository.save(brokerageOrder);// 修改用户佣金状态
                        saveWxUserAccount(brokerageOrder);//保存微信用户佣金账户金额
                    }else if(brokerageOrder.getPlatForm()==1){
                        brokerageOrder.setStatus(2);
                        brokerageOrderRepository.save(brokerageOrder);// 修改用户佣金状态
                        saveWbUserAccount(brokerageOrder);//保存微博用户佣金账户金额
                    }

                }
            }
        }
    }

    /**
     * 保存微信用户佣金账户金额
     *
     * @param brokerageOrder
     */
    private void saveWxUserAccount(BrokerageOrder brokerageOrder) {
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(brokerageOrder.getUserId(), 1);
        if (wxUserAccount == null) {
//            Brokerage brokerage = new Brokerage();
//            brokerage.setShopId(brokerageOrder.getShopId());
//            brokerage.setUserId(brokerageOrder.getUserId());
//            brokerage.setPlatForm(0);
            wxUserAccount = new WxUserAccount();
            wxUserAccount.setUserId(brokerageOrder.getUserId());
            wxUserAccount.setShopId(brokerageOrder.getShopId());
            wxUserAccount.setAccountType(1);
            wxUserAccount.setTotalBalance(brokerageOrder.getCommissionPrice());
            wxUserAccount.setKitBalance(brokerageOrder.getCommissionPrice());
            wxUserAccount.setTotalCount(brokerageOrder.getCommissionPrice());
            wxUserAccount.setCreateTime(new Date());
//            brokerageRepository.save(brokerage);
        } else {
            wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance() + brokerageOrder.getCommissionPrice());
            wxUserAccount.setKitBalance(wxUserAccount.getKitBalance() + brokerageOrder.getCommissionPrice());
            wxUserAccount.setTotalCount(wxUserAccount.getTotalCount() + brokerageOrder.getCommissionPrice());
            wxUserAccount.setUpdateTime(new Date());
        }
        wxUserAccountRepository.save(wxUserAccount);
    }

    /**
     * 保存微博用户佣金账户金额
     *
     * @param brokerageOrder
     */
    private void saveWbUserAccount(BrokerageOrder brokerageOrder) {
        WbUserAccount wbUserAccount = wbUserAccountRepository.findByUserIdAndAccountType(brokerageOrder.getUserId(), 1);
        if (wbUserAccount == null) {
//            Brokerage brokerage = new Brokerage();
//            brokerage.setShopId(brokerageOrder.getShopId());
//            brokerage.setUserId(brokerageOrder.getUserId());
//            brokerage.setPlatForm(0);
            wbUserAccount = new WbUserAccount();
            wbUserAccount.setUserId(brokerageOrder.getUserId());
            wbUserAccount.setShopId(brokerageOrder.getShopId());
            wbUserAccount.setAccountType(1);
            wbUserAccount.setTotalBalance(brokerageOrder.getCommissionPrice());
            wbUserAccount.setKitBalance(brokerageOrder.getCommissionPrice());
            wbUserAccount.setTotalCount(brokerageOrder.getCommissionPrice());
            wbUserAccount.setCreateTime(new Date());
//            brokerageRepository.save(brokerage);
        } else {
            wbUserAccount.setTotalBalance(wbUserAccount.getTotalBalance() + brokerageOrder.getCommissionPrice());
            wbUserAccount.setKitBalance(wbUserAccount.getKitBalance() + brokerageOrder.getCommissionPrice());
            wbUserAccount.setTotalCount(wbUserAccount.getTotalCount() + brokerageOrder.getCommissionPrice());
            wbUserAccount.setUpdateTime(new Date());
        }
        wbUserAccountRepository.save(wbUserAccount);
    }

    /**
     * 获取佣金清单详情
     *
     * @param orderInfoId
     * @return
     */
    public List<BrokerageDetailVo> getDetailList(Long orderInfoId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderInfoId(orderInfoId);
        List<Integer> products = new ArrayList();
        List<Integer> productSpecs = new ArrayList();
        List<BrokerageDetailVo> brokerageDetails = new ArrayList<BrokerageDetailVo>();
        for (OrderDetail orderDetail : orderDetails) {
            products.add(orderDetail.getPid());
            if (null != orderDetail.getProductSpecId()) {
                productSpecs.add(orderDetail.getProductSpecId());
            }
        }
        //商品列表
        List<Product> prods = productRepository.findAll(products);
        //商品规格列表
        List<ProductSpec> specs = productSpecRepository.findAll(productSpecs);
        //聚合数据
        brokerageDetails = doBrokerageDetails(brokerageDetails, orderDetails, prods, specs);
        return brokerageDetails;
    }

    private List<BrokerageDetailVo> doBrokerageDetails(List<BrokerageDetailVo> brokerageDetails, List<OrderDetail> orderDetails, List<Product> prods, List<ProductSpec> specs) {
        for (OrderDetail orderDetail : orderDetails) {
            BrokerageDetailVo brokerageDetailVo = new BrokerageDetailVo();
            BeanUtils.copyProperties(orderDetail, brokerageDetailVo);
            for (Product prod : prods) {
                if (prod.getPid().equals(brokerageDetailVo.getPid())) {
                    brokerageDetailVo.setName(prod.getName());
                    brokerageDetailVo.setPicSquare(ImgUtil.appendUrl(prod.getPicSquare(), 720));
                    break;
                }
            }
            for (ProductSpec spec : specs) {
                if (spec.getProductSpecId().equals(brokerageDetailVo.getProductSpecId())) {
                    brokerageDetailVo.setSpecValueOne(spec.getSpecValueOne());
                    brokerageDetailVo.setSpecValueTwo(spec.getSpecValueTwo());
                    brokerageDetailVo.setSpecValueThree(spec.getSpecValueThree());
                    break;
                }
            }
            brokerageDetails.add(brokerageDetailVo);
        }
        return brokerageDetails;
    }

    /**
     * 统计用户当天申请提现次数
     *
     * @param userId
     * @return
     */
    public int getKitCount(Integer userId,int type) {
        return jdbcBrokerageRepository.getKitCount(userId,type);
    }


    public Object getNowMonthSumPrice(Integer wxuserid) {
        return brokerageOrderRepository.getNowMonthSumPrice(wxuserid);
    }


    public List<CommVo> getCommTop20(Integer userid, Integer curpage) throws ParseException, IOException {
        String sql = "select u.headimgurl,u.nickname,b.total_price,b.commission_price,b.brokerage,b.order_date,o.status,ore.refund_status " +
                " from brokerage_order b " +
                " left join order_info o on o.order_info_id = b.order_info_id " +
                " left join wx_user u on u.user_id = o.user_id " +
                " left join order_refund ore on ore.order_info_id = o.order_info_id " +
                " where b.user_id="+userid+" order by b.order_date desc limit "+curpage+",10 ";
        List<Map<String, Object>> als = jdbcTemplate.queryForList(sql);
        List<CommVo> cmLs = new ArrayList<>();
        for (Map<String,Object> map : als){
            CommVo vo =   JsonMapper.map2Obj(map,CommVo.class);
            vo.setNickname(Base64Util.getFromBase64(vo.getNickname()));
            cmLs.add(vo);
        }
        return cmLs;
    }

    public PageItem<ChannelRecordVo> getChannelRecordList(ChannelRecordQo channelRecordQo, Integer shopId) throws IOException {
        return jdbcBrokerageRepository.getChannelRecordList(channelRecordQo,shopId);
    }

    public PageItemExt<BrokerageDetailListVo,Integer> getBrokerageDetailList(BrokerageDetailListQo brokerageDetailListQo) throws IOException {
        return jdbcBrokerageRepository.getBrokerageDetailList(brokerageDetailListQo);
    }

    @Transactional
    public void saveKitAndAccount(WxUserSession wxUserSession,int type,int kitMoney,WxUserAccount wxUserAccount,Integer unit) {
        WxAccountKit kit = new WxAccountKit();//提现申请表
        kit.setShopId(wxUserSession.getShopId());
        kit.setUserId(wxUserSession.getUserId());
        kit.setPlatForm(0);
        kit.setType(type);
        kit.setKitDate(new Date());
        kit.setKitMoney(kitMoney);
        kit.setStatus(0);
        brokerageKitRepository.save(kit);
        if(unit==null){
            wxUserAccount.setKitBalance(wxUserAccount.getKitBalance()-kitMoney);
            wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance()-kitMoney);
        }else{
            wxUserAccount.setKitBalance(wxUserAccount.getKitBalance()-kitMoney/100*unit);
            wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance()-kitMoney/100*unit);
        }
        wxUserAccountRepository.save(wxUserAccount);
    }



}
