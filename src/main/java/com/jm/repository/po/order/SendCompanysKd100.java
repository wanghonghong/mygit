package com.jm.repository.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>物流信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/10/14
 */
@Data
@Entity
@ApiModel(description = "物流信息")
public class SendCompanysKd100 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "物流信息")
    private Integer send_company_id;

    @ApiModelProperty(value = "物流公司字母分类")
    private String letter;

    @ApiModelProperty(value = "物流公司code")
    private String code;

    @ApiModelProperty(value = "物流公司名称")
    private String name;

    @ApiModelProperty(value = "物流公司备注")
    private String remark;
}
