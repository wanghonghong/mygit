package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>回收、公益网点</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class RecycleNodeVo {

    private Integer id;

    @ApiModelProperty("店铺Id")
    private Integer shopId;

    @ApiModelProperty("网点名字")
    private String name;

    @ApiModelProperty("网点图片")
    private String imgUrl;

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

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("营业时间 开始时间")
    private String businessTimeBegin;

    @ApiModelProperty("营业时间 结束时间")
    private String businessTimeEnd;

    @ApiModelProperty("type 0:回收箱，1：营业网点, 2:公益网点")
    private int type;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("联系人姓名")
    private String linkUser;

    @ApiModelProperty("联系人手机")
    private String linkPhone;

    @ApiModelProperty("公益网点设立时间")
    private Date publicNetworkDate;

    @ApiModelProperty(value = "公益网点编辑说明")
    private String instruction;

    @ApiModelProperty("status 0：下架 1：上架")
    private int status;

    @ApiModelProperty("回收标题")
    private String title;

    @ApiModelProperty("回收简介")
    private String node;

}
