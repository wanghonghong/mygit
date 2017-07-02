package com.jm.business.service.order.recycle;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookVo;
import com.jm.mvc.vo.order.OrderInfoForQueryVo;
import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderBookRepository;
import com.jm.repository.jpa.order.recycle.RecycleDetailRepository;
import com.jm.repository.po.order.recycle.RecycleDetail;
import com.jm.staticcode.converter.order.OrderBookConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RecycleDetailService {

	@Autowired
	private RecycleDetailRepository recycleDetailRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;

	public RecycleDetail findOrderId(Long id){
		return recycleDetailRepository.findRecycleDetailByOrderId(id);
	}

	/***
	 * 保存订单回收详情
	 * @param recycleDetail
	 * @return
	 */
	public RecycleDetail createRecycleOrder(RecycleDetail recycleDetail){
		return recycleDetailRepository.save(recycleDetail);
	}

}
