package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>派单历史表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/11/18
 */
@Data
@Entity
public class ZbDispatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "派单Id")
    private Integer dispatchId;

    @ApiModelProperty(value = "派单类型：1、业务派单 2、投诉派单 ")
    private Integer type;

    @ApiModelProperty(value = "派单负责渠道商Id")
    private Integer joinId;

    @ApiModelProperty(value = "渠道商姓名")
    private String userName;

    @ApiModelProperty(value = "渠道商联系手机")
    private String phoneNumber;

    @ApiModelProperty(value = "渠道商公司姓名")
    private String companyName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "角色等级")
    private String roleName;

    @ApiModelProperty(value = "总部派单客服id")
    private Integer checkId;

    @ApiModelProperty(value = "受理结果")
    private String result;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public ZbDispatchHistory(){
        this.createDate = new Date();
    }
}

