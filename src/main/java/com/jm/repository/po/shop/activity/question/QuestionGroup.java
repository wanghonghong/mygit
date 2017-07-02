package com.jm.repository.po.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@Entity
@ApiModel(description = "分组")
public class QuestionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "分组类型ID")
    private Integer  id;

    @ApiModelProperty(value = "分组名称")
    private String  groupName;

}
