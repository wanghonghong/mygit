package com.jm.mvc.vo.product.trans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * <p>修改运费模板子表配送地区</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 14:50
 */
@Data
public class TransTemplatesRelationUo {

    @ApiModelProperty(value = "可配送地区标识")
    private Integer transId;

    @ApiModelProperty(value = "模板标识")
    private Integer templatesId;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "配送地区")
    private String sendArea;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "配送地区ID")
    private String sendAreaId;

    @ApiModelProperty(value = "首件个数")
    private Integer firstNumber;

    @ApiModelProperty(value = "运费")
    private Integer transFare;

    @ApiModelProperty(value = "续件")
    private Integer nextNumber;

    @ApiModelProperty(value = "续费")
    private Integer nextTransFare;
}
