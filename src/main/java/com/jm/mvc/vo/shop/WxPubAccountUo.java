package com.jm.mvc.vo.shop;

import com.jm.repository.po.shop.ShopEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class WxPubAccountUo {

    @ApiModelProperty(value = "是否子商户(1：是 0：否)")
    private  Integer issub;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "支付秘钥")
    private String appKey;
    
    @ApiModelProperty(value = "是否开启自动回复(1：是 0：否)")
    private Integer isFixedReply;
    
    @ApiModelProperty(value = "是否开启尾连接(1:是 0：否)")
    private Integer isLastLink;
    



}
