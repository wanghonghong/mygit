package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class ZbUserQo {

    private String userName;

    private String phoneNumber;

    @ApiModelProperty(value = "性别 0男  1女 ")
    private String sex;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    private String staffCode; //员工号

    private String department; //部门

    @ApiModelProperty(value = "状态 1待审核  2已过审核")
    private Integer status;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ZbUserQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }
}
