package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ZbSoftQo {

    @ApiModelProperty(value = "软件版本名称")
    private String name;

    @ApiModelProperty(value = "创建时间 开始")
    private Date createDateStart;

    @ApiModelProperty(value = "创建时间 结束")
    private Date createDateEnd;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ZbSoftQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }


}
