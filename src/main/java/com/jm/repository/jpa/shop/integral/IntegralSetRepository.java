package com.jm.repository.jpa.shop.integral;

import com.jm.repository.po.shop.integral.IntegralSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * <p>积分设置</p>
 *
 * @author 吴克府
 * @version latest
 * @date 2016/9/30
 */
public interface IntegralSetRepository extends JpaRepository<IntegralSet, Integer>{

    IntegralSet findByShopId(Integer shopId);

}
