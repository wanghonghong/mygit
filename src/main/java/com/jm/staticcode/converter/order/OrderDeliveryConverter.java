/**
 * 
 */
package com.jm.staticcode.converter.order;

import com.jm.business.service.order.SendCompanysKd100Service;
import com.jm.mvc.vo.order.OrderDeliveryVo;
import com.jm.mvc.vo.order.OrderDetailVo;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.order.OrderDeliveryCreateVo;
import com.jm.repository.po.order.OrderDelivery;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>订单发货信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/6/6
 */
public class OrderDeliveryConverter {

	 public static OrderDelivery toOrderDelivery(OrderDeliveryCreateVo orderVo) {
		OrderDelivery orderDelivery = new OrderDelivery();
        BeanUtils.copyProperties(orderVo,orderDelivery);
        return orderDelivery;
	 }

	 public List<OrderDeliveryVo> p2v(List<OrderDelivery> orderDeliveryList, List<Map<String, Object>> detailVoList,SendCompanysKd100Service sendCompanysKd100Service){
	 	List<OrderDeliveryVo> orderDeliveryVoList = new ArrayList<>();
	 	for(OrderDelivery orderDelivery:orderDeliveryList){
			OrderDeliveryVo orderDeliveryVo = new OrderDeliveryVo();
			BeanUtils.copyProperties(orderDelivery,orderDeliveryVo);
			String url = sendCompanysKd100Service.send_list(orderDelivery.getTransCode(),orderDelivery.getTransNumber());
			if(url.contains("ERROR")){
				url = "ERROR";
			}
			orderDeliveryVo.setTransMsg(url);
			List<OrderDetailVo> orderDetailVoList = new ArrayList<>();
			for(Map<String, Object> detailObj:detailVoList){
				if(orderDeliveryList.size()>1){
					if(orderDelivery.getOrderDeliveryId().equals(detailObj.get("order_delivery_id"))){
						orderDetailVoList = doConverter(orderDetailVoList,detailObj);
					}
				}else{
					orderDetailVoList = doConverter(orderDetailVoList,detailObj);
				}
				orderDeliveryVo.setOrderDetails(orderDetailVoList);
			}
			orderDeliveryVoList.add(orderDeliveryVo);
		}
	 	return orderDeliveryVoList;
	 }

	 private List<OrderDetailVo> doConverter(List<OrderDetailVo> orderDetailVoList,Map<String, Object> detailObj){
		 OrderDetailVo orderDetailVo = new OrderDetailVo();
		 if (!StringUtils.isEmpty(detailObj.get("spec_pic"))) {
			 orderDetailVo.setPic(ImgUtil.appendUrl(detailObj.get("spec_pic").toString(), 100));
		 } else if(!StringUtils.isEmpty(detailObj.get("pic_square"))){
			 orderDetailVo.setPic(ImgUtil.appendUrl(detailObj.get("pic_square").toString(), 100));
		 }
		 orderDetailVoList.add(orderDetailVo);
		 return orderDetailVoList;
	 }
}
