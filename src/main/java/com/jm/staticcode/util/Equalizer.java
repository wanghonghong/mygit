package com.jm.staticcode.util;


import com.jm.mvc.vo.online.Customer;
import com.jm.mvc.vo.online.OnlineUser;
import com.jm.staticcode.constant.Constant;
import lombok.extern.log4j.Log4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;

@Log4j
public class Equalizer {


    private final static int HEART_BEAT_TIME = 1000*60*30;
    private static Map<String,List<OnlineUser>> userMap = new HashMap<>();//客服 key:shopUserId value:客服关系表
    private static Map<String,Customer> custormerMap = new HashMap<>();//用户与客服 关系，在线时间  key:用户环信账号  value 用户关系

    /**
     * 1.如何删除过期客户
     *
     * 2.如何删除过期客服人员
     * 获取店铺下的客服人员时候选择人员时候顺便更新
     * */

    private final static int PORT = 6379;
    private final static String REMOTE_SERVER = "114.55.103.195";


    public Equalizer (){
       // jedis = new Jedis(REMOTE_SERVER,PORT);
    }
    // 初始化 平衡器
    public Equalizer(Integer shopId,Integer userId,String hxAcct) {
       // jedis = new Jedis(REMOTE_SERVER,PORT);
        addOnlineServiceUser(shopId,userId,hxAcct);
    }


    // 添加 店铺与客服人员关系
    public static void addOnlineServiceUser(Integer shopId,Integer userId,String hxAcct) {
        String shopKey = shopId+"";
        List<OnlineUser> onlineUsers = userMap.get(shopKey);
        if(onlineUsers!=null) {
            Iterator<OnlineUser> it = onlineUsers.iterator();
            while (it.hasNext()){
                OnlineUser onlineUser =  it.next();
                if(onlineUser.getUserId() .equals(userId)){
                    onlineUser.setLastBeatTime(System.currentTimeMillis());
                    return ;
                }
            }
        }else{
            onlineUsers = new ArrayList<>();
        }
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setUserId(userId);
        onlineUser.setWeight(0);
        onlineUser.setHxAcct(hxAcct);
        onlineUser.setLastBeatTime(System.currentTimeMillis());
        onlineUsers.add(onlineUser);
        userMap.put(shopKey,onlineUsers);
    }

    //添加用户到客服人员下面
    public static Customer addCustormer(String hxAcc,Integer shopId,String shopAxAcct) throws IOException, ClassNotFoundException {
        Customer customer = custormerMap.get(hxAcc);
        if(customer!=null){
            customer.setLastBeatTime(System.currentTimeMillis());
            return customer;
        }
        customer = new Customer();
        customer.setLastBeatTime(System.currentTimeMillis());
        customer.setShopId(shopId);
        customer.setShopUserAcct(shopAxAcct);
        log.info("---user account:"+hxAcc+"shop account"+shopAxAcct);
        custormerMap.put(hxAcc,customer);
        List<OnlineUser> onlineUsers = userMap.get(shopId.toString());
        if(onlineUsers==null||onlineUsers.size()<1){
            return null;
        }
        for(OnlineUser  onlineUser:onlineUsers){
            String shopAcct = onlineUser.getHxAcct();
            if(shopAcct.equals(shopAxAcct)){
                Set<String> customers = onlineUser.getCustomers();
                if(customers==null){
                    customers = new HashSet<>();
                }
                customers.add(hxAcc);
                onlineUser.setCustomers(customers);
                break;
            }
        }
        return customer;
    }

    public static String getOnlineService(Integer shopId,String hxAcc){
        //判断用户map中是否有该用户的关系信息
        Customer customer = custormerMap.get(hxAcc);
        //有则刷新时间并且返回对应的聊天信息
        if(customer!=null){
            customer.setLastBeatTime(System.currentTimeMillis());
            return  customer.getShopUserAcct();
        }
        //筛选该店铺下的客服人员
        List<OnlineUser> onlineUsers = userMap.get(shopId+"");
        if(onlineUsers!=null){
            //删除过期客服人员
            Iterator<OnlineUser> it = onlineUsers.iterator();
            while(it.hasNext()){
                OnlineUser onlineUser = it.next();
                if(System.currentTimeMillis()-onlineUser.getLastBeatTime()>=HEART_BEAT_TIME){
                    it.remove();
                }
            }
            //按照权重排列onlineUser
            Collections.sort(onlineUsers);
            //获取最小的onlineUser
            if(onlineUsers.size()>0){
                OnlineUser onlineUser = onlineUsers.get(onlineUsers.size()-1);
                onlineUser.plus();
                return onlineUser.getHxAcct();
            }
        }
        //该店铺下没有在线的客服人员则返回空
        return null;
    }

    public static void logout(String shopUserAccount,Integer shopId){
        log.info("logout account:"+shopUserAccount);
        if(shopId==null||shopUserAccount==null){
            return;
        }
        List<OnlineUser> onlineUsers = userMap.get(shopId.toString());
        if(onlineUsers!=null&&onlineUsers.size()>0){
            log.info("logout start -----------------------");
            Iterator<OnlineUser> it = onlineUsers.iterator();
            while(it.hasNext()){
                OnlineUser onlineUser = it.next();
                log.info("onlineUser account:"+onlineUser.getHxAcct()+"------ shopUser account:"+shopUserAccount);
                if(shopUserAccount.equals(onlineUser.getHxAcct())){
                    Set<String> customers = onlineUser.getCustomers();
                    if(customers!=null){
                        for(String customer :customers){
                            log.info("clear custmors:"+customer);
                            custormerMap.remove(customer);
                        }
                    }
                    it.remove();
                }
            }
        }
    }

    /**
     * 定时清理失效粉丝
     * */
    public static void clearCustomer(){
        log.info("清理无效粉丝 任务开始------");
        Set<String> set = custormerMap.keySet();
        if(set==null||set.size()<1){
            return ;
        }
        Iterator<String> it  = set.iterator();
        while(it.hasNext()){
            String key = it.next();
            Customer customer = custormerMap.get(key);
            if(System.currentTimeMillis()-customer.getLastBeatTime()>=1000*60*30){
                String acct = customer.getShopUserAcct();
                Integer shopId = customer.getShopId();
                List<OnlineUser> onlineUsers = userMap.get(shopId+"");
                if(onlineUsers==null||onlineUsers.size()<1){
                    break;
                }
                if(onlineUsers!=null){
                    for(OnlineUser onlineUser :onlineUsers){
                        if(onlineUser.getHxAcct().equals(acct)){
                            log.info("清理无效客户:"+onlineUser.getHxAcct()+"-----");
                            onlineUser.minus();
                            break;
                        }
                    }
                    it.remove();
                }
            }
        }
    }

    public static Customer getCustormer(String hxAcct){
        Customer customer = custormerMap.get(hxAcct);
        if(customer==null){
            return customer;
        }else{
            return null;
        }
    }

    /**
     * 与服务端redis数据同步
     * */
    public static void dataSync() throws IOException, ClassNotFoundException {
        Jedis jedis  = new Jedis(REMOTE_SERVER,PORT);
        String custormerKey = "CUSTORMER_MAP";
        String shopKey = "SHOP_KEY";
        byte[] shopByte = jedis.get(shopKey.getBytes());
        Map<String,List<OnlineUser>> shops;
        //
        if(shopByte!=null){
            shops = SerializeUtil.unserialize(shopByte,Map.class);
            Set<String> set = shops.keySet();
            Iterator<String> it = set.iterator();
            while(it.hasNext()){
                String shopId =  it.next();
                List<OnlineUser> onlineUsers = userMap.get(shopId);

            }
        }
    }


    public static String onlineService(String custmorAcct,String shopId) throws Exception {
        HttpClientHelper helper = new HttpClientHelper();
        String url = Constant.PC_DOMAIN+"/online/"+ HxUtils.MODEL+"/onlineUserService";
        Map<String,String> params = new HashMap<>();
        params.put("account",custmorAcct);
        params.put("shopId",shopId);
        params.put("secretKey",Constant.HX_SCRET_KEY);
        String result = helper.sendGetRequest(url,null,params,500);
        log.info("========result:"+result);
        return result;
    }

    public static void customerAdd(String hxAcct,Integer shopId,String shopAcct) throws Exception {
        HttpClientHelper helper = new HttpClientHelper();
        String url = Constant.PC_DOMAIN+"/online/"+HxUtils.MODEL+"/customer";
        Map<String,String> params = new HashMap<>();
        params.put("customer",hxAcct);
        params.put("shopAcc",shopAcct);
        params.put("shopId",shopId.toString());
        params.put("scretKey",Constant.HX_SCRET_KEY);
        String result = helper.sendGetRequest(url,null,params,500);
        log.info("hxCustomer======:"+result);

    }





}
