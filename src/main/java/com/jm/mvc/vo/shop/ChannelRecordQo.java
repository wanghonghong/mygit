package com.jm.mvc.vo.shop;

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
public class ChannelRecordQo {

    private String phoneNumber;

    private String userName;

    @ApiModelProperty(value = "渠道类型 1：代理商a，2代理商b，3代理商c，4代理商d，5：分销代理a 6：分销代理b，7分销代理c")
    private Integer agentRole;

    private String nickname;

    @ApiModelProperty(value="用户角色")
    private int userRole;

    @ApiModelProperty(value = "缴费时间")
    private Date startDate;

    @ApiModelProperty(value = "缴费时间")
    private Date endDate;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public ChannelRecordQo(){
        this.pageSize=10;
        this.curPage=0;
    }

}
