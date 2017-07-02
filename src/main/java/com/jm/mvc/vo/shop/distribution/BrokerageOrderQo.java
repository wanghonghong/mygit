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
public class BrokerageOrderQo {

    private String phoneNumber;

    @ApiModelProperty(value = "订单信息标识")
    private Long orderNum;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

    private String userName;

    private String nickname;

    private int status;//1佣金清单  2有效佣金

    @ApiModelProperty(value = "订单开始日期")
    private Date startDate;

    @ApiModelProperty(value = "订单结束日期")
    private Date endDate;

    @ApiModelProperty(value = "订单类型：1佣金，2积分")
    private int type;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public BrokerageOrderQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
