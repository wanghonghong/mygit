package com.jm.mvc.vo.erp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(discriminator = "总部系统 公众号列表查询")
public class ErpWxUserQo {
    @ApiModelProperty(value = "用户编号")
    private Integer userId;//此id是根据关注公众号的微信用户而产生的序列id

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "1男 2女 0未知")
    private Integer sex;

    @ApiModelProperty(value = "角色")
    private Integer role;

    @ApiModelProperty(value = "地区编号")
    private Integer areaCode;

    @ApiModelProperty("开始时间")
    private String starTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ErpWxUserQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }
}
