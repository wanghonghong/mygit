package com.jm.mvc.vo.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>加盟Vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/11/1
 */
@Data
@ApiModel(description = "加盟数据返回总部平台客户中心vo")
public class JoinClassVo {

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

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

    @ApiModelProperty(value = "机构名称")
    private String companyName;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;


}
