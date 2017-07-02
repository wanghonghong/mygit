package com.jm.repository.po.zb.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>用户角色表</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/8/23/023
 */
@Data
@Entity
public class ZbUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer roleId;
}
