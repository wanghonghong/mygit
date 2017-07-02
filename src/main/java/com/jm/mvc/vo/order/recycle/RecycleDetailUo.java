package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
public class RecycleDetailUo {

    @ApiModelProperty("收货备注")
    private String receiveRemark;

    @ApiModelProperty("客服备注")
    private String customRemark;

}
