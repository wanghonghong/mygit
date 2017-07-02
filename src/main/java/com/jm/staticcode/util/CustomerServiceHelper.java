package com.jm.staticcode.util;


import com.jm.mvc.vo.online.OnlineUser;
import redis.clients.jedis.Jedis;

import java.util.*;

public class CustomerServiceHelper {
    private static Map<String,Map<String,String>> STAFF ;//员工列表  key:客户openid  value:服务的商家人员
    private static Map<String,List<OnlineUser>> SHOP_STAFFS;

    private static final String WAIT_QUEUE_KEY ="WAIT_QUEUE_KEY";
    private static final String STAFF_KEY ="STAFF_KEY";
    private static final String SHOP_STAFFS_KEY ="SHOP_STAFFS_KEY";

    private final static int PORT = 6379; // redis 端口
    private final static String REMOTE_SERVER = "127.0.0.1"; // redis 服务器地址
    private final static int CATCH_TIME = 1*60*1000;


    private static Map<String,Long> CUSTOMER_STAFF_TIME;
    private static Map<String,Long> SHOP_STAFF_TIME;

    private static RedisUtils redisUtils;
    private static final int CATCH_ON = 0;//0:ON  1:OFF
    static {
        redisUtils = new RedisUtils(REMOTE_SERVER,PORT);
        if(CATCH_ON==0){
            STAFF = new HashMap<>();
            SHOP_STAFFS = new HashMap<>();
            CUSTOMER_STAFF_TIME = new HashMap<>();
            SHOP_STAFF_TIME = new HashMap<>();
        }
    }

    /**
     * 获取权限最大的
     * @param users
     * @return
     */
    public static OnlineUser getOnlineUser(List<OnlineUser> users){
        OnlineUser user = null;
        if(users!=null&&users.size()>0){

        }
        return user;
    }

    /**
     * 获取该商店底下的等待接待的客户
     * @param appid
     * @return
     */
    public static Set<String> getWaitQueue(String appid) throws Exception {
        String key =  WAIT_QUEUE_KEY+appid;
        Set<String> waitQueue = redisUtils.getObject(key,Set.class);
        return waitQueue;
    }
    /**
     * 更新等待接待的客户
     * @param appid
     * @param waitQueue
     */
    public static void updateWaitQueue(String appid,Set<String> waitQueue){
        byte [] keyBytes = (WAIT_QUEUE_KEY+appid).getBytes();
        Jedis jedis  = redisUtils.getJedis();
        byte [] newQueueBytes = SerializeUtil.serialize(waitQueue);
        jedis.set(keyBytes,newQueueBytes);
    }
    /**
     * 更新该商店底下客服人员信息
     * @param staff
     * @param appid
     */
    public static void updateStaffQueue(Map<String,String> staff,String appid){
        STAFF.put(appid,staff);
        CUSTOMER_STAFF_TIME.put(appid,new Date().getTime());
        Jedis jedis  = redisUtils.getJedis();
        byte [] newQueueBytes = SerializeUtil.serialize(staff);
        jedis.set((STAFF_KEY+appid).getBytes(),newQueueBytes);
    }

    /**
     * 获取该商店底下客服人员信息
     * @param appid
     * @return
     * @throws Exception
     */
    public static Map<String,String> getStaffs(String appid) throws Exception {
        Map<String,String> staff = null;
        if(CATCH_ON==0&&new Date().getTime()-CUSTOMER_STAFF_TIME.get(appid)<CATCH_TIME){
            staff = STAFF.get(appid);
            return staff;
        }
        String key = STAFF_KEY+appid;
        staff = redisUtils.getObject(key,Map.class);
        STAFF.put(appid,staff);
        CUSTOMER_STAFF_TIME.put(appid,new Date().getTime());
        return staff;
    }

    /**
     * 获取商店底下全部在线客服人员
     * @param appid
     * @return
     */
    public static List<OnlineUser> getOnlineService(String appid) throws Exception {
        List<OnlineUser> shopStaff= null;
        if(CATCH_ON==0&&new Date().getTime()-SHOP_STAFF_TIME.get(appid)<CATCH_TIME){
            return SHOP_STAFFS.get(appid);
        }
        String key = SHOP_STAFFS_KEY+appid;
        shopStaff = redisUtils.getObject(key,List.class);
        SHOP_STAFFS.put(appid,shopStaff);
        SHOP_STAFF_TIME.put(appid,new Date().getTime());
        return shopStaff;
    }

    /**
     * 更新店铺在线客服人员
     * @param appid
     * @param users
     */
    public static void upDateOnlineService(String appid,List<OnlineUser> users){
        SHOP_STAFF_TIME.put(appid,new Date().getTime());
        SHOP_STAFFS.put(appid,users);
        String key = SHOP_STAFFS_KEY+appid;
        byte [] userBytes = SerializeUtil.serialize(users);
        Jedis jedis  = redisUtils.getJedis();
        jedis.set(key.getBytes(),userBytes);
    }

}
