package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>佣金发放设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "佣金发放设置")
public class PutSetVo {

    @ApiModelProperty(value = "主键id")
    private int id;

    @ApiModelProperty(value = "设置类型：1微信，2微博，3其他")
    private int payType;

    @ApiModelProperty(value = "发放类型：1手动，2提现")
    private int putType;

    @ApiModelProperty(value = "0:手动发放1:满200,2:定期发放,3:满额发放,4免审核,5需审核")
    private int autoType;

    @ApiModelProperty(value = "提现类型：0：未设置 1可提现")
    private int cashKit;

    @ApiModelProperty(value = "提现类型：0：未设置 1可提现")
    private int integralKit;

    @ApiModelProperty(value = "定期发放时间")
    private String sendTime;

    @ApiModelProperty(value = "满额发放")
    private int fullPut;

    @ApiModelProperty(value = "最低限额")
    private int minMoney;

    @ApiModelProperty(value = "提现次数,0:次数不限制")
    private int kitNum;

}
