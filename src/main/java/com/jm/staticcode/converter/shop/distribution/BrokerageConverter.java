package com.jm.staticcode.converter.shop.distribution;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.mvc.vo.shop.distribution.*;
import com.jm.mvc.vo.shop.recharge.AgentRechargeOrderCo;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.repository.po.shop.brokerage.BrokerageConfig;
import com.jm.repository.po.shop.brokerage.BrokerageProduct;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 佣金分销设置类bean转化
 * @author zhengww
 *
 */
public class BrokerageConverter {


	public static BrokerageSet toBrokerageSetting(BrokerageSetUo brokerageSetUo, BrokerageSet brokerageSet) {
		BeanUtils.copyProperties(brokerageSetUo, brokerageSet);
		return brokerageSet;
	}

	public static BrokerageSetVo toBrokerageSettingVo(BrokerageSet brokerageSet) {
		BrokerageSetVo brokerageSetVo = new BrokerageSetVo();
		BeanUtils.copyProperties(brokerageSet, brokerageSetVo);
		return brokerageSetVo;
	}

	public static BrokerageSet toBrokerageSetting(BrokerageSetCo brokerageSetCo) {
		BrokerageSet brokerageSet = new BrokerageSet();
		BeanUtils.copyProperties(brokerageSetCo, brokerageSet);
		return brokerageSet;
	}

	public static BrokerageConfigVo toBrokerageConfigVo(BrokerageConfig brokerageConfig) {
		BrokerageConfigVo brokerageConfigVo = new BrokerageConfigVo();
		BeanUtils.copyProperties(brokerageConfig, brokerageConfigVo);
		if(StringUtil.isNotNull(brokerageConfigVo.getImgUrl())){
			String imgUrl = ImgUtil.appendUrl(brokerageConfigVo.getImgUrl(),720);
			brokerageConfigVo.setImgUrl(imgUrl);
		}
		return brokerageConfigVo;
	}

	public static BrokerageConfig toBrokerageConfig(BrokerageConfigCo brokerageConfigCo) {
		BrokerageConfig brokerageConfig = new BrokerageConfig();
		BeanUtils.copyProperties(brokerageConfigCo, brokerageConfig);
		if(StringUtil.isNotNull(brokerageConfig.getImgUrl())){
			brokerageConfig.setImgUrl(ImgUtil.substringUrl(brokerageConfig.getImgUrl()));
		}
		return brokerageConfig;
	}

	public static BrokerageConfig toBrokerageConfig(BrokerageConfigUo brokerageConfigUo, BrokerageConfig brokerageConfig) {
		BeanUtils.copyProperties(brokerageConfigUo, brokerageConfig);
		if(StringUtil.isNotNull(brokerageConfig.getImgUrl())){
			brokerageConfig.setImgUrl(ImgUtil.substringUrl(brokerageConfig.getImgUrl()));
		}
		return brokerageConfig;
	}

	public static BrokerageProduct toBrokerageProduct(BrokerageProductCo brokerageProductCo) {
		BrokerageProduct brokerageProduct = new BrokerageProduct();
		BeanUtils.copyProperties(brokerageProductCo,brokerageProduct);
		return brokerageProduct;
	}

    public static PageItem<ChannelRecordVo> ChannelRecord2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<ChannelRecordVo> pageItemVo = new PageItem<>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<ChannelRecordVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			ChannelRecordVo channelRecordVo = JsonMapper.map2Obj(map,ChannelRecordVo.class);
			if(!StringUtils.isEmpty(map.get("nickname"))){
				channelRecordVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
			}
			if(!StringUtils.isEmpty(map.get("headimgurl"))) {
				channelRecordVo.setHeadimgurl(map.get("headimgurl").toString());
			}
			list.add(channelRecordVo);
		}
		pageItemVo.setCount(pageItem.getCount());
		pageItemVo.setItems(list);
		return pageItemVo;
    }

//    public static ShopSmallVo toSmallShopVo(ShopSmall shopSmall) {
//		ShopSmallVo shopSmallVo = new ShopSmallVo();
//		BeanUtils.copyProperties(shopSmall, shopSmallVo);
//		if(StringUtil.isNotNull(shopSmallVo.getBackUrl())){
//			String imgUrl = ImgUtil.appendUrl(shopSmallVo.getBackUrl(),720);
//			shopSmallVo.setBackUrl(imgUrl);
//		}
//		return shopSmallVo;
//    }
//
//	public static ShopSmall toSmallShop(ShopSmallCo shopSmallCo) {
//		ShopSmall shopSmall = new ShopSmall();
//		BeanUtils.copyProperties(shopSmallCo, shopSmall);
//		if(StringUtil.isNotNull(shopSmallCo.getBackUrl())){
//			shopSmall.setBackUrl(ImgUtil.substringUrl(shopSmall.getBackUrl()));
//		}
//		return shopSmall;
//	}
//
//
//	public static ShopSmall toSmallShop(ShopSmallUo shopSmallUo, ShopSmall shopSmall) {
//		BeanUtils.copyProperties(shopSmallUo, shopSmall);
//		if(StringUtil.isNotNull(shopSmall.getBackUrl())){
//			shopSmall.setBackUrl(ImgUtil.substringUrl(shopSmall.getBackUrl()));
//		}
//		return shopSmall;
//	}

    public static RechargeOrder toRechargeOrder(AgentRechargeOrderCo agentRechargeOrderCo) {
		RechargeOrder rechargeOrder = new RechargeOrder();
		BeanUtils.copyProperties(agentRechargeOrderCo, rechargeOrder);
		return rechargeOrder;
    }

    public static PageItemExt<BrokerageDetailListVo,Integer> BrokerageDetail2v(PageItem<Map<String, Object>> pageItem,Integer commissionPrice) throws IOException {
		PageItemExt<BrokerageDetailListVo,Integer> pageItems=new PageItemExt<BrokerageDetailListVo,Integer>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<BrokerageDetailListVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			BrokerageDetailListVo brokerageDetailListVo = JsonMapper.map2Obj(map,BrokerageDetailListVo.class);
			list.add(brokerageDetailListVo);
		}
		pageItems.setCount(pageItem.getCount());
		pageItems.setItems(list);
		pageItems.setExt(commissionPrice);
		return pageItems;
    }

    public static PageItem<BrokerageOrderVo> brokerageOrder2v(PageItem<Map<String, Object>> pageItem) throws IOException  {
		PageItem<BrokerageOrderVo> brokerageOrderVos= new PageItem<BrokerageOrderVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<BrokerageOrderVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			BrokerageOrderVo brokerageOrderVo = JsonMapper.map2Obj(map,BrokerageOrderVo.class);
			brokerageOrderVo.setNickname(Base64Util.getFromBase64(brokerageOrderVo.getNickname()));
			list.add(brokerageOrderVo);
		}
		brokerageOrderVos.setCount(pageItem.getCount());
		brokerageOrderVos.setItems(list);
		return brokerageOrderVos;
    }


    public static PageItem<WxAccountVo> WxAccount2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<WxAccountVo> wxAccountVos= new PageItem<WxAccountVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<WxAccountVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			WxAccountVo wxAccountVo = JsonMapper.map2Obj(map,WxAccountVo.class);
			wxAccountVo.setNickname(Base64Util.getFromBase64(wxAccountVo.getNickname()));
			list.add(wxAccountVo);
		}
		wxAccountVos.setCount(pageItem.getCount());
		wxAccountVos.setItems(list);
		return wxAccountVos;
    }

	public static PageItem<WxAccountKitVo> WxAccountKit2v(PageItem<Map<String, Object>> pageItem) throws IOException{
		PageItem<WxAccountKitVo> wxAccountVos= new PageItem<WxAccountKitVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<WxAccountKitVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			WxAccountKitVo wxAccountKitVo = JsonMapper.map2Obj(map,WxAccountKitVo.class);
			wxAccountKitVo.setNickname(Base64Util.getFromBase64(wxAccountKitVo.getNickname()));
			list.add(wxAccountKitVo);
		}
		wxAccountVos.setCount(pageItem.getCount());
		wxAccountVos.setItems(list);
		return wxAccountVos;
	}

    public static PageItem<BrokerageRecordVo> brokerageRecord2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<BrokerageRecordVo> brokerageOrderVos= new PageItem<BrokerageRecordVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<BrokerageRecordVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			BrokerageRecordVo brokerageRecordVo = JsonMapper.map2Obj(map,BrokerageRecordVo.class);
			brokerageRecordVo.setNickname(Base64Util.getFromBase64(brokerageRecordVo.getNickname()));
			list.add(brokerageRecordVo);
		}
		brokerageOrderVos.setCount(pageItem.getCount());
		brokerageOrderVos.setItems(list);
		return brokerageOrderVos;
    }

    public static PageItem<BrokerageProductVo> brokerageProduct2v(PageItem<Map<String, Object>> pageItem)  throws IOException{
		PageItem<BrokerageProductVo> brokerageProductVos= new PageItem<BrokerageProductVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<BrokerageProductVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			BrokerageProductVo brokerageProductVo = JsonMapper.map2Obj(map,BrokerageProductVo.class);
			list.add(brokerageProductVo);
		}
		brokerageProductVos.setCount(pageItem.getCount());
		brokerageProductVos.setItems(list);
		return brokerageProductVos;
    }

}
