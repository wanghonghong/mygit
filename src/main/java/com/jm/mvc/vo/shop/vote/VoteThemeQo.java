package com.jm.mvc.vo.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * zx
 */
@Data
@ApiModel(description = "投票选项")
public class VoteThemeQo {

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "平台")
    private int platform;//0为微信，1为微博

    @ApiModelProperty(value = "投票主题名称")
    private String name;

    @ApiModelProperty("投票类型  1 图片 2 视频 3 音频")
    private Integer voteType;

    @ApiModelProperty(value = "状态 1未发布  2 投票进行中 3 投票结束 9下架")
    private int status;


    @ApiModelProperty("投票开始时间查询起始时间")
    private Date beginStartTime;

    @ApiModelProperty("投票开始时间查询截止时间")
    private Date endStartTime;

    @ApiModelProperty("投票结束时间查询起始时间")
    private Date beginEndTime;

    @ApiModelProperty("投票结束时间查询截止时间")
    private Date endEndTime;


    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    public VoteThemeQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }


}
