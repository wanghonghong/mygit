package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>分销设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "佣金流水列表查询")
public class BrokerageRecordVo {

    @ApiModelProperty(value = "id")
    private int id;

    @ApiModelProperty(value = "用户ID")
    private int userId;

    @ApiModelProperty(value = "发放流水")
    private String sendNum;

    @ApiModelProperty(value = "代理商角色 0：普通用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
    private int agentRole ;

    @ApiModelProperty(value = "头像")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "发放时间")
    private Date sendTime;

    @ApiModelProperty(value = "发放金额")
    private int sendMoney;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博


    @ApiModelProperty(value = "发放方式")
    private int putType;

    @ApiModelProperty(value = "0:手动发放1:满200,2:定期发放,3:满额发放,4免审核,5需审核,6积分提现")
    private int autoType;

    @ApiModelProperty(value = "红包流水ID")
    private Integer redPayId;

    @ApiModelProperty(value = "状态 1：发放中    2：已发放待领取   3：发放失败   4：已领取  5：退款中   6：已退款")
    private int status;
}
