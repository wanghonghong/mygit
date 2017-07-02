package com.jm.mvc.vo.zb.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * Created by ME on 2016/8/17.
 */
@Data
public class NoticeCo {

    @ApiModelProperty(value = "公告状态:1、未发布（存草稿箱） 2、已发布")
    private Integer status;

    @ApiModelProperty(value = "公告查看人类别:1、总部人员 2、渠道商")
    private Integer lookType;

    @ApiModelProperty(value = "发布人id")
    private Integer userId;

    @ApiModelProperty(value = "公告类别：1、系统公告 2、其他公告")
    private Integer type;

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告内容")
    private String noticeContext;

    @ApiModelProperty(value = "可查看公告的部门列表")
    private String departments;

    @ApiModelProperty(value = "可查看公告的渠道商列表")
    private String joins;

}
