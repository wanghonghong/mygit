package com.jm.repository.po.shop;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>店铺管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@Entity
@ApiModel(description = "店铺")
public class Shop implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "主营项目")
    private Integer shopType;

    private String appId;

    @ApiModelProperty(value = "微博商家用户id （ cj 2017-03-04 ）")
    private Long  wbUid;

    private String appSecret;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;
    
    @ApiModelProperty(value = "经营地址")
    private String addr;
    
    @ApiModelProperty(value = "具体地址")
    private String specificAddr;
    
    @ApiModelProperty(value = "主图地址")
    private String imgUrl;
    
    @ApiModelProperty(value = "联系人")
    private String  linkMan;
    
    @ApiModelProperty(value = "微信账号")
    private String wxNum;
    
    @ApiModelProperty(value = "QQ邮箱")
    private String qqMail;
    
    @ApiModelProperty(value = "分享语1")
    private String shareLan1;
    
    @ApiModelProperty(value = "分享语2")
    private String shareLan2;
    
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
    
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    private Integer tempId;
    
    @ApiModelProperty(value = "创建的状态   0为已创建成功   1为预创建（未创建完成）")
    private Integer status;

    @ApiModelProperty(value = "项目图文手机端列表展示（1：仿微信 2：画风版）")
    private Integer showFormatXm;

    @ApiModelProperty(value = "乐享图文手机端列表展示（1：仿微信 2：画风版）")
    private Integer showFormatLx;

    @ApiModelProperty(value = "培训通知手机端列表展示（1：仿微信 2：画风版）")
    private Integer showFormatPx;

    @ApiModelProperty(value = "企业认证")
    private int companyAuth;

    @ApiModelProperty(value = "个人认证 0:未认证 1：已认证")
    private int userAuth;

    @ApiModelProperty(value = "聚米优商认证  0:未认证 1：已认证")
    private int jmAuth;

    @ApiModelProperty(value = "无假货承诺 0:未认证 1：已认证")
    private int promise;

    @ApiModelProperty(value = "7天无理由退换 0:未认证 1：已认证")
    private int exchange;

    @ApiModelProperty(value = "官方直营 0:未认证 1：已认证")
    private int directSell;

    @ApiModelProperty(value = "平安品质保险 0:未认证 1：已认证")
    private int safety;

    @ApiModelProperty(value = "门店   1 有  0无")
    private int isEntity;

    /*@ApiModelProperty(value = "店铺二维码海报ID")
    private Integer shopQrcode;*/

    @ApiModelProperty(value = "分销   0未开通  1开通")
    private int isOpen;

    @ApiModelProperty("是否关注推送/0:关闭 1:开始")  //chenjun 2016-10-08
    private Integer isSubscribePush;
    @ApiModelProperty("同个订单多商品出现免运费产品，该单免运费")
    private Integer transCondition0ne;
    @ApiModelProperty("同个订单多商品使用统一模板，只收最高那件商品的运费金额")
    private Integer transConditionTwo;
    @ApiModelProperty("同个订单多商品使用<不同运费模板>,只收最高那商品的运费")
    private Integer transConditionThree;
    @ApiModelProperty("店铺状态 0：开启  1：关闭 ")
    private int shopStatus;

    @ApiModelProperty("软件版本 1：电商类  2：预定 ")
    private Integer softId;

    public Shop(){
        this.createDate = new Date();
        this.status = 1;
        this.directSell =1;
        this.promise = 1;
        this.exchange = 1;
    }
    
}
