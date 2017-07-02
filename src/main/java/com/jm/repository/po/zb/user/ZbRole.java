package com.jm.repository.po.zb.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>角色表</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/8/23/023
 */
@Data
@Entity
public class ZbRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roleName;

    @ApiModelProperty(value = "角色类型")
    private Integer roleType;

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    private Date createDate;

    public ZbRole(){
        this.createDate = new Date();
    }

}

