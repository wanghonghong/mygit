package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>报名配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/8 17:37
 */
@Data
@Entity
@ApiModel(description = "报名配置")
public class EnrolmentConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "配置标题")
    private String titleName;

    @ApiModelProperty(value = "配置")
    private String setInfo;

    @ApiModelProperty(value = "店铺")
    private Integer shopId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "编辑时间")
    private Date updateDate;

    @ApiModelProperty(value = "0:使用中 1：删除")
    private int isValid;

}
