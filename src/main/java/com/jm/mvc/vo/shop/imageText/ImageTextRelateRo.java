package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/12 17:08
 */
@Data
public class ImageTextRelateRo {

    private Integer id;

    @ApiModelProperty("按钮名称")
    private String buttonName;

    @ApiModelProperty("跳链地址")
    private String linkPath;

}
