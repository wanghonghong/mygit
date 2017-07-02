package com.jm.mvc.vo.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;


@Data
public class WxPubAccountRo {

    @ApiModelProperty(value = "是否子商户(1：是 0：否)")
    private  Integer issub;

    @ApiModelProperty(value = "商户号")
    private String mchId;

    @ApiModelProperty(value = "支付秘钥")
    private String appKey;

    @ApiModelProperty(value = "公众号昵称")
    private String nickName;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public WxPubAccountRo(){
        this.curPage = 0;
        this.pageSize = 10;
    }


}
