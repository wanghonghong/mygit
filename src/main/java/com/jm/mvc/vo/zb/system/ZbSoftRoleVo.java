package com.jm.mvc.vo.zb.system;

import com.jm.repository.po.system.user.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ZbSoftRoleVo {

    private Integer softId;

    private List<Role> roles;
}
