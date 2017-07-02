package com.jm.mvc.vo.zb.system;

import com.jm.repository.po.zb.user.ZbRoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>角色Vo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class RoleVo {

    private Integer id;

    private String roleName;

    private String typeName;

    @ApiModelProperty(value = "角色类型")
    private Integer roleType;

    private String userName;//创建人名字

    private Date createDate;

    private  List<ZbRoleType>  zbRoleTypes;


}
