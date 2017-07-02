package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/12
 */
@Data
@ApiModel(description = "帖子")
public class PostCo {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "主题分类")
    private Integer type;

    @ApiModelProperty(value = "帖子内容")
    private String postContext;

    @ApiModelProperty(value = "是否匿名，1，是，2，否")
    private Integer isHide;

    @ApiModelProperty(value = "类型：1，已发布，2，草稿")
    private Integer status;

}
