package com.jm.business.service.order;

import com.jm.business.service.shop.IntegralService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderConfirmVo;
import com.jm.mvc.vo.order.OrderDetailCreateVo;
import com.jm.mvc.vo.order.OrderDiscountCo;
import com.jm.mvc.vo.shop.activity.ShopCardVo;
import com.jm.mvc.vo.shop.integral.IntegralProductVo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardQo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardUo;
import com.jm.mvc.vo.wx.wxuser.WxUserCardVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderDiscountRepository;
import com.jm.repository.jpa.shop.activity.CardProductRepository;
import com.jm.repository.jpa.shop.activity.ShopCardRepository;
import com.jm.repository.jpa.wx.WxUserCardRepository;
import com.jm.repository.po.order.OrderDiscount;
import com.jm.repository.po.shop.activity.CardProduct;
import com.jm.repository.po.shop.activity.ShopCard;
import com.jm.repository.po.shop.integral.IntegralProduct;
import com.jm.repository.po.wx.WxUserCard;
import com.jm.staticcode.converter.order.OrderDiscountConverter;
import com.jm.staticcode.converter.wx.WxUserCardConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderFavorableService {

    @Autowired
    private WxUserCardRepository userCardRepository;

    @Autowired
    private OrderDiscountRepository orderDiscountRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    @Autowired
    private CardProductRepository cardProductRepository;
    @Autowired
    private ShopCardRepository shopCardRepository;

    @Autowired
    private IntegralService integralService;

    //@Transactional

    public List<WxUserCardVo> findWxUserCardByUid(Integer wxUserId, Integer shopId,List<OrderConfirmVo> products,Integer totalPrice) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select card.*,card.id as ticket_id,sc.shop_id,sc.buy_money,sc.card_money,sc.card_name,sc.user_condition,sc.user_limit,sc.start_time,sc.end_time,sc.period_type from wx_user_card card left join shop_card sc on sc.id = card.card_id and sc.shop_id = "+shopId;
        sql+=" where card.status=1 and card.user_id ="+wxUserId;
        StringBuffer pidBuffer = new StringBuffer("");
        for(int i =0;i<products.size();i++){
            if(i>0){
                pidBuffer.append(",");
            }
            pidBuffer.append(products.get(i).getProductId());
        }
        sql+=" AND ( CASE sc.user_limit WHEN 0 THEN 1 = 1 ELSE EXISTS ( SELECT 1 FROM card_product cp WHERE cp.card_id = card.card_id AND cp.pid IN ("+pidBuffer.toString()+") ) END )";
        sql+=" AND (CASE sc.user_condition  WHEN 0 THEN 1=1 ELSE sc.buy_money <= "+totalPrice+" end) ";
        sql+=" AND (case sc.period_type WHEN 1 THEN card.expire_time >=now() ELSE sc.start_time <= now() AND sc.end_time >= now() END)";
        List<WxUserCardVo> cardVos = jdbcUtil.queryList(sql,WxUserCardVo.class);
        addLimitProducts(cardVos);
        return cardVos;
    }

    private void addLimitProducts(List<WxUserCardVo> cardVos) throws IllegalAccessException, IOException, InstantiationException {
        List<Integer> ids = new ArrayList<>();
        if(null==cardVos||cardVos.size()<1){
            return;
        }
        for(WxUserCardVo card:cardVos){
            if(card.getUserLimit()==1){
                ids.add(card.getCardId());
            }
        }
        if(ids.size()<1){
            return;
        }
        StringBuffer buffer = new StringBuffer("select * from card_product cp where card_id in ( ");
        for(int i=0;i<ids.size();i++){
            if(i>0){
                buffer.append(",");
            }
            buffer.append(ids.get(i));
        }
        buffer.append(")");
        List<CardProduct> cardProducts = jdbcUtil.queryList(buffer.toString(),CardProduct.class);
        if(cardProducts==null||cardProducts.size()<1){
            return;
        }
        for(WxUserCardVo card:cardVos){
            List<CardProduct> cps = new ArrayList<>();
            for(CardProduct cardProduct:cardProducts){
                if(card.getCardId().equals(cardProduct.getCardId())){
                    cps.add(cardProduct);
                }
            }
            card.setCardProducts(cps);
        }
    }

    public List<WxUserCardVo> findWxUserCardByUid(Integer wxUserId, Integer shopId) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select card.*,card.id as ticket_id,sc.shop_id,sc.buy_money,sc.card_money,sc.card_name,sc.user_condition,sc.user_limit,sc.start_time,sc.end_time,sc.period_type from wx_user_card card left join shop_card sc on sc.id = card.card_id and sc.shop_id = "+shopId;
        sql+=" where card.status=1 and card.user_id ="+wxUserId;
        sql+=" AND (case sc.period_type WHEN 1 THEN card.expire_time >=now() ELSE sc.start_time <= now() AND sc.end_time >= now() END)";
        return jdbcUtil.queryList(sql,WxUserCardVo.class);
    }

    /*public WxUserCardVo receiveCard(Integer userId,Integer cardId) throws Exception {
        WxUserCard card = this.findWxUserCardByUidAndCardId(userId,cardId);
        if(card==null){
            throw new Exception("无该卡券");
        }
        card.setStatus(1);
        userCardRepository.save(card);
        return WxUserCardConverter.p2v(card);
    }*/
    @Transactional
    public Map<String,Object> receiveCard(Integer wxUserCardId, Integer userId) throws Exception {
        WxUserCard card = userCardRepository.findOne(wxUserCardId);
        if(card==null){
            throw new Exception("无该卡券");
        }else if(userId.intValue()!=card.getUserId().intValue()){
            throw new Exception("该卡券不属于你");
        }
        Map<String,Object> result = new HashMap<>();
        if(card.getStatus()==0){
            ShopCard shopCard = shopCardRepository.findOne(card.getCardId());
            if(shopCard.getPeriodType()==1){
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, shopCard.getFixDate());
                card.setExpireTime(cal.getTime());
            }
            card.setStatus(1);
            card = userCardRepository.save(card);
            result.put("code",0);
        }else{
           result.put("code",1);
        }
        result.put("card",WxUserCardConverter.p2v(card));
        return  result;
    }

    public WxUserCard findWxUserCardByUidAndCardId(Integer userId,Integer cardId) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select * from wx_user_card uc where uc.user_id ="+userId+" and uc.card_id ="+cardId+"";
        List<WxUserCard> list = jdbcUtil.queryList(sql,WxUserCard.class);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return  null;
    }

    public  WxUserCard findWxUserCardById(Integer userCardId){
        return userCardRepository.findOne(userCardId);
    }

    public WxUserCardVo findCardDetailByCardId(Integer userCardId) throws IllegalAccessException, IOException, InstantiationException {
        String sql = "select card.*,card.id as ticket_id,sc.shop_id,sc.buy_money,sc.card_money,sc.card_name,sc.user_condition,sc.user_limit from wx_user_card card left join  shop_card sc on sc.id = card.card_id  where card.status=1 and card.id="+userCardId;
        List<WxUserCardVo> cardVos = jdbcUtil.queryList(sql,WxUserCardVo.class);
        if(cardVos.size()>0){
            return cardVos.get(0);
        }
        return null;
    }


    public WxUserCardVo findCardVoById(Integer userCardId){
        return userCardRepository.findUserCardVoByUserCardId(userCardId);
    }

    @Transactional
    public  WxUserCard updateWxUserCard(WxUserCardUo wxUserCardUo){
        WxUserCard card = WxUserCardConverter.u2p(wxUserCardUo);
        return userCardRepository.save(card);
    }
    @Transactional
    public WxUserCard shareUserCard(WxUserCardVo cardVo,Integer userId){
        WxUserCard cardCo = new WxUserCard();
        BeanUtils.copyProperties(cardVo,cardCo);
        cardCo.setShareId(cardVo.getUserId());
        cardCo.setUserId(userId);
        WxUserCard userCard = new WxUserCard();
        BeanUtils.copyProperties(cardVo,userCard);
        userCard.setStatus(9);
        List<WxUserCard> cards = new ArrayList<>();

        userCardRepository.save(cards);
        return cardCo;
    }

    @Transactional
    public WxUserCardVo addUserCard(Integer userId,ShopCardVo shopCard) throws Exception {

        //判断是否已经抢过该券
        int i = userCardRepository.isHasTicket(shopCard.getId(),userId);
        if(i>0){
            throw new Exception("你已经抢过该券");
        }

        //userCardRepository
        WxUserCard card = new WxUserCard();
        //卡券状态 0 可用 1 已使用 9 删除
        card.setStatus(1);
        card.setUserId(userId);
        if(shopCard.getPeriodType()==1){
            long expireDate = shopCard.getFixDate()*1000*3600*24;
            card.setExpireTime(new Date(System.currentTimeMillis()+expireDate));
        }else if(shopCard.getPeriodType()==2){
            card.setExpireTime(shopCard.getEndTime());
        }
        //卡券领取方式:0 自己领取 1 赠送
        card.setType(0);
        card.setCardId(shopCard.getId());
        userCardRepository.save(card);
        WxUserCardVo wxUserCardVo  = new WxUserCardVo();
        BeanUtils.copyProperties(card,wxUserCardVo);
        BeanUtils.copyProperties(shopCard,wxUserCardVo);
        return wxUserCardVo;
    }

    public PageItem<WxUserCardVo> getUserCardPage(WxUserCardQo userCardQo, int currentPage, int pageSize) throws IllegalAccessException, IOException, InstantiationException {
        StringBuffer sql = new StringBuffer( "SELECT sc.* FROM wx_user_card uc, shop_card sc WHERE sc.id = uc.card_id ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(userCardQo.getStartTime()!=null){
            //sql.append(jdbcUtil.appendAnd("sc.start_time",userCardQo.getStartTime(),null));
            sql.append(" and  ( CASE WHEN sc.start_time IS NULL THEN 1 = 1 ELSE sc.start_time <= '"+sdf.format(userCardQo.getStartTime())+"' END)");
        }
        if(userCardQo.getEndTime()!=null){
            sql.append(" AND (CASE WHEN sc.end_time IS NULL THEN 1 =1 ELSE sc.end_time >='" +sdf.format(userCardQo.getEndTime())+"' END)");
            //sql.append(jdbcUtil.appendAnd("sc.end_time",null,userCardQo.getEndTime()));
        }
        if(userCardQo.getBuyMoney()>0 ){
            sql.append(jdbcUtil.appendAnd("sc.buy_money",userCardQo.getBuyMoney()));
        }
        sql.append(jdbcUtil.appendAnd("uc.user_id",userCardQo.getUserId()));
        if(userCardQo.getStatus()!=null){
            sql.append(jdbcUtil.appendAnd("uc.status",userCardQo.getStatus()));
        }
        if(userCardQo.getUserCondition()!=null){
            sql.append(jdbcUtil.appendAnd("uc.user_condition",userCardQo.getUserCondition()));
        }

        return jdbcUtil.queryPageItem(sql.toString(),currentPage,pageSize,WxUserCardVo.class);
    }



    public WxUserCardVo getBestCard(List<WxUserCardVo> cards, List<OrderDetailCreateVo> products) throws IllegalAccessException, IOException, InstantiationException {
        Collections.sort(cards);
        WxUserCardVo bestCard =null;
        getLimitProduct(cards);
        for(WxUserCardVo card: cards ){
            if(card.getUserLimit()==0){
                bestCard = card;
                break;
            }else if(card.getUserLimit() ==1){
                List<CardProduct> cardProducts = card.getCardProducts();
                if(cardProducts!=null){
                    for(OrderDetailCreateVo detailCreateVo :products){
                        for(CardProduct cp :cardProducts){
                            if(cp.getPid().equals(detailCreateVo.getPid())){
                                bestCard = card;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return bestCard;
    }
    //获取指定商品的id
    public  void getLimitProduct(List<WxUserCardVo> cards) throws IllegalAccessException, IOException, InstantiationException {
        if(cards.size()<1){
            return;
        }
        StringBuffer sql = new StringBuffer("SELECT cp.card_id,cp.pid,cp.id FROM card_product cp WHERE EXISTS (SELECT 1 FROM shop_card sc WHERE sc.id = cp.card_id and sc.id in ( ");
        for(int i = 0;i<cards.size();i++){
            if(i>0){
                sql.append(",");
            }
            sql.append(cards.get(i).getCardId());
        }
        sql.append(") )");
        List<CardProduct> result =  jdbcUtil.queryList(sql.toString(),CardProduct.class);
        for(CardProduct cardProduct :result){
            for(WxUserCardVo userCardVo :cards){
                if(userCardVo.getCardId().equals(cardProduct.getCardId())){
                    List<CardProduct> cardProducts = userCardVo.getCardProducts();
                    if(cardProducts==null){
                        cardProducts = new ArrayList<>();
                    }
                    cardProducts.add(cardProduct);
                }
            }
        }
    }

    /*public <T>List<T> getLimitProduct(Class<T> clazz,Iterator<T> it) throws IllegalAccessException, InstantiationException {
       if(it!=null){
           while(it.hasNext()){
               T t = clazz.newInstance();
               t = it.next();

           }
       }
    }*/

    @Transactional
    public OrderDiscount save(OrderDiscountCo orderDiscountCo){
        OrderDiscount orderDiscount  = OrderDiscountConverter.c2p(orderDiscountCo);
        return orderDiscountRepository.save(orderDiscount);
    }
    @Transactional
    public List<OrderDiscount> save(Iterable<OrderDiscountCo> orderDiscountCos){
        List<OrderDiscount> discounts = new ArrayList<>();
        for(OrderDiscountCo OrderDiscountCo:orderDiscountCos){
            OrderDiscount orderDiscount  = OrderDiscountConverter.c2p(OrderDiscountCo);
            discounts.add(orderDiscount);
        }
        return orderDiscountRepository.save(discounts);
    }

    @Transactional
    public List<WxUserCard> save(List<WxUserCard> cards){
        return userCardRepository.save(cards);
    }

    @Transactional
    public void useUserCard(Integer wxUserCardId) {
        userCardRepository.useCard(wxUserCardId);
    }

    public List<WxUserCard> findCards(Integer[] wxCards){
        List<Integer> cardIds = new ArrayList<>();
        for(int i =0;i<wxCards.length;i++){
            cardIds.add(wxCards[i]);
        }
        return findCards(cardIds);
    }

    public List<WxUserCard> findCards(List<Integer> wxCards){
        return userCardRepository.findAll(wxCards);
    }

    public WxUserCard getWxUserCard(Integer wxUserCardId){
        return userCardRepository.findOne(wxUserCardId);
    }

    public Integer getPoint(List<OrderDetailCreateVo> products) throws IllegalAccessException, IOException, InstantiationException {
        List<IntegralProductVo> integralProducts = integralService.getIntegralProductByOrderDetails(products);
        Integer point = 0;
        for(OrderDetailCreateVo pro :products){
            for(IntegralProductVo product:integralProducts){
                if((product.getProductSpecId()!=null&&pro.getProductSpecId() .equals(product.getProductSpecId()))||(product.getProductSpecId()==null&&pro.getPid().equals(product.getPid()))){
                    if(product.getIntegral()==null){
                        point+= (product.getPrice()*pro.getCount());
                    }else{
                        point+=(product.getIntegral()*pro.getCount());
                    }
                }
            }
        }
        return point;
    }
}
