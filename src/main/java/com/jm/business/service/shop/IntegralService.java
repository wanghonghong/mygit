package com.jm.business.service.shop;

import com.jm.business.domain.shop.AwardIntegralDo;
import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.wx.WxMessageService;
import com.jm.business.service.wx.WxService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderDetailCreateVo;
import com.jm.mvc.vo.shop.WxUserAccountVo;
import com.jm.mvc.vo.shop.integral.*;
import com.jm.mvc.vo.shop.recharge.RechargeOrderCo;
import com.jm.mvc.vo.shop.recharge.RechargeOrderVo;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderDiscountRepository;
import com.jm.repository.jpa.order.recycle.RecycleDetailRepository;
import com.jm.repository.jpa.order.recycle.RecycleRewardRepository;
import com.jm.repository.jpa.shop.RechargeOrderRepository;
import com.jm.repository.jpa.shop.distribution.BrokerageOrderRepository;
import com.jm.repository.jpa.shop.integral.IntegralProductRepository;
import com.jm.repository.jpa.shop.integral.IntegralRecordRepository;
import com.jm.repository.jpa.shop.integral.IntegralSetRepository;
import com.jm.repository.jpa.wx.WxUserAccountRepository;
import com.jm.repository.jpa.wx.WxUserRepository;
import com.jm.repository.po.order.OrderBook;
import com.jm.repository.po.order.OrderDiscount;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.recycle.RecycleDetail;
import com.jm.repository.po.order.recycle.RecycleReward;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.brokerage.BrokerageOrder;
import com.jm.repository.po.shop.integral.IntegralProduct;
import com.jm.repository.po.shop.integral.IntegralRecord;
import com.jm.repository.po.shop.integral.IntegralSet;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.IntegralConverter;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>积分服务</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/09/30
 */
@Log4j
@Service
public class IntegralService {

    @Autowired
    private IntegralProductRepository integralProductRepository;

    @Autowired
    protected IntegralSetRepository integralSetRepository;

    @Autowired
    private WxUserAccountRepository wxUserAccountRepository;

    @Autowired
    private IntegralRecordRepository integralRecordRepository;

    @Autowired
    private RechargeOrderRepository rechargeOrderRepository;

    @Autowired
    protected JdbcUtil jdbcUtil;

    @Autowired
    private BrokerageOrderRepository brokerageOrderRepository;

    @Autowired
    private OrderDiscountRepository orderDiscountRepository;

    @Autowired
    private RecycleRewardRepository recycleRewardleRepository;

    @Autowired
    private RecycleDetailRepository recycleDetailRepository;

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private WxMessageService wxMessageService;

    @Autowired
    private WxPubAccountService wxPubAccountService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private WxService wxService;
    /**
     * 订单奖励
     * @param orderBook
     */

    @Transactional
    public void  setRecycleAward(OrderBook orderBook) throws Exception {
        IntegralSet integralSet = getCacheIntegralSet(orderBook.getShopId());
        RecycleReward recycleReward = recycleRewardleRepository.findRecyCleRewardByShopId(orderBook.getShopId());
        if(recycleReward!=null && recycleReward.getRewardType()!=null){
            if (recycleReward.getRewardType().equals(0)){//积分发放
                if(integralSet!=null &&  integralSet.getIsOpen()==1){//积分开通
                    sendIntegralRecycleReward(orderBook,integralSet);
                }
            }else if(recycleReward.getRewardType().equals(1)){//红包发放

                //sendRedRecycleReward();
            }
        }
        sendRed(orderBook);
    }

    private void sendRed(OrderBook orderBook) throws Exception {
        WxUser wxUser = wxUserRepository.findWxUserByUserId(orderBook.getUserId());
        Shop shop = shopService.getCacheShop(orderBook.getShopId());
        WeixinActInfo requestParam = getWeixinParam(null, wxUser, shop, 106);
        wxService.sendRed(requestParam);
    }

    /**
     * 分装微信红包请求信息
     * @param wxUser
     * @param shop
     * @param balance
     * @return
     */
    public WeixinActInfo getWeixinParam(String ip, WxUser wxUser, Shop shop, int balance) {
        WeixinActInfo requestParam = new WeixinActInfo();
        //获取微信公众账号信息
        WxPubAccount wxPubAccount = wxPubAccountService.findWxPubAccountByAppid(wxUser.getAppid());
        //获取店铺信息
        requestParam.setWxappid(wxPubAccount.getAppid());
        requestParam.setMchId(wxPubAccount.getMchId());
        requestParam.setAppkey(wxPubAccount.getAppKey());
        requestParam.setSendName(shop.getShopName());//店铺名称
        requestParam.setReOpenid(wxUser.getOpenid());
        requestParam.setTotalAmount(balance);
        if(ip==null){
            requestParam.setClientIp("127.0.0.1");//调用接口的机器Ip地址
        }else{
            requestParam.setClientIp(ip);//调用接口的机器Ip地址
        }
        requestParam.setTotalNum(1);
        requestParam.setWishing("感谢参与环保公益事业");
        requestParam.setActName("回大叔感谢");
        requestParam.setRemark("回大叔感谢");
        return requestParam;
    }

    /**
     * 回收订单积分发放奖励
     * @param orderBook
     */
    private void sendIntegralRecycleReward(OrderBook orderBook ,IntegralSet integralSet)  throws Exception{
        RecycleDetail recycleDetail = recycleDetailRepository.findRecycleDetailByOrderId(orderBook.getId());
        WxUser wxuser = wxUserRepository.findWxUserByUserId(orderBook.getUserId());
        //发放奖励
        if (recycleDetail.getReward()==0){
            //本人奖励
            if (recycleDetail.getRewardCount()>0){
                saveRecycleReward(recycleDetail,orderBook,wxuser, integralSet);
            }

            //一级奖励
            if (recycleDetail.getUpperCount()>0){
                if (wxuser.getUpperOne() !=null && wxuser.getUpperOne()>0){
                    WxUser oneUser = wxUserRepository.findWxUserByUserId(wxuser.getUpperOne());
                    saveOneRecycleReward(recycleDetail,orderBook,wxuser,oneUser, integralSet);
                }
            }
        }
        recycleDetail.setReward(1);
        recycleDetailRepository.save(recycleDetail);
    }

    private void saveRecycleReward(RecycleDetail recycleDetail, OrderBook orderBook, WxUser wxuser,IntegralSet integralSet)  throws Exception{
        IntegralRecord integralRecordNew = new IntegralRecord();
        integralRecordNew.setIntegralType(4);
        integralRecordNew.setCount(recycleDetail.getRewardCount());
        integralRecordNew.setUserId(orderBook.getUserId());
        integralRecordNew.setNickname(wxuser.getNickname());
        integralRecordNew.setShopId(orderBook.getShopId());
        integralRecordNew.setOrderId(orderBook.getId());
        integralRecordRepository.save(integralRecordNew);
        saveWxUserAccount(wxuser.getUserId(), orderBook.getShopId(),recycleDetail.getRewardCount());
        WxTemplateDo wxTemplateDo = getWxTemplateDo(integralRecordNew,wxuser,integralSet);
        wxMessageService.sendTemplate(wxTemplateDo);
    }

    private void saveOneRecycleReward(RecycleDetail recycleDetail, OrderBook orderBook,WxUser wxuser , WxUser oneUser,IntegralSet integralSet)   throws Exception{
        IntegralRecord integralRecordNew = new IntegralRecord();
        integralRecordNew.setIntegralType(4);
        integralRecordNew.setCount(recycleDetail.getUpperCount());
        integralRecordNew.setUserId(oneUser.getUserId());
        integralRecordNew.setNickname(oneUser.getNickname());
        integralRecordNew.setShopId(orderBook.getShopId());
        integralRecordNew.setOrderId(orderBook.getId());
        integralRecordRepository.save(integralRecordNew);
        saveWxUserAccount(oneUser.getUserId(), orderBook.getShopId(),recycleDetail.getUpperCount());
        WxTemplateDo wxTemplateDo = getOneWxTemplateDo(integralRecordNew,wxuser,oneUser,integralSet);
        wxMessageService.sendTemplate(wxTemplateDo);
    }

    private WxTemplateDo getWxTemplateDo(IntegralRecord integralRecord, WxUser wxuser,IntegralSet integralSet) throws Exception {
        WxTemplateDo wxTemplateDo = new WxTemplateDo();
        wxTemplateDo.setTouser(wxuser.getOpenid());
        wxTemplateDo.setUrl(Constant.APP_DOMAIN + "/shop/index?shopId=" + integralRecord.getShopId());
        wxTemplateDo.setAppid(wxuser.getAppid());
        wxTemplateDo.setType(13);
        wxTemplateDo.setIncomeMoney(String.valueOf(integralRecord.getCount()));
        wxTemplateDo.setCompany(integralSet.getUnitName());
        wxTemplateDo.setFirst("预约上门回收奖励"+integralSet.getUnitName()+"了");
        wxTemplateDo.setRemark("恭喜你，你预约的衣物我们收到了，这是奖励您的"+integralSet.getUnitName()+"，推荐好友预约您也有奖励哦！"+integralSet.getUnitName()+"可以兑换好多礼物，点击立即兑换");
        wxTemplateDo.setIncomeType("积分奖励");
        wxTemplateDo.setIncomeTime(Toolkit.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return wxTemplateDo;
    }

    private WxTemplateDo getOneWxTemplateDo(IntegralRecord integralRecord, WxUser wxuser,WxUser oneUser,IntegralSet integralSet) throws Exception {
        WxTemplateDo wxTemplateDo = new WxTemplateDo();
        wxTemplateDo.setTouser(oneUser.getOpenid());
        wxTemplateDo.setUrl(Constant.APP_DOMAIN + "/shop/index?shopId=" + integralRecord.getShopId());
        wxTemplateDo.setAppid(oneUser.getAppid());
        wxTemplateDo.setType(13);
        wxTemplateDo.setIncomeMoney(String.valueOf(integralRecord.getCount()));
        wxTemplateDo.setCompany(integralSet.getUnitName());
        wxTemplateDo.setFirst("好友回收成功奖励"+integralSet.getUnitName());
        wxTemplateDo.setRemark("你的好友【"+Base64Util.getFromBase64(wxuser.getNickname())+"】预约回收了，这是奖励给您的"+integralSet.getUnitName()+"，继续推荐好友还有奖励哦！"+integralSet.getUnitName()+"可以兑换好多礼物，点击立即兑换");
        wxTemplateDo.setIncomeType("积分奖励");
        wxTemplateDo.setIncomeTime(Toolkit.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return wxTemplateDo;
    }


    /**
     * 保存积分设置
     *
     * @param integralSetCo
     * @return
     */
    public IntegralSet saveIntegralSet(IntegralSetCo integralSetCo) {
        IntegralSet integralSet = new IntegralSet();
        BeanUtils.copyProperties(integralSetCo, integralSet);
        return integralSetRepository.save(integralSet);
    }

    /**
     * 修改积分设置
     *
     * @param integralSetUo
     * @return
     * @throws Exception
     */
    public IntegralSet updateIntegralSet(IntegralSetUo integralSetUo) throws Exception {
        IntegralSet integralSet = integralSetRepository.findOne(integralSetUo.getId());
        if (integralSet.getShopId() != integralSetUo.getShopId()) {
            throw new Exception("无权限修改该店铺积分设置");
        }
        Toolkit.copyPropertiesIgnoreNull(integralSetUo, integralSet);
        integralSet.setUpdateTime(new Date());
        return integralSetRepository.save(integralSet);
    }

    /**
     * 获取积分设置
     *
     * @param shopId
     * @return
     * @throws Exception
     */
    public IntegralSetVo getIntegralSet(Integer shopId) throws Exception {
        IntegralSet integralSet = integralSetRepository.findByShopId(shopId);
        if (integralSet == null) {
            return new IntegralSetVo();
        }
        if (integralSet.getShopId() != shopId) {
            throw new Exception("无权限获取该店铺积分设置信息");
        }
        IntegralSetVo integralSetVo = new IntegralSetVo();
        BeanUtils.copyProperties(integralSet, integralSetVo);
        return integralSetVo;
    }


    public IntegralSetVo queryWxUserIntegral(Integer id) {
        IntegralSet integralSet = integralSetRepository.findOne(id);
        IntegralSetVo integralSetVo = new IntegralSetVo();
        BeanUtils.copyProperties(integralSet, integralSetVo);
        return integralSetVo;
    }

    /**
     * 查询奖励列表
     *
     * @param qo
     * @return
     * @throws IOException
     */
    public PageItem<IntegralRecordVo> queryIntegralAward(IntegralRecordQo qo) throws IOException {
        String sqlList = "select ui.id,ui.integral_type,ui.count,ui.create_time,u.headimgurl,u.user_name,u.nickname,u.phone_number " +
                "from wx_user u,integral_record ui where u.user_id=ui.user_id and ui.shop_id=" + qo.getShopId();
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("u.phone_number", qo.getPhoneNumber()));
        if (StringUtils.isNotEmpty(qo.getNickname())) {
            sqlCondition.append(JdbcUtil.appendLike("u.nickname", Base64Util.enCoding(qo.getNickname())));
        }
        sqlCondition.append(JdbcUtil.appendAnd("u.user_name", qo.getUserName()));
        sqlCondition.append(JdbcUtil.appendAnd("ui.integral_type", qo.getIntegralType()));
        sqlCondition.append(JdbcUtil.appendAnd("ui.create_time", qo.getBeginTime(), qo.getEndTime()));
        sqlCondition.append(JdbcUtil.appendOrderBy("ui.create_time"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList + sqlCondition, qo.getCurPage(), qo.getPageSize());
        IntegralSet integralSet = getCacheIntegralSet(qo.getShopId());
        String unutName = "";
        if (integralSet != null) {
            unutName = integralSet.getUnitName();
        }
        return IntegralConverter.p2v(pageItem, unutName);
    }

    /**
     * 手机端积分奖励查询
     *
     * @param qo
     * @return
     * @throws IOException
     */
    public PageItem<IntegralRecordVo> queryAppIntegralAward(IntegralRecordQo qo) throws IOException {
        String sqlList = "select ui.id,ui.integral_type,ui.count,ui.create_time,u.headimgurl,u.user_name,u.nickname,u.phone_number " +
                "from wx_user u,integral_record ui where u.user_id=ui.user_id ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("u.user_id", qo.getUserId()));
        sqlCondition.append(JdbcUtil.appendOrderBy("ui.create_time"));
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList + sqlCondition, qo.getCurPage(), qo.getPageSize());
        IntegralSet integralSet = getCacheIntegralSet(qo.getShopId());
        String unutName = "";
        if (integralSet != null) {
            unutName = integralSet.getUnitName();
        }
        return IntegralConverter.p2v(pageItem, unutName);
    }

    /**
     * 保存积分商品换购设置
     *
     * @param integralProductCo
     * @return
     */
    public IntegralProduct saveIntegralProduct(IntegralProductCo integralProductCo) {
        IntegralProduct integralProduct = new IntegralProduct();
        BeanUtils.copyProperties(integralProductCo, integralProduct);
        integralProduct.setUpdateTime(new Date());
        return integralProductRepository.save(integralProduct);
    }

    /**
     * 查询积分商品换购设置
     *
     * @param integralProductQo
     * @return
     * @throws IOException
     */
    public PageItem<IntegralProductVo> queryIntegralProduct(IntegralProductQo integralProductQo) throws IOException {
        String sqlList = "select t.id,t.integral,t.type,t.shop_id,p.pid,p.name,p.price,p.pic_square pic " +
                "from product p LEFT JOIN integral_product t on t.pid=p.pid where p.shop_id=" + integralProductQo.getShopId() + " and p.status !=9 order by t.update_time desc";
        PageItem<Map<String, Object>> pageItem = jdbcUtil.queryPageItem(sqlList, integralProductQo.getCurPage(), integralProductQo.getPageSize());
        return IntegralConverter.toIntegralProductVos(pageItem);
    }

    /**
     * 修改积分商品换购设置
     *
     * @param integralProductUo
     * @return
     * @throws IOException
     */
    public IntegralProduct updateIntegralProduct(IntegralProductUo integralProductUo) throws Exception {
        IntegralProduct integralProduct = integralProductRepository.findOne(integralProductUo.getId());
        if (!integralProduct.getShopId().equals(integralProductUo.getShopId())) {
            throw new Exception("无权限修改该店铺积分商品设置");
        }
        BeanUtils.copyProperties(integralProductUo, integralProduct);
        integralProduct.setUpdateTime(new Date());
        return integralProductRepository.save(integralProduct);
    }

    /**
     * 获取用户积分账户信息
     *
     * @param userId
     * @return
     */
    public WxUserAccountVo getUserIntegral(Integer userId) {
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(userId, 2);
        WxUserAccountVo wxUserAccountVo = new WxUserAccountVo();
        if (wxUserAccount == null) {
            wxUserAccountVo.setUserId(userId);
            wxUserAccountVo.setBalance(0);
        } else {
            BeanUtils.copyProperties(wxUserAccount, wxUserAccountVo);
        }
        return wxUserAccountVo;
    }

    /**
     * 保存商品积分设置列表
     *
     * @param integralProductCos
     * @param shopId
     * @return
     */
    public List<IntegralProduct> saveIntegralProducts(List<IntegralProductCo> integralProductCos, Integer shopId) {
        List<IntegralProduct> integralProducts = new ArrayList<>();
        for (IntegralProductCo integralProductCo : integralProductCos) {
            if (integralProductCo.getId() == null) {
                IntegralProduct integralProduct = new IntegralProduct();
                BeanUtils.copyProperties(integralProductCo, integralProduct);
                integralProduct.setType(0);
                integralProduct.setShopId(shopId);
                integralProduct.setUpdateTime(new Date());
                integralProducts.add(integralProduct);
            } else {
                IntegralProduct integralProduct = integralProductRepository.findOne(integralProductCo.getId());
                integralProduct.setShopId(shopId);
                integralProduct.setType(0);
                integralProduct.setUpdateTime(new Date());
                integralProducts.add(integralProduct);
            }
        }
        return integralProductRepository.save(integralProducts);
    }

    public List<IntegralProductVo> getIntegralProductByPids(String pids) throws IllegalAccessException, IOException, InstantiationException {
        String[] pid = pids.split(",");
        List ids = new ArrayList();
        for (int i = 0; i < pid.length; i++) {
            Integer id = Integer.parseInt(pid[i]);
            ids.add(id);
        }
        return getIntegralProductByPids(ids);
    }

    public List<IntegralProductVo> getIntegralProductByPids(List<Integer> pids) throws IllegalAccessException, IOException, InstantiationException {
        //integralProductRepository.findAll(pids);
        if (pids.size() < 1) {
            return null;
        }
        StringBuffer sql = new StringBuffer("select ip.*,p.product_price from integral_product ip left join product p on ip.pid = p.pid where ip.pid in (");
        for (int i = 0; i < pids.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(pids.get(i));
        }
        sql.append(")");
        List<IntegralProductVo> integralProductList = jdbcUtil.queryList(sql.toString(), IntegralProductVo.class);
        return integralProductList;
    }

    public List<IntegralProductVo> getIntegralProductByOrderDetails(List<OrderDetailCreateVo> products) throws IllegalAccessException, IOException, InstantiationException {
        if(products.size()<1){
            return null;
        }
        StringBuffer  sql = new StringBuffer("SELECT ip.*,ps.product_spec_id,( CASE WHEN ps.product_spec_id in ( 0");
        StringBuffer pidSql = new StringBuffer("WHERE p.pid in ( ");
        StringBuffer specSql = new StringBuffer("  AND ps.product_spec_id in ( 0 ");
        for(int i =0;i<products.size();i++){
            if(products.get(i).getProductSpecId()!=null&&products.get(i).getProductSpecId()!=0){
                sql.append(",");
                specSql.append(",");
                sql.append(products.get(i).getProductSpecId());
                specSql.append(products.get(i).getProductSpecId());
            }
            if(i>0){
                pidSql.append(",");
            }
            pidSql.append(products.get(i).getPid());
        }
        pidSql.append(")");
        specSql.append(")");
        sql.append(" ) THEN ps.spec_price ELSE p.price END) as price FROM integral_product ip LEFT JOIN product p ON p.pid = ip.pid ");
        sql.append(" LEFT JOIN product_spec ps ON ps.pid = ip.pid  ");
        sql.append(specSql);
        sql.append(pidSql);
        return jdbcUtil.queryList(sql.toString(),IntegralProductVo.class);
    }

    /**
     * 保存用户积分账户
     *
     * @param userId
     * @param shopId
     * @param count
     */
    private void saveWxUserAccount(Integer userId, Integer shopId, Integer count) {
        if (userId == null) {
            return;
        }
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(userId, 2);
        if (wxUserAccount == null) {
            wxUserAccount = new WxUserAccount();
        }
        wxUserAccount.setUserId(userId);
        wxUserAccount.setShopId(shopId);
        wxUserAccount.setUpdateTime(new Date());
        wxUserAccount.setAccountType(2);
        wxUserAccount.setTotalCount(wxUserAccount.getTotalCount() + count);
        wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance() + count);
        wxUserAccount.setKitBalance(wxUserAccount.getKitBalance() + count);
        wxUserAccountRepository.save(wxUserAccount);
    }

    /**
     * 获取登录奖励
     *
     * @param userId
     * @return
     */
    public List<IntegralRecord> getLoginAward(Integer userId) {
        return integralRecordRepository.queryLoginWxUserIntegral(userId);
    }

    /**
     * 每日登陆奖励
     *
     * @param awardIntegralDo
     */
    @Transactional
    private Integer loginAwardIntegral(AwardIntegralDo awardIntegralDo, IntegralSet integralSet) {
        Integer userId = awardIntegralDo.getUserId();
        Integer shopId = awardIntegralDo.getShopId();
        List<IntegralRecord> integralRecords = integralRecordRepository.queryLoginWxUserIntegral(userId);
        if (integralRecords == null || integralRecords.size() == 0) {
            if (integralSet.getIsLogin() == 1 && integralSet.getLoginAward() > 0) {
                IntegralRecord integralRecordNew = new IntegralRecord();
                integralRecordNew.setShopId(shopId);
                integralRecordNew.setCount(integralSet.getLoginAward());
                integralRecordNew.setUserId(userId);
                integralRecordNew.setIntegralType(1);
                integralRecordRepository.save(integralRecordNew);
                saveWxUserAccount(userId, shopId, integralSet.getLoginAward());
            }
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 推荐关注奖励
     *
     * @param awardIntegralDo
     */
    private void recommendAward(AwardIntegralDo awardIntegralDo, IntegralSet integralSet) {
        Integer oneAward = integralSet.getOneRecommendAward();
        if (oneAward != null && oneAward > 0) {
            IntegralRecord integralRecord = new IntegralRecord();
            BeanUtils.copyProperties(awardIntegralDo, integralRecord);
            integralRecord.setShopId(awardIntegralDo.getShopId());
            if (awardIntegralDo.getUpOneUserId() != null) {
                integralRecord.setUserId(awardIntegralDo.getUpOneUserId());
                integralRecord.setOneUserId(awardIntegralDo.getUserId());
                integralRecord.setCount(oneAward);
                integralRecord.setIntegralType(2);
                integralRecordRepository.save(integralRecord);
                saveWxUserAccount(awardIntegralDo.getUpOneUserId(), awardIntegralDo.getShopId(), oneAward);
            }
        }
        Integer twoAward = integralSet.getTwoRecommendAward();
        if (twoAward != null && twoAward > 0) {
            IntegralRecord integralRecord = new IntegralRecord();
            BeanUtils.copyProperties(awardIntegralDo, integralRecord);
            integralRecord.setShopId(awardIntegralDo.getShopId());
            if (awardIntegralDo.getUpTwoUserId() != null) {
                integralRecord.setUserId(awardIntegralDo.getUpTwoUserId());
                integralRecord.setTwoUserId(awardIntegralDo.getUserId());
                integralRecord.setCount(twoAward);
                integralRecord.setIntegralType(2);
                integralRecordRepository.save(integralRecord);
                saveWxUserAccount(awardIntegralDo.getUpTwoUserId(), awardIntegralDo.getShopId(), twoAward);
            }
        }
    }

    /**
     * 购买奖励
     *
     * @param awardIntegralDo
     */
    private void buyAward(AwardIntegralDo awardIntegralDo, IntegralSet integralSet) {
        if (integralSet.getBuyMoney() == 0 || awardIntegralDo.getBuyMoney() == 0
                || integralSet.getBuyMoney() > awardIntegralDo.getBuyMoney()) {
            return;
        }
        IntegralRecord integralRecord = new IntegralRecord();
        BeanUtils.copyProperties(awardIntegralDo, integralRecord);
        integralRecord.setShopId(awardIntegralDo.getShopId());
        integralRecord.setUserId(awardIntegralDo.getUserId());
        integralRecord.setCount(integralSet.getReturnIntegral());
        integralRecord.setIntegralType(3);
        integralRecordRepository.save(integralRecord);
        saveWxUserAccount(awardIntegralDo.getUserId(), awardIntegralDo.getShopId(), integralSet.getReturnIntegral());
    }

    @Cacheable(value = "IntegralSet", key = "#shopId")
    public IntegralSet getCacheIntegralSet(Integer shopId) {
        return integralSetRepository.findByShopId(shopId);
    }


    /**
     * 积分奖励
     *
     * @param awardIntegralDo
     */
    public JmMessage awardIntegral(AwardIntegralDo awardIntegralDo) {
        IntegralSet integralSet = getCacheIntegralSet(awardIntegralDo.getShopId());
        JmMessage jmMessage = new JmMessage(0, "获得积分成功");
        if (integralSet == null || integralSet.getIsOpen() == 0 || integralSet.getIsAward() == 0) {
            jmMessage.setCode(2);
            jmMessage.setMsg("该店铺未开通积分功能");
            return jmMessage;
        }
        if (awardIntegralDo.getIntegralType() == 1) {
            if (integralSet.getIsLogin() == 0) {
                jmMessage.setCode(2);
                jmMessage.setMsg("该店铺未开通积分登录功能");
                return jmMessage;
            }
            Integer res = loginAwardIntegral(awardIntegralDo, integralSet);
            jmMessage.setData(integralSet.getLoginAward());
            if (res == 1) {
                jmMessage.setCode(1);
                jmMessage.setMsg("该用户已经登录过");
            }
        } else if (awardIntegralDo.getIntegralType() == 2) {
            if (integralSet.getIsRecommend() == 1) {
                recommendAward(awardIntegralDo, integralSet);
            }
        } else if (awardIntegralDo.getIntegralType() == 3) {
            if (integralSet.getIsBuy() == 1) {
                buyAward(awardIntegralDo, integralSet);
            }
        }
        return jmMessage;
    }

    /**
     * 手机端积分管理
     *
     * @param userId
     * @param shopId
     * @return
     * @throws Exception
     */
    public IntegralInfoVo getIntegralInfo(Integer userId, Integer shopId) throws Exception {
        IntegralSet integralSet = getCacheIntegralSet(shopId);
        WxUserAccountVo wxUserAccountVo = getUserIntegral(userId);
        IntegralRecordQo qo = new IntegralRecordQo();
        qo.setUserId(userId);
        IntegralInfoVo integralInfoVo = new IntegralInfoVo();
        integralInfoVo.setWxUserAccountVo(wxUserAccountVo);
        if (integralSet == null) {
            integralInfoVo.setIsPay(0);
            integralInfoVo.setIntegralRecordVos(new PageItem<IntegralRecordVo>());
        } else {
            integralInfoVo.setIsPay(integralSet.getIsPay());
            PageItem<IntegralRecordVo> integralRecordVos = queryIntegralAward(qo);
            integralInfoVo.setIntegralRecordVos(integralRecordVos);
        }
        return integralInfoVo;
    }


    public JmMessage delete(int id, Integer shopId) {
        IntegralProduct integralProduct = integralProductRepository.findOne(id);
        if (!integralProduct.getShopId().equals(shopId)) {
            return new JmMessage(1, "无权限修改该店铺积分商品设置");
        }
        integralProductRepository.delete(integralProduct);
        return new JmMessage(0, "商品积分换购设置成功");
    }

    public RechargeOrder integralRecharge(RechargeOrderCo rechargeOrderCo) {
        RechargeOrder rechargeOrder = IntegralConverter.toRechargeOrder(rechargeOrderCo);
        return rechargeOrderRepository.save(rechargeOrder);
    }

    public List<RechargeOrderVo> getIntegralRecharge(Integer userId) {
        List<RechargeOrder> rechargeOrderList = rechargeOrderRepository.findByUserIdAndStatus(userId, 1);
        return IntegralConverter.toRechargeOrderVoList(rechargeOrderList);
    }

    public void integralRecharge(Long orderId) {
        RechargeOrder rechargeOrder = rechargeOrderRepository.findOne(orderId);
        IntegralSet integralSet = getCacheIntegralSet(rechargeOrder.getShopId());
        Integer count = integralSet.getUnit() * rechargeOrder.getMoney() / 100;
        WxUserAccount wxUserAccount = wxUserAccountRepository.findByUserIdAndAccountType(rechargeOrder.getUserId(), 2);
        if (wxUserAccount == null) {
            wxUserAccount = new WxUserAccount();
            wxUserAccount.setAccountType(2);
        } else {
            wxUserAccount.setUpdateTime(new Date());
        }
        wxUserAccount.setUserId(rechargeOrder.getUserId());
        wxUserAccount.setShopId(rechargeOrder.getShopId());
        wxUserAccount.setTotalCount(wxUserAccount.getTotalCount() + count);
//		wxUserAccount.setNoKit(wxUserAccount.getNoKit()+count);
        wxUserAccountRepository.save(wxUserAccount);
    }


    public RechargeOrder findRechargeById(Long orderInfoId) {
        return rechargeOrderRepository.findByOrderInfoId(orderInfoId);
    }

    public void saveIntegralRecharge(RechargeOrder rechargeOrder) {
        rechargeOrderRepository.save(rechargeOrder);
    }

    /**
     * 获取用户购买的商品的够积分抵扣金额
     */
    public Integer getIntegralDiscount(Integer userId, List<OrderDetailCreateVo> list, Integer shopId) throws Exception {
        IntegralSetVo integralSetVo = getIntegralSet(shopId);
        if (integralSetVo.getIsOpen() == 0 || integralSetVo.getIsExchange() == 0) {
            return null;
        }
        WxUserAccount userAccount = wxUserAccountRepository.findByUserIdAndAccountType(userId, 2);
        //账号可使用剩余积分
        if (userAccount.getKitBalance() == 0) {
            return null;
        }
        Integer unit = integralSetVo.getUnit();
        StringBuffer sql = new StringBuffer("select * from  integral_product ip where ");
        sql.append(jdbcUtil.appendAnd("ip.shop_id", shopId));
        sql.append(" and pid in ( ");
        for (int i = 0; i < list.size(); i++) {
            OrderDetailCreateVo orderDetail = list.get(i);
            if (i > 0) {
                sql.append(",");
            }
            sql.append(orderDetail.getPid());
        }
        List<IntegralProduct> integralProducts = jdbcUtil.queryList(sql.toString(), IntegralProduct.class);
        if (integralProducts.size() < 1) {
            return null;
        }
        int totalIntegeral = 0;
        for (IntegralProduct product : integralProducts) {
            if (product.getType() == 0) {

            }
        }

        return 0;
    }

    /**
     * 封装积分订单清单
     *
     * @param orderInfo
     * @return
     */
    private void setBrokerageOrder(OrderInfo orderInfo) {
        OrderDiscount orderDiscount = orderDiscountRepository.findByOrderDiscount(orderInfo.getOrderInfoId(), 0);
        if(orderDiscount!=null){
            BrokerageOrder brokerageOrder = new BrokerageOrder();
            brokerageOrder.setShopId(orderInfo.getShopId());//店铺id
            brokerageOrder.setUserId(orderInfo.getUserId());//用户id
            brokerageOrder.setOrderInfoId(orderInfo.getOrderInfoId());//订单id
            brokerageOrder.setOrderDate(orderInfo.getCreateDate());//订单生成时间
            brokerageOrder.setTotalPrice(orderInfo.getTotalPrice());//商品总额
            brokerageOrder.setCommissionPrice(orderDiscount.getCount());//积分数量
            brokerageOrder.setType(2);//订单类型，积分
            brokerageOrder.setPlatForm(0);//平台
            brokerageOrder.setStatus(0);//状态
            brokerageOrderRepository.save(brokerageOrder);
        }
    }
}
