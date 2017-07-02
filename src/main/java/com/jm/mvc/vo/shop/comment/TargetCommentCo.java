package com.jm.mvc.vo.shop.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by zx on 2017/5/2.
 */
@Data
@ApiModel(description = "图文评论")
public class TargetCommentCo {

    @ApiModelProperty("目标id")
    private Integer targetId;

    @ApiModelProperty("评论类型 1图文")
    private  int targetType;
    @ApiModelProperty("评论")
    private String comment;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("预置排序")
    private int sort;

    @ApiModelProperty("父节点评论id")
    private  Integer pid;

    @ApiModelProperty(value = "0上架  9删除 ")
    private int status;

    @ApiModelProperty(value = "用户ID")
    private int userId;
}
