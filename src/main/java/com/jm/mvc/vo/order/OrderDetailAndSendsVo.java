package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/23
 */

@Data
public class OrderDetailAndSendsVo {

    @ApiModelProperty(value = "订单标识")
    private Long orderInfoId;

    @ApiModelProperty(value = "物流信息")
    private List<SendsVo> sendsVoList;
}
