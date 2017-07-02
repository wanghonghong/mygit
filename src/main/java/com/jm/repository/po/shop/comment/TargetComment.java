package com.jm.repository.po.shop.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

/**
 * zx
 */
@Data
@Entity
@ApiModel(discriminator = "评论模块")
public class TargetComment {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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

    @ApiModelProperty(value = "点赞人数")
    private  int reward;
}
