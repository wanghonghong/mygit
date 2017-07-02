package com.jm.staticcode.constant;

import com.jm.business.domain.AreaDo;
import com.jm.business.domain.WxAreaDo;
import com.jm.business.domain.shop.SubscribePushDo;
import com.jm.business.domain.ZtreeNode;
import com.jm.repository.po.wx.WxMenuVisit;
import com.jm.staticcode.util.Toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * <p>常量</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class Constant {

    public static String APP_DOMAIN ;//= Toolkit.getProperties("config/application.properties").getProperty("system.app.domain");

    public static String DOMAIN ;

    public static String COMPRESS = Toolkit.getProperties("config/application.properties").getProperty("compress");
    public static String PLAT_FORM = Toolkit.getProperties("config/application.properties").getProperty("plat.form");

    public static String PC_DOMAIN ;//= Toolkit.getProperties("config/application.properties").getProperty("system.pc.domain")+"/msa";

    public static String COMPONENT_APP_ID; //Toolkit.getProperties("config/application.properties").getProperty("component.appid");

    public static String COMPONENT_APPSECRET ;//Toolkit.getProperties("config/application.properties").getProperty("component.appsecret");

    //js，css版本
//    public static String MSA_VERSION = Toolkit.getProperties("config/application.properties").getProperty("msa.version");

    //异常标识
    public static String MSA_EXCEPTION = Toolkit.getProperties("config/application.properties").getProperty("msa.exception");

    //静态资源路径
    public static String STATIC_URL = Toolkit.getProperties("config/application.properties").getProperty("static.url");

    //静态资源路径
    public static String TPL_CACHE = Toolkit.getProperties("config/application.properties").getProperty("tpl.cache");

    //第三方静态资源路径
    public static String THIRD_URL = Toolkit.getProperties("config/application.properties").getProperty("third.url");

    //COS
    public static final String COS_PATH = "https://res.jumiweigu.com";

    //万象优图路径
    public static String IMAGE_URL = "https://image.jumiweigu.com/";

    public static String RE_IMAGE_URL = "http://image.jumiweigu.com/";


    public static String TOKEN = "jumiweigu123321";
    
    public static String ENCODINGAESKEY="jumiweiguyidongshangcheng1233jumishangcheng";

    public static String XML_FORMAT = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";

    public static String APP_ID = "APPID";

    public static String REDIS_AUTH = "AUTH"; //redis 保存目录

    public static String ACCESS_TOKEN = "ACCESS_TOKEN";

    public static String AUTH_CODE = "AUTH_CODE";

    public static String  COMPONENT_ACCESS_TOKEN = "COMPONENT_ACCESS_TOKEN";

    public static String  REDIRECT_URI = "REDIRECT_URI";
    
    public static String CODE= "CODE";
    
    public static String AUTH_SCOPE = "SCOPE";
    
    public static String AUTH_STATE="STATE";
    
    public static String APPKEY= "000111222333444555666777888999jm";//公众号那边配置的
    
    public static String PARTNERKEY= "000111222333444555666777888999jm";

    public static String SESSION_USER= "jmuser";

    public static String JK_MANAGE_APPID= Toolkit.getProperties("config/application.properties").getProperty("jk_manage_appid");

    public static String SESSION_LOGIN_CODE= "JM_LOGIN_CODE";//登录验证码

    public static String SESSION_WX_USER= "JM_WX_USER";

    public static String SERVICE_MCHID = "1343280701"; //聚米为谷服务商商户号

    public static String SERVICE_APPKEY = "000111222333444555666777888999jm"; //聚米为谷服务商商户号

    public static String SERVICE_APPID="wx46506c1578a96cac";//服务商聚米为谷的公众号
    
    public static String JKRED_SERVICE_APPID="wxb23e582a9f9b3908";//服务商聚米为谷的公众号

    public static List<WxAreaDo> WX_AREA_LIST = new ArrayList<>();//静态微信地区

    public static List<AreaDo> AREA_LIST = new ArrayList<>();//静态地区

    public static List<ZtreeNode>  AREA_ZTREE_LIST = new ArrayList<>();//静态地区树

    public static Vector<SubscribePushDo> SUBSCRIBE_PUSH_LIST = new Vector<>();//关注推送列表

    public static List<ZtreeNode> WX_AREA_LIST_ALL = new ArrayList<>();//静态地区树

    public static Vector<WxMenuVisit> WX_MENU_VISIT_LIST = new Vector<>();  //菜单点击记录保存

    /**
     * 二维码支付收款账户商户id
     */
    public static String QRCODE_PAY_MCH_ID = "1342370001";

    public  static String QRCODE_PAY_KEY = "000111222333444555666777888999jm";

    public static int REFRESH_TIME = 1000*60*5;

    //切换服务器与本地测试配置
    public static String MSA_SERVICE = Toolkit.getProperties("config/application.properties").getProperty("erp.url");

    public static String HX_SCRET_KEY = "jimiweigu";

    //微博
    public static String  GRANT_TYPE  = "authorization_code"; //授权请求类型
    public static String CLIENT_ID ; //	申请应用时分配的AppKey。
    public static String CLIENT_SECRET ; // 申请应用时分配的AppSecret
    public static int COUNT = 200 ; // 单页返回的记录条数，默认为50，最大不超过200,微博粉丝列表。
    public static int UID_COUNT = 5000 ; // 单页返回的记录条数，默认为500，最大不超过5000,微博粉丝uid列表。
    public static String RSA_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANg64raE1hbolA4O" +
            "yEG3f5DMrC53xD7/FUC+KyKDxplGBSe1/adRaOJQNAZG1aV6vt/n1sXVnm1qT8Sa" +
            "LkAVGy0jnk9rFZCK4agozTnM3YACg7Giy2FE4WOcl8vy8UedgyN9cxjP2MxZ4vdQ" +
            "oJdy+vrK9oPUvkt/PzdqTRxg/SRRAgMBAAECgYEA0A+RWRjmvDJdTE2TChEccWNQ" +
            "BoJ91jQy6hJShSqRbai/ix+GDHq8Vo/gD24XY9yBUAfQVJqhkBzs1nuZJ1ZnxzmN" +
            "tz+KrWtXIcvvxdcIBMg8WbKBy13b9Tm2X4AI7LxIv6yIEAUNFiAVPIH1/4ACOXbV" +
            "QKAnysUeHAxywLeN40UCQQDwTTUXsW9qyhHw4V3KsB/VXyPMvY/DmLHBdze+jhzu" +
            "f71tKbcBvPLUIYTanRNbUwhvDEGv5PKufl0TklDsY5DbAkEA5lsbPPRI2ibar81K" +
            "tJZhjS7wTSfLyE3/BoeX7wg3ndbgrW7vUBZfZNn38VAPqxBbUZrdGYyvvTgeOX1M" +
            "wmYhQwJBAMRqVztGjQt8n62EiGVkEKEXYuu3Bguag7DfAXGAN6W6Q9s2fZ+uEc3S" +
            "mAUY3vmGpR4fppFfUf3Oy8VzkzGlMIECQCJ8+XMCz/V5Y7x2a/zllu1gXbHvh18R" +
            "7mC35FNxU4JaLYFJ3qKK9vVOfQSwC+h8pm9tVDUDDzyxLjVFQTzJtakCQQCoCtql" +
            "DviGW8fzacnsuXCyU8EhOcJyxb3KneiqjRnp9Ii+E5KZyg5mPeXkgTfKVKK6k1KD" +
            "audyast9kW0xBvjz"; //微博私钥



}
