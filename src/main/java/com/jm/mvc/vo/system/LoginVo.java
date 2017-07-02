package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>登录信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@ApiModel(description = "登录信息")
public class LoginVo {

    @ApiModelProperty("手机号码")
    private String phoneNumber;

    @ApiModelProperty("密码")
    private String password;
    
    @ApiModelProperty("记住我")
    private Integer remember;

    @ApiModelProperty("验证码")
    private String code;

}
