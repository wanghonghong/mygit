package com.jm.mvc.vo.join;

import com.jm.repository.po.system.JmJoinCheck;
import com.jm.repository.po.system.user.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>加盟Vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/11/1
 */
@Data
@ApiModel(description = "加盟Qo")
public class JoinVo {

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer status;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("加盟子类型：1、个人加盟 2、公司加盟")
    private Integer subType;

    @ApiModelProperty("申请角色Id")
    private Integer applyRoleId;

    @ApiModelProperty("申请角色")
    private String applyRole;

    @ApiModelProperty("联系姓名")
    private String userName;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("联系邮箱")
    private String email;

    @ApiModelProperty("业绩")
    private String performance;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty("公司姓名")
    private String companyName;

    @ApiModelProperty("营业执照")
    private String businessLicense;

    @ApiModelProperty("经营人数")
    private Integer manageNum;

    @ApiModelProperty("公司网址")
    private String companyUrl;

    @ApiModelProperty("公司简介")
    private String companyDesc;

    @ApiModelProperty(value="审核时间")
    private String checkTime;

    @ApiModelProperty(value="审核人名字")
    private String checkerName;

    @ApiModelProperty(value="审核人Id")
    private Integer checkerId;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "角色列表采集")
    private List<Role> roles;

    @ApiModelProperty("审核建议")
    private List<JmJoinCheck> checkList;

    @ApiModelProperty("审核内容")
    private String checkContext;

}
