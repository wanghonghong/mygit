package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ZbSoftMenuVo {

    @ApiModelProperty(value = "版本号ID")
    private Integer softId;

    @ApiModelProperty(value = "资源ID")
    private String resourceIds;

}
