package com.jm.application.main;


import com.jm.application.config.BeanConfig;
import com.jm.application.config.DataSourceConfig;
import com.jm.repository.jpa.system.WxAreaRepository;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.ErrorPageFilter;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * <p>web项目</p>
 * <p>1. 相当于 web.xml 配置</p>
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Configuration
@EnableCaching
@EnableAutoConfiguration
@EntityScan("com")
@EnableJpaRepositories(value = "com", entityManagerFactoryRef="entityManagerFactoryPrimary")
@ComponentScan("com")
@Import({DataSourceConfig.class, BeanConfig.class})
public class WebApplication extends SpringBootServletInitializer {

    protected WebApplicationContext run(SpringApplication application) {
        application.getSources().remove(ErrorPageFilter.class);
        return super.run(application);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        initServletContext(servletContext);
        super.onStartup(servletContext);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class);
    }

    /**
     * 初始化servlet容器
     * @param servletContext
     */
    private void initServletContext(ServletContext servletContext){
//        servletContext.setAttribute("MSA_VERSION", Constant.MSA_VERSION);
        servletContext.setAttribute("THIRD_URL", Constant.THIRD_URL);
        servletContext.setAttribute("STATIC_URL", Constant.STATIC_URL);
        servletContext.setAttribute("TPL_CACHE", Constant.TPL_CACHE);
        servletContext.setAttribute("DOMAIN", Constant.DOMAIN);
        servletContext.setAttribute("COMPRESS", Constant.COMPRESS);
        servletContext.setAttribute("PLAT_FORM", Constant.PLAT_FORM);
    }

}

