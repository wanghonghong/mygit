package com.jm.repository.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.jm.business.exception.JmException;
import com.jm.staticcode.util.JmRestTemplate;
import com.jm.staticcode.util.JsonMapper;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;

/**
 * <p>公用的client</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class BaseClient {

    protected JmRestTemplate restTemplate = new JmRestTemplate();

    /**
     * 清空DefaultCache缓存
     * @param key
     */
    @CacheEvict(value ="default_cache", key="#key")
    public void delDefaultCache(String key){
    }
    
    
    protected JavaType getJavaType(Type type, Type subType){
        JavaType javaType = null;
        if(subType instanceof JavaType){
            javaType = JsonMapper.getMapper().getTypeFactory().
                    constructParametricType((Class) type, (JavaType) subType);
        }else if (subType instanceof Class) {
            javaType = JsonMapper.getMapper().getTypeFactory().
                    constructParametricType((Class) type, (Class) subType);
        }
        return javaType;
    }

    protected <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws Exception{
        Map map = restTemplate.getForObject(url,Map.class,urlVariables);
        if(map.get("errcode")!=null && !"0".equals(String.valueOf(map.get("errcode")))){
            throw new Exception("调用微信接口出错:"+JsonMapper.toJson(map));
        }
        return JsonMapper.parse(JsonMapper.toJson(map),responseType);
    }

    protected <T> T postForObject(String url, Object request, Class<T> responseType, Object... urlVariables) throws Exception{
        Map map = restTemplate.postForObject(url,request,Map.class,urlVariables);
        if(map.get("errcode")!=null && !"0".equals(String.valueOf(map.get("errcode")))){
            throw new Exception("调用微信接口出错:"+JsonMapper.toJson(map));
        }
        return JsonMapper.parse(JsonMapper.toJson(map),responseType);
    }

}
