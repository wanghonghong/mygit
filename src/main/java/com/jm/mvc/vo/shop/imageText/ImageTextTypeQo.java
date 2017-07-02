/**
 * 
 */
package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>店铺资源</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/8 11:38
 */
@Data
public class ImageTextTypeQo {

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
    private  Integer articleType;

    public ImageTextTypeQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
