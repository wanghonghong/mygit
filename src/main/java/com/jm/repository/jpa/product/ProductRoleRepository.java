package com.jm.repository.jpa.product;

import com.jm.repository.po.product.ProductGroupRelation;
import com.jm.repository.po.product.ProductRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * <p>商品分类</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/23
 */
public interface ProductRoleRepository extends JpaRepository<ProductRole, Integer>{

    List<ProductRole> findProductRoleByPid(Integer pid);
}
