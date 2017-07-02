package com.jm.mvc.vo.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * zx.
 */
@Data
@ApiModel(description = "投票主题和投票选项的关联表")
public class VoteRelQo {

    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty(value = "投票主题id")
    private Integer themeId;

    @ApiModelProperty(value = "投票选项id")
    private Integer itemId;

    @ApiModelProperty(value = "投票数")
    private Integer votesNum;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public VoteRelQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }

}
