package com.jm.staticcode.util;

import com.fasterxml.jackson.databind.JavaType;
import com.jm.repository.client.dto.auth.ResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>聚米自定义RestTemplate</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/9
 */
public class JmRestTemplate extends RestTemplate {

	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder", JmRestTemplate.class.getClassLoader());

	private static final boolean jackson2Present =
			ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", JmRestTemplate.class.getClassLoader()) &&
					ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", JmRestTemplate.class.getClassLoader());

	private static final boolean jacksonPresent =
			ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", JmRestTemplate.class.getClassLoader()) &&
					ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", JmRestTemplate.class.getClassLoader());

    public JmRestTemplate() {
        //注册转换器
        this.setMessageConverters(configureMessageConverters());
        //setErrorHandler(new RestApiErrorHandler());
    }
    
    public List<HttpMessageConverter<?>> configureMessageConverters(){
    	List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
    	// 默认非 UTF-8
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        stringConverter.setWriteAcceptCharset(false);
        converters.add(stringConverter);
        
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<Source>());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        
        if (jaxb2Present) {
            converters.add(new Jaxb2RootElementHttpMessageConverter());
        }
        if (jackson2Present) {
        	MappingJackson2HttpMessageConverter convert = new MappingJackson2HttpMessageConverter();
        	convert.setObjectMapper(JsonMapper.getMapper());
            converters.add(convert);
        }
    	return converters;
    }

   /* @Override
    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return super.responseEntityExtractor(getJavaType(ResultDto.class,responseType));
    }

    protected <T> ResponseExtractor<T> httpMessageConverterExtractor(Type responseType) {
        return new HttpMessageConverterExtractor(getJavaType(ResultDto.class,responseType), getMessageConverters());
    }

    *//**
     * 获取JavaType
     * @param type
     * @param subType
     * @return
     *//*
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
    }*/

}
