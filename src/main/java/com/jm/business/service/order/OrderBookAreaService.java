package com.jm.business.service.order;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookAreaQo;
import com.jm.mvc.vo.order.OrderBookAreaVo;
import com.jm.mvc.vo.order.OrderBookVo;
import com.jm.mvc.vo.order.OrderInfoForQueryVo;
import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
import com.jm.mvc.vo.order.recycle.RecycleNodeVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderBookAreaRepository;
import com.jm.repository.jpa.order.OrderBookRepository;
import com.jm.repository.po.order.OrderBookArea;
import com.jm.staticcode.converter.order.OrderBookConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class OrderBookAreaService {

	@Autowired
	private OrderBookAreaRepository orderBookAreaRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;

	public OrderBookArea save(OrderBookArea area){
		return orderBookAreaRepository.save(area);
	};

	public List<OrderBookArea> findListByShopId(Integer shopId){
		return orderBookAreaRepository.findByShopId(shopId);
	};

	public PageItem<OrderBookAreaVo> findListByShopId(OrderBookAreaQo orderBookAreaQo) throws IllegalAccessException, IOException, InstantiationException {
		String sql = "select * from order_book_area where shop_id="+orderBookAreaQo.getShopId();
		PageItem<OrderBookAreaVo> pageItem = jdbcUtil.queryPageItem(sql,orderBookAreaQo.getCurPage(),orderBookAreaQo.getPageSize(),OrderBookAreaVo.class);
		return pageItem;
	};

	public OrderBookArea findByIdAndShopId(Integer id,Integer shopId){
		return orderBookAreaRepository.findByIdAndShopId(id,shopId);
	};


	@Transactional
	public void deleteByShopId(Integer shopId,Integer id){
		orderBookAreaRepository.deleteByShopId(shopId,id);
	};


}
