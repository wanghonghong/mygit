/**
 * 
 */
package com.jm.business.service.order;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.online.market.ShoppingCartListVo;
import com.jm.mvc.vo.online.market.ShoppingCartQueryVo;
import com.jm.mvc.vo.online.market.ShoppingCartVo;
import com.jm.mvc.vo.order.OrderConfirmVo;
import com.jm.mvc.vo.qo.ShoppingCartQo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.ShoppingCartRspository;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.product.ProductSpecRepository;
import com.jm.repository.po.order.ShoppingCart;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductSpec;
import com.jm.staticcode.converter.online.market.ShoppingCartConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *<p>购物车商品管理</p>
 *
 * @author liangrs
 * @version latest
 * @data 2016年5月9日
 */
@Service
public class ShoppingCartService {


	@Autowired
	private ProductSpecRepository productSpecRepository;

	@Autowired
	private ShoppingCartRspository shoppingCartRspository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	protected JdbcUtil jdbcUtil;
	
	@Autowired
	private JdbcRepository jdbcRepository;

	public ShoppingCart addShoppringCart(ShoppingCart shoppingCart,Integer shopId,Integer userId){
		shoppingCart.setShopId(shopId);
		shoppingCart.setUserId(userId);
		ShoppingCart sc = shoppingCartRspository.findShoppingCartByShopIdAndUserIdAndProductIdAndProductSpecIdAndType(shopId, userId, shoppingCart.getProductId(),shoppingCart.getProductSpecId(),0);
		if(null!=sc){
			sc.setCount(sc.getCount()+shoppingCart.getCount());
			sc.setUpdateTime(new Date());
			return shoppingCartRspository.save(sc);
		}else{
			shoppingCart.setUpdateTime(new Date());
			return shoppingCartRspository.save(shoppingCart);
		}
	}

	public List<ShoppingCart> addShoppringCart(ShoppingCart shoppingCart,Integer shopId,Integer userId,int type){
		shoppingCart.setShopId(shopId);
		shoppingCart.setUserId(userId);
		shoppingCart.setType(type);
		List<ShoppingCart> sc = shoppingCartRspository.findShoppingCartByShopIdAndUserIdAndProductIdAndType(shopId, userId, shoppingCart.getProductId(),type);
		List<ShoppingCart> shoppingCartList = new ArrayList<>();
		if(sc.size()>0){
			for(ShoppingCart shoppingCart1:sc){
				shoppingCart1.setUpdateTime(new Date());
				shoppingCartList.add(shoppingCart1);
			}
		}else{
			shoppingCart.setUpdateTime(new Date());
			shoppingCartList.add(shoppingCart);
		}
		return shoppingCartRspository.save(shoppingCartList);
	}

	/*public ShoppingCart addShoppringCart(ShoppingCart shoppingCart,Integer shopId,Integer userId,int type){
		shoppingCart.setShopId(shopId);
		shoppingCart.setUserId(userId);
		shoppingCart.setType(type);
		ShoppingCart sc = shoppingCartRspository.findShoppingCartByShopIdAndUserIdAndProductIdAndType(shopId, userId, shoppingCart.getProductId(),type);
		if(null!=sc){
			sc.setUpdateTime(new Date());
			return shoppingCartRspository.save(sc);
		}else{
			shoppingCart.setUpdateTime(new Date());
			return shoppingCartRspository.save(shoppingCart);
		}
	}*/

	/***
	 * 删除兴趣单
	 * @return
	 */
	public void deleteShoppingCart(){
		jdbcRepository.deleteShoppingCart();
	}
	
	public Integer deleteShoppingCart(Long id){
		ShoppingCart shoppingCart = shoppingCartRspository.findOne(id);
		if(!StringUtils.isEmpty(shoppingCart)){
			shoppingCartRspository.delete(id);
			return 0;
		}else{
			return 1;
		}
	}
	
	public void updateShoppingCart(Long id,Integer count){
		ShoppingCart shoppingCart = shoppingCartRspository.findOne(id);
		shoppingCart.setCount(count);
		shoppingCartRspository.save(shoppingCart);
	}

	/**
	 * 在线客服模块下的购物车查询
	 * @param shoppingCartQueryVo
	 * @return
	 */
	public PageItem<ShoppingCartVo> queryShoppingCart(ShoppingCartQueryVo shoppingCartQueryVo) throws IOException {
        String sqlList = "select p.pid,p.name,p.pic_square,sc.price,sc.count,sc.update_time,sc.user_id,sc.type,"
                + "ps.product_spec_id,ps.spec_pic,ps.spec_value_one,ps.spec_value_two,ps.spec_value_three "
                + "FROM shopping_cart sc LEFT JOIN product p on p.pid=sc.product_id "
                + "LEFT JOIN product_spec ps on ps.product_spec_id=sc.product_spec_id where 1=1";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("sc.shop_id", shoppingCartQueryVo.getShopId()));
		sqlCondition.append(JdbcUtil.appendAnd("sc.type", shoppingCartQueryVo.getType()));
        sqlCondition.append(JdbcUtil.appendAnd("sc.update_time",shoppingCartQueryVo.getBeginDate(),shoppingCartQueryVo.getEndDate()));
		sqlCondition.append(JdbcUtil.appendAnd("sc.update_time"));
		sqlCondition.append(JdbcUtil.appendOrderBy("sc.update_time"));
        List<Map<String,Object>> pageItem = jdbcUtil.queryList(sqlList+sqlCondition);
        List<ShoppingCartListVo> list = ShoppingCartConverter.p2v(pageItem);
        StringBuilder userIds = new StringBuilder();
        PageItem<Map<String,Object>> pageItem1 = new PageItem<>();
        PageItem<ShoppingCartVo> shoppingCartVoPageItem = new PageItem<>();
        if(list.size()>0){
            for(ShoppingCartListVo shoppingCartListVo : list){
                userIds.append(shoppingCartListVo.getUserId()+",");
            }
            userIds.deleteCharAt(userIds.length()-1);
            String sqlList1 = " select wx.user_id,wx.nickname,su.phone_number,wx.headimgurl,wx.appid,wx.openid"
                    + " from wx_user wx LEFT JOIN shop_user su ON su.id=wx.shop_user_id"
                    + " where wx.is_subscribe=1 and wx.user_id in ("+userIds+")";
            StringBuilder sqlCondition1 = new StringBuilder();
			sqlCondition1.append(JdbcUtil.appendAnd("su.phone_number", shoppingCartQueryVo.getPhoneNumber()));
			sqlCondition1.append(JdbcUtil.appendAnd("su.user_name", shoppingCartQueryVo.getUserName()));
            if(!StringUtils.isEmpty(shoppingCartQueryVo.getNickname())) {
                sqlCondition.append(JdbcUtil.appendAnd("wx.nickname", Base64Util.enCoding(shoppingCartQueryVo.getNickname())));
            }
            pageItem1 = jdbcUtil.queryPageItem(sqlList1+sqlCondition1,shoppingCartQueryVo.getCurPage(),shoppingCartQueryVo.getPageSize());
            shoppingCartVoPageItem = ShoppingCartConverter.p2v(pageItem1,list);
        }
        return shoppingCartVoPageItem;
	}

	public List<ShoppingCartQo> queryShoppingCart(Integer userId,Integer shopId){
		//查询购物车列表
		List<ShoppingCart> carts = shoppingCartRspository.findShoppingCartByShopIdAndUserIdAndType(shopId,userId,0);
		List<Integer> products = new ArrayList();
		List<Integer> productSpecs = new ArrayList();
		//返回值
		List<ShoppingCartQo> shoppingCarts = new ArrayList();
		for (ShoppingCart cart : carts){
			products.add(cart.getProductId());
			if(null!=cart.getProductSpecId()){
				productSpecs.add(cart.getProductSpecId());
			}
		}
		//商品列表
		List<Product> prods = productRepository.findAll(products);
		//商品规格列表
		List<ProductSpec> specs = productSpecRepository.findAll(productSpecs);
		//聚合数据
		shoppingCarts = doShoppingCarts(shoppingCarts,carts,prods,specs);
		return shoppingCarts;
	}
	
	/**
	 * 
	 *<p>在结算页面展示选中购物车去结算的数据</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月18日
	 */
	public List<ShoppingCartQo> getShoppingCarts(String ids,String counts){
		String[] shopCartIds = ids.split(",");
		List<Long> idss = new ArrayList<>();
		for (int i = 0; i < shopCartIds.length; i++) {
			idss.add(Long.parseLong(shopCartIds[i]));
		}
		//查询购物车列表
		List<ShoppingCart> carts = shoppingCartRspository.findAll(idss);
		List<ShoppingCart> ls = new ArrayList<ShoppingCart>();
		List<Integer> products = new ArrayList();
		List<Integer> productSpecs = new ArrayList();
		//返回值
		List<ShoppingCartQo> shoppingCartQos = new ArrayList();
		for (ShoppingCart cart : carts){
			products.add(cart.getProductId());
			if(null!=cart.getProductSpecId()){
				productSpecs.add(cart.getProductSpecId());
			}
		}
		//商品列表
		List<Product> prods = productRepository.findAll(products);
		//商品规格列表
		List<ProductSpec> specs = productSpecRepository.findAll(productSpecs);
		//聚合数据
		shoppingCartQos = doShoppingCarts(shoppingCartQos,carts,prods,specs,counts);
		return shoppingCartQos;
	}
	
	public ShoppingCart queryById(Integer id){
		return shoppingCartRspository.queryById(id);
	}
	
	public Page<ShoppingCart> getList(){
		PageRequest pageRequest = new PageRequest(0,8);
		return shoppingCartRspository.findAll(getSpec(),pageRequest);
	}
	
	public List<ShoppingCartQo> getgetShoppingCart(int pid){
		ShoppingCartQo qo = new ShoppingCartQo();
		qo.setProductId(pid);
		return null;
	}
	
	public Specification<ShoppingCart> getSpec(){
        Specification<ShoppingCart> shoppingCart = new Specification<ShoppingCart>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().addAll(predicates);
                return predicate;
            }
        };
        return shoppingCart;
    }
	
	/**
	 * 
	 *<p>对数据做聚合</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月19日
	 */
	private List<ShoppingCartQo> doShoppingCarts(List<ShoppingCartQo> shoppingCarts,List<ShoppingCart> carts,List<Product> prods,List<ProductSpec> specs){
		for (ShoppingCart cart : carts){
			ShoppingCartQo qo = new ShoppingCartQo();
			BeanUtils.copyProperties(cart,qo);
			for (Product prod : prods){
				if (prod.getPid().equals(qo.getProductId())){
					qo.setProductName(prod.getName());
					qo.setPic(ImgUtil.appendUrl(prod.getPicSquare(),100));
					if(!StringUtils.isEmpty(prod.getIsLimitCount())){
						qo.setIsLimitCount(prod.getIsLimitCount());
					}else{
						qo.setIsLimitCount(0);
					}
					if(!StringUtils.isEmpty(prod.getLimitCount())){
						qo.setLimitCount(prod.getLimitCount());
					}else{
						qo.setLimitCount(0);
					}
                    if(!StringUtils.isEmpty(prod.getTotalCount())){
                        qo.setStock(prod.getTotalCount());
                    }else{
                        qo.setStock(0);
                    }
					break;
				}
			}
			for (ProductSpec spec : specs){
				if (spec.getProductSpecId().equals(qo.getProductSpecId())){
					if(!StringUtils.isEmpty(spec.getSpecPic())){
						qo.setPic(ImgUtil.appendUrl(spec.getSpecPic(),100));
					}
					qo.setSpecValueOne(spec.getSpecValueOne());
					qo.setSpecValueTwo(spec.getSpecValueTwo());
					qo.setSpecValueThree(spec.getSpecValueThree());
                    if(!StringUtils.isEmpty(spec.getTotalCount())){
                        qo.setStock(spec.getTotalCount());
                    }else{
                        qo.setStock(0);
                    }
					break;
				}
			}
			shoppingCarts.add(qo);
		}
		return shoppingCarts;
	}
	
	
	/**
	 * 
	 *<p>对数据做聚合</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月19日
	 */
	private List<ShoppingCartQo> doShoppingCarts(List<ShoppingCartQo> shoppingCarts,List<ShoppingCart> carts,List<Product> prods,List<ProductSpec> specs,String count){
		String[] counts = count.split(",");
		int i = 0;
		for (ShoppingCart cart : carts){
			ShoppingCartQo qo = new ShoppingCartQo();
			BeanUtils.copyProperties(cart,qo);
			qo.setCount(Integer.parseInt(counts[i]));
			for (Product prod : prods){
				if (prod.getPid().equals(qo.getProductId())){
					qo.setProductName(prod.getName());
					qo.setPic(ImgUtil.appendUrl(prod.getPicSquare(),100));
                    if(!StringUtils.isEmpty(prod.getTotalCount())){
                        qo.setStock(prod.getTotalCount());
                    }else{
                        qo.setStock(0);
                    }
					break;
				}
			}
			for (ProductSpec spec : specs){
				if (spec.getProductSpecId().equals(qo.getProductSpecId())){
					qo.setSpecValueOne(spec.getSpecValueOne());
					qo.setSpecValueTwo(spec.getSpecValueTwo());
					qo.setSpecValueThree(spec.getSpecValueThree());
					if(!StringUtils.isEmpty(spec.getSpecPic())){
						qo.setPic(ImgUtil.appendUrl(spec.getSpecPic(),100));
					}
                    if(!StringUtils.isEmpty(spec.getTotalCount())){
                        qo.setStock(spec.getTotalCount());
                    }else{
                        qo.setStock(0);
                    }
					break;
				}
			}
			i++;
			shoppingCarts.add(qo);
		}
		return shoppingCarts;
	}
	
	/**
	 * 
	 *<p>点击立即购买时跳转结算页面数据展示</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月19日
	 */
	public List<ShoppingCartQo> getShoppingCart(List<OrderConfirmVo> orderConfirmVos) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT p.pid as product_id, ps.product_spec_id, (CASE WHEN ps.spec_price IS NOT NULL THEN ps.spec_price ELSE p.price END) as price,");
		sql.append(" ps.spec_value_one,ps.spec_value_two,ps.spec_name_three,p.name as product_name,p.shop_id,p.limit_count,");
		sql.append(" ( CASE WHEN ps.spec_pic IS NOT NULL THEN  ps.spec_pic ELSE p.pic_square END) as pic,");
		sql.append(" ( CASE WHEN ps.total_count IS NULL THEN p.total_count  ELSE p.total_count END )as stock");
		sql.append(" FROM product p LEFT JOIN product_spec ps ON ps.pid = p.pid  ");
		StringBuffer specSql = new StringBuffer(jdbcUtil.getInSql("ps.product_spec_id",orderConfirmVos,OrderConfirmVo.class,"ProductSpecId"));
		sql.append(specSql);
		sql.append(" WHERE p.status = 0");
		StringBuffer pidSql = new StringBuffer(jdbcUtil.getInSql("p.pid",orderConfirmVos,OrderConfirmVo.class,"ProductId"));
		sql.append(pidSql);
		List<ShoppingCartQo> shoppingCartQos = jdbcUtil.queryList(sql.toString(),ShoppingCartQo.class);
		for(ShoppingCartQo shoppingCartQo :shoppingCartQos) {
			for (OrderConfirmVo orderConfirmVo : orderConfirmVos) {
				if (shoppingCartQo.getProductSpecId() == null || orderConfirmVo.getProductSpecId() == null) {
					if (shoppingCartQo.getProductId().equals(orderConfirmVo.getProductId())) {
						shoppingCartQo.setCount(orderConfirmVo.getCount());
					}
				} else {
					if (shoppingCartQo.getProductSpecId().equals(orderConfirmVo.getProductSpecId())) {
						shoppingCartQo.setCount(orderConfirmVo.getCount());
					}
				}

			}
			shoppingCartQo.setPic(ImgUtil.appendUrl(shoppingCartQo.getPic(), 100));
		}
		return shoppingCartQos;
	}
	
	/**
	 * 
	 *<p>为ShoppingCartQo对象注入值</p>
	 *
	 * @author liangrs
	 * @version latest
	 * @data 2016年5月19日
	 */
	public List<ShoppingCartQo> setShoppingCartQo(List<ShoppingCartQo> shoppingCartQos,ProductSpec ps,Product p,Integer count){
		ShoppingCartQo qo = new ShoppingCartQo();
		if(null!=p){
			qo.setProductId(p.getPid());
			if(!StringUtils.isEmpty(p.getPicSquare())){
				qo.setPic(ImgUtil.appendUrl(p.getPicSquare(),100));
			}
            if(!StringUtils.isEmpty(p.getTotalCount())){
                qo.setStock(p.getTotalCount());
            }else{
                qo.setStock(0);
            }
			qo.setPrice(p.getPrice());
			qo.setProductName(p.getName());
		}
		if(null!=ps){
			if(!StringUtils.isEmpty(ps.getSpecPic())){
				qo.setPic(ImgUtil.appendUrl(ps.getSpecPic(),100));
			}
			if(!StringUtils.isEmpty(ps.getSpecValueTwo())){
				qo.setSpecValueTwo(ps.getSpecValueTwo());
			}
			if(!StringUtils.isEmpty(ps.getSpecValueThree())){
				qo.setSpecValueThree(ps.getSpecValueThree());
			}
			if(!StringUtils.isEmpty(ps.getSpecValueOne())){
				qo.setSpecValueOne(ps.getSpecValueOne());
			}
			if(!StringUtils.isEmpty(ps.getSpecPrice())){
				qo.setPrice(ps.getSpecPrice());
			}
			if(!StringUtils.isEmpty(ps.getProductSpecId())){
				qo.setProductSpecId(ps.getProductSpecId());
			}
            if(!StringUtils.isEmpty(ps.getTotalCount())){
                qo.setStock(ps.getTotalCount());
            }else{
                qo.setStock(0);
            }
		}
		qo.setCount(count);
		shoppingCartQos.add(qo);
		return shoppingCartQos;
	}
}
