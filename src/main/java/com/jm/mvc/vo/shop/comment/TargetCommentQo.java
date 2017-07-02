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
public class TargetCommentQo {

    @ApiModelProperty(value = "主键id")
    private Integer Id;
    @ApiModelProperty("目标id")
    private Integer targetId;
    @ApiModelProperty("评论类型 1图文")
    private  int targetType;

    @ApiModelProperty("预置排序")
    private int sort;

    @ApiModelProperty("父节点评论id")
    private  Integer pid;
    @ApiModelProperty("多个父节点评论id")
    private  String pids;

    @ApiModelProperty(value = "0上架  9删除 ")
    private int status;

    @ApiModelProperty(value = "用户ID")
    private int userId;


    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    public TargetCommentQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
