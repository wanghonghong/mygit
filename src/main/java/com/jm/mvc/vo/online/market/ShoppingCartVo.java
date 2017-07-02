package com.jm.mvc.vo.online.market;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>客服营销中主动营销下的购物车</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/11/9
 */

@Data
public class ShoppingCartVo {

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "微信头像")
    private String headimgurl;

    @ApiModelProperty(value = "微信昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "关注时间")
    private String appid;

    @ApiModelProperty(value = "用户的标识，对当前公众号唯一")
    private String openid;

    //每个用户下的购物车列表和兴趣单列表
    private List<ShoppingCartListVo> shoppingCartListVoList;

}
