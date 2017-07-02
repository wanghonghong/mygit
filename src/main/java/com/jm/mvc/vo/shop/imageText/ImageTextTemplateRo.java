package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Date;

/**
 * <p>h5模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 10:19
 */
@Data
@ApiModel(description = "h5模板")
public class ImageTextTemplateRo {
    private Integer id;
    @Lob
    @ApiModelProperty(value = "H5模板详情内容")
    private String detailJson;

    @ApiModelProperty(value = "二维码地址")
    private String qrcodeUrl;
}
