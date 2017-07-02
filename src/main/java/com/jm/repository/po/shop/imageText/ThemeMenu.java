package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>主题菜单H5</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 10:09
 */
@Data
@Entity
@ApiModel(discriminator = "主题菜单H5")
public class ThemeMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int pid;

    @ApiModelProperty(value = "H5模板详情内容")
    private String menuName;

    @ApiModelProperty("是否生效 Y/N")
    private String isValid;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;
}
