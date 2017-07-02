package com.jm.repository.po.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>模板配置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "会员中心设置")
public class UserCenterConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(value = "店铺id")
    private int shopId;

    @ApiModelProperty(value = "会员背景图片")
    private String topPic;

    @ApiModelProperty(value = "尊享语")
    private String topName;

    @ApiModelProperty(value = "选中项的id，通过','分割")
    private String funItems;

}
