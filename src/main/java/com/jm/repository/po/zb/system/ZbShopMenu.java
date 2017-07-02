package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>总部软件版本菜单</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2017/04/24
 */
@Data
@Entity
public class ZbShopMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "资源ID")
    private Integer resourceId;

}

