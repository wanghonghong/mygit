package com.jm.repository.po.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>派单表</p>
 *
 * @author whh
 * @version 1.1
 * @date 2016/11/16
 */
@Data
@Entity
public class ZbDispatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "派单类型：1、业务派单 2、投诉派单 ")
    private Integer type;

    @ApiModelProperty(value = "订单来源：1、客服增单 2、电子申请 ")
    private Integer source;

    @ApiModelProperty(value = "需求或投诉类型：1、客服增单 2、初次使用 3、软件模块 4、内容服务 5、功能使用 6、服务态度 7、费用疑问 8、新增需求")
    private Integer subType;

    @ApiModelProperty(value = "订单状态：1、等待派单 2、处理中 3、申请改派 4、已回单 5、已通过")
    private Integer status;

    @ApiModelProperty(value = "商家姓名")
    private String businessName;

    @ApiModelProperty(value = "联系手机")
    private String phoneNumber;

    @ApiModelProperty(value = "公司姓名")
    private String companyName;

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

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

    @ApiModelProperty(value = "派单负责渠道商Id")
    private Integer joinId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public ZbDispatch(){
        this.createDate = new Date();
    }
}

