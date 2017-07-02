package com.jm.staticcode.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import com.jm.mvc.vo.shop.integral.IntegralProductVo;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>自定义数据格式转换</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/3
 */
public class JsonMapper {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // 设置将驼峰命名法转换成下划线的方式输入输出
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        // 设置时间为 ISO-8601 日期
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // 序列化BigDecimal时之间输出原始数字还是科学计数，默认false，即是否以toPlainString()科学计数方式来输出
        mapper.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, false);

        //设定是否使用Enum的toString函数来读取Enum, 为False时使用Enum的name()函数来读取Enum,
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        // 如果输入不存在的字段时不会报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 使用默认的Jsckson注解
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static void setMapper(ObjectMapper mapper) {
        JsonMapper.mapper = mapper;
    }

    public static <T> T parse(String json, Class<T> objectType) throws IOException {
        if (json == null) {
            return null;
        }
        Assert.notNull(objectType, "objectType cannot be null.");
        return mapper.readValue(json, objectType);
    }
    
    //json转为List
    public static <T> List<T> toList(String json,Class<T[]> objectType){
    	  List<T> list = null;  //目标list
          T[] arr = null; //ObjectMapper无法将json直接解析成对象的list，
          try {
              ObjectMapper mapper = new ObjectMapper(); 
              list = Arrays.asList(mapper.readValue(json, objectType)); //执行转换
          }catch (JsonParseException e){
              e.printStackTrace();
          }catch (JsonMappingException e){
              e.printStackTrace();
          }catch (IOException e){
              e.printStackTrace();
          }
          return list;
    	
    }

    public static <T> T parse(InputStream stream, Class<T> objectType) throws IOException {
        Assert.notNull(stream, "stream cannot be null.");
        Assert.notNull(objectType, "objectType cannot be null.");
        return mapper.readValue(stream, objectType);
    }

    public static String toJson(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T map2Obj(Map<String,Object> map,Class<T> objectType) throws IOException {
        return JsonMapper.parse(JsonMapper.toJson(map),objectType);
    }

}
