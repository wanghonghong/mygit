package com.jm.repository.client.dto.wb;

import lombok.Data;

/**
 * <p>微博返回信息</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/4 16:09
 */
@Data
public class WbResultMsg {

    private Integer errorCode;

    private String error;

    private String result;

    private String request;

    private Integer code;// whh,返回消息代码

    private String msg;//whh,返回消息描述

    private String data;//whh,返回具体消息

    public WbResultMsg(Integer errorCode, String error,String result,String request){
    	this.errorCode = errorCode;
    	this.error = error;
        this.result = result;
        this.request = request;
    }
    public WbResultMsg(){
  
    }

}
