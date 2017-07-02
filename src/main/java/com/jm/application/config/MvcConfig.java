package com.jm.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <p>springmvc 配置</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/12
 */
/*@Configuration
@EnableWebMvc*/
public class MvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 配制拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        //registry.addInterceptor(new UserCookieInterceptor());
    }

}
