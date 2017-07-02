package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
public class UserForUpdateVo {


    @ApiModelProperty("验证码")
    private String code;

    private String phoneNumber;

    private String password;


}
