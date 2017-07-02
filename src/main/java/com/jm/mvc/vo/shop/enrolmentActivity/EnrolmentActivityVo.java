package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>活动报名</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/8 17:37
 */
@Data
@ApiModel(description = "活动报名")
public class EnrolmentActivityVo {

    private Integer id;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "主图")
    private String img;

    @ApiModelProperty(value = "短信")
    private String sms;

    @ApiModelProperty(value = "通知时间")
    private Date noticeDate;

    @Lob
    @ApiModelProperty(value = "活动详情内容")
    private String detailJson;

    @ApiModelProperty(value = "报名配置id")
    private Integer confId;

    @ApiModelProperty(value = "配置")
    private String setInfo;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "状态 0:未开始 1：执行中 2：已结束 3：删除 ")
    private int status;

    @ApiModelProperty(value = "报名人数")
    private Integer count;


}
