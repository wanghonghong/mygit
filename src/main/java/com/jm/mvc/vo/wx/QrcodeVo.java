package com.jm.mvc.vo.wx;

import lombok.Data;

@Data
public class QrcodeVo {

	private String ticket;
	private String expireSeconds;
	private String url;
}
