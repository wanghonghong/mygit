package com.jm.business.service.shop;

import com.jm.mvc.vo.JmMessage;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.ShopTipSetRepository;
import com.jm.repository.po.shop.shopSet.ShopTipSet;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>店铺打赏配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/20 10:14
 */
@Log4j
@Service
public class ShopTipSetService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ShopTipSetRepository tipConfigRepository;

    @ApiOperation("打赏配置保存")
    public JmMessage saveShopTip(ShopTipSet shopTipSet, Integer shopId){
        ShopTipSet shopTipSetOld = tipConfigRepository.findShopTipSetByShopId(shopId);
        if(shopTipSetOld!=null){
            shopTipSet.setId(shopTipSetOld.getId());
            BeanUtils.copyProperties(shopTipSet,shopTipSetOld);
            tipConfigRepository.save(shopTipSetOld);
            return new JmMessage(0,"配置保存成功!");
        }
        tipConfigRepository.save(shopTipSet);
        return new JmMessage(0,"配置保存成功!");
    }

    @ApiOperation("查找店铺打赏配置")
    public ShopTipSet findTipConfByShopId(Integer shopId){
        return tipConfigRepository.findShopTipSetByShopId(shopId);
    }
}
