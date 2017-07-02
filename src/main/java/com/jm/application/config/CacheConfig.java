package com.jm.application.config;

import com.jm.mvc.interceptor.JMCacheErrorHandler;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;


/**
 * <p>ehcache异常处理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/4
 */
@Configuration
public class CacheConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        CacheInterceptor cacheInterceptor = event.getApplicationContext().getBean("cacheInterceptor", CacheInterceptor.class);
        Assert.notNull(cacheInterceptor, "cacheInterceptor can not be null.");
        cacheInterceptor.setErrorHandler(new JMCacheErrorHandler());
    }
}
