package com.jm.mvc.vo.shop.resource;

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
 * @Date 2016/8/13 9:37
 */
@Data
public class ShopResGroupCo {

    @ApiModelProperty(value = "分组组名")
    private String groupName;
    @ApiModelProperty(value = "资源类型：图片1，视频2，语音3")
    private Integer resType;

}
