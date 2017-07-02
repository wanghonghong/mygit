package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 15:05
 */
@Data
public class ImageTextTypeRo {
    private Integer id;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("分享引导语")
    private String shareText;

    @ApiModelProperty("分类主图")
    private String imageUrl;

    @ApiModelProperty("排序")
    private  Integer sort;

    @ApiModelProperty("1:不可编辑分类名称")
    private Integer isEdit;

    @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
    private  Integer articleType;
}
