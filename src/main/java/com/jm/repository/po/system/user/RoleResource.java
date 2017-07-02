package com.jm.repository.po.system.user;

import lombok.Data;

import javax.persistence.*;

/**
 * <p>角色资源表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity
public class RoleResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer roleId;

    private Integer resourceId;

    private Integer softId;

}
