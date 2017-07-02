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
public class QuestionActivityQo {

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "创建开始时间")
    private Date startDate;

    @ApiModelProperty(value = "创建结束时间")
    private Date endDate;

    @ApiModelProperty(value = "状态")
    private Integer status;//-1 全部  0未开始 1 进行中 2 已结束

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public QuestionActivityQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
