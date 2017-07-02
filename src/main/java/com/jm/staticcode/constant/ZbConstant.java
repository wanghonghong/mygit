package com.jm.staticcode.constant;

import com.jm.business.domain.ZtreeNode;
import com.jm.business.domain.zb.ResourceDo;
import com.jm.staticcode.util.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>常量</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class ZbConstant {

    public static String MSA_VERSION = Toolkit.getProperties("config/application.properties").getProperty("msa.version");

    public static String MSA_EXCEPTION = Toolkit.getProperties("config/application.properties").getProperty("msa.exception");

    public static String STATIC_URL = Toolkit.getProperties("config/application.properties").getProperty("static.url");

    public static String SESSION_USER= "jmuser";

    //切换服务器与本地测试配置
    public static String ERP_SERVICE = Toolkit.getProperties("config/application.properties").getProperty("msa.url");

    //万象优图路径
    public static String  IMAGE_URL = Toolkit.getProperties("config/application.properties").getProperty("image.url");

    public static List<ResourceDo> RESOURCE_LIST = new ArrayList<>();//静态菜单

    public static List<ZtreeNode>  RESOURCE_ZTREE_LIST = new ArrayList<>();//静态菜单树
}
