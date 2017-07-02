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
public class ImageCommentQo {

    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty("图文标题")
    private String imageTextTile;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("图文查询起始时间")
    private Date beginCreateTime;

    @ApiModelProperty("投图文查询截止时间")
    private Date endCreateTime;


    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
    public ImageCommentQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
