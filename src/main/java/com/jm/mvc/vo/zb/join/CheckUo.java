package com.jm.mvc.vo.zb.join;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class CheckUo {

    @ApiModelProperty("被审核人userId")
    private Integer userId;

    @ApiModelProperty("审批人")
    private Integer checkerId;

    @ApiModelProperty("加盟类型：1、代理商 2、服务商")
    private Integer type;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer status;

    @ApiModelProperty("审核意见")
    private String checkContext;

}
