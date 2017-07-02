package com.jm.mvc.vo.wx;

import lombok.Data;

@Data
public class WeixinPayReturnParam {
	private String returnCode;
	private String prepayId;

}
