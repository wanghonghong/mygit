package com.jm.business.service.wx;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jm.business.service.shop.ActivityService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.mvc.vo.wx.TokenTicketVo;
import com.jm.mvc.vo.wx.wxuser.WeixinUser;
import com.jm.mvc.vo.wx.wxuser.WxUserUpDo;
import com.jm.repository.client.WxAuthClient;
import com.jm.repository.client.WxClient;
import com.jm.repository.client.dto.WxUserDto;
import com.jm.repository.client.dto.auth.*;
import com.jm.repository.client.dto.wxuser.WxUserUpdateDto;
import com.jm.repository.jpa.wx.WxAuthRepository;
import com.jm.repository.jpa.wx.WxPubAccountRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.repository.po.wx.WxAuth;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.converter.WxUserConverter;
import com.jm.staticcode.converter.shop.WxPubAccountConverter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>微信授权</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Slf4j
@Service
public class WxAuthService {
    @Autowired
    private WxAuthClient wxAuthClient;
    @Autowired
    private WxPubAccountRepository wxPubAccountRepository;
    @Autowired
    private WxAuthRepository wxAuthRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private WxPubAccountService pubAccountService;
    @Autowired
    private WxGroupService groupService;
    @Autowired
    private WxUserLevelService wxUserLevelService;
    @Autowired
    private WxService wxService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private WxClient wxClient;


    /**
     * 获取每个公众号token
     * @param appid
     * @return
     * @throws Exception
     */
    public String getAuthAccessToken(String appid) throws Exception {
        TokenTicketVo tokenTicketVo = getTokenTicket(appid);
       return tokenTicketVo.getAccessToken();
    }

    /**
     * 通过appid获取TokenTicket
     * @param appid
     * @return
     * @throws Exception
     */
    @Cacheable(value ="default_cache", key="#appid")
    public synchronized TokenTicketVo getTokenTicket(String appid) throws Exception {
        WxPubAccount wxPubAccount = wxPubAccountRepository.findOne(appid);
        if (wxPubAccount.getExpiresAt() < System.currentTimeMillis()) {
            //刷新token
            AuthAccessToken authAccessToken = refreshToken(wxPubAccount);
            wxPubAccount.setToken(authAccessToken.getAuthorizerAccessToken());
            wxPubAccount.setRefreshToken(authAccessToken.getAuthorizerRefreshToken());
            wxPubAccount.setExpiresAt(authAccessToken.getExpiresAt());
            //重新获取ticket
            TicketDto ticket = wxAuthClient.getAuthTicket(wxPubAccount.getToken());
            wxPubAccount.setJsapiTicket(ticket.getTicket());
            wxPubAccountRepository.saveAndFlush(wxPubAccount);
        }
        TokenTicketVo tokenTicketVo = new TokenTicketVo();
        tokenTicketVo.setAccessToken(wxPubAccount.getToken());
        tokenTicketVo.setAppid(wxPubAccount.getAppid());
        tokenTicketVo.setJsapiTicket(wxPubAccount.getJsapiTicket());
        tokenTicketVo.setExpiresAt(wxPubAccount.getExpiresAt());
        return tokenTicketVo;
    }

    //刷新token
    private AuthAccessToken refreshToken(WxPubAccount wxPubAccount) throws Exception {
        RefreshTokenParam refreshTokenParam = new RefreshTokenParam(Constant.COMPONENT_APP_ID,wxPubAccount.getAppid(),wxPubAccount.getRefreshToken());
        return wxAuthClient.getRefeshToken(getCommonAccessToken(),refreshTokenParam);
    }

    /**
     * 获取 CommonAccessToken
     * @return
     */
    public String getCommonAccessToken(){
        WxAuth wxAuth = wxAuthRepository.findOne(1);
        return wxAuth.getToken();
    }

    public String getWxCommonAccessToken(String componentVerifyTicket) throws Exception {
        CommonAccessTokenParam param = new  CommonAccessTokenParam();
        param.setComponentAppid(Constant.COMPONENT_APP_ID);
        param.setComponentAppsecret(Constant.COMPONENT_APPSECRET);
        param.setComponentVerifyTicket(componentVerifyTicket);//从缓存取ket
        CommonAccessToken commonAccessToken = wxAuthClient.getCommonAccessToken(param);
        return commonAccessToken.getComponentAccessToken();
    }

    /**
     *<p>获取预授权码</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年5月21日
     */
    public PreAuthCode getPreAuthCode() throws Exception {
    	PreAuthCodeParam preAuthCodeParam = new PreAuthCodeParam();
    	preAuthCodeParam.setComponentAppid(Constant.COMPONENT_APP_ID);
    	return wxAuthClient.getPreAuthCode(getCommonAccessToken(), preAuthCodeParam);
    }

    /**
     * 获取授权页URL事实
     * @return
     */
    public String getAuthPageUrl() throws Exception {
        PreAuthCode preAuthCode = getPreAuthCode();
        return WxUrl.AUTH_PAGE.replace(Constant.APP_ID,Constant.COMPONENT_APP_ID).replace("PRE_AUTH_CODE",preAuthCode.getPreAuthCode())
                .replace(Constant.REDIRECT_URI,Constant.PC_DOMAIN+"/authorization_code");
    }
    
    
    /**
     * 获取授权页URL事实 to聚客红包
     * @return
     */
    public String getAuthPageUrlToJk() throws Exception {
        PreAuthCode preAuthCode = getPreAuthCode();
        return WxUrl.AUTH_PAGE.replace(Constant.APP_ID,Constant.COMPONENT_APP_ID).replace("PRE_AUTH_CODE",preAuthCode.getPreAuthCode())
                .replace(Constant.REDIRECT_URI,Constant.PC_DOMAIN+"/authorization_code_jk");
    }

    /**
     * 保存公众号授权信息(商家店铺)
     * @param authorizationCode
     * @param shopid 
     * @throws IOException 
     */
    public int saveAuthInfo(String authorizationCode, Integer shopid,Integer userId) throws Exception{
    	AuthAccessToken accessToken = wxAuthClient.getAuthAccessToken(getCommonAccessToken(),authorizationCode); //获取token，获取公众号信息
        WxPubAccount wxpub = wxPubAccountRepository.findOne(accessToken.getAuthorizerAppid());
        if(wxpub==null||wxpub.getAppid()==null){
            wxpub = new WxPubAccount();
        }
        //调用微信接口，更新数据库公众账号信息
        WxPubAccountDto wxPubAccountDto = getPubAccount(accessToken.getAuthorizerAppid());
        if(wxPubAccountDto.getAuthorizerInfo().getVerifyTypeInfo().getId()==-1){ //未认证
            return 3;
        }
        WxPubAccountConverter.toWxPubAccount(wxPubAccountDto,wxpub);
        wxpub.setToken(accessToken.getAuthorizerAccessToken());
        wxpub.setRefreshToken(accessToken.getAuthorizerRefreshToken());
        wxpub.setExpiresAt(accessToken.getExpiresAt());
        wxpub.setUserId(userId);
        wxpub.setType(0);
        //重新获取ticket
        TicketDto ticket = wxAuthClient.getAuthTicket(wxpub.getToken());
        wxpub.setJsapiTicket(ticket.getTicket());
        if(wxpub.getIsAuth()==0){  //未授权
            wxpub.setIsGet(0);
            wxpub.setIsAuth(1);  //设置状态，代表已经授权
        }
        wxPubAccountRepository.save(wxpub);
        int isOk=1;//1 授权失败
        //判断公众号是否被店铺使用过
        Shop shop = shopService.getShopByAppId(wxpub.getAppid());

        if(shop!=null){
            if (shop.getShopId().equals(shopid)){
                wxpub.setIsGet(0);
                wxPubAccountRepository.save(wxpub);
                isOk = 0;//0 授权成功
            }else{
                isOk=2;//2 公众号已经被授权过
            }
        }else{
            if(shopid!=null){
                //保存Appid到店铺
                Shop newShop=	shopService.findShopById(shopid);
                newShop.setAppId(wxpub.getAppid());
                newShop.setImgUrl(wxpub.getHeadImg());
                newShop= shopService.saveShop(newShop);
                if(newShop!=null){
                    isOk = 0;//0 授权成功
                    wxService.defaultMenu(wxpub.getAppid(), newShop.getShopId());//设置默认菜单
                    wxUserLevelService.setDefaultLevel(wxpub.getAppid());//设置用户默认等级

                    //商家系统授权成功后，聚客红包的活动需要停止掉
                    List<Activity> activitys = activityService.findActByAppid(wxpub.getAppid());
                    for (Activity activity : activitys) {
						if(activity.getStatus()!=3){
							//只要不是已经停止的活动都需要将活动结束掉
							activity.setStatus(3);
							activity.setEndTime(new Date());
							activityService.saveActivity(activity);
						}
					}
                }
            }
        }

		return isOk;
    }
    
    /**
     * 保存公众号授权信息（聚客红包）
     * @param authorizationCode
     * @return
     * @throws Exception 
     */
    public int saveJkAuthInfo(String authorizationCode,Integer userId) throws Exception{
    	AuthAccessToken accessToken = wxAuthClient.getAuthAccessToken(getCommonAccessToken(),authorizationCode); //获取token，获取公众号信息
        WxPubAccount wxpub = wxPubAccountRepository.findOne(accessToken.getAuthorizerAppid());
        if(wxpub==null||wxpub.getAppid()==null){
            wxpub = new WxPubAccount();
        	wxpub.setIsGet(1);//聚客红包不需要拉取用户信息
        }
        if(wxpub!=null && wxpub.getType()==0 && wxpub.getIsAuth()==1){
        	 return 0;//代表该公众号已经在分销系统开店授权，聚客红包这边直接拦截
        }
       
        //调用微信接口，更新数据库公众账号信息
        WxPubAccountDto wxPubAccountDto = getPubAccount(accessToken.getAuthorizerAppid());
        if(wxPubAccountDto.getAuthorizerInfo().getVerifyTypeInfo().getId()==-1){ //未认证
            return 3;
        }
        WxPubAccountConverter.toWxPubAccount(wxPubAccountDto,wxpub);
        wxpub.setToken(accessToken.getAuthorizerAccessToken());
        wxpub.setRefreshToken(accessToken.getAuthorizerRefreshToken());
        wxpub.setExpiresAt(accessToken.getExpiresAt());
        wxpub.setIsAuth(1);
        wxpub.setType(1);
        wxpub.setUserId(userId);
        //重新获取ticket
        TicketDto ticket = wxAuthClient.getAuthTicket(wxpub.getToken());
        wxpub.setJsapiTicket(ticket.getTicket());
        wxPubAccountRepository.save(wxpub);	
        return 1;
    }
    
    
    //通过公众号获取用户信息
    private void getUserInfo(WxPubAccount account,String accessToken) throws Exception{
    	if(account.getIsGet()==0){//未拉取过用户信息
    		account.setIsGet(1);  //将状态修改为已拉取过
    		wxPubAccountRepository.save(account);
            List<String> openids = getOpenids(accessToken); //通过accessToken获取公众号下得用户id列表
            int len = openids.size();
            //通过openid列表获取用户信息更新数据库
            List<WxUser> wxUserList = new ArrayList<>();
            for (int i=0;i< len;i++){
                WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(account.getAppid(), openids.get(i).toString());
                if(wxUser==null){
                    wxUser =  new WxUser();
                }
                WxUserDto wxUserDto = wxAuthClient.getWxUserDetail(accessToken,openids.get(i).toString());
                wxUserList.add(WxUserConverter.toWxUserForUpdate(wxUser,wxUserDto,account.getAppid()));
                wxUser.setIsSubscribe(1);  //关注状态
                wxUserList.add(wxUser);
                if (i>0 && i%10000==0){   //10000条插入一次
                    wxUserService.saveWxUser(wxUserList);
                    wxUserList.clear();
                }
            }
            wxUserService.saveWxUser(wxUserList);
            //从微信接口获取分组保存到数据库
            groupService.saveGroups(accessToken,account.getAppid());
      	}
    }

    /**
     * 通过accessToken获取公众号下得用户id列表
     * @param accessToken
     * @return
     */
    private List<String> getOpenids(String accessToken){
        List<String> openids = new ArrayList<>();
        WeixinUser user = getWxUser(accessToken, null);
        openids.addAll(user.getData().getOpenid());
        int currentCount = user.getCount();
        while(user.getTotal()>currentCount){
            user = getWxUser(accessToken, user.getNextOpenid());
            openids.addAll(user.getData().getOpenid());
            currentCount += user.getCount();
        }
        return openids;
    }

    /**
     * 获取微信公众账号基本信息
     * @param authorizerAppid
     * @return
     */
    public WxPubAccountDto getPubAccount(String authorizerAppid) throws Exception {
        WxPubAccountParam wxPubAccountParam = new WxPubAccountParam(Constant.COMPONENT_APP_ID,authorizerAppid);
        return wxAuthClient.getPubAccount(getCommonAccessToken(),wxPubAccountParam);
    }

    /**
     * 设置componentVerifyTicket
     * @param componentVerifyTicket
     */
    public void setComponentVerifyTicket(String componentVerifyTicket) throws Exception{
        if(componentVerifyTicket!=null){
            WxAuth wxAuth = wxAuthRepository.findOne(1);
            if(wxAuth==null){
                wxAuth = new WxAuth();
            }
            if (wxAuth.getExpiresAt()<System.currentTimeMillis()){ //表示过期
                String token = getWxCommonAccessToken(componentVerifyTicket); //获取CommonAccessToken
                wxAuth.setToken(token);
                wxAuth.setExpiresAt(System.currentTimeMillis()+6000*1000l);
            }
            wxAuth.setTicket(componentVerifyTicket);
            wxAuth.setUpdateTime(new Date());
            wxAuthRepository.save(wxAuth);
        }
    }
    
    /**
     *<p>根据code获取token（第三方带公众号授权）</p>
     *
     * @author chenyy
     * @version latest
     * @throws IOException 
     * @data 2016年6月2日
     */
    public AccessToken getTokenByCode(String code,String appid) throws Exception {
    	AuthTokenParam authTokenParam = new AuthTokenParam();
    	authTokenParam.setCode(code);
    	authTokenParam.setAppid(appid);
    	authTokenParam.setComponentAccessToken(getCommonAccessToken());
    	return wxAuthClient.getTokenByCode(authTokenParam);
    }
    
    public AccessToken refreshWebToken(String refreshToken,String appid) throws Exception {
    	AuthTokenParam authTokenParam = new AuthTokenParam();
    	authTokenParam.setAppid(appid);
    	authTokenParam.setRefreshToken(refreshToken);
    	authTokenParam.setComponentAccessToken(getCommonAccessToken());
    	return wxAuthClient.refreshWebToken(authTokenParam);
    }
    
    
    /**
     *<p>获取code</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年6月2日
     */
    public void getCode(String appid,String redUrl,String scope,HttpServletResponse response) throws Exception{
    	String url = WxUrl.AUTH_CODE_GET.replace("COMPONENT_APPID", Constant.COMPONENT_APP_ID)
				.replace(Constant.APP_ID, appid)
				.replace(Constant.REDIRECT_URI, redUrl)
				.replace(Constant.AUTH_SCOPE, scope)
				.replace(Constant.AUTH_STATE, "1");
		response.sendRedirect(url);
    }

    /**
     * 网页授权完重定向到微信服务器，返回code
     * @param request
     * @param response
     * @throws Exception
     */
    public void sendRedirect(HttpServletRequest request,HttpServletResponse response,String appid) throws Exception{
        //代表未授权过
        String rePath = Constant.APP_DOMAIN + request.getRequestURI();
        String query =  request.getQueryString();
        if(query!=null){
            rePath = rePath+"?"+query;
        }
        String url = WxUrl.AUTH_CODE_GET.replace("COMPONENT_APPID", Constant.COMPONENT_APP_ID)
                .replace(Constant.APP_ID, appid)
                .replace(Constant.REDIRECT_URI, URLEncoder.encode(rePath,"UTF-8"))
                .replace(Constant.AUTH_SCOPE, "snsapi_base")
                .replace(Constant.AUTH_STATE, "1");
        response.sendRedirect(url);
    }

    /**
     *<p>获取微信用户</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年6月15日
     */
    public  WeixinUser getWxUser(String accessToken,String nextOpenid){
    	String url = "";
    	if(nextOpenid==null || nextOpenid.equals("")){
    		url = WxUrl.WXUSER_LIST_GET.replace("ACCESS_TOKEN", accessToken);
    	}else{
    		url = WxUrl.WXUSER_LIST_GET_NEXT_OPENID.replace("ACCESS_TOKEN", accessToken)
    				.replace("NEXT_OPENID", nextOpenid);
    	}
    	return wxAuthClient.getWxUser(url);
    }

    //定时获取公众号用户信息
    public void getUserInfoTask() throws Exception {
        List<WxPubAccount> wxPubAccounts = wxPubAccountRepository.findWxPubAccountByIsGet(0);
        if(wxPubAccounts!=null && wxPubAccounts.size()>0){
            for (WxPubAccount wxPubAccount : wxPubAccounts){
                getUserInfo(wxPubAccount,getAuthAccessToken(wxPubAccount.getAppid()));
            }
        }
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo() throws Exception {
        //所有公众号
        List<WxPubAccount> wxPubAccounts = wxPubAccountRepository.findByIsAuth(1);
        for (WxPubAccount wxPubAccount:wxPubAccounts) {
            String accessToken = getAuthAccessToken(wxPubAccount.getAppid());
            List<String> openids = getOpenids(accessToken);//wxUserService.findOpenidByAppid(wxPubAccount.getAppid());
            List<WxUserUpDo> userDos = new ArrayList();
            for (String openid:openids ) {
                WxUserUpDo userDo = new WxUserUpDo();
                userDo.setOpenid(openid);
                userDos.add(userDo);
            }
            //一次最多请求100条，需要做集合割据
            List<List<WxUserUpDo>> newDos =  dealBySubList(userDos,100);
            List<WxUser> wxUserList = new ArrayList<>();
            for (List<WxUserUpDo> newDo:newDos){
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("user_list",newDo);
                WxUserUpdateDto wxUserUpdateDto = wxClient.batchGetUserInfo(paramMap,accessToken);
                List<WxUserUpdateDto> returnUpDto = wxUserUpdateDto.getUserInfoList();
                for (WxUserUpdateDto dto :returnUpDto){
                    WxUser wxUser = wxUserService.findWxUserByAppidAndOpenid(wxPubAccount.getAppid(),dto.getOpenid());
                    if(wxUser==null){
                        wxUser =  new WxUser();
                    }
                    if(dto.getSubscribe()==1){//判断下用户是否处于关注状态
                        wxUserList.add(WxUserConverter.updateUserInfo(wxUser,dto,wxPubAccount.getAppid()));
                    }
                }
               List<WxUser> returnUsers=  wxUserService.saveWxUser(wxUserList);
            }
        }

    }

    /**
     * 分割list 批处理
     * @param wxUserDos  集合
     * @param batchCount 每次割据条数
     */
    public  List<List<WxUserUpDo>> dealBySubList(List<WxUserUpDo> wxUserDos, int batchCount){
        int sourListSize = wxUserDos.size();
        int subCount = sourListSize%batchCount==0 ? sourListSize/batchCount : sourListSize/batchCount+1;
        int startIndext = 0;
        int stopIndext = 0;
        List<List<WxUserUpDo>> newUserUpDos = new ArrayList<>();
        for(int i=0;i<subCount;i++){
            stopIndext = (i==subCount-1) ? stopIndext + sourListSize%batchCount : stopIndext + batchCount;
            List<WxUserUpDo> tempList = new ArrayList<WxUserUpDo>(wxUserDos.subList(startIndext, stopIndext));
            newUserUpDos.add(tempList);
            startIndext = stopIndext;
        }
        return newUserUpDos;
    }
    
    /**
     *<p>用户取消授权</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年8月4日
     */
    public void cancelAuth(String appid){
    	WxPubAccount account = pubAccountService.findWxPubAccountByAppid(appid);
    	account.setIsAuth(0);//状态设置为未授权过
    	pubAccountService.save(account);
    	Shop shop = shopService.getShopByAppId(appid);
    	if(shop!=null ){
    		shop.setAppId("");
    		shopService.saveShop(shop);
    	}
        groupService.deleteGroupByAppid(appid);//删除分组
    }
}
