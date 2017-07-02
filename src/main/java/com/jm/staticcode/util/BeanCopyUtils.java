package com.jm.staticcode.util;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.util.Map;

public class BeanCopyUtils {

    public static <T> T mapToBean(Map<String,Object> source, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T result =  tClass.newInstance();
        BeanWrapper beanWrapper = new BeanWrapperImpl(result);
        beanWrapper.setPropertyValues(source);
        return result;
    }
}


