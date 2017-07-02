package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/6/12/012
 */
@Data
@Entity
@ApiModel(description = "店铺资源分组")
public class ShopResGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "分组组名")
    private String groupName;

    @ApiModelProperty(value = "资源类型：图片1，视频2，语音3")
    private Integer resType;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "组标识/ N:不可编辑 Y:可编辑")
    private String groupFlag;

}
