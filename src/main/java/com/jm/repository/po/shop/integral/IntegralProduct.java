package com.jm.repository.po.shop.integral;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@Entity
@ApiModel(description = "积分商品")
public class IntegralProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ApiModelProperty(value = "商品ID")
    private Integer pid;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "0 全额抵扣 1 是 限额抵扣")
    private Integer type;
    
    @ApiModelProperty(value = "抵扣积分")
    private Integer integral;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public IntegralProduct(){
        this.createTime = new Date();
    }

}
