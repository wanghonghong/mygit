package com.jm.mvc.vo.wx;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenTicketVo implements Serializable {

	private String appid;

	private String accessToken;

	private String jsapiTicket;

	private long expiresAt;

}
