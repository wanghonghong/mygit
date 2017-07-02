package com.jm.business.service.shop;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.activity.ShopCardCo;
import com.jm.mvc.vo.shop.activity.ShopCardQo;
import com.jm.mvc.vo.shop.activity.ShopCardUo;
import com.jm.mvc.vo.shop.activity.ShopCardVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.activity.ActivityCardRepository;
import com.jm.repository.jpa.shop.activity.CardProductRepository;
import com.jm.repository.jpa.shop.activity.ShopCardRepository;
import com.jm.repository.po.shop.activity.ActivityCard;
import com.jm.repository.po.shop.activity.CardProduct;
import com.jm.repository.po.shop.activity.ShopCard;
import com.jm.repository.po.wx.WxUserCard;
import com.jm.staticcode.converter.shop.CardConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>卡卷服务</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/30
 */
@Log4j
@Service
public class CardService {

    @Autowired
    private ShopCardRepository shopCardRepository;

    @Autowired
    private CardProductRepository cardProductRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    @Autowired
    private ActivityCardRepository activityCardRepository;

    @Autowired
    private JdbcRepository jdbcRepository;

    @Transactional
    public JmMessage addCard(ShopCardCo shopCardCo) throws Exception {
        ShopCard shopCard = CardConverter.c2p(shopCardCo);
        shopCard.setCreateTime(new Date());
        ShopCard shopCardNew = shopCardRepository.save(shopCard);
        if (!StringUtils.isEmpty(shopCardCo.getPids())){  //卡卷配置指定商品
            cardProductRepository.save(CardConverter.c2ps(shopCardNew.getId(),shopCardCo.getPids()));
        }
        return new JmMessage(0,"礼券新增成功!!");
    }

    @Transactional
    public JmMessage updateCard(ShopCardUo shopCardUo) throws Exception {
        int cardByActCount = jdbcRepository.countCardIdByActivity(shopCardUo.getId());
        if (cardByActCount>0){
            return new JmMessage(1,"该礼券已参与活动，不允许编辑，请认真核实!!!");
        }
        ShopCard shopCard = shopCardRepository.findOne(shopCardUo.getId());
        BeanUtils.copyProperties(shopCardUo,shopCard);
        if (!StringUtils.isEmpty(shopCardUo.getPids())){  //卡卷配置指定商品
            cardProductRepository.delete(cardProductRepository.findByCardId(shopCardUo.getId()));
            cardProductRepository.save(CardConverter.c2ps(shopCardUo.getId(),shopCardUo.getPids()));
        }
        shopCardRepository.save(shopCard);
        return new JmMessage(0,"礼券编辑成功!!");
    }

    public ShopCardVo getCard(Integer id, Integer shopId) throws Exception {
        ShopCard shopCard = shopCardRepository.findOne(id);
        if (!shopCard.getShopId().equals(shopId)){
            throw new Exception("无权限操作");
        }
        List<CardProduct> lst = cardProductRepository.findByCardId(id);
        ShopCardVo shopCardVo = new ShopCardVo();
        BeanUtils.copyProperties(shopCard,shopCardVo);
        shopCardVo.setCardProducts(lst);
        return shopCardVo;
    }

    public ShopCardVo getShopCard(Integer cardId) throws IllegalAccessException, IOException, InstantiationException {
        String sql ="SELECT sc.*, count(uc.id) AS handedOut FROM shop_card sc LEFT JOIN wx_user_card uc ON sc.id = uc.card_id WHERE sc.id = "+cardId+" GROUP BY sc.id";
        List<ShopCardVo> cardVos = jdbcUtil.queryList(sql,ShopCardVo.class);
        ShopCardVo cardVo = null;
        if(cardVos!=null&&cardVos.size()>0){
            cardVo = cardVos.get(0);
        }

        return cardVo;
    }

    public JmMessage deleteCard(Integer id, Integer shopId) throws Exception {
        int cardByActCount = jdbcRepository.countCardIdByActivity(id);
        if (cardByActCount>0){
            return new JmMessage(1,"该礼券已参与活动，不允许删除!!!");
        }
        ShopCard shopCard = shopCardRepository.findOne(id);
        if (!shopCard.getShopId().equals(shopId)){
            throw new Exception("无权限操作");
        }
        shopCardRepository.delete(shopCard);
        return new JmMessage(0,"礼券删除成功!!");
    }

    public PageItem<ShopCard> queryCard(ShopCardQo shopCardQo) {
        PageRequest pageRequest = JdbcUtil.getPageRequest(shopCardQo.getCurPage(),shopCardQo.getPageSize(),"createTime");
        Page<ShopCard> shopCards = shopCardRepository.findAll(pageRequest);
        PageItem<ShopCard> pageItem = new PageItem<>();
        pageItem.setCount(shopCards.getTotalPages());
        pageItem.setItems(shopCards.getContent());
        return pageItem;
    }

    public PageItem<ShopCardVo> queryCardList(ShopCardQo shopCardQo) throws IllegalAccessException, IOException, InstantiationException {
        String sql ="SELECT sc.* FROM shop_card sc WHERE sc.shop_id = "+shopCardQo.getShopId()+"  order by sc.create_time desc";
        PageItem<ShopCardVo> pageItem = jdbcUtil.queryPageItem(sql,shopCardQo.getCurPage(),shopCardQo.getPageSize(),ShopCardVo.class);
        return pageItem;
    }

    public PageItem<ShopCardVo> getCardList(ShopCardQo shopCardQo) throws Exception{
        String sql ="SELECT sc.* FROM shop_card sc WHERE sc.shop_id = "+shopCardQo.getShopId()+" and ( sc.period_type=1 or ( sc.period_type=2 and sc.end_time >= now()))  order by sc.create_time desc";
        PageItem<ShopCardVo> pageItem = jdbcUtil.queryPageItem(sql,shopCardQo.getCurPage(),shopCardQo.getPageSize(),ShopCardVo.class);
        return pageItem;
    }
}