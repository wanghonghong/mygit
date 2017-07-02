package com.jm.mvc.vo.shop.shopSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>模板配置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "会员中心设置")
public class UserCenterConfigUo {

    @ApiModelProperty(value = "主键id")
    private int id;

    @ApiModelProperty(value = "会员背景图片")
    private String topPic;

    @ApiModelProperty(value = "尊享语")
    private String topName;

    @ApiModelProperty(value = "选中项的id，通过','分割")
    private String funItems;

}
