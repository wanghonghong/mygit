package com.jm.staticcode.converter.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jm.business.service.order.SendCompanysKd100Service;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.*;
import com.jm.repository.po.order.OrderInfo;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.wx.Base64Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
public class OrderConverter {

    public static OrderInfo toOrder(OrderInfoCreateVo orderVo) {
    	OrderInfo order = new OrderInfo();
        BeanUtils.copyProperties(orderVo,order);
        return order;
    }

    public static OrderInfo toOrder(OrderInfoForUpdateVo orderVo) {
    	OrderInfo order = new OrderInfo();
        BeanUtils.copyProperties(orderVo,order);
        return order;
    }

	public static OrderInfoVo toOrderVo(OrderInfo orderInfo) {
		OrderInfoVo orderInfoVo = new OrderInfoVo();
		BeanUtils.copyProperties(orderInfo,orderInfoVo);
		return orderInfoVo;
	}

    
    public static PageItem<OrderInfoVo> toOrderInfoVo(Page<OrderInfo> orderInfoPage) {
    	PageItem<OrderInfoVo> pageItem = new PageItem<OrderInfoVo>();
    	if(orderInfoPage!=null){
    		pageItem.setCount(orderInfoPage.getTotalPages());
    		List<OrderInfoVo> orderInfoVos = new ArrayList<>();
    		List<OrderInfo> orders = orderInfoPage.getContent();
    		for(OrderInfo orderInfo : orders){
    			OrderInfoVo orderInfoVo = new OrderInfoVo();
    			BeanUtils.copyProperties(orderInfo, orderInfoVo);
    			orderInfoVos.add(orderInfoVo);
    		}
    		pageItem.setItems(orderInfoVos);
    		pageItem.setCount(orderInfoPage.getTotalPages());
    	}
    	return pageItem;
    }

	public static PageItem<OrderInfoVo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<OrderInfoVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<OrderInfoVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderInfoVo orderInfoVo = JsonMapper.map2Obj(map,OrderInfoVo.class);
			if(orderInfoVo.getPlatform()==0){
				if(!StringUtils.isEmpty(map.get("nickname"))){
					orderInfoVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
				}
			}
			if(!StringUtils.isEmpty(map.get("headimgurl"))) {
				orderInfoVo.setHeadimgurl(map.get("headimgurl").toString());
			}
			list.add(orderInfoVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public OrderInfoDetailVo m2l(List<Map<String,Object>> maps,List<Map<String, Object>> ms1,List<Map<String, Object>> detailVoList,SendCompanysKd100Service sendCompanysKd100Service,int type) throws IOException {
		OrderInfoDetailVo orderInfoDetailVo = new OrderInfoDetailVo();
		for (Map<String,Object> map : maps){
			orderInfoDetailVo = JsonMapper.map2Obj(map,OrderInfoDetailVo.class);
			if(!StringUtils.isEmpty(map.get("nickname"))){
				orderInfoDetailVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
			}
		}
		orderInfoDetailVo = getOrderInfoDetailVo(orderInfoDetailVo,ms1,detailVoList,sendCompanysKd100Service,type);
		return orderInfoDetailVo;
	}

	public static OrderInfoDetailVo m2v(List<Map<String,Object>> maps) throws IOException {
        OrderInfoDetailVo orderInfoDetailVo = new OrderInfoDetailVo();
        for (Map<String,Object> map : maps){
            orderInfoDetailVo = JsonMapper.map2Obj(map,OrderInfoDetailVo.class);
            if(!StringUtils.isEmpty(map.get("nickname"))){
                orderInfoDetailVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
            }
        }
        return orderInfoDetailVo;
	}

	public static List<OrderDetailVo> convertOrderDetailVo(List<Map<String,Object>> maps) throws IOException {
		List<OrderDetailVo> orderDetails = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderDetailVo orderDetailVo = JsonMapper.map2Obj(map,OrderDetailVo.class);
			if (!StringUtils.isEmpty(map.get("spec_pic"))) {
				orderDetailVo.setPic(ImgUtil.appendUrl(map.get("spec_pic").toString(), 100));
			} else if (!StringUtils.isEmpty(map.get("pic_square"))) {
				orderDetailVo.setPic(ImgUtil.appendUrl(map.get("pic_square").toString(), 100));
			} else {
				orderDetailVo.setPic("");
			}
			orderDetails.add(orderDetailVo);
		}
		return orderDetails;
	}

	public static List<OrderInfoVo> convertOrderInfoVo(List<Map<String,Object>> maps) throws IOException {
		List<OrderInfoVo> orderInfoVoList = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderInfoVo orderInfoVo = JsonMapper.map2Obj(map,OrderInfoVo.class);
			orderInfoVoList.add(orderInfoVo);
		}
		return orderInfoVoList;
	}

	public static OrderAndDispatchDeliveryVo convertOrderAndDeliveryVo(List<Map<String,Object>> maps,SendCompanysKd100Service sendCompanysKd100Service) throws IOException {
		OrderAndDispatchDeliveryVo orderAndDispatchDeliveryVo = new OrderAndDispatchDeliveryVo();
		List<OrderAndDeliveryVo> orderAndDeliveryVoList = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderAndDeliveryVo orderAndDeliveryVo = JsonMapper.map2Obj(map,OrderAndDeliveryVo.class);
			if(!StringUtils.isEmpty(map.get("nickname"))){
				orderAndDeliveryVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
			}
			String transMsg = sendCompanysKd100Service.send_list(orderAndDeliveryVo.getTransCode(),orderAndDeliveryVo.getTransNumber());
			if(transMsg.contains("ERROR")){
				transMsg = "ERROR";
			}
			orderAndDeliveryVo.setTransMsg(transMsg);
			orderAndDeliveryVoList.add(orderAndDeliveryVo);
		}
		orderAndDispatchDeliveryVo.setOrderAndDeliveryVos(orderAndDeliveryVoList);
		return orderAndDispatchDeliveryVo;
	}

	public static PageItem<ProductOrderCountVo> productOrderCount2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<ProductOrderCountVo> pageItemVo = new PageItem<>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<ProductOrderCountVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			ProductOrderCountVo productOrderCountVo = JsonMapper.map2Obj(map,ProductOrderCountVo.class);
			if(!StringUtils.isEmpty(map.get("nickname"))){
				productOrderCountVo.setNickname(Base64Util.getFromBase64(map.get("nickname").toString()));
			}
			if(!StringUtils.isEmpty(map.get("headimgurl"))) {
				productOrderCountVo.setHeadimgurl(map.get("headimgurl").toString());
			}
			list.add(productOrderCountVo);
		}
		pageItemVo.setCount(pageItem.getCount());
		pageItemVo.setItems(list);
		return pageItemVo;
	}

	private List<OrderDetailVo> doConverter(List<OrderDetailVo> orderDetailVoList,Map<String, Object> obj,Map<String, Object> detailObj) throws IOException {
		OrderDetailVo orderDetailVo = new OrderDetailVo();
		orderDetailVo = JsonMapper.map2Obj(detailObj,OrderDetailVo.class);
		if (!StringUtils.isEmpty(detailObj.get("spec_pic"))) {
			orderDetailVo.setPic(ImgUtil.appendUrl(detailObj.get("spec_pic").toString(), 100));
		} else if(!StringUtils.isEmpty(detailObj.get("pic_square"))){
			orderDetailVo.setPic(ImgUtil.appendUrl(detailObj.get("pic_square").toString(), 100));
		} else {
			orderDetailVo.setPic("");
		}
		orderDetailVoList.add(orderDetailVo);
		return orderDetailVoList;
	}

	private OrderInfoDetailVo getOrderInfoDetailVo(OrderInfoDetailVo orderInfoDetailVo,List<Map<String, Object>> ms1,
        List<Map<String, Object>> detailVoList,SendCompanysKd100Service sendCompanysKd100Service,int type) throws IOException {
		List<OrderDeliveryVo> orderDeliveryVoList = new ArrayList<>();
		for(Map<String, Object> obj:ms1){
			OrderDeliveryVo orderDeliveryVo = new OrderDeliveryVo();
			orderDeliveryVo = JsonMapper.map2Obj(obj,OrderDeliveryVo.class);
			String transMsg = sendCompanysKd100Service.send_list(orderDeliveryVo.getTransCode(),orderDeliveryVo.getTransNumber());
			if(transMsg.contains("ERROR")){
				transMsg = "ERROR";
			}
			orderDeliveryVo.setTransMsg(transMsg);
			List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
			for(Map<String, Object> detailObj:detailVoList){
				if(ms1.size()>1){
					if(obj.get("order_delivery_id").equals(detailObj.get("order_delivery_id"))){
						orderDetailVoList = doConverter(orderDetailVoList,obj,detailObj);
					}
				}else{
					if(type==1){//供货管理查看订单详情和物流信息
						if(obj.get("order_delivery_id").equals(detailObj.get("order_delivery_id"))&&!StringUtils.isEmpty(detailObj.get("supply_user_id"))){
							orderDetailVoList = doConverter(orderDetailVoList,obj,detailObj);
						}
					}else{//type为0表示订单管理和发货管理查看详情和物流信息
						orderDetailVoList = doConverter(orderDetailVoList,obj,detailObj);
					}
				}
				orderDeliveryVo.setOrderDetails(orderDetailVoList);
			}
			orderDeliveryVoList.add(orderDeliveryVo);
			orderInfoDetailVo.setOrderDeliveryVo(orderDeliveryVoList);
		}
		return orderInfoDetailVo;
	}

	public static List<OrderDispatchVo> convertOrderDispatchVo(List<Map<String,Object>> maps) throws IOException {
		List<OrderDispatchVo> orderDispatchVoList = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderDispatchVo orderDispatchVo = JsonMapper.map2Obj(map,OrderDispatchVo.class);
			orderDispatchVoList.add(orderDispatchVo);
		}
		return orderDispatchVoList;
	}

}
