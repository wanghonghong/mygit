package com.jm.repository.client;

import lombok.Data;

@Data
public class WxResultDto<T> {
	
	private int errcode;
	
	private String errmsg;

	private T data;
	
}
