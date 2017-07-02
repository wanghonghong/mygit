package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>报名配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/8 17:37
 */
@Data
@ApiModel(description = "报名配置")
public class EnrolmentConfUo {

    private Integer id;

    @ApiModelProperty(value = "配置标题")
    private String titleName;

    @ApiModelProperty(value = "配置")
    private String setInfo;

    @ApiModelProperty(value = "编辑时间")
    private Date updateDate;

    public EnrolmentConfUo(){
        this.updateDate = new Date();
    }

}
