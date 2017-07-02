package com.jm.repository.po.shop;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>实体门店</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/8/12
 */
@Data
@Entity
@ApiModel(description = "实体门店")
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeId;
    
    @ApiModelProperty(value = "归属于该店铺")
    private Integer shopId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;
    
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;
    
    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "省地区代码")
    private String provinceCode;
    
    @ApiModelProperty(value = "市")
    private String city;
    
    @ApiModelProperty(value = "市地区代码")
    private String cityCode;
    
    @ApiModelProperty(value = "区")
    private String district;
    
    @ApiModelProperty(value = "区地区代码")
    private String districtCode;
    
    @ApiModelProperty(value = "详细地址")
    private String address;
    
    @ApiModelProperty(value = "营业时间  开始时间")
    private String officeHoursStart;
    
    @ApiModelProperty(value = "营业时间  结束时间")
    private String officeHoursEnd;
    
    @ApiModelProperty(value = "门店照片")
    private String storeImg;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "微信号")
    private String wxnum;

    @ApiModelProperty(value = "QQ邮箱")
    private String qqmail;



}
