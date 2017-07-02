package com.jm.staticcode.constant;

/**
 * <p>微信公众号请求地址</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class WxUrl {

    //菜单创建
    public static String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //菜单获取
    public static String MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    //获取token
    public static String ACCESS_TOKEN_GET = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //获取预授权码
    public static String PRE_AUTH_CODE_GET = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
    
    //获取commonAccesstoken
    public static String COMMON_ACCESS_TOKEN_GET = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    
    //获取获取预授权码pre_auth_code
    public static String PRE_AUTH_CODE = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";

    //授权页auth_page
    public static String AUTH_PAGE = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI";

    //使用授权码换取公众号的接口调用凭据和授权信息
    public static String AUTHORIZATION_INFO = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_ACCESS_TOKEN";

    //刷新token
    public static String REFRESH_TOKEN_GET =  "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";

    //获取授权方的公众号基本信息
    public static String PUB_ACCOUNT_INFO_GET = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
    
    //获取code
    public static String AUTH_CODE_GET = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&component_appid=COMPONENT_APPID#wechat_redirect";
    
    //根据code获取token
    public static String AUTH_TOKEN_GET = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";

    //设置用户的备注名
    public static String SET_REMARK_POST= "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";
    
    //微信支付 统一支付接口 
    public static  String WEIXIN_PRE_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    
    /**
	 * 创建二维码ticket(需要post)
	 */
	public static String GET_TICKET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	/**
	 * 获取二维码
	 */
	public static String GET_QRCODE="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	
	/**
	 * 上传微信素材
	 */
	public static String UP_METID="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	/**
	 * 获取微信用户列表，拉取从第一条开始拉取
	 */
	public static String WXUSER_LIST_GET="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
	/**
	 * 获取微信用户列表，拉取从NEXT_OPENID 上一次的最后一条为本次的第一条
	 */
	public static String WXUSER_LIST_GET_NEXT_OPENID ="https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
	
	/**
	 * 拉取用户信息
	 */
	public static String AUTH_GET_USER_INFO= "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 批量获取微信用户信息
	 */
	public static String BATCHGET_USER_INFO ="https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
	
	/**
	 * 微信授权接口
	 */
	public static String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	/**
	 * 根据code获取accessToken(单用户)
	 */
	public static String AUTH_GET_ACCESS_TOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	/**
	 * 微信红包URL
	 */
	public static String WEIXIN_RED = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	
	/**
	 * 微信客服接口
	 */
	public static String WEIXIN_KEFU="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	
	/**
	 * 获取用户分组接口
	 */
	public static String GET_GROUPS="https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
	/**
	 * 创建分组
	 */
	public static String CREATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
	/**
	 * 修改分组
	 */
	public static String UPDATE_GROUP="https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
	/**
	 * 删除分组
	 */
	public static String DELETE_GROUP="https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
	/**
	 * 移动分组
	 */
	public static String MOVE_GROUP="https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";
	/**
	 * 批量移动分组
	 */
	public static String BATCH_MOVE_GROUP="https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
	/**
	 * 修改用户备注
	 */
	public static String WX_USER_MARK="https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";
	
	/**
	 * 群发消息内容里面需要用到图片，需要在此上传
	 */
	public static String WX_CONTENT_IMG_UPLOAD =  "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	/**
	 * 上传图文消息素材
	 */
	public static String WX_UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
	/**
	 * 发送群发消息(根据openid)
	 */
	public static String WX_CONTENT_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	
	/**
	 * 发送群发消息(根据分组)
	 */
	public static String WX_CONTENT_SEND_TO_ALL="https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	
	/**
	 * 上传永久素材
	 */
	public static String WX_ADD_MATERIAL =  "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
	/**
	 * 新增永久图文素材
	 */
	public static String WX_ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	/**
	 * 预览消息
	 */
	public static String PREVIEW ="https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";
	/**
	 * 删除微信永久素材
	 */
	public static String DELMATERIAL =  "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";
	/**
	 * 退款
	 */
	public static String WX_PAY_RE_FUN="https://api.mch.weixin.qq.com/secapi/pay/refund";
	/**
	 * 设置所属行业
	 */
	public static String WX_SET_INDUSTRY =  "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取设置的行业信息
	 */
	public static String WX_GET_INDUSTRY =  "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
	
	/**
	 * 获取模板ID
	 */
	public static String WX_GET_TEMPLATE_ID ="https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	/**
	 * 获取模板列表
	 */
	public static String WX_GET_TEMPLATE_LIST =  "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";
	/**
	 * 删除模板
	 */
	public static String  WX_DELETE_TEMPLATE ="https://api,weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";
	/**
	 * 发送模板消息
	 */
	public static String WX_SEND_TEMPLATE_MSG ="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	/**
	 * 已发放的红包进行查询红包的具体信息
	 */
	public static String WX_RED_SEND_STATUS = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
}
