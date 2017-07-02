package com.jm.repository.client.wb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jm.mvc.vo.JmMessage;
import com.jm.repository.client.dto.wb.*;
import com.jm.repository.po.wb.WbShopUser;
import com.jm.repository.po.wb.WbUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WbUrl;
import com.jm.staticcode.util.HttpClientHelper;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>微博接口</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/22 10:16
 */
@Slf4j
@Repository
public class WbClient extends WbBaseClient {
    /**
     * <p>授权获取token</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 14:38
     */
    public WbAccessToken getTokenByCode(String code,String uri) throws Exception {
        String url = WbUrl.ACCESS_TOKEN_URL+"?";
        Map map = new HashMap();
        String redirectUri = Constant.PC_DOMAIN +uri;
//      redirectUri = "http://acyyhr.oicp.net/msa"+uri;
        map.put("client_id",Constant.CLIENT_ID);
        map.put("client_secret",Constant.CLIENT_SECRET);
        map.put("grant_type",Constant.GRANT_TYPE);
        map.put("redirect_uri",redirectUri);
        map.put("code",code);
        url +=Toolkit.mapToParam(map);
        WbAccessToken wbAccessToken = postForObjectWb(url,null,WbAccessToken.class);
        log.info("-----------OAUTH2授权获取token-----------"+wbAccessToken);
        return wbAccessToken;
    }
    /**
     * <p>创建微博菜单</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 14:36
     * @param uid
     * @param menu
     * @param wbShopUser
     */
    public WbResultMsg createWbMenu(Long uid, WbMenuVo menu, WbShopUser wbShopUser) throws Exception {
        WbResultMsg wbResultMsg = new WbResultMsg();
        if(wbShopUser!=null){
            String url =WbUrl.MENU_CREATE.replace(Constant.ACCESS_TOKEN,wbShopUser.getAccessToken());
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String filterVo = mapper.writeValueAsString(menu);
            filterVo = Toolkit.underscoreName(filterVo);
            String menuJson = URLEncoder.encode(filterVo,"UTF-8");
            Map map = new HashMap();
            map.put("menus",menuJson);
            url +="&"+ Toolkit.mapToParam(map);
            wbResultMsg = postForObject(url,"",WbResultMsg.class);
        }else{
            wbResultMsg.setError("创建菜单失败，微博商家用户信息丢失");
        }
        log.info("----------创建微博菜单------------"+wbResultMsg);
        return wbResultMsg;
    }

    /**
     * <p>获取微博服务器上面的菜单</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/8 10:01
     */
    public WbMenuDto getMenu(String accessToken) throws Exception {
        String url = WbUrl.MENU_GET.replace(Constant.ACCESS_TOKEN,accessToken);
        WbMenuDto wbMenuDto = getForObjectWb(url,WbMenuDto.class);
        String rs = JsonMapper.toJson(wbMenuDto);
        log.info("----------获取微博服务器上面的Menu------------"+rs);
        return wbMenuDto;
    }

    /**
     * <p>根据token获取用户信息</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 14:38
     */
    public WbShopUser getTokenInfo(String accessToken) throws Exception {
        String url = WbUrl.GET_TOKEN_INFO.replace(Constant.ACCESS_TOKEN,accessToken);
        WbShopUser wbShopUser = postForObjectWb(url,null,WbShopUser.class);
        wbShopUser.setAccessToken(accessToken);
        return wbShopUser;
    }

    //查询微博用户信息
    public WbUserDto3 getWbUser(String accessToken, String uid) throws Exception {
        String url = "https://api.weibo.com/2/users/show.json?access_token=ACCESS_TOKEN&uid=UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",uid);
        WbUserDto3 user =  getForObjectWb(url,WbUserDto3.class);
        return user;
    }

    //批量获取用户粉丝列表
    public WbUserDto3 getWbFolloers(String accessToken, Long uid, Integer cursor){
        String url = "https://api.weibo.com/2/friendships/followers.json?access_token=ACCESS_TOKEN&uid=UID&count=COUNT&cursor=CURSOR";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",Toolkit.parseObjForStr(uid)).replace("COUNT",Toolkit.parseObjForStr(Constant.COUNT)).replace("CURSOR",Toolkit.parseObjForStr(cursor));
        WbUserDto3 wbUserDto3 =  restTemplate.getForObject(url,WbUserDto3.class);
        return wbUserDto3;
    }

    //获取订阅者列表
    public WbUidDto getFollowerUids(String accessToken, long nextUid)throws  Exception{
        String url = "https://m.api.weibo.com/2/messages/subscribers/get.json?access_token=ACCESS_TOKEN&next_uid=NEXT_UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("NEXT_UID",Toolkit.parseObjForStr(nextUid));
        HttpClientHelper<WbUidDto> httpClientHelper = new HttpClientHelper();
        WbUidDto wbUidDto = httpClientHelper.sendGet(url,null,null,WbUidDto.class);
        return wbUidDto;
    }


    //获取用户的关注列表
    public WbUserDto3 getWbFriends(String accessToken, String uid){
        String url = "https://api.weibo.com/2/friendships/friends.json?access_token=ACCESS_TOKEN&uid=UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",uid);
        WbUserDto3 wbUserDto3 =  restTemplate.getForObject(url,WbUserDto3.class);
        return wbUserDto3;
    }

    //获取某个用户最新发表的微博列表
    public WbContenDto getNewWbContext(String accessToken, String uid) throws IOException {
        String url = "https://api.weibo.com/2/statuses/user_timeline.json?access_token=ACCESS_TOKEN&uid=UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",uid);
        WbContenDto wbContenDto =  restTemplate.getForObject(url,WbContenDto.class);
        return wbContenDto;
    }

    //获取当前登录用户及其所关注（授权）用户的最新微博
    public WbContenDto getHomeWbContext(String accessToken, String uid) throws IOException {
        String url = "https://api.weibo.com/2/statuses/home_timeline.json?access_token=ACCESS_TOKEN&uid=UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",uid);
        WbContenDto wbContenDto =  restTemplate.getForObject(url,WbContenDto.class);
        return wbContenDto;
    }

    //查询粉丝用户信息
    public WbUserDto getWbFollower(String accessToken, String uid) throws Exception {
        String url = "https://api.weibo.com/2/eps/user/info.json?access_token=ACCESS_TOKEN&uid=UID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("UID",uid);
        HttpClientHelper<WbUserDto> httpClientHelper = new HttpClientHelper();
        WbUserDto wbUserDto = httpClientHelper.sendGet(url,null,null,WbUserDto.class);
        return wbUserDto;
    }

    //查询用户的所有分组信息
    public WbGroupDto getWbUserGroup(String accessToken, String ruleType) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/get.json?access_token=ACCESS_TOKEN&rule_type=RULE_TYPE";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("RULE_TYPE",ruleType);
        HttpClientHelper<WbGroupDto> httpClientHelper = new HttpClientHelper();
        WbGroupDto wbGroupDto = httpClientHelper.sendGet(url,null,null,WbGroupDto.class);
        return wbGroupDto;
    }


    //查询用户所在分组
    public WbUserGroupDto queryWbUserGroup(String accessToken, String followerId) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/getid.json?access_token=ACCESS_TOKEN&follower_id=FOLLOWER_ID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("FOLLOWER_ID",followerId);
        HttpClientHelper<WbUserGroupDto> httpClientHelper = new HttpClientHelper();
        WbUserGroupDto wbUserGroupDto = httpClientHelper.sendGet(url,null,null,WbUserGroupDto.class);
        return wbUserGroupDto;
    }

    //创建分组
    public WbUserGroupDto createGroup(String accessToken, String name) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/create.json?access_token=ACCESS_TOKEN&name=NAME";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("NAME",name);
        HttpClientHelper<WbUserGroupDto> httpClientHelper = new HttpClientHelper();
        WbUserGroupDto wbUserGroupDto = httpClientHelper.sendPost(url,null,null,WbUserGroupDto.class);
        return wbUserGroupDto;
    }

    //删除分组
    public WbUserGroupDto deleteGroup(String accessToken, String groupid) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/delete.json?access_token=ACCESS_TOKEN&id=ID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("ID",groupid);
        HttpClientHelper<WbUserGroupDto> httpClientHelper = new HttpClientHelper();
        WbUserGroupDto wbUserGroupDto = httpClientHelper.sendPost(url,null,null,WbUserGroupDto.class);
        return wbUserGroupDto;
    }

    //修改分组名
    public WbUserGroupDto updateGroup(String accessToken, String groupid,String name) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/update.json?access_token=ACCESS_TOKEN&id=ID&name=NAME";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("ID",groupid).replace("NAME",name);
        HttpClientHelper<WbUserGroupDto> httpClientHelper = new HttpClientHelper();
        WbUserGroupDto wbUserGroupDto = httpClientHelper.sendPost(url,null,null,WbUserGroupDto.class);
        return wbUserGroupDto;
    }

    //移动用户分组
    public WbUserGroupDto moveUserGroup(String accessToken, String followerId,String toGroupId) throws Exception {
        String url = "https://m.api.weibo.com/2/messages/custom_rule/member/update.json?access_token=ACCESS_TOKEN&follower_id=FOLLOWER_ID&to_groupid=TO_GROUPID";
        url = url.replace("ACCESS_TOKEN", accessToken).replace("FOLLOWER_ID",followerId).replace("TO_GROUPID",toGroupId);
        HttpClientHelper<WbUserGroupDto> httpClientHelper = new HttpClientHelper();
        WbUserGroupDto wbUserGroupDto = httpClientHelper.sendPost(url,null,null,WbUserGroupDto.class);
        return wbUserGroupDto;
    }

    //微博退款
    public JmMessage toWbRefund(String url) throws Exception {
        JmMessage jmMessage = restTemplate.postForObject(url,null,JmMessage.class);
        return jmMessage;
    }


}
