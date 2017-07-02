package com.jm.repository.po.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * <p>链接表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23
 */
@Data
@Entity
@ApiModel(description = "链接")
public class Link {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "链接标识")
    private Integer id;

	@ApiModelProperty(value = "链接名称")
    private String linkName;
    
	@ApiModelProperty(value = "链接地址")
    private String linkUrl;
	
	@ApiModelProperty(value = "父标识")
	private Integer parentId;

    @ApiModelProperty(value = "菜单类型,0 直接返回 ,1 有查询信息")
    private String linkType;

    @ApiModelProperty(value = "链接编码，ptype 商品类型，product 商品")
    private String linkCode;
    
	@ApiModelProperty(value = "链接类型")
    private String type;
    
	@ApiModelProperty(value = "链接key")
    private String linkKey;
    

}
