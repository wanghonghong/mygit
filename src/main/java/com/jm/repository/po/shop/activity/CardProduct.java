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
 * <p>卡卷商品关联</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/25
 */
@Data
@Entity
@ApiModel(description = "卡卷商品")
public class CardProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ApiModelProperty(value = "卡卷ID")
    private Integer cardId;

    @ApiModelProperty(value = "商品ID")
    private Integer pid;

}
