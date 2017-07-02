package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Data;

import java.util.Date;

/**
 * <p>运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 15:50
 */

@Data
@Entity
@ApiModel(description = "运费模板")
public class TransTemplates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "模板标识")
	private Integer templatesId;

	@ApiModelProperty(value = "模板名称")
	private String templatesName;

	@ApiModelProperty(value = "商家Id")
	private Integer shopId;

	@ApiModelProperty(value = "新增时间")
	private Date creatTime;

	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	 
}
