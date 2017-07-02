package com.jm.repository.po.system.user;

import lombok.Data;

import javax.persistence.*;

/**
 * <p>用户角色表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer roleId;
    
    private Integer shopId;
    
    private Integer level;

    private String hxAccount;

}
