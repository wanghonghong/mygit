package com.jm.repository.po.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 会员中心软件版本
 */
@Data
@Entity
@ApiModel(description = "会员中心软件版本")
public class UserCenterVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "软件版本Id")
    private Integer softId;

    @ApiModelProperty(value = "菜单选项Id")
    private Integer funsId;


}
