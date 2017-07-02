package com.jm.repository.po.zb.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>部门表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/10/11
 */
@Data
@Entity
public class ZbDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private String departmentName;

    private Integer userId;//创建人id

    private Date createDate;

    public ZbDepartment(){
        this.createDate = new Date();
    }

}

