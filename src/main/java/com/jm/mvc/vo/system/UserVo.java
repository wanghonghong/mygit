package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "用户")
public class UserVo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    @ApiModelProperty(value = "头像")
    private String headImg;


}
