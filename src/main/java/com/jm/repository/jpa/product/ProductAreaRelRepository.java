package com.jm.repository.jpa.product;

import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductAreaRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>商品主表</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/23
 */
public interface ProductAreaRelRepository extends JpaRepository<ProductAreaRel, Integer>{

    @Query( value = "select * from product_area_rel where pid=?1 and user_id=?2 ",nativeQuery=true)
    ProductAreaRel findByPidAndUserId(Integer pid, Integer userId);

    @Query( value = "select * from product_area_rel where pid=?1",nativeQuery=true)
    List<ProductAreaRel> findByPid(Integer pid);

    @Query( value = "select * from product_area_rel where pid in(?1)",nativeQuery=true)
    List<ProductAreaRel> findByPid(String pid);
}
