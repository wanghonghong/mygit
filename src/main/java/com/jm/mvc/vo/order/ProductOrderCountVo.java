package com.jm.mvc.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>订单</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/9
 */
@Data
@ApiModel(description = "用户购买总数")
public class ProductOrderCountVo {

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "支付日期")
    private Date payDate;

}
