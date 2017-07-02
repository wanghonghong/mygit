package com.jm.business.service.product.impl;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.trad.*;
import com.jm.repository.po.product.TradSign;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public interface ProductTradServiceImpl {


    /**
     * 新增活动获取
     * @return
     */
    List<TradSign> getCacheTradSign();

    /**
     * 新增活动
     * @param tradActivityCo
     */
    void setTradActivity(TradActivityCo tradActivityCo,Integer shopId);

    /**
     * 修改活动
     * @param tradActivityUo
     */
    void updateTradActivity(TradActivityUo tradActivityUo);

    /**
     * 删除活动
     * @param id
     */
    void deleteTradActivity(Integer id);

    void updateStatus(TradActivityStatusUo tradActivityStatusUo);

    PageItem<TradProductDetailVo> getTradProduct(TradProductQo tradProductQo, Integer shopId) throws IOException;

    TradActivityDetailVo getTradActivityDetail(Integer id);

    List<TradProductDetailVo> getTradProductByPids(TradProductTypeQo tradProductTypeQo)  throws IOException ;
}
