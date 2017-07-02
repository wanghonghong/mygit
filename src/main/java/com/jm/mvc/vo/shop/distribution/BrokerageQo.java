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
@ApiModel(description = "佣金清单列表查询")
public class BrokerageQo {

    private String phoneNumber;

    private String userName;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博  -1 全部

    private String nickname;

    @ApiModelProperty(value = "提现时间")
    private Date startDate;

    @ApiModelProperty(value = "提现时间")
    private Date endDate;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "0:佣金余额为0的客户,1:发放客户，2:跑路粉丝")
    private Integer type;

    @ApiModelProperty(value = "帐号类型： 1 佣金帐号 2 积分")
    private int accountType;

    public BrokerageQo(){
        this.pageSize=10;
        this.curPage=0;
    }
}
