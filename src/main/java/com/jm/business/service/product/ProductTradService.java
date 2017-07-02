package com.jm.business.service.product;

import com.jm.business.service.product.impl.ProductTradServiceImpl;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.trad.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.product.*;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.TradActivity;
import com.jm.repository.po.product.TradSign;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.product.TradActivityConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 * `
 * 
 * @author zhengwww
 * @version latest
 * @date 2016/5/13
 */
@Service
public class ProductTradService implements ProductTradServiceImpl {

	@Autowired
	private TradSignRepository tradSignRepository;

	@Autowired
	private TradActivityRepository tradActivityRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private JdbcUtil jdbcUtil;

	private static String TRAD_PRODUCT_DETAIL = "select  t.id,p.pic_square,p.pic_rectangle,t.name,p.name as product_name,p.price as product_price,t.price,t.status,t.start_time,t.end_time,t.image_url,t.pid,t.times,t.sign,t.position,t.indiana_type,t.group_type,t.indiana_price,t.sale_count from trad_activity t LEFT JOIN product p on t.pid = p.pid where t.status != 9   and p.status=0  ";
	@Override
	@Cacheable(value ="trad_sign")
	public List<TradSign> getCacheTradSign(){
		List<TradSign>  tradSigns=tradSignRepository.findAll();
		for (TradSign tradSign:tradSigns){
			if (tradSign.getImgType()==0){
				tradSign.setImageUrl(Constant.THIRD_URL+tradSign.getImageUrl());
			}else {
				tradSign.setImageUrl(ImgUtil.appendUrl(tradSign.getImageUrl()));
			}
		}
		return tradSigns;
	}

	@Override
	public void setTradActivity(TradActivityCo tradActivityCo,Integer shopId) {
		TradActivity tradActivity = TradActivityConverter.toTradActivity(tradActivityCo);
		tradActivity.setShopId(shopId);
		tradActivityRepository.save(tradActivity);
	}

	@Override
	public void updateTradActivity(TradActivityUo tradActivityUo) {
		TradActivity tradActivity = tradActivityRepository.findOne(tradActivityUo.getId());
		tradActivity = TradActivityConverter.toTradActivity(tradActivity,tradActivityUo);
		tradActivityRepository.save(tradActivity);
	}

	@Override
	public void deleteTradActivity(Integer id) {
		TradActivity tradActivity = tradActivityRepository.findOne(id);
		tradActivity.setStatus(9);
		tradActivityRepository.save(tradActivity);
	}

	@Override
	public void updateStatus(TradActivityStatusUo tradActivityStatusUo) {
		String[] pid = tradActivityStatusUo.getIds().split(",");
		for (int i = 0; i < pid.length; i++) {
			Integer id = Integer.parseInt(pid[i]);
			//根据商品id获取商品，填充状态
			TradActivity tradActivity = tradActivityRepository.findOne(id);
			tradActivity.setStatus(tradActivityStatusUo.getStatus());
			tradActivityRepository.save(tradActivity);
		}
	}

	@Override
	public PageItem<TradProductDetailVo> getTradProduct(TradProductQo tradProductQo, Integer shopId) throws IOException {
		String sql = TRAD_PRODUCT_DETAIL;
		StringBuilder sqlCondition1 = new StringBuilder();
		sqlCondition1.append(jdbcUtil.appendAnd("t.shop_id",shopId));
		sqlCondition1.append(jdbcUtil.appendLike("p.name",tradProductQo.getProductName()));
		sqlCondition1.append(jdbcUtil.appendLike("t.name",tradProductQo.getName()));
		sqlCondition1.append(jdbcUtil.appendAnd("t.type",tradProductQo.getType()));
		if(-1 != tradProductQo.getStatus()){
			sqlCondition1.append(jdbcUtil.appendAnd("t.status",tradProductQo.getStatus()));
		}
		sqlCondition1.append(jdbcUtil.appendOrderBy("t.creat_time"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql+sqlCondition1,tradProductQo.getCurPage(),tradProductQo.getPageSize());
		List<TradSign> tradSigns = getCacheTradSign();
		return TradActivityConverter.tradProduct2v(pageItem,tradSigns);
	}

	@Override
	public TradActivityDetailVo getTradActivityDetail(Integer id) {
		TradActivity tradActivity = tradActivityRepository.findOne(id);
		Product product = productService.getCacheProduct(tradActivity.getPid());
		TradActivityDetailVo tradActivityDetailVo = TradActivityConverter.toTradActivityDetailVo(tradActivity);
		tradActivityDetailVo.setProductName(product.getName());
		String picSquare =  ImgUtil.appendUrl(product.getPicSquare(),720);
		tradActivityDetailVo.setProductPrice(product.getPrice());
		tradActivityDetailVo.setPicSquare(picSquare);
		return tradActivityDetailVo;
	}

	@Override
	public List<TradProductDetailVo> getTradProductByPids(TradProductTypeQo tradProductTypeQo)  throws IOException {
		List<TradSign> tradSigns = getCacheTradSign();
		String sql = TRAD_PRODUCT_DETAIL;
		StringBuilder sqlCondition1 = new StringBuilder();
		sqlCondition1.append(jdbcUtil.appendAnd("t.type",tradProductTypeQo.getType()));
		if (StringUtil.isNotNull(tradProductTypeQo.getIds())){
			sqlCondition1.append(jdbcUtil.appendIn("t.pid",tradProductTypeQo.getIds()));
		}
		List<Map<String,Object>> list = jdbcUtil.queryList(sql);
		return TradActivityConverter.TradProductDetail2v(list,tradSigns);
	}

}
