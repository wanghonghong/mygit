package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/21
 */
@Data
@ApiModel(description = "回复qo")
public class ReplyCo {

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty(value = "回复人Id")
    private Integer userId;

    @ApiModelProperty(value = "回复人头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "回复内容")
    private String context;

    @ApiModelProperty(value = "是否匿名，1，是，2，否")
    private Integer isHide;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ReplyCo() {
        this.curPage = 0;
        this.pageSize = 20;
    }
}
