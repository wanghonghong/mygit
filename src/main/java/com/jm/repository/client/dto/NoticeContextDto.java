package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by ME on 2016/11/16.
 */
@Data
@ApiModel(description = "总部通知具体信息dto")
public class NoticeContextDto {

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告内容")
    private String noticeContext;

    @ApiModelProperty(value = "发布时间")
    private Date createDate;

}
