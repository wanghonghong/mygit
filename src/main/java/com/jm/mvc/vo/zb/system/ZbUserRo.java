package com.jm.mvc.vo.zb.system;

import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbPost;
import com.jm.repository.po.zb.user.ZbRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class ZbUserRo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "性别:1男2女0未知")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    private String staffCode; //员工号

    private String newStaffCode; //新增的员工号

    private Integer department; //部门Id
    private String departmentName; //部门

    @ApiModelProperty(value = "岗位Id")
    private Integer post;

    @ApiModelProperty(value = "岗位")
    private String postName;

    private Date createDate;

    private List<ZbDepartment> zbDepartmentList;

    private List<ZbPost> zbPostList;

    private List<ZbRole> zbRoleList;

    @ApiModelProperty(value = "角色Id")
    private Integer roleId;

}
