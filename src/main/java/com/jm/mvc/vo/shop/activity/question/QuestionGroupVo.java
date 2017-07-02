package com.jm.mvc.vo.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(description = "问卷分组")
public class QuestionGroupVo {

    @ApiModelProperty(value = "分组类型ID")
    private Integer  id;

    @ApiModelProperty(value = "分组名称")
    private String  groupName;

}
