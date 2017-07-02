package com.jm.mvc.controller.product;

import com.jm.business.service.product.ProductService;
import com.jm.business.service.product.TransTemplatesService;
import com.jm.business.service.system.AreaService;
import com.jm.business.service.system.WxAreaService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.product.trans.*;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.po.product.TransTemplates;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/31 16:44
 */

@Api
@RestController
@RequestMapping(value = "/trans_temp")
public class TransTemplatesController {
	
	@Autowired
	private TransTemplatesService transTemplatesService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private ProductService productService;
	@Autowired
	private WxAreaService wxAreaService;
	@Autowired
	private ShopRepository shopRepository;
	
	@ApiOperation("获取运费模板列表")
    @RequestMapping(value="",method=RequestMethod.GET)
    public List getTransTemplatesList(@ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId = 0;
		if(user!=null){
			shopId = Toolkit.parseObjForInt(user.getShopId());
		}
		return  transTemplatesService.findALL(shopId);
    }

	/**
	 * 新增运费模板
	 * @param transTemplatesCo
	 * @return
	 */
	@ApiOperation("新增运费模板")
	@RequestMapping(value="",method=RequestMethod.POST)
	public JmMessage addTransTemplates(@ApiParam("新增运费模板CO") @RequestBody @Valid TransTemplatesCo transTemplatesCo,
									   @ApiParam(hidden=true) HttpServletRequest request){
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		String shopId = "0"; //取不到店铺id的默认值
		if(user!=null){
			shopId = StringUtil.formatNull(user.getShopId());
			if("".equals(shopId)){shopId = "0";}
		}
		boolean flag = transTemplatesService.saveTransTemplates(transTemplatesCo,shopId);
		if(flag){
			return new JmMessage(0,"新增运费模板成功!");
		}else {
			return new JmMessage(1,"新增运费模板失败!");
		}
	}

	/**
	 * 获取运费模板
	 * @param request
	 * @return
     */
	@ApiOperation("获取运费模板")
	@RequestMapping(value="/tempId/{tempId}",method=RequestMethod.GET)
	public TransTemplatesRo addTransTemplates(@ApiParam(hidden=true) HttpServletRequest request,@PathVariable("tempId") Integer tempId){
		return transTemplatesService.findTransTempByTempId(tempId);
	}

	/**
	 * @param transTemplatesUo
	 * @return
	 */
	@ApiOperation("修改运费模板")
	@RequestMapping(value="",method=RequestMethod.PUT)
	public JmMessage updateTransTemplates(@RequestBody @Valid TransTemplatesUo transTemplatesUo){
		boolean flag = transTemplatesService.updateTransTemplates(transTemplatesUo);
		if(flag){
			return new JmMessage(0,"修改运费模板成功");
		}else {
			return new JmMessage(1,"修改运费模板失败,该模板已被删除!");
		}
	}
	/**
	 * 删除运费模板，级联子表
	 * @param request
	 * @return
	 */
	@ApiOperation("删除运费模板")
	@RequestMapping(value="/tempId/{tempId}",method=RequestMethod.DELETE)
	public JmMessage deleteTransTemplates(@ApiParam(hidden=true) HttpServletRequest request,@PathVariable("tempId") Integer tempId ){

		boolean isDel = productService.findByTransId(Toolkit.parseObjForInt(tempId));
		if(!isDel){
			return new JmMessage(1,"该运费模板无法删除，有商品正使用这运费模板!!");
		}else{
			boolean flag = transTemplatesService.deleteTransTemplates(Toolkit.parseObjForInt(tempId));
			if(flag){
				return new JmMessage(0,"删除运费模板成功");
			}else {
				return new JmMessage(1,"删除运费模板失败");
			}
		}

	}
	@ApiOperation("商品发布加载运费模板列表New")
	@RequestMapping(value="/get_transTemp_list",method=RequestMethod.GET)
	public List<TransTemplateVo> getTransTempsList(@ApiParam(hidden=true) HttpServletRequest request) throws IOException {
    	//获取运费模板
		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId = 0;
		if(user!=null){
			shopId = Toolkit.parseObjForInt(user.getShopId());
		}
	    List<TransTemplates> transTempList = transTemplatesService.findALLTemps(shopId);
		List<TransTemplateVo> transTemplateVos = new ArrayList<>();
		for (TransTemplates transTemplates:transTempList) {
			TransTemplateVo ttVo = new TransTemplateVo();
			BeanUtils.copyProperties(transTemplates,ttVo);
			transTemplateVos.add(ttVo);
		}
		return transTemplateVos;

	}

	/**
	 * 地区树展示
	 * @param request
	 * @param response
	 * @throws IOException
     */
	@RequestMapping(value="/get_area_list",method=RequestMethod.POST)
	public List getAreaList(@ApiParam(hidden=true) HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		return  Constant.AREA_ZTREE_LIST;
	}
	/**
	 * 微信地区树展示
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/get_filter_area",method=RequestMethod.GET)
	public List getFilterAreaList(@ApiParam(hidden=true) HttpServletRequest request,
								  HttpServletResponse response){
		return Constant.WX_AREA_LIST_ALL;
	}

	/**
	 * 运费算法可选条件
	 * @param request
	 * @param response
     */
	@RequestMapping(value="/trans_set",method=RequestMethod.PUT)
	public JmMessage updateTransSet(@ApiParam(hidden=true) HttpServletRequest request,
							   HttpServletResponse response, @RequestBody TransShopUo transShopUo){

		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId = 0;
		if(user!=null){
			shopId = Toolkit.parseObjForInt(user.getShopId());
		}
		Shop shop = shopRepository.findOne(shopId);
		if(shop != null){
			BeanUtils.copyProperties(transShopUo,shop);
			shopRepository.save(shop);
			return new JmMessage(0,"配置修改成功!");
		}
		return new JmMessage(1,"配置修改失败!");
	}
	@RequestMapping(value="/trans_set",method=RequestMethod.GET)
	public TransShopVo getTransSet(@ApiParam(hidden=true) HttpServletRequest request,
									HttpServletResponse response){

		JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
		Integer shopId = 0;
		if(user!=null){
			shopId = Toolkit.parseObjForInt(user.getShopId());
		}
		Shop shop = shopRepository.findOne(shopId);
		TransShopVo transShopVo = new TransShopVo();
		BeanUtils.copyProperties(shop,transShopVo);
		return transShopVo;
	}
}
