package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ME on 2016/11/16.
 */
@Data
@ApiModel(description = "总部通知dto")
public class NoticeDto {

    @ApiModelProperty(value = "通知id")
    private Integer Id;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "公告查看人类别:1、总部人员 2、渠道商")
    private Integer lookType;

    @ApiModelProperty(value = "公告状态:1、未发布（存草稿箱） 2、已发布")
    private Integer status;

    @ApiModelProperty(value = "公告类别：总部公告(1、系统公告 2、其他公告)  渠道商公告：(1、系统公告2、功能使用3、市场政策4、市场活动5、费用结算6、公司动态7、行业动态)")
    private Integer type;

    @ApiModelProperty(value = "公告类型:1、总部通知消息查看 2、已发布消息查看 3、草稿箱消息查看")
    private Integer bigType;

    @ApiModelProperty(value = "发布人userId")
    private Integer userId;

    @ApiModelProperty(value = "查看人部门Id")
    private String departmentId;

    @ApiModelProperty(value = "可查看公告的渠道商列表")
    private String joins;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
}
