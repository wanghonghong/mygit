package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
public class JmUserUo {

    private Integer userId;
	 
    private Integer roleId;
	 
    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    @ApiModelProperty(value = "身份证号")
    private String cardNum;

    @ApiModelProperty(value = "身份证照片")
    private String cardNumImg;

    @ApiModelProperty(value = "邮箱")
    private String email;

}
