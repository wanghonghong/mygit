package com.jm.staticcode.util;

import com.jm.staticcode.util.http.HttpSender;
import com.jm.staticcode.util.http.IdleConnectionMonitorThread;
import com.jm.staticcode.util.http.ResponseBodyKey;
import com.qcloud.cos.ErrorCode;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class HttpClientHelper<T> extends HttpSender {

    // 连接池维护的最大连接数
    private static final int DEFAULT_MAX_TOTAL_CONNECTION = 500;
    // 每一个路由关联的最大连接数
    private static final int DEFAULT_MAX_ROUTE_CONNECTION = 500;
    // 默认发生错误时,retry的
    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    // 监控空闲线程
    private IdleConnectionMonitorThread idleMonitor;


    public String postRequest(String url,
                                  Map<String, String> headers,
                                  Map<String, Object> params,
                                  int timeout) throws Exception {

        int retry = 0;
        String responseStr = "";
        while (retry < DEFAULT_MAX_RETRY_COUNT) {
            HttpPost httpPost = new HttpPost(url);

            setTimeout(httpPost, timeout);
            setHeaders(httpPost, headers);

            ContentType utf8TextPlain =
                    ContentType.create("text/plain", Consts.UTF_8);
            String postJsonStr = new JSONObject(params).toString();
            StringEntity stringEntity = new StringEntity(postJsonStr, utf8TextPlain);
            httpPost.setEntity(stringEntity);
            try {
                HttpResponse httpResponse = cosHttpClient.execute(httpPost);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                responseStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                return responseStr;
            } catch (ParseException | IOException e) {
                ++retry;
                if (retry == DEFAULT_MAX_RETRY_COUNT) {
                    LOG.error("sendJsonRequest occur a error: {}", e.toString());
                    JSONObject errorRet = new JSONObject();
                    errorRet.put(ResponseBodyKey.CODE, ErrorCode.NETWORK_ERROR);
                    errorRet.put(ResponseBodyKey.MESSAGE, e.toString());
                    responseStr = errorRet.toString();
                }
            } finally {
                httpPost.releaseConnection();
            }
        }
        return responseStr;

    }

    public T sendPost(String url,Map<String,String> headers,Map<String,Object> params,Class<T> tClass) throws Exception {

        String str = this.postRequest(url,headers,params,500);
        return result(str,tClass);
    }

    public T sendGet(String url,Map<String,String> headers,Map<String,String> params,Class<T> tClass) throws Exception {
        HttpSender sender = new HttpSender();
        String str = sender.sendGetRequest(url,headers,params,500);
        return result(str,tClass);
    }

    private T result(String str,Class<T> tClass) throws Exception {
        return JsonMapper.parse(str,tClass);
        /*JSONObject json = new JSONObject(str);
        Set<String> keys = json.keySet();
        Map<String,Object> values = new HashMap<String,Object>();
        for(String key :keys){
            values.put(key,json.get(key));
        }
        T result = tClass.newInstance();
        BeanWrapper beanWrapper = new BeanWrapperImpl(result);
        beanWrapper.setPropertyValues(values);*/
    }

//    public static void main(String[] args)  {
//        HxHttpClient client = new HxHttpClient();
//
//        Map<String,Object> message = new HashMap<String,Object>();
//        Map<String,String> headers = new HashMap<>();
//
//        try {
//            /*message.put("nickname","nickname");
//            message.put("password","123456");
//            message.put("username","hx_ttt");*/
//            HxToken token = client.getToken();
//            headers.put("Authorization"," Bearer "+token.getAccess_token());
//            System.out.println(token.getAccess_token());
//            message.put("target_type","users");
//            String[] target = new String[]{"hx_181"};
//            message.put("target",target);
//            JSONObject msg = new JSONObject();
//            msg.put("type","chat");
//            msg.put("msg","hello world");
//            message.put("msg",msg);
//            message.put("from","hxs_1");
//
//            HxResultMessage resultMessage =  client.postHxMsg(HxUtils.HX_URL+ HxUtils.ORG_NAME+ HxUtils.APP_NAME+"/messages",headers,message);
//            System.out.println(resultMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
