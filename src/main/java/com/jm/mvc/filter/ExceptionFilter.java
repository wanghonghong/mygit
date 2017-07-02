
package com.jm.mvc.filter;

import com.jm.business.exception.ExceptionMsg;
import com.jm.business.exception.JmException;
import com.jm.business.service.system.JmExceptionService;
import com.jm.mvc.vo.JmUserSession;
import com.jm.repository.po.system.LogJmException;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ApplicationContextUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * <p>异常过滤器</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
public class ExceptionFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("Exception filter start");
        try{
            filterChain.doFilter(request, response);
        }catch(Throwable e){
            log.error(e.getMessage());
            e.printStackTrace();
            try{
                if ("1".equals(Constant.MSA_EXCEPTION)){
                    ExceptionMsg message = new ExceptionMsg(-1,e.getMessage());
                    if (e instanceof JmException){
                        JmException jmException = (JmException) e;
                        message.setJmcode(1);
                        message.setMsg(jmException.getMessage());
                    }
                    Toolkit.writeJson(response,JsonMapper.toJson(message));
                }else{ //异常保存到数据库
                    JmExceptionService jmExceptionService = ApplicationContextUtil.getApplicationContext().getBean(JmExceptionService.class);
                    JmUserSession jmUserSession = (JmUserSession)request.getSession().getAttribute(Constant.SESSION_USER);
                    LogJmException expLog = new LogJmException();
                    expLog.setStatus(Constant.MSA_EXCEPTION);
                    expLog.setLogMsg(e.getMessage());
                    expLog.setCreateTime(new Date());
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw, true));
                    expLog.setLogDetail(sw.toString());
                    expLog.setRequestUrl(request.getRequestURI());
                    expLog.setShopId(jmUserSession.getShopId());
                    expLog.setAppid(jmUserSession.getAppId());
                    jmExceptionService.saveJmException(expLog);
                }
            }catch(Throwable w){
                log.error(w.getMessage());
                w.printStackTrace();
            }
        }
        log.debug("Exception filter end");
    }

}
