package com.jm.repository.client;

import com.jm.mvc.vo.wx.wxuser.WeixinUser;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.client.dto.WxUserDto;
import com.jm.repository.client.dto.auth.*;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.io.IOException;

/**
 * <p>微信授权接口调用</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Slf4j
@Repository
public class WxAuthClient extends BaseClient {

    //过去时间配置
    private int expires = 90;

    //过去时间配置
    private int AUTH_EXPIRES = 850;


    /**
     * 获取微信AccessToken
     * @param appId
     * @param appSecret
     * @return
     */
    public AccessToken getAccessToken(String appId,String appSecret) throws Exception {
        String url = WxUrl.ACCESS_TOKEN_GET.replace(Constant.APP_ID,appId).replace("APPSECRET",appSecret);
        AccessToken accessToken = getForObject(url,AccessToken.class);
        if(accessToken==null || accessToken .getAccessToken()==null){
            return null;
        }else{
            accessToken.setExpiresAt(System.currentTimeMillis()+accessToken.getExpiresIn()*expires);
            return accessToken;
        }
    }

    /**
     * 获取commonAccessToken
     * @param param
     * @return
     */
    public CommonAccessToken getCommonAccessToken(CommonAccessTokenParam param) throws Exception {
        CommonAccessToken accessToken = postForObject(WxUrl.COMMON_ACCESS_TOKEN_GET, param, CommonAccessToken.class);
        if(accessToken==null || accessToken.getComponentAccessToken()==null){
            return null;
        }else{
            accessToken.setExpiresAt(System.currentTimeMillis()+accessToken.getExpiresIn()*expires);
            return accessToken;
        }
    }

    /**
     * 获取预授权码
     * @param commonAccessToken
     * @param authCodeParam
     * @return
     */
    public PreAuthCode getPreAuthCode(String commonAccessToken, PreAuthCodeParam authCodeParam) throws Exception {
        String url = WxUrl.PRE_AUTH_CODE.replace(Constant.COMPONENT_ACCESS_TOKEN, commonAccessToken);
        return postForObject(url,authCodeParam,PreAuthCode.class);
    }

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     * @param commonAccessToken
     * @param authorizationCode
     * @return
     */
    public AuthAccessToken getAuthAccessToken(String commonAccessToken, String authorizationCode) throws Exception {
        AuthInfoParam authInfoParam = new AuthInfoParam(Constant.COMPONENT_APP_ID,authorizationCode);
        String url = WxUrl.AUTHORIZATION_INFO.replace(Constant.COMPONENT_ACCESS_TOKEN, commonAccessToken);
        AuthInfo authInfo = postForObject(url,authInfoParam,AuthInfo.class);
        AuthAccessToken authAccessToken = authInfo.getAuthorizationInfo();
        authAccessToken.setExpiresAt(System.currentTimeMillis()+authAccessToken.getExpiresIn()*AUTH_EXPIRES);
        return authAccessToken;
    }

    /**
     * 获取刷新token
     * @param commonAccessToken
     * @param refreshTokenParam
     * @return
     */
    public AuthAccessToken getRefeshToken(String commonAccessToken, RefreshTokenParam refreshTokenParam) throws Exception {
        String url = WxUrl.REFRESH_TOKEN_GET.replace(Constant.COMPONENT_ACCESS_TOKEN, commonAccessToken);
        AuthAccessToken authAccessToken = postForObject(url,refreshTokenParam,AuthAccessToken.class);
        authAccessToken.setExpiresAt(System.currentTimeMillis()+authAccessToken.getExpiresIn()*AUTH_EXPIRES);
        return authAccessToken;
    }

    /**
     * 获取公众账号基本信息
     * @param commonAccessToken
     * @param wxPubAccountParam
     * @return
     */
    public WxPubAccountDto getPubAccount(String commonAccessToken, WxPubAccountParam wxPubAccountParam){
        String url = WxUrl.PUB_ACCOUNT_INFO_GET.replace(Constant.COMPONENT_ACCESS_TOKEN, commonAccessToken);
        return restTemplate.postForObject(url,wxPubAccountParam,WxPubAccountDto.class);
    }
    
    /**
     * 设置用户的备注名称
     * @param accessToken
     * @param remarkParam
     * @return
     */
    public ResultMsg setRemark(String accessToken, SetRemarkParam remarkParam){
        String url = WxUrl.SET_REMARK_POST.replace(Constant.ACCESS_TOKEN, accessToken);
        return restTemplate.postForObject(url,remarkParam,ResultMsg.class);
    }
    
    public AccessToken getTokenByCode(AuthTokenParam authTokenParam) throws IOException{
    	String url = WxUrl.AUTH_TOKEN_GET.replace("COMPONENT_APPID", Constant.COMPONENT_APP_ID)
    			.replace(Constant.APP_ID, authTokenParam.getAppid())
    			.replace(Constant.CODE, authTokenParam.getCode())
    			.replace("COMPONENT_ACCESS_TOKEN", authTokenParam.getComponentAccessToken());
        log.info("----url-----"+url+"------------");
        String json  = restTemplate.getForObject(url, String.class);    //restTemplate.postForObject(url,authTokenParam,WbAccessToken.class);
        AccessToken accessToken  =JsonMapper.parse(json, AccessToken.class);
        accessToken.setExpiresAt(System.currentTimeMillis()+accessToken.getExpiresIn()*AUTH_EXPIRES);
    	return accessToken;
    }
   
    public AccessToken refreshWebToken(AuthTokenParam authTokenParam) throws IOException{
    	String url = "https://api.weixin.qq.com/sns/oauth2/component/refresh_token?appid=APPID&grant_type=refresh_token"
    			+ "&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN&refresh_token=REFRESH_TOKEN";
    	url = url.replace("COMPONENT_APPID", Constant.COMPONENT_APP_ID)
    			.replace(Constant.APP_ID, authTokenParam.getAppid())
    			.replace("REFRESH_TOKEN", authTokenParam.getRefreshToken())
    			.replace("COMPONENT_ACCESS_TOKEN", authTokenParam.getComponentAccessToken());
        String json  = restTemplate.getForObject(url, String.class);    //restTemplate.postForObject(url,authTokenParam,WbAccessToken.class);
        AccessToken accessToken  =JsonMapper.parse(json, AccessToken.class);
        accessToken.setExpiresAt(System.currentTimeMillis()+accessToken.getExpiresIn()*AUTH_EXPIRES);
    	return accessToken;
    }
    
    
    
    /**
     *<p>获取微信用户</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年6月15日
     */
    public WeixinUser getWxUser(String url){
    	return restTemplate.getForObject(url, WeixinUser.class);
    }
    
    /**
     *<p>获取微信用户详情</p>
     *
     * @author chenyy
     * @version latest
     * @throws IOException 
     * @data 2016年6月15日
     */
    public WxUserDto getWxUserDetail(String accessToken,String openid) throws IOException{
        String url = WxUrl.AUTH_GET_USER_INFO.replace("ACCESS_TOKEN", accessToken)
                .replace("OPENID", openid);
        return restTemplate.getForObject(url,WxUserDto.class);
    }

    /**
     * 通过accessToken获取ticket
     * @param accessToken
     * @return
     * @throws Exception
     */
    public TicketDto getAuthTicket(String accessToken) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        url = url.replace(Constant.ACCESS_TOKEN, accessToken);
        TicketDto ticketDto = restTemplate.getForObject(url,TicketDto.class);
        ticketDto.setExpiresAt(System.currentTimeMillis()+ticketDto.getExpiresIn()*AUTH_EXPIRES);
        return ticketDto;
    }




}
