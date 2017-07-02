package com.jm.mvc.vo.shop;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@ApiModel(description = "实体门店")
public class ShopEntityForQueryVo {
	
    
    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    public ShopEntityForQueryVo(){
    	this.curPage = 0;
    	this.pageSize = 20;
    }
    
}
