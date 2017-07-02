package com.jm.mvc.vo.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by zx on 2017/3/30.
 */
@Data
@ApiModel(description = "投票主题和投票选项的关联表")
public class VoteRelVo {

    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty(value = "投票主题id")
    private Integer themeId;

    @ApiModelProperty(value = "投票选项id")
    private Integer itemId;

    @ApiModelProperty(value = "投票数")
    private Integer votesNum;

    @Lob
    @ApiModelProperty("详情内容")
    private String detail;

    @ApiModelProperty(value = "投票主题名称")
    private String name;



    @ApiModelProperty("投票类型  1 图片 2 视频 3 音频")
    private Integer voteType;

    @ApiModelProperty("资源类型")
    private  Integer resType;

    @ApiModelProperty("资源名称")
    private  String resName;

    @ApiModelProperty("资源路径")
    private  String resUrl;


}
