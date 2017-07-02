package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.integral.IntegralRecordVo;
import com.jm.mvc.vo.shop.integral.IntegralProductVo;
import com.jm.mvc.vo.shop.recharge.RechargeOrderCo;
import com.jm.mvc.vo.shop.recharge.RechargeOrderVo;
import com.jm.repository.po.shop.RechargeOrder;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>积分装换</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/09/30
 */
public class IntegralConverter {

	public static PageItem<IntegralRecordVo> p2v(PageItem<Map<String,Object>> pageItemMap,String unitName) throws IOException {
		PageItem<IntegralRecordVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<IntegralRecordVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			IntegralRecordVo integralRecordVo = JsonMapper.map2Obj(map,IntegralRecordVo.class);
			integralRecordVo.setNickname(Base64Util.getFromBase64(integralRecordVo.getNickname()));
			integralRecordVo.setUnitName(unitName);
            list.add(integralRecordVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static PageItem<IntegralProductVo> toIntegralProductVos(PageItem<Map<String, Object>> pageItemMap) throws IOException {
		PageItem<IntegralProductVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<IntegralProductVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			IntegralProductVo integralProductVo = JsonMapper.map2Obj(map,IntegralProductVo.class);
			integralProductVo.setPic(ImgUtil.appendUrl(String.valueOf(map.get("pic")),100));
            list.add(integralProductVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

    public static RechargeOrder toRechargeOrder(RechargeOrderCo rechargeOrderCo) {
		RechargeOrder rechargeOrder = new RechargeOrder();
		BeanUtils.copyProperties(rechargeOrderCo,rechargeOrder);
		rechargeOrder.setType(1);
		rechargeOrder.setOrderNum(Toolkit.getOrderNum("J"));
		return rechargeOrder;
    }

	public static List<RechargeOrderVo> toRechargeOrderVoList(List<RechargeOrder> rechargeOrderList) {
		List<RechargeOrderVo> rechargeOrderVoList = new ArrayList<>();
		for (RechargeOrder rechargeOrder : rechargeOrderList){
			RechargeOrderVo rechargeOrderVo = new RechargeOrderVo();
			BeanUtils.copyProperties(rechargeOrder,rechargeOrderVo);
			rechargeOrderVoList.add(rechargeOrderVo);
		}
		return rechargeOrderVoList;
	}
}
