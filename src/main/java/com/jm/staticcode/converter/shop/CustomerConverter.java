package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.mvc.vo.erp.ErpShopRo;
import com.jm.repository.po.wx.WxUserGroup;
import com.jm.repository.po.wx.WxUserLevel;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerConverter {

    public static PageItem<CustomerRo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<CustomerRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<CustomerRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CustomerRo customerRo =   JsonMapper.map2Obj(map,CustomerRo.class);
            customerRo.setUnissued((Toolkit.parseObjForInt(customerRo.getTotalCount())-Toolkit.parseObjForInt(customerRo.getBalance()))+"");
            customerRo.setNickname(Base64Util.getFromBase64(customerRo.getNickname()));
            System.out.println(map.get("level_id")  +"||" + map.get("groupid"));
            list.add(customerRo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

    public static PageItem<CustomerRo> wbp2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<CustomerRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<CustomerRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CustomerRo customerRo =   JsonMapper.map2Obj(map,CustomerRo.class);
            list.add(customerRo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

    public static PageItem<CustomerRo> p2vv(PageItem<Map<String,Object>> pageItemMap) throws IOException {
        PageItem<CustomerRo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<CustomerRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            CustomerRo customerRo =   JsonMapper.map2Obj(map,CustomerRo.class);
            customerRo.setUnissued((Toolkit.parseObjForInt(customerRo.getTotalCount())-Toolkit.parseObjForInt(customerRo.getBalance()))+"");
            customerRo.setNickname(Base64Util.getFromBase64(customerRo.getNickname()));
            float numb= (float)(Toolkit.parseObjForInt(map.get("total_count"))-Toolkit.parseObjForInt(map.get("balance")))/100;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String sa = df.format(numb);//返回的是String类型
            customerRo.setUnissued(sa);
            double numa= Toolkit.obj2Double(map.get("average"))/100;
            String s = df.format(numa);//返回的是String类型
            customerRo.setAllprice(Toolkit.parseObjForInt(map.get("allprice")));
            customerRo.setAverage(s);
            customerRo.setFrequency(Toolkit.parseObjForInt(map.get("frequency")));
            customerRo.setUnissued((Toolkit.parseObjForInt(customerRo.getTotalCount())-Toolkit.parseObjForInt(customerRo.getBalance()))+"");
            if(map.get("lasttime")!=null){
                customerRo.setLasttime( Toolkit.strToDate2(map.get("lasttime")+""));
            }

            list.add(customerRo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }


}
