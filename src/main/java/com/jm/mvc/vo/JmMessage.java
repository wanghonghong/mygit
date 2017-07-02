package com.jm.mvc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>聚米消息类</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/11/011
 */

@Data
@ApiModel(description = "聚米消息类")
public class JmMessage {

    @ApiModelProperty(value = "编码")
    private Integer code;

    @ApiModelProperty(value = "信息")
    private String msg;

    @ApiModelProperty(value = "原因")
    private String cause;

    @ApiModelProperty(value = "返回数据")
    private Object data;
    
    public JmMessage() {
		super();
	}
    
    public JmMessage(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
    
    public JmMessage(Integer code,String msg,String cause){
        this.code = code;
        this.msg = msg;
        this.cause = cause;
    }
    
    public JmMessage(Integer code,String msg,Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

	
    
}
