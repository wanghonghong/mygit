package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>登录信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@ApiModel(description = "登录信息")
public class ZbLoginVo {



    @ApiModelProperty("手机号（用户名）")
    private String phoneNumber;

    @ApiModelProperty("密码")
    private String password;
    


}
