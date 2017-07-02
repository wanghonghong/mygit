package com.jm.business.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>异常统一提示类</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/6/23
 */

@Data
@ApiModel(description = "异常统一提示类")
public class ExceptionMsg {

    @ApiModelProperty(value = "编码")
    private Integer jmcode;

    @ApiModelProperty(value = "信息")
    private String msg;

    @ApiModelProperty(value = "原因")
    private String cause;


    public ExceptionMsg(Integer jmcode, String msg){
        this.jmcode = jmcode;
        this.msg = msg;
        this.cause = null;
    }

    public ExceptionMsg(Integer jmcode, String msg, String cause){
        this.jmcode = jmcode;
        this.msg = msg;
        this.cause = cause;
    }
    
}
