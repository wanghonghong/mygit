package com.jm.repository.po.system.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>角色表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;

    private Integer type;

    private Integer softId;

}
