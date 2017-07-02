package com.jm.mvc.vo.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Date;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(description = "问卷调查活动")
public class QuestionActivitysVo {

    private Integer id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty("主图")
    private String imageUrl;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "限制人群：1，男 2，女   以逗号分隔")
    private String limitSex;

    @ApiModelProperty(value = "奖励类型  0积分 1礼券 2礼品，用逗号拼接")
    private  String awardType;

    @Lob
    @ApiModelProperty("限制地区")
    private String area;

    @Lob
    @ApiModelProperty("限制地区code")
    private String areaCode;

    @ApiModelProperty(value = "限定人数")
    private int limitCount;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

}
