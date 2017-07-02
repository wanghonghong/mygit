package com.jm.mvc.controller.product;

import com.jm.business.service.product.*;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.area.*;
import com.jm.mvc.vo.product.group.ProductGroupVo;
import com.jm.mvc.vo.product.product.*;
import com.jm.mvc.vo.product.trans.TransTempVo;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/5/23
 */
@Api
@Slf4j
@RestController
@RequestMapping(value = "/good")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private TransTemplatesService transTemplatesService;

    @Autowired
    private ProductQrcodeService productQrcodeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrokerageSetService brokerageSetService;

    @ApiOperation("新增商品初始数据获取")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ProductBascVo getProductMsg(@ApiParam(hidden = true) HttpServletRequest request) throws IOException {
        //获取店铺信息
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        int shopId = user.getShopId();
        //获取分组标签
        ProductBascVo productBascVo = new ProductBascVo();
        List<ProductGroupVo> groupList = productGroupService.queryGroupListByShopId(shopId);
        productBascVo.setGroupList(groupList);
        //获取运费模板列表
        List<TransTempVo> transList = transTemplatesService.getTemps(shopId);
        productBascVo.setTransList(transList);
        //获取佣金配置
        BrokerageSet brokerageSet = brokerageSetService.getCacheBrokerageSet(shopId);
        productBascVo.setBrokerageSet(brokerageSet);
        return productBascVo;
    }

    @ApiOperation("新增商品")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage saveGood(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("新增商品VO") @RequestBody @Valid ProductDetailCo productDetailCo) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        int shopId = user.getShopId();
        goodsService.saveGood(productDetailCo, shopId);
        return new JmMessage(0, "新增成功");
    }

    @ApiOperation("获取单个商品规格信息")
    @RequestMapping(value = "/spec/{pid}", method = RequestMethod.GET)
    public List<ProductSpecPcVo> getProductSpecPc(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid) throws IOException {
        //获取店铺信息
        List<ProductSpecPcVo> productSpecPcVo = goodsService.getProductSpecPc(pid);
        return productSpecPcVo;
    }

    @ApiOperation("修改商品基本信息获取")
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    public ProductDetailVo getProductMsg(@ApiParam("商品标识") @PathVariable("pid") Integer pid, @ApiParam(hidden = true) HttpServletRequest request) throws IOException {
        //获取店铺信息
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        int shopId = user.getShopId();
        //获取分组标签
        List<ProductGroupVo> groupList = productGroupService.queryGroupListByShopId(shopId);
        //获取运费模板列表
        List<TransTempVo> transList = transTemplatesService.getTemps(shopId);
        //获取商品具体信息
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo = goodsService.getProductMsg(productDetailVo, pid);
        BrokerageSet brokerageSet = brokerageSetService.getCacheBrokerageSet(shopId);
        productDetailVo.setBrokerageSet(brokerageSet);
        productDetailVo.setGroupList(groupList);
        productDetailVo.setTransList(transList);
        return productDetailVo;
    }

    @ApiOperation("修改商品")
    @RequestMapping(value = "/{pid}", method = RequestMethod.PUT)
    public JmMessage updateGood(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid, @RequestBody @Valid ProductDetailUo productDetailUo) {
//        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        goodsService.udpateGood(productDetailUo);
        return new JmMessage(0, "修改成功");
    }




    @ApiOperation("商品列表查询")
    @RequestMapping(value = "/productList/{status}", method = RequestMethod.POST)
    public PageItem<Map<String, Object>> toGoodFtl(@ApiParam(hidden = true) HttpServletRequest request,
                                                   @ApiParam("上下架") @PathVariable("status") Integer status,
                                                   @RequestBody @Valid ProductQo productQo) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        productQo.setStatus(status);
        PageItem<Map<String, Object>> productList = goodsService.queryGoodsManager(productQo, user.getShopId());
        if (null != productList.getItems() && productList.getItems().size() > 0) {
            for (Map<String, Object> map : productList.getItems()) {
                String picSquare = ImgUtil.appendUrl(map.get("pic_square").toString(), 720);
                String picRectangle = ImgUtil.appendUrl(map.get("pic_rectangle").toString(), 720);
                if ("null" != String.valueOf(map.get("qrcode_url"))) {
                    String qrcodeUrl = ImgUtil.appendUrl(map.get("qrcode_url").toString(), 720);
                    map.put("qrcode_url", qrcodeUrl);
                }
                map.put("pic_square", picSquare);
                map.put("pic_rectangle", picRectangle);

            }
        }
        return productList;
    }

    @ApiOperation("商品列表查询")
    @RequestMapping(value = "/products/{pids}", method = RequestMethod.GET)
    public List<Product> getProductByPids(@ApiParam("商品ids") @PathVariable("pids") String pids) {
        return goodsService.getProductByPids(pids);
    }


    @ApiOperation("返回项目全地址")
    @RequestMapping(value = "/locationUrl", method = RequestMethod.GET)
    public Map<String, Object> toGoodFtl(@ApiParam(hidden = true) HttpServletRequest request) {
        String url = Constant.DOMAIN;
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("locationUrl", url);
        map.put("shopId", user.getShopId());
        return map;
    }

    @ApiOperation("单个、批量修改商品状态(伪删除)")
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public JmMessage updateStatus(@RequestBody ProductStatusUo productStatus
    ) {
        goodsService.updateStatus(productStatus);
        return new JmMessage(0, "修改成功");
    }

    @ApiOperation("商品复制")
    @RequestMapping(value = "/copy/{pid}", method = RequestMethod.PUT)
    public JmMessage copy(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        goodsService.copy(pid, user.getShopId());
        return new JmMessage(0, "复制成功");
    }

    @ApiOperation("修改商品顺序")
    @RequestMapping(value = "/sort", method = RequestMethod.PUT)
    public JmMessage updateSort(@ApiParam("新增商品VO") @RequestBody @Valid ProductSortUo productSortUo
    ) {
        goodsService.updateSort(productSortUo);
        return new JmMessage(0, "修改成功");
    }

    /**
     * ======================================地区供货===================================
     */
    @ApiOperation("获取供货商列表")
    @RequestMapping(value = "/offer_role", method = RequestMethod.GET)
    public List<OfferRole> getOfferRole(@ApiParam(hidden = true) HttpServletRequest request) throws IOException {
        //获取店铺信息
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        int shopId = user.getShopId();
        List<OfferRole> list = goodsService.getOfferRoleList(shopId);
        return list;
    }

    @ApiOperation("地区供货列表查询")
    @RequestMapping(value = "/area_producs", method = RequestMethod.POST)
    public PageItem<ProductAreaVo> getAreaProduct(@ApiParam(hidden = true) HttpServletRequest request,
                                                  @RequestBody @Valid ProductAreaQo productQo) throws IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        PageItem<ProductAreaVo> productList = goodsService.queryAreaProduct(productQo, user.getShopId());
        return productList;
    }


    @ApiOperation("获取单个商品地区供货信息")
    @RequestMapping(value = "/product_area_offer", method = RequestMethod.POST)
    public PageItem<ProductAreaRelVo> getProductAreaOfferList(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody @Valid ProductAreaOfferQo productQo) throws IOException {
        //获取店铺信息
        PageItem<ProductAreaRelVo> pageItems = goodsService.getProductAreaOfferList(productQo);
        return pageItems;
    }

    @ApiOperation("获取单个商品地区供货信息")
    @RequestMapping(value = "/product_area_offer/{pid}", method = RequestMethod.GET)
    public List<ProductAreaRelVo> getProductAreaOfferList(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid) throws IOException {
        List<ProductAreaRelVo> list = goodsService.getAreaOfferList(pid);//获取店铺信息
        return list;
    }

    @ApiOperation("获取单个商品单个供货商地区供货信息")
    @RequestMapping(value = "/product_area_offer", method = RequestMethod.GET)
    public ProductAreaRelVo getProductAreaRel(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid, @ApiParam("用户id") @PathVariable("userId") Integer userId) throws IOException {
        //获取店铺信息
        ProductAreaRelVo productAreaRel = goodsService.getProductAreaRel(pid, userId);
        return productAreaRel;
    }

    @ApiOperation("保存单个供货商地区供货信息")
    @RequestMapping(value = "/product_area_offer/{offerType}", method = RequestMethod.POST)
    public JmMessage saveAreaOffer(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody @Valid ProductAreaRelCo productAreaRelCo, @ApiParam("供货标识") @PathVariable("offerType") Integer offerType) throws IOException {
        //获取店铺信息
        goodsService.saveAreaOffer(productAreaRelCo, offerType);
        return new JmMessage();
    }

    @ApiOperation("修改单个供货商供货信息")
    @RequestMapping(value = "/product_area_offer/{offerType}", method = RequestMethod.PUT)
    public JmMessage updateAreaOffer(@ApiParam(hidden = true) HttpServletRequest request, @RequestBody @Valid ProductAreaRelUo productAreaRelUo, @ApiParam("供货标识") @PathVariable("offerType") Integer offerType) throws IOException {
        goodsService.updateAreaOffer(productAreaRelUo, offerType);
        return new JmMessage();
    }

    @ApiOperation("保存平台供货属性")
    @RequestMapping(value = "/product_plant_offer/{pid}/{offerType}", method = RequestMethod.PUT)
    public JmMessage setPlantOffer(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("pid") Integer pid, @ApiParam("供货标识") @PathVariable("offerType") Integer offerType) throws IOException {
        goodsService.setPlantOffer(pid, offerType);
        return new JmMessage(0, "操作成功");
    }

    @ApiOperation("删除地区供货")
    @RequestMapping(value = "/product_area_delete/{id}", method = RequestMethod.DELETE)
    public JmMessage delete(@ApiParam(hidden = true) HttpServletRequest request, @ApiParam("商品标识") @PathVariable("id") Integer id) throws IOException {
        goodsService.delete(id);
        return new JmMessage(0, "操作成功");
    }

    @ApiOperation("生成商品详情二维码")
    @RequestMapping(value = "/qrcode/{pid}", method = RequestMethod.POST)
    public JmMessage goodQrcode(@ApiParam(hidden = true) HttpServletRequest request,
                                @ApiParam("商品编号") @PathVariable("pid") Integer pid) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = user.getShopId();
        String url = Constant.APP_DOMAIN + "/product/" + pid + "?shopId=" + shopId;
        Product product = productService.getProduct(pid);
        if(product.getQrcodeUrl()!=null && !product.getQrcodeUrl().equals("")){
            return new JmMessage(0,ImgUtil.appendUrl(product.getQrcodeUrl(), 720));
        }else{
            JmMessage msg = productQrcodeService.getPcQrcoce(url);
            if (msg.getCode().equals(0)) {
                product.setQrcodeUrl(ImgUtil.substringUrl(msg.getMsg()));
                productService.save(product);
                msg.setMsg(ImgUtil.appendUrl(msg.getMsg(), 720));
            }
            return msg;
        }
    }

}
