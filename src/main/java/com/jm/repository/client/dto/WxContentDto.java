package com.jm.repository.client.dto;

import lombok.Data;

@Data
public class WxContentDto {
	
	/**
	 * 错误码
	 */
	private String errcode;
	/**
	 * 错误信息
	 */
	private String errmsg;
	/**
	 * 消息发送任务的ID
	 */
	private String msgId;
	/**
	 * 消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
	 */
	private String msgDataId;
	
	private String type;
	private String mediaId;
	private String createdAt;

}
