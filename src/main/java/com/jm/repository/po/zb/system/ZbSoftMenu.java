package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>总部软件版本菜单</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2017/04/24
 */
@Data
@Entity
public class ZbSoftMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "版本号ID")
    private Integer softId;

    @ApiModelProperty(value = "资源ID")
    private Integer resourceId;

}

