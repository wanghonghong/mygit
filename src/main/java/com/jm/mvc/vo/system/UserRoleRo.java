package com.jm.mvc.vo.system;

import lombok.Data;

import javax.persistence.Entity;

/**
 * <p>用户角色表</p>
 */
@Data
public class UserRoleRo {

    private Integer id;

    private Integer userId;

    private Integer roleId;
    
    private Integer shopId;
    
    private Integer level;

    private String hxAccount;

    private Integer type;

}
