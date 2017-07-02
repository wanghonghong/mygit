package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/2/18
 */

@Data
@ApiModel(description = "送礼单发货时发送短信")
public class SendMsgOrderDispatchCo {

    @ApiModelProperty(value = "送礼人")
    private String giverName;

    @ApiModelProperty(value = "收货人")
    private String userName;

    @ApiModelProperty("收货人手机")
    private String phoneNumber;

    @ApiModelProperty(value = "收货地址")
    private String detailAddress;

    @ApiModelProperty(value = "物流公司名称")
    private String transName;

    @ApiModelProperty(value = "物流公司code")
    private String transNumber;

    @ApiModelProperty(value = "礼品名")
    private String productName;



}
