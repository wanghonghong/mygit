package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author Administrator
 * @version latest
 * @date 2016/10/12
 */
@Data
public class OrderDeliveryForUpdateVo {

    @ApiModelProperty(value = "发货备注")
    private String deliveryNote;
}
