package com.jm.mvc.controller.shop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jm.business.service.shop.ShopEntityService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.shop.ShopEntityForCreateVo;
import com.jm.mvc.vo.shop.ShopEntityForQueryVo;
import com.jm.mvc.vo.shop.ShopEntityPageItem;
import com.jm.repository.po.shop.ShopEntity;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.ShopEntityConverter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Api
@RestController
@RequestMapping(value = "/shopEntity")
public class ShopEntityController {
	@Autowired
    private ShopEntityService entityStoreService;

	@Autowired
	private ShopService shopService;


	@ApiOperation("实体门店列表查询")
	 @RequestMapping(value = "/findAll", method = RequestMethod.POST)
	 public ShopEntityPageItem<ShopEntity> list (@ApiParam(hidden=true) HttpServletRequest request,@RequestBody @Valid ShopEntityForQueryVo queryVo) {
		 JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		 Integer shopId=user.getShopId();
		 Shop shop = shopService.findShopById(shopId);
		 Page<ShopEntity> page = entityStoreService.queryEntityStores(queryVo,shopId);
		 return ShopEntityConverter.p2vs(page,shop);
	 }
	 
	 
 	@ApiOperation("新增实体门店")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage add(@ApiParam("实体门店VO") @RequestBody @Valid ShopEntityForCreateVo entityVo,
    					 @ApiParam(hidden=true) HttpServletRequest request){
 		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId=user.getShopId();
 		ShopEntity entity1 = ShopEntityConverter.toEntityStore(entityVo);
 		entity1.setShopId(shopId);
 		ShopEntity entity = entityStoreService.save(entity1);

		List<ShopEntity>  entityls=entityStoreService.findShopEntityByShopId(shopId);
		//查看是否是第一条门店信息
		if (entityls.size()==1){
			Shop shop=shopService.findShopById(shopId);
			shop.setIsEntity(1);
			shopService.saveShop(shop);
			return new JmMessage(0, "第一条门店信息");
		}else{
			return new JmMessage(1, "不是第一条");
		}
 	}

	@ApiOperation("门店 是否勾选")
	@RequestMapping(value = "/is_entity/{isentity}", method = RequestMethod.POST)
	public JmMessage add(@ApiParam(hidden=true) HttpServletRequest request,@PathVariable("isentity") Integer isentity){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Shop shop=shopService.findShopById(user.getShopId());
		shop.setIsEntity(isentity);
		Shop shop1=shopService.saveShop(shop);
		if(shop1!=null){
			return new JmMessage(0, "成功");
		}
		return new JmMessage(1, "失败");
	}

		
 	
 	@ApiOperation("编辑/预览 ")
    @RequestMapping(value = "/{storeId}", method = RequestMethod.POST)
    public ShopEntity edit(@ApiParam("实体门店标识")  @PathVariable("storeId") Integer storeId){
 		ShopEntity entity = entityStoreService.findShopEntityById(storeId);
        return ShopEntityConverter.p2v(entity);
 	}
 	
 	@ApiOperation("删除")
    @RequestMapping(value = "/{storeId}", method = RequestMethod.DELETE)
    public JmMessage delete(@ApiParam("实体门店标识")  @PathVariable("storeId") Integer storeId,@ApiParam(hidden=true) HttpServletRequest request){
 		entityStoreService.delete(storeId);
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		List<ShopEntity>  entityls=entityStoreService.findShopEntityByShopId(user.getShopId());
		if(entityls.size()<=0){
			Shop shop=shopService.findShopById(user.getShopId());
			shop.setIsEntity(0);
			shopService.saveShop(shop);
			return new JmMessage(0, "无数据");
		}
 		return new JmMessage(1, "存在数据");
 	}

	@ApiOperation("编辑实体门店")
	@RequestMapping(value = "/{storeId}", method = RequestMethod.PUT)
	public JmMessage update(@ApiParam("实体门店标识")  @PathVariable("storeId") Integer storeId,
							@ApiParam("实体门店VO") @RequestBody @Valid ShopEntityForCreateVo entityVo,
						 	@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId=user.getShopId();
		ShopEntity entity1 = ShopEntityConverter.toEntityStore(entityVo);
		entity1.setStoreId(storeId);
		entity1.setShopId(shopId);
		ShopEntity entity = entityStoreService.save(entity1);
		if(entity!=null){
			return new JmMessage(0, "成功");
		}
		return new JmMessage(1, "失败");
	}


	@ApiOperation("在线导航")
	@RequestMapping(value = "/navigation/{storeId}", method = RequestMethod.GET)
	public ModelAndView navigation(@ApiParam("实体门店标识")  @PathVariable("storeId") Integer storeId,
								   @ApiParam(hidden=true) HttpServletRequest request){
		ShopEntity entity = entityStoreService.findShopEntityById(storeId);
		String longitude ="";
		String latitude = "";
        String stroename="";
        String city ="";
		if( entity != null){
				if(entity.getLongitude() !=null){
					longitude = entity.getLongitude();
				}
				if(entity.getLatitude() !=null){
					latitude = entity.getLatitude();
				}
                if(entity.getStoreName() !=null){
                    stroename = entity.getStoreName();
                }
                if(entity.getCity()!=null){
                    city =entity.getCity();
                }
		}
		ModelAndView view = new ModelAndView();
		request.setAttribute("longitude", longitude);
		request.setAttribute("latitude", latitude);
        request.setAttribute("stroename", stroename);
        request.setAttribute("city", city);
		request.setAttribute("type", 10);
		view.setViewName("/app/shop/baidumap_navi");
		return view;
	}
	
}
