package com.jm.repository.jpa.shop.integral;

import com.jm.repository.po.shop.integral.IntegralProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * <p>积分商品换购</p>
 *
 * @author 吴克府
 * @version latest
 * @date 2016/9/30
 */
public interface IntegralProductRepository extends JpaRepository<IntegralProduct, Integer>{

    @Query( value = "select t.*,p.name,p.price,p.pic_square from integral_product t,product p where t.pid=p.pid and t.shop_id=?1",nativeQuery=true)
    List<Object[]> queryIntegralProduct(Integer shopId);

}
