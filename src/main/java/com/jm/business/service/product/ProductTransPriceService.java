package com.jm.business.service.product;

import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.product.TransTemplatesRelationRepository;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.product.ProductTrans;
import com.jm.repository.po.product.TransTemplatesRelation;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品运费服务层
 * Created by cj on 2016/7/5.
 */
@Service
public class ProductTransPriceService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransTemplatesRelationRepository transTemplatesRelationRepository;
    @Autowired
    private ShopRepository shopRepository;


    /**
     * 在点击购买时精算运费金额
     * @param productTransList
     * @return
     */
    public Integer getTransPrice(List<ProductTrans> productTransList){
        Integer totalPrice =0;
        if(productTransList!=null && productTransList.size()>0){
            ProductTrans productTrans = productTransList.get(0);
            Integer pid = productTrans.getPid();
            Integer shopId = Toolkit.parseObjForInt(productRepository.findOne(pid).getShopId());
            Shop shop = shopRepository.findOne(shopId);
            totalPrice =  getTransPriceAI(productTransList,shop);
        }
        return totalPrice;
    }

    /**
     * 在点击购买时精算运费金额 接口2
     * @param productTransList
     * @return
     */
    public Integer getTransPrice(List<ProductTrans> productTransList,Shop shop){
        Integer totalPrice =0;
        if(shop != null && productTransList!=null && productTransList.size()>0){
            totalPrice =  getTransPriceAI(productTransList,shop);
        }
        return totalPrice;
    }
    /**
     * 在点击购买时精算运费金额（聚米人工智能算法）
     * @param productTransList
     * @return
     */
    private Integer getTransPriceAI(List<ProductTrans> productTransList,Shop shop){
            Integer totalPrice =0;
            List pids = new ArrayList();
            for (ProductTrans productTrans: productTransList) {
                pids.add(productTrans.getPid());
            }
            List<Product> products = productRepository.findAll(pids);//该订单的所有商品
            if(1==Toolkit.parseObjForInt(shop.getTransCondition0ne()).intValue()){   //该单有免费商品，该订单全部免邮
                boolean flag = false;
                for (Product product : products) {
                    if(1==product.getIsUseTrans() && 0 == product.getTransFare()){
                        flag = true;
                        break;
                    }
                }
                if(flag){
                    return totalPrice;
                }
            }
            List transIds = productRepository.findTransIdByPids(pids); //该订单的所涉及的所有运费模板
            List<Integer> transPrices = new ArrayList();//分类精算该订单的运费金额
            for (Object obj :transIds) {
                Integer transId = Toolkit.parseObjForInt(obj);
                List<Product> productLists = new ArrayList(); //针对订单商品进行运费模板分类（同一运费模板的商品）
                for (Product product: products) {
                    int transTempId = product.getTransId();
                    if(transId.intValue() == transTempId){
                        productLists.add(product);
                    }
                }
                if(productLists.size()>0){
                    Integer transPrice = this.caculateTransPrice(transId,productLists,productTransList,shop);
                    transPrices.add(transPrice);
                }
            }
            if(transPrices.size()>0){
                if(1==Toolkit.parseObjForInt(shop.getTransConditionThree()).intValue()){
                    Collections.sort(transPrices);  //排序从小到大
                    int count = transPrices.size();
                    totalPrice = Toolkit.parseObjForInt(transPrices.get(count-1));
                }else{
                    for (Integer transPrice:transPrices) {
                        totalPrice += transPrice;
                    }
                }
            }
        return totalPrice;
    }

    /**
     * 根据不同的运费换算类型进行计算运费金额（统一运费 or 运费模板）
     * @param transId
     * @param productLists
     * @param productTransList
     * @return
     */
    private Integer caculateTransPrice(Integer transId, List<Product> productLists, List<ProductTrans> productTransList,Shop shop) {
        Integer totalPrice = 0;
        List<ProductTrans> productTransListNew = new ArrayList();
        if(0==transId.intValue()){ //统一运费模板
             if(1==Toolkit.parseObjForInt(shop.getTransConditionTwo()).intValue()){
                    /*List pids = new ArrayList();
                    for (Product product: productLists) {
                        pids.add(product.getPid());
                    }
                    totalPrice = productRepository.findMaxFaceByPids(pids);*/
                 Collections.sort(productLists);
                 totalPrice = productLists.get(0).getTransFare();
            }else{
                    for (Product product: productLists) {
                        totalPrice += product.getTransFare();
                    }
            }
        }else{ //个性运费模板
            for (Product product :productLists) {
                int pid = product.getPid();
                for (ProductTrans productTrans:productTransList) {
                     int orderPid = productTrans.getPid();
                     if(pid == orderPid){
                         productTransListNew.add(productTrans);
                     }
                }
            }
            if(productTransListNew.size()>0){
                List<TransTemplatesRelation> transTemRellist = transTemplatesRelationRepository.findByTempId(transId);
                totalPrice = calculateSingleTransTemp(productTransListNew,transTemRellist,transId);
            }
        }
        return totalPrice;
    }

    /**
     *根据运费模板id来算运费（适合该订单是同一个性运费模板）
     * @param productTransList
     * @param transTemRellist
     * @param transId
     * @return
     */
    private Integer calculateSingleTransTemp(List<ProductTrans> productTransList, List<TransTemplatesRelation> transTemRellist, Integer transId) {
        Integer totalPrice =0;
        boolean flag = false;
            for (TransTemplatesRelation transTempRel:transTemRellist) {
                String areaIds = transTempRel.getSendAreaId();
                String[] strArr = areaIds.split(",");
                ProductTrans productTrans = productTransList.get(0);
                String areaId = productTrans.getAreaId();
                for ( String str: strArr) {
                    if(str.equals(areaId)){
                        flag = true;
                        totalPrice = calculateMoney(transTempRel,productTransList,totalPrice);
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
        if(!flag){
            TransTemplatesRelation transTempRel = transTemplatesRelationRepository.findByTempIdDefaul(transId);
            if(transTempRel!=null){
                totalPrice =  calculateMoney(transTempRel,productTransList,totalPrice);
            }
        }
        return totalPrice;
    }

    private Integer calculateMoney(TransTemplatesRelation transTempRel, List<ProductTrans> productTransList,Integer totalPrice) {

        Integer fistNum = transTempRel.getFirstNumber();//首件个数
        Integer transFare = Toolkit.parseObjForInt(transTempRel.getTransFare());//首件运费金额
        Integer nextNum = transTempRel.getNextNumber();//续件个数
        Integer nextTrans = Toolkit.parseObjForInt(transTempRel.getNextTransFare());//续件运费
        if(nextTrans==0){
            totalPrice = transFare;
        }else{
            Integer count = 0;
            for (ProductTrans pt:productTransList) {
                count +=Toolkit.parseObjForInt(pt.getBuyCount());
            }
            if(count<=fistNum){
                totalPrice += transFare;
            }else if(count>fistNum && count<=(fistNum+nextNum)){
                totalPrice +=(transFare+nextTrans);
            }else if(count>fistNum && count>(fistNum+nextNum)){
                Integer a = count - fistNum;
                Integer b = a/nextNum;
                Integer c = a%nextNum;
                if(c==0){
                    totalPrice +=(transFare+b*nextTrans);
                }else{
                    totalPrice +=transFare+((b+1)*nextTrans);
                }
            }
        }
        return totalPrice;
    }


    /**
     * 显示商品详情页的运费金额
     * @param product
     * @return
     */
    public String[] getProdTransPrice(Product product){
        Integer totalPrice =0;
        int flag = product.getIsUseTrans();
        String[]  priceArr =  null;
        Map  map = new HashMap();
        if(flag==1){
            totalPrice = product.getTransFare(); //统一邮费
            priceArr =  new String[1];
            priceArr[0]=Toolkit.parseObjForStr(totalPrice);//统一邮费
        }else if(flag==2) {
            Integer templatesId = product.getTransId(); // 运费模板id
            List<TransTemplatesRelation> transTemRellist = transTemplatesRelationRepository.findByTempId(templatesId);
            if (transTemRellist.size() > 0) {
                List transPrices = new ArrayList();
                for (TransTemplatesRelation ttr:transTemRellist) {
                    Integer transFare = ttr.getTransFare();
                    transPrices.add(transFare);
                }
                Collections.sort(transPrices);  //排序
                int count = transPrices.size();
                if(1==count){
                    priceArr =  new String[1];
                    priceArr[0]=Toolkit.parseObjForStr(transPrices.get(count-1));
                }else if(1<count){
                    String min = Toolkit.parseObjForStr(transPrices.get(0));
                    String max = Toolkit.parseObjForStr(transPrices.get(count-1));
                    if(min.equals(max)){
                        priceArr =  new String[1];
                        priceArr[0]=min;
                    }else{
                        priceArr =  new String[2];
                        priceArr[0]=min;
                        priceArr[1]=max;
                    }
                }
            }
        }
        return priceArr;
    }
}
