package com.jm.staticcode.constant;

/**
 * <p>微博请求的url接口地址</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/22 10:22
 */
public class WbUrl {
    //根据商家微博token 获取授权用户信息
    public static String  GET_TOKEN_INFO = "https://api.weibo.com/oauth2/get_token_info?access_token=ACCESS_TOKEN";

    //微博授权之后，获取授权用户的UID
    public static String  GET_UID_URL = "https://api.weibo.com/2/account/get_uid.json?access_token=ACCESS_TOKEN";

    //获取token接口地址
    public static String  ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

    //授权接口
    public static String  AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";

    //菜单创建
    public static String MENU_CREATE = "https://m.api.weibo.com/2/messages/menu/create.json?access_token=ACCESS_TOKEN";

    //菜单获取
    public static String MENU_GET = "https://m.api.weibo.com/2/messages/menu/show.json?access_token=ACCESS_TOKEN";

    //微博退款
    public static String REFUND_URL = "http://pay.sc.weibo.com/api/merchant/pay/refund/apply?";
}
