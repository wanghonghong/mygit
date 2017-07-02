package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>佣金收费配置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "提现申请表")
public class WxAccountKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "用户id")
    private int userId;

    @ApiModelProperty(value = "平台：0微信，1微博")
    private int platForm;

    @ApiModelProperty(value = "提现类型：1佣金，2积分")
    private int type;

    @ApiModelProperty(value = "申请提现时间")
    private Date kitDate;

    @ApiModelProperty(value = "提现金额")
    private int kitMoney;

    @ApiModelProperty(value = "状态0：待审核，1待下次审核，2，审核通过")
    private int status;

}
