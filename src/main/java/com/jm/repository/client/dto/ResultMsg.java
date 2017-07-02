package com.jm.repository.client.dto;

import lombok.Data;

/**
 * <p>微信返回</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19
 */
@Data
public class ResultMsg {

    private Integer errcode;

    private String errmsg;
    
    public ResultMsg(Integer errcode,String errmsg){
    	this.errcode = errcode;
    	this.errmsg = errmsg;
    	
    }
    public ResultMsg(){
  
    }

}
