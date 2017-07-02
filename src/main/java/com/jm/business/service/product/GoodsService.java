package com.jm.business.service.product;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.area.*;
import com.jm.mvc.vo.product.group.ProductGroupRelationCo;
import com.jm.mvc.vo.product.group.ProductGroupRelationUo;
import com.jm.mvc.vo.product.group.ProductGroupRelationVo;
import com.jm.mvc.vo.product.product.*;
import com.jm.repository.jpa.product.*;
import com.jm.repository.po.product.*;
import com.jm.staticcode.converter.product.ProductConverter;
import com.jm.staticcode.converter.product.ProductGrouptRelConverter;
import com.jm.staticcode.converter.product.ProductSpecConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品服务层
 *
 * @author zhengww
 * @date 2016-5-19
 */
@Service
public class GoodsService {

    //主表
    @Autowired
    private ProductRepository productRepository;

    //分类关系
    @Autowired
    private ProductGroupRelationRepository productGroupRelationRepository;

    //商品规格
    @Autowired
    private ProductSpecRepository productSpecRepository;

    @Autowired
    private JdbcProductRepository jdbcRepository;

    @Autowired
    private ProductAreaRelRepository productAreaRelRepository;

    @Autowired
    private ProductRoleRepository productRoleRepository;
    /**
     * 新增商品
     *
     * @param productDetailCo
     * @return
     */
    @Transactional
    public void saveGood(ProductDetailCo productDetailCo, Integer shopId) {
        //填充商品主表(店铺)
        Product product = ProductConverter.toProduct(productDetailCo.getProduct());
        List<ProductGroupRelationCo> productGroupRelCo = productDetailCo.getGroupRelationList();
        List<ProductRoleCo> productRoleCoList = productDetailCo.getProductRoleList();
        List<ProductSpecPcCo> productSpecCoList = productDetailCo.getProductSpecList();
        if(productSpecCoList.size()==0){
            product.setIsSpec(0);
        }else{
            product.setIsSpec(1);
        }
        product.setShopId(shopId);
        product = productRepository.save(product);
        Integer pid = product.getPid();
        //填充分类关系表
        List<ProductGroupRelation> groupRelationList = new ArrayList<ProductGroupRelation>();
        if (productGroupRelCo != null && productGroupRelCo.size() > 0) {
            for (ProductGroupRelationCo productGroupRelationCo : productGroupRelCo) {
                ProductGroupRelation productGroupRelation = ProductGrouptRelConverter.toProductGroupRelation(productGroupRelationCo);
                productGroupRelation.setPid(pid);
                groupRelationList.add(productGroupRelation);
            }
            productGroupRelationRepository.save(groupRelationList);
        }

        //填充商品角色限购表
        List<ProductRole>  productRoles = new ArrayList<ProductRole>();
        if(productRoleCoList != null && productRoleCoList.size()>0){
            for (ProductRoleCo productRoleCo : productRoleCoList){
                ProductRole productRole = ProductConverter.toProductRole(productRoleCo);
                productRole.setPid(pid);
                productRoles.add(productRole);
            }
            productRoleRepository.save(productRoles);
        }

        //填充规格表
        List<ProductSpec> ProductSpecList = new ArrayList<ProductSpec>();
        if (productSpecCoList != null && productSpecCoList.size() > 0) {
            for (ProductSpecPcCo productSpecPcCo : productSpecCoList) {
                ProductSpec productSpec = ProductSpecConverter.toProductSpec(productSpecPcCo);
                productSpec.setPid(pid);
                ProductSpecList.add(productSpec);
            }
            productSpecRepository.save(ProductSpecList);
        }
    }

    /* *
     * 获取商品具体信息
     *
     * @param updateQo
     * @param pid
     * @return
     */
    public ProductDetailVo getProductMsg(ProductDetailVo productDetailVo, Integer pid) {
        //获取商品主表信息
        Product product = productRepository.findOne(pid);
        //获取商品分组
        List<ProductGroupRelation> groupRelationList = productGroupRelationRepository.findProductGroupRelationByPid(pid);
        //获取商品规格表信息

        //productDetailVo.setGroupRelationList(ProductGrouptRelConverter.toProductGroupRelationVo(groupRelationList));
        //productDetailVo.setProductSpecList(productSpecList);
        //组装商品分组信息
        List<ProductGroupRelationVo> groupRelationListVo = new ArrayList<ProductGroupRelationVo>();
        if (groupRelationList != null && groupRelationList.size() > 0) {
            for (ProductGroupRelation productGroupRelation : groupRelationList) {
                ProductGroupRelationVo productGroupRelationVo = ProductGrouptRelConverter.toProductGroupRelationVo(productGroupRelation);
                groupRelationListVo.add(productGroupRelationVo);
            }
        }

        List<ProductRole> productRoles = productRoleRepository.findProductRoleByPid(pid);
        List<ProductRoleVo> productRoleVos = new ArrayList<ProductRoleVo>();
        if(productRoles !=null && productRoles.size()>0){
            for (ProductRole productRole : productRoles){
                ProductRoleVo productRoleVo = ProductConverter.toProductRoleVo(productRole);
                productRoleVos.add(productRoleVo);
            }
        }

        List<ProductSpecPcVo> productSpecPcVoList = new ArrayList<ProductSpecPcVo>();
        if(product.getIsSpec()!=0){
            List<ProductSpec> productSpecList = productSpecRepository.findProductSpecByPid(pid);
            //组装商品规格信息
            if (productSpecList != null && productSpecList.size() > 0) {
                for (ProductSpec productSpec : productSpecList) {
                    ProductSpecPcVo productSpecPcVo = ProductSpecConverter.toProductSpecPcVo(productSpec);
                    productSpecPcVoList.add(productSpecPcVo);
                }
            }
        }
        //组装商品信息
        productDetailVo.setProduct(ProductConverter.toProductVo(product));
        productDetailVo.setGroupRelationList(groupRelationListVo);
        productDetailVo.setProductRoleList(productRoleVos);
        productDetailVo.setProductSpecList(productSpecPcVoList);
        return productDetailVo;
    }

    /**
     * 修改商品信息
     *
     * @param productDetailUo
     * @return
     */

    @Transactional
    public void udpateGood(ProductDetailUo productDetailUo) {
        //修改商品主表信息
        Product product = productRepository.findOne(productDetailUo.getProduct().getPid());
        Product productU = ProductConverter.toProduct(productDetailUo.getProduct(),product);
        List<ProductSpecPcUo> productSpecList = productDetailUo.getProductSpecList();
        if(productSpecList.size() == 0){
            productU.setIsSpec(0);
        }else{
            productU.setIsSpec(1);
        }
        productU = productRepository.save(productU);
        Integer pid = productU.getPid();
        //查询分组关系表
        List<ProductGroupRelation> groupList = productGroupRelationRepository.findProductGroupRelationByPid(pid);
        //判断分类是否为空，不为空则保存，为空则查询是否存在原有分类，存在则删除，不存在则不操作
        if (null != groupList && groupList.size() > 0) {
            productGroupRelationRepository.delete(groupList);
        }
        //获取商品修改分类集合
        List<ProductGroupRelationUo> groupRelationList = productDetailUo.getGroupRelationList();
        List<ProductGroupRelation> groupList2 = new ArrayList<ProductGroupRelation>();
        if (null != groupRelationList && groupRelationList.size() > 0) {
            for (ProductGroupRelationUo productGroupRelationUo : groupRelationList) {
                ProductGroupRelation productGroupRelation = ProductGrouptRelConverter.toProductGroupRelation(productGroupRelationUo);
                productGroupRelation.setPid(pid);
                groupList2.add(productGroupRelation);
            }
            //保存分类信息
            productGroupRelationRepository.save(groupList2);
        }

        //删除角色限购关系
        List<ProductRole> productRoles = productRoleRepository.findProductRoleByPid(pid);
        if(productRoles!=null && productRoles.size()>0){
            productRoleRepository.delete(productRoles);
        }

        List<ProductRoleUo> productRoleUos = productDetailUo.getProductRoleList();
        List<ProductRole> productRoles2 = new ArrayList<ProductRole>();
        if(productRoleUos.size()>0 && productRoleUos != null){
            for (ProductRoleUo productRoleUo : productRoleUos){
                ProductRole productRole = ProductConverter.toProductRole(productRoleUo);
                productRole.setPid(pid);
                productRoles2.add(productRole);
            }
            productRoleRepository.save(productRoles2);
        }

        //判断是否存在删除的规格
        if(StringUtil.isNotNull(productDetailUo.getDelId())){
            String[] delId = productDetailUo.getDelId().split(",");
            for (int i = 0; i < delId.length; i++) {
                Integer id = Integer.parseInt(delId[i]);
                //根据商品id获取商品，填充状态
                ProductSpec productSpec = productSpecRepository.findOne(id);
                productSpec.setStatus(1);//1表示删除
                productSpecRepository.save(productSpec);
            }
        }
        //获取商品规格集合
        List<ProductSpec> specList2 = new ArrayList<ProductSpec>();
        if (null != productSpecList && productSpecList.size() > 0) {
            for (ProductSpecPcUo productSpecPcUo : productSpecList) {
                ProductSpec productSpec = ProductSpecConverter.toProductSpec(productSpecPcUo);
                productSpec.setPid(pid);
                specList2.add(productSpec);
            }
            //保存规格信息
            productSpecRepository.save(specList2);
        }
    }

    /**
     * 当前商品底下的规格
     * @param pid
     * @return
     */
    public List<ProductSpecPcVo> getProductSpecPc(Integer pid) {
        List<ProductSpec>  productSpecs = productSpecRepository.findProductSpecByPid(pid);
        List<ProductSpecPcVo> productSpecPcVos = new ArrayList<ProductSpecPcVo>();
        if (productSpecs != null && productSpecs.size() > 0){
            for (ProductSpec productSpec : productSpecs){
                ProductSpecPcVo productSpecPcVo = ProductSpecConverter.toProductSpecPcVo(productSpec);
                productSpecPcVos.add(productSpecPcVo);
            }
        }
        return productSpecPcVos;
    }


    /**
     * 获取所有商品列表
     *
     * @param productQo
     * @return
     */
    public PageItem<Map<String, Object>> queryGoodsManager(
            ProductQo productQo, Integer shopId) {
        return jdbcRepository.getGoodsManager(productQo, shopId);
    }

    /**
     * 单个、批量修改商品状态
     *
     * @param productStatusUo
     */
    @Transactional
    public void updateStatus(ProductStatusUo productStatusUo) {
        String[] pid = productStatusUo.getIds().split(",");
        for (int i = 0; i < pid.length; i++) {
            Integer id = Integer.parseInt(pid[i]);
            //根据商品id获取商品，填充状态
            Product product = productRepository.findOne(id);
            product.setStatus(productStatusUo.getStatus());
            productRepository.save(product);

            //如果该商品删除，则删除该商品下的分类
            if(productStatusUo.getStatus()==9){
                List<ProductGroupRelation> groupRel = productGroupRelationRepository.findProductGroupRelationByPid(id);
                List<ProductGroupRelation> groupRel2 = new ArrayList<ProductGroupRelation>();
                if(groupRel != null && groupRel.size()>0){
                    for(ProductGroupRelation productGroupRelation : groupRel){
                        productGroupRelation.setStatus(9);
                        groupRel2.add(productGroupRelation);
                    }
                    productGroupRelationRepository.save(groupRel2);
                }
            }

        }
    }

    public List<Product> getProductByPids(String pids) {
        String[] pid = pids.split(",");
        List ids = new ArrayList();
        for (int i = 0; i < pid.length; i++) {
            Integer id = Integer.parseInt(pid[i]);
            ids.add(id);
        }
        List<Product>  products = new ArrayList<>();
        List<Product>  productList = productRepository.findAll(ids);
        for (Product product:productList) {
            if(product.getStatus()==0){
                String picSquare = ImgUtil.appendUrl(product.getPicSquare(),720);
                product.setPicSquare(picSquare);
                String picRectangle = ImgUtil.appendUrl(product.getPicRectangle(),720);
                product.setPicRectangle(picRectangle);
                products.add(product);
            }
        }
        return products;
    }

    /**
     * 复制商品
     * @param pid
     */
    @Transactional
    public void copy(Integer pid,Integer shopId) {
        //复制商品基本信息
        Product product = productRepository.findOne(pid);
        ProductCo productCo = ProductConverter.toProductCo(product);
        Product productNew =  ProductConverter.toProduct(productCo);
        productNew.setIsSpec(product.getIsSpec());
        productNew.setShopId(shopId);
        productNew = productRepository.save(productNew);
        Integer newPid = productNew.getPid();
        //复制商品分组信息
        List<ProductGroupRelation> groupRel = productGroupRelationRepository.findProductGroupRelationByPid(pid);
        if(groupRel!= null && groupRel.size()>0){
            List<ProductGroupRelation> groupRelNew = new ArrayList<ProductGroupRelation>();
            for(ProductGroupRelation productGroupRelation : groupRel){
                ProductGroupRelationCo groupRelCo = ProductGrouptRelConverter.toProductGroupRelationCo(productGroupRelation);
                ProductGroupRelation groupRelationNew = ProductGrouptRelConverter.toProductGroupRelation(groupRelCo);
                groupRelationNew.setPid(newPid);
                groupRelNew.add(groupRelationNew);
            }
            productGroupRelationRepository.save(groupRelNew);
        }
        //判断是否存在规格复制商品规格信息
        if(productNew.getIsSpec()==1){
            List<ProductSpec> spec = productSpecRepository.findProductSpecByPid(pid);
            if(spec!= null && spec.size()>0){
                List<ProductSpec> specNewList = new ArrayList<ProductSpec>();
                for(ProductSpec productSpec : spec){
                    ProductSpecPcCo productSpecPcCo = ProductSpecConverter.toProductSpecPcCo(productSpec);
                    ProductSpec productSpecNew = ProductSpecConverter.toProductSpec(productSpecPcCo);
                    productSpecNew.setPid(newPid);
                    specNewList.add(productSpecNew);
                }
                productSpecRepository.save(specNewList);
            }
        }
    }

    /**
     * 修改商品顺序
     * @param productSortUo
     */
    public void updateSort(ProductSortUo productSortUo) {
        Product product = productRepository.findOne(productSortUo.getPid());
        product.setSort(productSortUo.getSort());
        productRepository.save(product);
    }

    public PageItem<ProductAreaVo> queryAreaProduct(ProductAreaQo productQo, Integer shopId) throws IOException {
        return jdbcRepository.queryAreaProduct(productQo,shopId);
    }

    public List<OfferRole> getOfferRoleList(int shopId) throws IOException {
        return jdbcRepository.getOfferRoleList(shopId);
    }

    public PageItem<ProductAreaRelVo> getProductAreaOfferList(ProductAreaOfferQo productQo) throws IOException {
        return jdbcRepository.getProductAreaOfferList(productQo);
    }

    public ProductAreaRelVo getProductAreaRel(Integer pid, Integer userId) {
        ProductAreaRel productAreaRel = productAreaRelRepository.findByPidAndUserId(pid,userId);
        ProductAreaRelVo productAreaRelVo = new ProductAreaRelVo();
        if(productAreaRel != null){
            productAreaRelVo = ProductConverter.toProductAreaRelVo(productAreaRel);
        }
        return productAreaRelVo;
    }

    public List<ProductAreaRelVo> getAreaOfferList(Integer pid) {
        List<ProductAreaRel> productAreaRels = productAreaRelRepository.findByPid(pid);
        List<ProductAreaRelVo> productAreaRelVos = new ArrayList<>();
        if(productAreaRels.size() >0 && productAreaRels != null){
            for(ProductAreaRel productAreaRel:productAreaRels){
                ProductAreaRelVo productAreaRelVo = ProductConverter.toProductAreaRelVo(productAreaRel);
                productAreaRelVos.add(productAreaRelVo);
            }
        }
        return productAreaRelVos;
    }

    @Transactional
    public void saveAreaOffer(ProductAreaRelCo productAreaRelCo,Integer offerType) {
        Product product = productRepository.findOne(productAreaRelCo.getPid());
        if(offerType!=product.getOfferType()){
            product.setOfferType(offerType);
            productRepository.save(product);
        }
        ProductAreaRel productAreaRel=productAreaRelRepository.findByPidAndUserId(productAreaRelCo.getPid(),productAreaRelCo.getUserId());
        if (productAreaRel != null){
            productAreaRel=ProductConverter.toProductAreaRel(productAreaRelCo);
            productAreaRel.setId(productAreaRel.getId());
        }else{
            productAreaRel=ProductConverter.toProductAreaRel(productAreaRelCo);
        }
        productAreaRelRepository.save(productAreaRel);
    }

    @Transactional
    public void updateAreaOffer(ProductAreaRelUo productAreaRelUo,Integer offerType) {
        Product product = productRepository.findOne(productAreaRelUo.getPid());
        if(offerType!=product.getOfferType()){
            product.setOfferType(offerType);
            productRepository.save(product);
        }
        ProductAreaRel productAreaRel = productAreaRelRepository.findByPidAndUserId(productAreaRelUo.getPid(),productAreaRelUo.getUserId());
        productAreaRel=productAreaRelRepository.save(ProductConverter.toProductAreaRel(productAreaRel,productAreaRelUo));
        productAreaRelRepository.save(productAreaRel);
    }

    public void setPlantOffer(Integer pid,Integer offerType) {
        Product product = productRepository.findOne(pid);
        if(offerType!=product.getOfferType()){
            product.setOfferType(offerType);
            productRepository.save(product);
        }
    }

    public void delete(Integer id) {
        productAreaRelRepository.delete(id);
    }


}
