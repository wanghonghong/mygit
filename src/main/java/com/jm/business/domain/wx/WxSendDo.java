package com.jm.business.domain.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
@ApiModel(description = "微信群发do")
public class WxSendDo {
	
	@ApiModelProperty(value = "发送的消息类型")
	private String msgtype;
	
	@ApiModelProperty(value = "发送的内容 文本类型的为文本内容，图片的为mediaId")
	private String content;
	
	@ApiModelProperty(value = "群发接收对象openids")
	private List<String>openids;
	
	@ApiModelProperty(value = "1：全部群发  2：根据分组群发  3：根据openid群发")
	private int type;
	
	@ApiModelProperty(value = "分组id")
	private Integer groupid;
	

}
