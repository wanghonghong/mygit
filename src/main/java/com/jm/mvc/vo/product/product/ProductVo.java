package com.jm.mvc.vo.product.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@ApiModel(description = "商品")
public class ProductVo {

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "商品名称")
    private String name;
    
    @ApiModelProperty(value = "商品价格")
    private int price;
    
    @ApiModelProperty(value = "商品类型/商品属性")
    private int typeId;

    @ApiModelProperty(value = "商品标签ID")
    private int  tagId;

    @ApiModelProperty(value = "商品视频")
    private String productVideo;

    @ApiModelProperty(value = "商品图片正方形")
    private String picSquare;

    @ApiModelProperty(value = "商品图片长方形")
    private String picRectangle;

    @ApiModelProperty(value = "商品详情图片正方形")
    private String detailSquarePic;

    @ApiModelProperty(value = "商品详情图片长方形")
    private String detailRectanglePic;

    @ApiModelProperty(value = "商品销量")
    private int saleCount;

    @ApiModelProperty(value = "商品数量")
    private int totalCount;
    
    @ApiModelProperty(value = "商品货号")
    private String code;
    
    @ApiModelProperty(value = "运费")
	private int transFare;
    
    @ApiModelProperty(value = "是否启用运费模板1：统一邮费 2：运费模板")
	private int isUseTrans;
    
    @ApiModelProperty(value = "运费标识")
    private int transId;
    
    @ApiModelProperty(value = "商品顺序")
    private int sort;
    
    @ApiModelProperty(value = "是否预售:0不预售，1.预售")
    private int isPerSale;
    
    @ApiModelProperty(value = "预售开始时间")
    private Date perSaleStartTime;
    
    @ApiModelProperty(value = "预售结束时间")
    private Date perSaleEndTime;
    
    @ApiModelProperty(value = "购买限制")
    private String isLimitBuy;

    @ApiModelProperty(value = "是否限购,0不限购，1限购")
    private int isLimitCount;
    
    @ApiModelProperty(value = "限购数量")
    private int limitCount;
    
    @ApiModelProperty(value = "上下架")
    private int status;//0上架  1下架  2售完下架

    @ApiModelProperty(value = "交易类型")
    private int tradeType;//1.标准版，2.拼团版，3.夺宝版（一元购），4.展示板（不可购买），5.限展版（）

    @ApiModelProperty(value = "商品永久二维码地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "当前版本")
    private String curVersion;

    @ApiModelProperty(value = "商品分享语")
    private String share;

    @ApiModelProperty(value = "点赞量")
    private int likes;

    @ApiModelProperty(value = "商品分享语")
    private String detailJson;

    @ApiModelProperty(value = "阅读量")
    private int readCount;

    @ApiModelProperty(value = "是否支持分组购买，0仅支持当前分组，1支持该分组级以上分组可购买")
    private int isGroupBuy;

    @ApiModelProperty(value = "支持够买星级的id---对应wx_user_group的groupid  ")
    private int groupBuyId;

    @ApiModelProperty(value ="是否体验送礼")
    private int gift;

}
