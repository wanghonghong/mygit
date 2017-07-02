package com.jm.staticcode.converter.product;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.area.*;
import com.jm.mvc.vo.product.product.*;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductAreaRel;
import com.jm.repository.po.product.ProductRole;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class ProductConverter {

	public static ProductVo toProductVo(Product product) {
		ProductVo productVo = new ProductVo();
		//图片转换
		BeanUtils.copyProperties(product, productVo);
		productVo.setPicSquare(ImgUtil.appendUrl(product.getPicSquare(),720));
		productVo.setPicRectangle(ImgUtil.appendUrl(product.getPicRectangle(),720));
		productVo.setDetailSquarePic(ImgUtil.appendUrls(product.getDetailSquarePic(),720));
		productVo.setDetailRectanglePic(ImgUtil.appendUrls(product.getDetailRectanglePic(),720));
		return productVo;
	}
	public static ProductCo toProductCo(Product product) {
		ProductCo productCo = new ProductCo();
		BeanUtils.copyProperties(product, productCo);
		return productCo;
	}

	public static Product toProduct(ProductCo productCo) {
		Product product = new Product();
		BeanUtils.copyProperties(productCo, product);
		//图片转换
		String picSquare = ImgUtil.substringUrl(product.getPicSquare());
		String picRectangle = ImgUtil.substringUrl(product.getPicRectangle());
		String detailSquare = ImgUtil.substringUrls(product.getDetailSquarePic());
		String detailRectangle = ImgUtil.substringUrls(product.getDetailRectanglePic());
		product.setPicSquare(picSquare);
		product.setPicRectangle(picRectangle);
		product.setDetailSquarePic(detailSquare);
		product.setDetailRectanglePic(detailRectangle);
		product.setCreateTime(new Date());
		return product;
	}

	public static Product toProduct(ProductUo productUo,Product product) {
		BeanUtils.copyProperties(productUo, product);
		//图片转换
		String picSquare = ImgUtil.substringUrl(product.getPicSquare());
		String picRectangle = ImgUtil.substringUrl(product.getPicRectangle());
		String detailSquare = ImgUtil.substringUrls(product.getDetailSquarePic());
		String detailRectangle = ImgUtil.substringUrls(product.getDetailRectanglePic());
		product.setPicSquare(picSquare);
		product.setPicRectangle(picRectangle);
		product.setDetailSquarePic(detailSquare);
		product.setDetailRectanglePic(detailRectangle);
		product.setUpdateTime(new Date());
		return product;
	}

    public static PageItem<ProductAreaVo> productArea2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<ProductAreaVo> productAreas = new PageItem<ProductAreaVo>();
		List<Map<String,Object>> maps = pageItem.getItems();
		List<ProductAreaVo> list = new ArrayList<>();
		for(Map<String, Object> map : maps ){
			ProductAreaVo productAreaVo =  JsonMapper.map2Obj(map,ProductAreaVo.class);
			String picSquare =  ImgUtil.appendUrl(map.get("pic_square").toString(),720);
			productAreaVo.setPicSquare(picSquare);
			list.add(productAreaVo);
		}
		productAreas.setCount(pageItem.getCount());
		productAreas.setItems(list);
		return productAreas;
    }

    public static List<OfferRole> offerRole2v(List<Map<String, Object>> maps) throws IOException {
		List<OfferRole> list = new ArrayList<>();
		for(Map<String, Object> map : maps ){
			OfferRole offerRole =  JsonMapper.map2Obj(map,OfferRole.class);
			list.add(offerRole);
		}
		return list;
    }

    public static PageItem<ProductAreaRelVo> productAreaRel2v(PageItem<Map<String, Object>> pageItem) throws IOException {
		PageItem<ProductAreaRelVo> pageItems = new PageItem<ProductAreaRelVo>();
		List<ProductAreaRelVo> list = new ArrayList<>();
		for(Map<String, Object> map : pageItem.getItems() ){
			ProductAreaRelVo productAreaRelVo =  JsonMapper.map2Obj(map,ProductAreaRelVo.class);
			productAreaRelVo.setPhoneNumber(String.valueOf(map.get("phone_number"))==null?"":String.valueOf(map.get("phone_number")));
			productAreaRelVo.setUserName(String.valueOf(map.get("user_name"))==null?"":String.valueOf(map.get("user_name")));
			list.add(productAreaRelVo);
		}
		pageItems.setCount(pageItem.getCount());
		pageItems.setItems(list);
		return pageItems;
    }

	public static ProductAreaRelVo toProductAreaRelVo(ProductAreaRel productAreaRel) {
		ProductAreaRelVo productAreaRelVo = new ProductAreaRelVo();
		BeanUtils.copyProperties(productAreaRel,productAreaRelVo);
		return productAreaRelVo;
	}

	public static ProductAreaRel toProductAreaRel(ProductAreaRelCo productAreaRelCo) {
		ProductAreaRel productAreaRel = new ProductAreaRel();
		BeanUtils.copyProperties(productAreaRelCo,productAreaRel);
		return productAreaRel;
	}

	public static ProductAreaRel toProductAreaRel(ProductAreaRel productAreaRel,ProductAreaRelUo productAreaRelUo) {
		BeanUtils.copyProperties(productAreaRelUo,productAreaRel);
		return productAreaRel;
	}

    public static ProductRole toProductRole(ProductRoleCo productRoleCo) {
		ProductRole productRole = new ProductRole();
		BeanUtils.copyProperties(productRoleCo,productRole);
		return productRole;
    }

	public static ProductRoleVo toProductRoleVo(ProductRole productRole) {
		ProductRoleVo productRoleVo = new ProductRoleVo();
		BeanUtils.copyProperties(productRole,productRoleVo);
		return productRoleVo;
	}

	public static ProductRole toProductRole(ProductRoleUo productRoleUo) {
		ProductRole productRole = new ProductRole();
		BeanUtils.copyProperties(productRoleUo,productRole);
		return productRole;
	}
}
