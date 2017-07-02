package com.jm.application.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.jm.mvc.filter.ExceptionFilter;
import com.jm.mvc.filter.LoginFilter;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>过滤器跟Servlet配置</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
@Configuration
public class ServletConfig {

    //不拦截的文件
    private String exclusionFile = ".html,.js,.css,.gif,.jpg,.png,.ico,.ttf,.woff";

   /* @Bean
    public FilterRegistrationBean corsFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CorsFilter());
        filterRegistrationBean.addUrlPatterns("*//*");
        filterRegistrationBean.addInitParameter(CorsFilter.PARAM_CORS_ALLOWED_ORIGINS, "*");
        filterRegistrationBean.addInitParameter(CorsFilter.PARAM_CORS_ALLOWED_METHODS, CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS);
        return filterRegistrationBean;
    }*/

    @Bean
    public FilterRegistrationBean exceptionFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ExceptionFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", exclusionFile);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", exclusionFile);
        filterRegistrationBean.addInitParameter("exclusionPaths", "/user,/sendmsg/0," +
                "/sendmsg/1,/login,/wx,/wx/az,/system/backPwd,/success,/err,/exist,/pay/pay_record,/pay/integral_recode," +
                "/pay/qrcode_pay_callback,/online/HX/onlineUserService,/online/HX/customer,/wbOauth,/wbOauth/scope,/redirectWbOauth,/developWb,/captcha-image,/wb," +
                "/zb/register,/zb/backPwd,/zb/register/focus_scan,/zb/register/finish_register,/zb/sendmsg/1,/zb/sendmsg/0,/zb/user,/zb/login,/zb/index,/xcx/auth");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean druid() {
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(statViewServlet, "/druid/*");
        return servletRegistrationBean;
    }

}
