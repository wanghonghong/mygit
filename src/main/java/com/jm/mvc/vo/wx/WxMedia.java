package com.jm.mvc.vo.wx;

import lombok.Data;

@Data
public class WxMedia {
	private String type;
	private String mediaId;
	private String createdAt;
	private String errcode;
	private String url;
}
