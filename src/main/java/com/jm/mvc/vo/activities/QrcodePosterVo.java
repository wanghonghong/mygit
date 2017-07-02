package com.jm.mvc.vo.activities;

import com.jm.mvc.vo.order.OrderDetailCreateVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>二维码创建Vo</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/22
 */
@Data
@ApiModel(description = "二维码创建Vo")
public class QrcodePosterVo {

    @ApiModelProperty(value = "二维码列表")
    private List<QrcodePosterForCreateVo> qrcodePosters;

    @ApiModelProperty(value = "保存的类型 0:商家二维码  1：聚客二维码")
    private int type;


}
