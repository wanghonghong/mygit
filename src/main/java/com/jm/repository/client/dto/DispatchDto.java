package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ME on 2016/11/16.
 */
@Data
@ApiModel(description = "总部派单dto")
public class DispatchDto {

    @ApiModelProperty(value = "派单类型：1、业务派单 2、投诉派单 ")
    private Integer type;

    @ApiModelProperty(value = "订单来源：1、客服增单 2、电子申请 ")
    private Integer source;

    @ApiModelProperty(value = "需求或投诉类型：1、客服增单 2、初次使用 3、软件模块 4、内容服务 5、功能使用 6、服务态度 7、费用疑问 8、新增需求")
    private Integer subType;

    @ApiModelProperty(value = "订单状态：1、等待派单 2、处理中 3、申请改派 4、已回单")
    private Integer status;

    @ApiModelProperty(value = "商家姓名")
    private String businessName;

    @ApiModelProperty(value = "联系手机")
    private String phoneNumber;

    @ApiModelProperty(value = "公司姓名")
    private String companyName;

    @ApiModelProperty(value = "派单负责渠道商Id")
    private Integer joinId;

    @ApiModelProperty(value = "创建时间开始")
    private Date startTime;

    @ApiModelProperty(value = "创建时间结束")
    private Date endTime;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;
}
