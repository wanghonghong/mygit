package com.jm.business.service.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jm.mvc.vo.qo.ShoppingCartQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.product.ProductTrans;
import com.jm.repository.po.wx.WxUserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.mvc.vo.PageItem;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.order.OrderDetailRepository;
import com.jm.repository.jpa.order.OrderRepository;
import com.jm.repository.po.order.OrderDetail;
import com.jm.repository.po.order.OrderInfo;
import org.springframework.util.StringUtils;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/10
 */
@Service
public class OrderDetailService {

   @Autowired
   private OrderDetailRepository orderDetailRepository;
   
   @Autowired
   private JdbcRepository jdbcRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    public List<OrderDetail> queryOrderDetail(Long orderInfoId){
        return orderDetailRepository.findOrderDetailByOrderInfoId(orderInfoId);
    }
    
    public List<OrderDetail> getOrderDetailByOrderInfoId(Long orderInfoId) throws IllegalAccessException, IOException, InstantiationException {
        String sql ="SELECT * FROM order_detail od WHERE od.order_info_id ="+orderInfoId;
        return jdbcUtil.queryList(sql,OrderDetail.class);
    	/*List<OrderDetail> list = orderDetailRepository.findOrderDetailByOrderInfoId(orderInfoId);
    	return list;*/
    }
    
    public PageItem<Map<String, Object>> getOrderDetails(String orderInfoId){
    	return jdbcRepository.getOrderByIds(orderInfoId);
    }

    public List<ProductTrans> getProductTrans(List<ShoppingCartQo> shoppingCartQos, WxUserAddress addr){
        String areaCode = "";
        List<ProductTrans> productTranses = new ArrayList<>();
        for (ShoppingCartQo shoppingCartQo : shoppingCartQos){
            ProductTrans productTrans = new  ProductTrans();
            productTrans.setPid(shoppingCartQo.getProductId());
            productTrans.setBuyCount(shoppingCartQo.getCount());
            if(addr!=null){
                if(!StringUtils.isEmpty(addr.getAreaCode())){
                    //如果地区编号不为空则取出计算运费
                    areaCode = addr.getAreaCode();
                }else{
                    areaCode="350100";
                }
            }else{
                areaCode="350100";
            }
            productTrans.setAreaId(areaCode);
            productTranses.add(productTrans);
        }
        return productTranses;
    }

    
}
