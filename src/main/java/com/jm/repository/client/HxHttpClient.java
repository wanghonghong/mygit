package com.jm.repository.client;


import com.jm.repository.client.dto.HxResultMessage;
import com.jm.repository.client.dto.HxToken;
import com.jm.staticcode.util.HttpClientHelper;
import com.jm.staticcode.util.HxUtils;

import java.util.HashMap;
import java.util.Map;

public class HxHttpClient  {

    public HxResultMessage postHxMsg(String url,Map<String,String> headers,Map<String,Object> message) throws Exception {
        HttpClientHelper<HxResultMessage> client = new HttpClientHelper<HxResultMessage>();
        HxResultMessage result = client.sendPost(url,headers,message,HxResultMessage.class);
        return result;
    }

    public HxToken getToken() throws Exception {
        String url = HxUtils.HX_URL+ HxUtils.ORG_NAME+HxUtils.APP_NAME+"/token";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("grant_type","client_credentials");
        params.put("client_id",HxUtils.CLIENT_ID);
        params.put("client_secret",HxUtils.SECRET);
        HttpClientHelper<HxToken> client = new HttpClientHelper<>();
        HxToken token = client.sendPost(url,null,params,HxToken.class);
        return token;
    }



//    public static void main(String [] args) throws Exception {
//
//        /*HxHttpClient client = new HxHttpClient();
//        Map<String,String> message = new HashMap<String,String>();
//        message.put("nickname","nickname");
//        message.put("password","123456");
//        message.put("username","hx_ttt");
//        HxResultMessage resultMessage = client.regHxAccount(HxUtils.HX_URL+ HxUtils.ORG_NAME+HxUtils.APP_NAME+"/users",message);*/
//        HxHttpClient client = new HxHttpClient();
//        HxToken token  = client.getToken();
//        System.out.println(token.getAccess_token());
//    }
}
