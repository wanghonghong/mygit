package com.jm.mvc.vo.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>派单加盟Vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/11/1
 */
@Data
@ApiModel(description = "加盟Qo")
public class DispatchJoinVo {

    @ApiModelProperty("加盟id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("加盟子类型：1、个人加盟 2、公司加盟")
    private Integer subType;

    @ApiModelProperty("申请角色")
    private String roleName;

    @ApiModelProperty("联系姓名")
    private String userName;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty("公司姓名")
    private String companyName;

}
