package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>H5模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 10:02
 */
@Data
@Entity
@ApiModel(discriminator = "H5  模板")
public class ImageTextTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @ApiModelProperty(value = "H5模板详情内容")
    private String detailJson;

    @ApiModelProperty("是否生效 Y/N")
    private String isValid;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "主题菜单id")
    private Integer menuId;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "二维码地址")
    private String qrcodeUrl;
}
