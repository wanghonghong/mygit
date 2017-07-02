package com.jm.staticcode.util;


import com.jm.mvc.vo.online.OnlineUser;
import java.util.*;

public class CustomerServiceUtils {

    /**
     * 获取服务的商家人员 为空则需要添加人员到待接待列表
     * @param openid
     * @param appid
     * @return
     * @throws Exception
     */
    public static OnlineUser getOnlineUser(String openid,String appid) throws Exception {
        // 1.  判断该用户是否与客服建立关系
        Map<String,String> staffs = CustomerServiceHelper.getStaffs(appid);
        String shopUserAccount = staffs.get(openid);
        if(shopUserAccount!=null){
            //存在关系则返回
            return getOnlineUserByAccount(appid,shopUserAccount);
        }
        return null;
    }

    /**
     * 通过 店铺服务人员的账号获取 onlineUser
     * @param account
     * @param appid
     * @return
     * @throws Exception
     */
    public static OnlineUser getOnlineUserByAccount(String account,String appid) throws Exception {
        List<OnlineUser> onlineUsers = CustomerServiceHelper.getOnlineService(appid);
        for(OnlineUser onlineUser : onlineUsers){
            if(onlineUser.getHxAcct().equals(account)){
                return onlineUser;
            }
        }
        return null;
    }

    /**
     * 手机端获取客服人员
     * @param openid
     * @param appid
     * @return
     * @throws Exception
     */
    public static OnlineUser appGetOnlineUserByAccount(String openid,String appid) throws Exception {
        Map<String,String> staffs = CustomerServiceHelper.getStaffs(appid);
        String shopUserAccount = staffs.get(openid);
        if(shopUserAccount!=null){
            return getOnlineUserByAccount(appid,shopUserAccount);
        }else{
            List<OnlineUser> onlineUsers = CustomerServiceHelper.getOnlineService(appid);
            return CustomerServiceHelper.getOnlineUser(onlineUsers);
        }
    }
    /**
     * 从队列中删去待接待 用户
     * @param appid
     * @param openid
     * @return
     * @throws Exception
     */
    public static boolean deleteUserFromWaitQueue(String appid,String openid) throws Exception {
        Set<String> waitQueue = CustomerServiceHelper.getWaitQueue(appid);
        boolean flag  = waitQueue.remove(openid);
        CustomerServiceHelper.updateWaitQueue(appid,waitQueue);
        return flag;
    }

    /**
     * 添加用户到待接待队列
     * @param appid
     * @param openid
     */
    public static void addCustomerToWaitQueue(String appid,String openid) throws Exception {
        Set<String> waitQueue = CustomerServiceHelper.getWaitQueue(appid);
        if(!waitQueue.contains(appid)){
            waitQueue.add(appid);
            CustomerServiceHelper.updateWaitQueue(appid,waitQueue);
        }
    }

    /**
     * 删除 客户与 客服的关系
     * @param appid
     * @param openid
     * @return
     */
    public static boolean deleteStaffQueue(String appid,String openid)  {
        boolean flag = true;
        try{
            Map<String,String> staffs = CustomerServiceHelper.getStaffs(appid);
            staffs.remove(openid);
            CustomerServiceHelper.updateStaffQueue(staffs,appid);
        }catch (Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 添加客服人员与客户的关系
     * @param appid
     * @param openid
     * @param serviceAccount
     * @return
     */
    public static  boolean addStaffQueue(String appid,String openid,String serviceAccount){
        boolean flag = true;
        try{
            Map<String,String> staffs = CustomerServiceHelper.getStaffs(appid);
            staffs.put(openid,serviceAccount);
            CustomerServiceHelper.updateStaffQueue(staffs,appid);
        }catch (Exception e){
            flag = false;
        }
        return  flag;
    }
}
