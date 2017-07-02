package com.jm.business.domain.shop;

import com.jm.repository.po.wx.WxUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>关注推送领域对象模型</p>
 *
 * @version latest
 * @Author wukf
 * @Date 2016/10/27
 */
@Data
@ApiModel(description = "需要给某个用户推送的信息")
public class SubscribePushDo {

    private Integer userId;

    private String clientIp;

    @ApiModelProperty("推送类型 0.关注推送 1.二维码海报;2.现金红包;3.礼券红包;4.商品列表;5.商品详情;6.图文列表7.图文详情;8.商城首页;9.提示语 10 微信图文")
    private Integer pushType;

    @ApiModelProperty("推送内容")
    private String pushContext;

    @ApiModelProperty(value = "发送时间")
    private Integer pushTime;

    @ApiModelProperty(value = "是否新用户")
    private boolean newUser;
}
