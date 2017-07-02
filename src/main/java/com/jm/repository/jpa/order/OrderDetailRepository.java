package com.jm.repository.jpa.order;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;

/**
 * <p>订单详情</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    
	
	
    Page<OrderDetail> findAll(Specification<OrderDetail> specification, Pageable pageable);

    //@Query(value = "select * from Order_Detail where order_info_id=?1", nativeQuery = true)
    
    List<OrderDetail> findOrderDetailByOrderInfoId(Long orderInfoId);


    @Query(value = "SELECT d.order_detail_id, p.pic_square, p.name, p.price, s.spec_name_one, s.spec_value_one, s.spec_pic, s.spec_price, d.count " +
            " FROM order_detail d LEFT JOIN product p ON d.pid = p.pid LEFT JOIN product_spec s on d.product_spec_id = s.product_spec_id " +
            " WHERE d.order_info_id =?1 ORDER BY d.create_time desc",nativeQuery=true)
    List getDetailList(Long orderInfoId);
    
    @Query(value = "select o.* from order_detail o"
    		+ " where order_info_id in (?1)",nativeQuery=true)
    List<OrderDetail> orderDetail(String orderInfoIds);
    
    OrderDetail findOrderDetailByProductSpecId(Integer productSpecId);

	List<OrderDetail> findOrderDetailByOrderInfoId(
			Integer orderInfoId);

    @Query(value = "select o.* from order_detail o"
    		+ " where order_info_id in (?1)",nativeQuery=true)
	List<OrderDetail> findByOrderInfoIdIn(List<Long> orderInfoIds);
	
    
}