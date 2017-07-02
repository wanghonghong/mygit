package com.jm.mvc.vo.shop.distribution;

import com.jm.mvc.vo.order.OrderInfoVo;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * <p>订单信息</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@ApiModel(description = "订单信息")
public class BrokerageOrderInfoVo {

    private OrderInfoVo orderInfo;

    List<BrokerageDetailVo> brokerageDetails;

    @ApiModelProperty(value = "微信头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;
}
