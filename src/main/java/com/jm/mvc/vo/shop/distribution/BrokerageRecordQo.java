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
public class BrokerageRecordQo {

    @ApiModelProperty(value = "用户ID")
    private int userId;

    private String phoneNumber;

    @ApiModelProperty(value = "发放流水")
    private String sendNum;

    @ApiModelProperty(value = "平台")
    private int platForm;//0为微信，1为微博

    private String nickname;

    @ApiModelProperty(value = "发放方式")
    private int putType;

    @ApiModelProperty(value = "发放时间")
    private Date startDate;

    @ApiModelProperty(value = "发放时间")
    private Date endDate;

    private String userName;

    @ApiModelProperty(value = "状态 1：发放中    2：已发放待领取   3：发放失败   4：已领取  5：退款中   6：已退款")
    private int status;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public BrokerageRecordQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
