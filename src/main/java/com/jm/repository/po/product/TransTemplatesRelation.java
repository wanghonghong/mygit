package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>运费模板</p>
 *
 * @author zhengww
 * @version latest
 * @date 2016/4/25
 */

@Data
@Entity
@ApiModel(description = "运费模板关联表")
public class TransTemplatesRelation {

	 @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
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
