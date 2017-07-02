package com.jm.staticcode.converter.product;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.trad.*;
import com.jm.repository.po.product.TradActivity;
import com.jm.repository.po.product.TradSign;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class TradActivityConverter {


	public static TradActivity toTradActivity(TradActivityCo tradActivityCo) {
		TradActivity tradActivity = new TradActivity();
		BeanUtils.copyProperties(tradActivityCo,tradActivity);
		if (tradActivity.getImageUrl()!=null){
			tradActivity.setImageUrl(ImgUtil.substringUrl(tradActivity.getImageUrl()));
		}
		return tradActivity;
	}

	public static TradActivity toTradActivity(TradActivity tradActivity, TradActivityUo tradActivityUo) {
		BeanUtils.copyProperties(tradActivityUo,tradActivity);
		if (tradActivity.getImageUrl()!=null){
			tradActivity.setImageUrl(ImgUtil.substringUrl(tradActivity.getImageUrl()));
		}
		return tradActivity;
	}

	public static TradActivityDetailVo toTradActivityDetailVo(TradActivity tradActivity) {
		TradActivityDetailVo tradActivityDetailVo = new TradActivityDetailVo();
		BeanUtils.copyProperties(tradActivity,tradActivityDetailVo);
		if (tradActivityDetailVo.getImageUrl()!=null){
			tradActivityDetailVo.setImageUrl(ImgUtil.appendUrl(tradActivityDetailVo.getImageUrl(),720));
		}
		return tradActivityDetailVo;
	}

	public static PageItem<TradProductDetailVo> tradProduct2v(PageItem<Map<String, Object>> pageItem, List<TradSign> tradSigns) throws IOException {
		PageItem<TradProductDetailVo> tradProducts = new PageItem<TradProductDetailVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<TradProductDetailVo> list = new ArrayList<>();
		for(Map<String, Object> map : maps ){
			TradProductDetailVo tradProductDetailVo =  JsonMapper.map2Obj(map,TradProductDetailVo.class);
			if (tradProductDetailVo.getSign()!=99){
				for (TradSign tradSign : tradSigns){
					if (tradProductDetailVo.getSign()==tradSign.getId()){
						tradProductDetailVo.setImageUrl(tradSign.getImageUrl());
					}
				}
			}else {
				tradProductDetailVo.setImageUrl(ImgUtil.appendUrl(tradProductDetailVo.getImageUrl(),720));
			}
			tradProductDetailVo.setPicSquare(ImgUtil.appendUrl(tradProductDetailVo.getPicSquare(),720));
			tradProductDetailVo.setPicRectangle(ImgUtil.appendUrl(tradProductDetailVo.getPicRectangle(),720));
			list.add(tradProductDetailVo);
		}
		tradProducts.setCount(pageItem.getCount());
		tradProducts.setItems(list);
		return tradProducts;
	}

    public static List<TradProductDetailVo> TradProductDetail2v(List<Map<String, Object>> list, List<TradSign> tradSigns) throws IOException {
		List<TradProductDetailVo> tradProductDetails = new ArrayList<>();
		for(Map<String, Object> map : list ){
			TradProductDetailVo tradProductDetailVo =  JsonMapper.map2Obj(map,TradProductDetailVo.class);
			if (tradProductDetailVo.getSign()!=99){
				for (TradSign tradSign : tradSigns){
					if (tradProductDetailVo.getSign()==tradSign.getId()){
						tradProductDetailVo.setImageUrl(tradSign.getImageUrl());
					}
				}
			}else {
				tradProductDetailVo.setImageUrl(ImgUtil.appendUrl(tradProductDetailVo.getImageUrl(),720));
			}
			tradProductDetailVo.setPicSquare(ImgUtil.appendUrl(tradProductDetailVo.getPicSquare(),720));
			tradProductDetailVo.setPicRectangle(ImgUtil.appendUrl(tradProductDetailVo.getPicRectangle(),720));
			tradProductDetails.add(tradProductDetailVo);
		}
		return tradProductDetails;
    }
}
