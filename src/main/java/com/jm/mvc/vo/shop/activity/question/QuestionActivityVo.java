package com.jm.mvc.vo.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
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
public class QuestionActivityVo {

    private Integer id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty("主图")
    private String imageUrl;

    @ApiModelProperty("分享语")
    private String shareText;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "限制人群：1，男 2，女   以逗号分隔")
    private String limitSex;

    @ApiModelProperty(value = "奖励类型  0积分 1礼券 2礼品，用逗号拼接")
    private  String awardType;

    @ApiModelProperty(value = "积分类型  0固定 1随机")
    private  int integralType;

    @ApiModelProperty(value = " 固定积分数值")
    private  int integralCount;

    @ApiModelProperty(value = "最大积分 ")
    private  int maxIntegral;

    @ApiModelProperty(value = "最小积分 ")
    private  int minIntegral;

    @ApiModelProperty(value = "礼券Id ")
    private  Integer  cardId;

    @ApiModelProperty(value = "积分类型  0全部中奖 1随机部分中奖")
    private  int giftType;

    @ApiModelProperty(value = "中奖人数")
    private  int giftCount;

    @ApiModelProperty(value = "商品id")
    private  int giftId;

    @Lob
    @ApiModelProperty("限制地区")
    private String area;

    @Lob
    @ApiModelProperty("限制地区code")
    private String areaCode;

    @ApiModelProperty(value = "限定人数")
    private int limitCount;

    @ApiModelProperty("答后提交：1姓名 2手机  3收货地址 4邮箱 5微信号 6 qq号")
    private String submit;

    @Lob
    @ApiModelProperty(value = "详情内容")
    private String detailJson;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

}
