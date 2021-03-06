package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(description = "发送短信")
public class SendMsgCo {

    @ApiModelProperty("验证码")
    private String code;

    private String phoneNumber;

}
