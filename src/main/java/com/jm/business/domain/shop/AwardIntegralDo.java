package com.jm.business.domain.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>积分奖励DO</p>
 *
 * @version latest
 * @Author wukf
 * @Date 2016/10/10
 */
@Data
@ApiModel(description = "积分奖励")
public class AwardIntegralDo {

    private Integer userId;

    private Integer shopId;

    @ApiModelProperty(value = "奖励类型：1 登录 2 推荐关注 3 购买返利")
    private Integer integralType;

    private Integer upOneUserId;

    private Integer upTwoUserId;

    private String nickname;

    private Integer buyMoney;

}
