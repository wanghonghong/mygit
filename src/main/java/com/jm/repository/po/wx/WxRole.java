package com.jm.repository.po.wx;

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
 * @date 2016/5/15
 */
@Data
@Entity
public class WxRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleName;

    private Integer level;

}
