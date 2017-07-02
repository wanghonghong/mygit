package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class ReviewUo {


    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("被审核人")
    private Integer joinId;

    @ApiModelProperty("审批人")
    private Integer userId;

    @ApiModelProperty("审核内容")
    private String checkContext;

    @ApiModelProperty("审核时间")
    private Date checkTime;

}
