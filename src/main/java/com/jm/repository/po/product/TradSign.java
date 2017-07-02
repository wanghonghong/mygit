package com.jm.repository.po.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * <p>商品类型</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@Entity
@ApiModel(description = "交易模式角标")
public class TradSign implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键id")
    private Integer  id;

    @ApiModelProperty(value = "交易模式类型 1:一元夺宝   2：拼团 3.秒杀  4.拍卖  5.众筹")
    private int  type;

    @ApiModelProperty(value = "角标名称")
    private String  name;

    @ApiModelProperty(value = "图片地址")
    private String  imageUrl;

    @ApiModelProperty(value = "图片类型：0固定图片  1上传图片")
    private int  imgType;

}
