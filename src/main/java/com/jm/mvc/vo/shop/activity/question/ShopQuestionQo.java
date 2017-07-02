package com.jm.mvc.vo.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class ShopQuestionQo {

    @ApiModelProperty(value = "商家id")
    private Integer shopSignId;

    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "创建开始时间")
    private Date startDate;

    @ApiModelProperty(value = "创建结束时间")
    private Date endDate;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ShopQuestionQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
