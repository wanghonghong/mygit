package com.jm.mvc.vo.zb.system;

import com.jm.repository.po.system.user.Role;
import lombok.Data;

import java.util.List;

@Data
public class ZbSoftRoleResVo {

    private Integer roleId;

    private String resourceIds;

    private Integer softId;
}
