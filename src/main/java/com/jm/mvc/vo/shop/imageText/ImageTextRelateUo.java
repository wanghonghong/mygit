package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/12 17:09
 */
@Data
public class ImageTextRelateUo {

    private Integer id;

    @ApiModelProperty("按钮名称")
    private String buttonName;

    @ApiModelProperty("跳链地址")
    private String linkPath;
}
