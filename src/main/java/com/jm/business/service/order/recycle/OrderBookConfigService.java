package com.jm.business.service.order.recycle;

import com.jm.mvc.vo.order.recycle.OrderBookConfigCo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.recycle.OrderBookConfigRepository;
import com.jm.repository.jpa.order.recycle.RecycleDetailRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.recycle.OrderBookConfig;
import com.jm.repository.po.order.recycle.RecycleDetail;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class OrderBookConfigService {

	@Autowired
	private OrderBookConfigRepository orderBookConfigRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;


	/**
	 * 查看配置
	 * @return
	 */
	public OrderBookConfig queryOrderBookConfig(Integer shopId){
		OrderBookConfig orderBookConfig = orderBookConfigRepository.findOrderBookConfigByShopId(shopId);
		return orderBookConfig;
	}

	/***
	 * 保存地区限定
	 * @param orderBookConfigCo
	 * @return
	 */
	public OrderBookConfig addOrderBookConfig(OrderBookConfigCo orderBookConfigCo ){
		OrderBookConfig orderBookConfig = new OrderBookConfig();
		BeanUtils.copyProperties(orderBookConfigCo,orderBookConfig);
		OrderBookConfig obc = orderBookConfigRepository.findOrderBookConfigByShopId(orderBookConfigCo.getShopId());
		if(obc == null){
			obc = orderBookConfigRepository.save(orderBookConfig);
		}else{
			obc.setAreaLimit(orderBookConfigCo.getAreaLimit());
			obc = orderBookConfigRepository.save(obc);
		}
		return obc;
	}

}
