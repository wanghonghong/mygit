package com.jm.repository.po.zb.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>角色资源表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity

public class ZbRoleResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer roleId;

    private Integer resourceId;

}
