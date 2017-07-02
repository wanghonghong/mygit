package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>登录日志表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/10/11
 */
@Data
@Entity
public class ZbLoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "登录人userId")
    private Integer userId;

    @ApiModelProperty(value = "登录ip")
    private String ip;

    @ApiModelProperty(value = "登录地址")
    private String address;

    @ApiModelProperty(value = "登录工具")
    private String tool;

    @ApiModelProperty(value = "登录时间")
    private Date createDate;

    public ZbLoginLog(){
        this.createDate = new Date();
    }

}

