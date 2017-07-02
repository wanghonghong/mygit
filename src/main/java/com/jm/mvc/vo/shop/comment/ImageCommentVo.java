package com.jm.mvc.vo.shop.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by zx on 2017/5/3.
 */
@Data
@ApiModel(description = "图文评论列表")
public class ImageCommentVo {

    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty("图文标题")
    private String imageTextTile;

    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("浏览数")
    private int pv;
    @ApiModelProperty("用户数")
    private int uv;

    @ApiModelProperty("点赞人数")
    private int reward;

    @ApiModelProperty("评论数")
    private int commentNum;



}
