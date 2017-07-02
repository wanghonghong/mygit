package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>渠道商类型表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/12/6
 */
@Data
@Entity
public class ZbJoinType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "渠道商名字")
    private String joinName;

}

