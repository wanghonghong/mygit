package com.jm.mvc.filter;

import com.jm.business.exception.ExceptionMsg;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.ZbConstant;
import com.jm.staticcode.util.*;
import lombok.extern.log4j.Log4j;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>登录过滤器</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
@Log4j
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletContext().getAttribute("basePath")==null){
            request.getServletContext().setAttribute("basePath",request.getContextPath());
        }

        if (request.getServletContext().getAttribute("DOMAIN")==null){
            request.getServletContext().setAttribute("DOMAIN",Constant.DOMAIN);
        }

        if(isExclusion(request)){
            filterChain.doFilter(request, response);
        }else {
            if (Toolkit.isWxBrowser(request)) { //如果是微信请求
                return;
            }
            Object user = null;
            if (Constant.PLAT_FORM.equals("0")){
                user = request.getSession().getAttribute(Constant.SESSION_USER);
            }else {
                user = request.getSession().getAttribute(ZbConstant.SESSION_USER);
            }
            if (user==null){
                if (Toolkit.isAjaxRequest(request)){ //如果是ajax请求
                    ExceptionMsg message = new ExceptionMsg(-2,"用户未登录");
                    Toolkit.writeJson(response, JsonMapper.toJson(message));
                }else{
                    response.sendRedirect(request.getContextPath()+"/login");
                }
                return;
            }
            filterChain.doFilter(request, response);
         } 
    }

    /**
     * 不过来的问题
     * @param request
     * @return
     */
    private boolean isExclusion(HttpServletRequest request) {
        //不拦截的文件
        String exclusions = getFilterConfig().getInitParameter("exclusions");
        for(String exclusion : exclusions.split(",")){
            if (request.getRequestURI().endsWith(exclusion) ){
                return true;
            }
        }
        //不拦截的路径
        String exclusionPaths = getFilterConfig().getInitParameter("exclusionPaths");
        for(String exclusionPath : exclusionPaths.split(",")){
            if ( request.getServletPath().equals(exclusionPath) ){
                return true;
            }
        }
        return false;
    }

}
