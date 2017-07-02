package com.jm.mvc.vo.shop.integral;

/**
 * <p>积分奖励清单</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/10/08
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "店铺积分")
public class IntegralRecordVo {

    private Integer id;

    @ApiModelProperty(value = "积分类型：1 登录 2 推荐关注 3 购买返利")
    private Integer integralType;

    @ApiModelProperty(value = "积分数量")
    private int count;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String headimgurl;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "一级微客名称")
    private String oneUserName;

    @ApiModelProperty(value = "二级微客名称")
    private String twoUserName;

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "单位积分")
    private String unitName;
    
}
