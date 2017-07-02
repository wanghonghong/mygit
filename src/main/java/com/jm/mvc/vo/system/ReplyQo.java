package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/22
 */
@Data
@ApiModel(description = "帖子回复qo")
public class ReplyQo {


    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public ReplyQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }
}
