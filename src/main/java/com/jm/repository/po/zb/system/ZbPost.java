package com.jm.repository.po.zb.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>岗位表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/10/11
 */
@Data
@Entity
public class ZbPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String postName;

    private Integer userId;//创建人id

    private Date createDate;

    public ZbPost(){
        this.createDate = new Date();
    }
}

