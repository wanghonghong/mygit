package com.jm.repository.po.wx;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>用户角色表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/15
 */
@Data
@Entity
public class WxUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private String appId;
    
    private Integer level;

}
