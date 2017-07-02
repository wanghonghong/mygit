package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>登录日志vo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class LoginLogVo {

    @ApiModelProperty(value = "登录ip")
    private String ip;

    @ApiModelProperty(value = "登录地址")
    private String address;

    @ApiModelProperty(value = "登录工具")
    private String tool;

    @ApiModelProperty(value = "登录时间")
    private Date createDate;


}
