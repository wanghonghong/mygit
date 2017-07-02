package com.jm.business.service.wb;

import com.jm.business.service.order.OrderService;
import com.jm.business.service.order.PayRecordService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.JmMessage;
import com.jm.repository.client.dto.wb.WbResultMsg;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.order.OrderRefundRepository;
import com.jm.repository.jpa.order.PayRecordRepository;
import com.jm.repository.jpa.wb.WbShopUserRepository;
import com.jm.repository.po.order.OrderInfo;
import com.jm.repository.po.order.OrderRefund;
import com.jm.repository.po.order.PayRecord;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WbUrl;
import com.jm.staticcode.util.Toolkit;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * <p>微博支付服务层</p>
 * @version latest
 * @Author whh
 * @Date 2017/4/13
 */
@Slf4j
@Service
public class WbPayService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WbShopUserRepository wbShopUserRepository;
    @Autowired
    private OrderRefundRepository refundRepository;
    @Autowired
    private WbClient wbClient;
    @Autowired
    private PayRecordRepository payRecordRepository;

    /**
     * 生成rsa签名的函数 (规则详见接口文档)
     * @param params    array  参数数组
     * @return string   string 签名
     */
    private String generateRsaSign (Map<String,String> params) {
        Map<String,String> newParams = new TreeMap<String,String>();
        for(String key : params.keySet()){
            if("sign" == key || "sign_type" == key || params.get(key) == null || "".equals(params.get(key))){
                continue;
            }
            newParams.put(key, params.get(key));
        }
        List<String> pairs = new ArrayList<String>();
        for (String key : newParams.keySet()) {
            pairs.add(key+"="+newParams.get(key));
        }
        String sign_data = StringUtils.join(pairs.toArray(), "&");
        try {
            byte[] buffer = Base64.decode(Constant.RSA_PRIVATE_KEY);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

            java.security.Signature signature = java.security.Signature.getInstance("Sha1WithRSA");
            signature.initSign(rsaPrivateKey);
            signature.update( sign_data.getBytes("UTF-8"));

            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 微博退款
     *
     * @return
     * @throws Exception
     */
    public JmMessage toRefund(OrderRefund orderRefund) throws Exception{
        OrderInfo orderInfo = orderService.findOrderInfoById(orderRefund.getOrderInfoId());
        Shop shop = shopService.findShopById(orderInfo.getShopId());
        PayRecord payRecord = payRecordRepository.findOne(orderInfo.getPayId());
        Map<String,String> map = new HashMap<String, String>();
        map.put("sign_type","rsa");//签名方式
//        map.put("seller_id","5919515026");//商户微博 id
        map.put("seller_id",shop.getWbUid().toString());//商户微博 id
//        map.put("pay_id","124104013389898922");//微博支付单号
        map.put("pay_id",payRecord.getTransactionId());//微博支付单号
//        map.put("out_refund_id","14939565002655216");//外部退款单号
        map.put("out_refund_id",orderInfo.getOrderNum());//外部退款单号
        map.put("detail_data",payRecord.getTransactionId()+"^"+orderRefund.getRefundMoney().toString()+"^"+orderRefund.getRefundReason()); //退款请求明细，退款金额以分为单位。交易退款数据集格式：微博支付交易号（pay_id)^退款金额^退款理由
        //map.put("detail_data","124107959156121917"+"\u005E"+1+"\u005E中国人");//退款请求明细，退款金额以分为单位。交易退款数据集格式：微博支付交易号（pay_id)^退款金额^退款理由
        //生成签名
        map.put("sign", this.generateRsaSign(map));
        String url = WbUrl.REFUND_URL+Toolkit.mapToParam(map);
        JmMessage jmMessage = wbClient.toWbRefund(url);
        return jmMessage;
    }

}
