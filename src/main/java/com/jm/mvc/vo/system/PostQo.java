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
public class PostQo {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "类型：1，已发布，2，草稿")
    private Integer status;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "帖子特征:1、强烈推荐 2、热帖 3、精")
    private Integer feature;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public PostQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }
}
