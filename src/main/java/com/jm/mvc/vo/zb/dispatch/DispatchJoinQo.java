package com.jm.mvc.vo.zb.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/11/16.
 */
@Data
@ApiModel(description = "总部派单加盟信息分页查询Qo")
public class DispatchJoinQo {

    private String userName;

    private String phoneNumber;

    private Integer applyRoleId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商 ")
    private Integer type;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public DispatchJoinQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
