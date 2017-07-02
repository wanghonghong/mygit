package com.jm.mvc.vo.zb.dispatch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ME on 2016/11/16.
 */
@Data
@ApiModel(description = "总部派单Vo")
public class ZbDispatchVo {

    @ApiModelProperty(value = "派单id")
    private Integer Id;

    @ApiModelProperty(value = "需求或投诉类型：1、客服增单 2、初次使用 3、软件模块 4、内容服务 5、功能使用 6、服务态度 7、费用疑问 8、新增需求")
    private Integer subType;

    @ApiModelProperty(value = "订单状态：1、等待派单 2、处理中 3、申请改派 4、已回单")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "商家姓名")
    private String businessName;

    @ApiModelProperty(value = "联系手机")
    private String phoneNumber;

    @ApiModelProperty(value = "公司姓名")
    private String companyName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "需求留言")
    private String need;

}
