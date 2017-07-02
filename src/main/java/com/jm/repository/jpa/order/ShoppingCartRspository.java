package com.jm.repository.jpa.order;

import com.jm.mvc.vo.qo.ShoppingCartQo;
import com.jm.repository.po.order.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 *<p></p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
public interface ShoppingCartRspository extends JpaRepository<ShoppingCart, Long> {
	
	@Query( value = " select shopCart.id,shopCart.price,shopCart.count,shopCart.product_id,"
				  + " shopCart.product_spec_id,shopCart.shop_id,shopCart.user_id,productSpec.spec_value_one,productSpec.spec_value_two,productSpec.spec_value_three,"
		          + " product.name,product.pic,shop.shop_name from shopping_cart shopCart left join product_spec productSpec on shopCart.product_spec_id=productSpec.product_spec_id"
		          + " left join product product on shopCart.product_id=product.pid left join shop shop on shopCart.shop_id=shop.shop_id where shop.shop_id=?1 and shopCart.user_id=?2",nativeQuery=true)
	List<ShoppingCartQo> queryObject(Integer userId,Integer shopId);
	
	@Query( value = " select * from shopping_cart where id=?1",nativeQuery=true)
	ShoppingCart queryById(Integer id);
	
	
	ShoppingCart findShoppingCartByShopIdAndUserIdAndProductIdAndProductSpecIdAndType(Integer shopId,Integer userId,Integer productId,Integer productSpecId,int type);

	List<ShoppingCart> findShoppingCartByShopIdAndUserIdAndProductIdAndType(Integer shopId,Integer userId,Integer productId,int type);

	ShoppingCart findShoppingCartByShopIdAndUserIdAndProductSpecId(Integer shopId,Integer userId,Integer productSpecId);
	
	/*@Query( value = " select * from shopping_cart where shop_id=?1 and user_id=?2 and product_id=?3 and product_spec_id=?4",nativeQuery=true)
	ShoppingCart queryByProductId(Integer shopId,Integer userId,Integer productId,Integer productSpecId);*/
	
	Page<ShoppingCart> findAll(Specification<ShoppingCart> specification, Pageable pageable);

	List<ShoppingCart> findShoppingCartByShopIdAndUserIdAndType(int shopId, int userId,int type);

}
