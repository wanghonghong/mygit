package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ZbSoftVo {

    private Integer id;

    @ApiModelProperty(value = "软件版本名称")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
