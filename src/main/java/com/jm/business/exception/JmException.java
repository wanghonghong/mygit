package com.jm.business.exception;

import org.springframework.http.HttpStatus;

/**
 * <p>聚米异常统一处理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/11/011
 */
public class JmException extends RuntimeException {

    private Integer jmCode;

    private String msg;

    private String cause;

    public JmException(String msg) {
        this.msg = msg;
    }

    public JmException(Integer jmCode, String msg) {
        this.jmCode = jmCode;
        this.msg = msg;
    }

    public JmException(Integer jmCode, String msg,String cause) {
        this.jmCode = jmCode;
        this.msg = msg;
        this.cause = cause;
    }
}
