package com.jm.mvc.vo.zb.notice;

import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbJoinType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * Created by ME on 2016/8/17.
 */
@Data
public class NoticeVo {

    private Integer Id;

    @ApiModelProperty(value = "公告类别：1、系统公告 2、其他公告")
    private Integer type;

    @ApiModelProperty(value = "公告标题")
    private String title;

    @ApiModelProperty(value = "公告内容")
    private String noticeContext;

    @ApiModelProperty(value = "发布时间")
    private Date createDate;

    @ApiModelProperty(value = "可查看部门列表")
    private String departments;

    @ApiModelProperty(value = "部们列表")
    private List<ZbDepartment> zbDepartmentList;

    @ApiModelProperty(value = "可查看公告的渠道商列表")
    private String joins;

    @ApiModelProperty(value = "部们列表")
    private List<ZbJoinType> joinList;
}
