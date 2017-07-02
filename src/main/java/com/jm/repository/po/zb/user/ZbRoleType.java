package com.jm.repository.po.zb.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>角色类型表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2017/4/6
 */
@Data
@Entity
public class ZbRoleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String typeName;

    private Integer userId;//创建人id

    private Date createDate;

    public ZbRoleType(){
        this.createDate = new Date();
    }

}

