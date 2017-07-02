package com.jm.mvc.vo.zb.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ME on 2016/11/1.
 */
@Data
@ApiModel(description = "总部派单历史查看Qo")
public class DispatchHistoryQo {

    @ApiModelProperty(value = "派单Id")
    private Integer dispatchId;

}
