package com.jm.repository.client.wb;

import com.jm.staticcode.util.JmRestTemplate;
import com.jm.staticcode.util.JsonMapper;

import java.util.Map;

/**
 * <p>微博公用的client</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/22 10:13
 */
public class WbBaseClient {
    protected JmRestTemplate restTemplate = new JmRestTemplate();

    protected <T> T getForObject(String url,Class<T> responseType) throws Exception{
        String rs = MyHttp.sendGet(url);
        return JsonMapper.parse(rs,responseType);
    }

    protected <T> T postForObject(String url, String request, Class<T> responseType, Object... urlVariables) throws Exception{
        String rs = MyHttp.sendPost(url,request);
        return JsonMapper.parse(rs,responseType);
    }

    protected <T> T getForObjectWb(String url, Class<T> responseType, Object... urlVariables) throws Exception{
        Map map = restTemplate.getForObject(url,Map.class,urlVariables);
        return JsonMapper.parse(JsonMapper.toJson(map),responseType);
    }

    protected <T> T postForObjectWb(String url, Object request, Class<T> responseType, Object... urlVariables) throws Exception{
        String jsonParams = restTemplate.postForObject(url,request,String.class,urlVariables);
        return JsonMapper.parse(jsonParams,responseType);
    }

}
