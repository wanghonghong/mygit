package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Data
public class AgentDataDto {

    private String userName;

    private String phoneNumber;

    private String applyRole;

    @ApiModelProperty("加盟类型：1、个人加盟 2、公司加盟")
    private Integer type;

    @ApiModelProperty("申请状态：0:未申请审核 1、初次审核  2、二次审核 3、已通过审核")
    private Integer status;

    private String companyName;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;



}
