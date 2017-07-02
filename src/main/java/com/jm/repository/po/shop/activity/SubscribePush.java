package com.jm.repository.po.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>关注推送</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@Entity
@ApiModel(discriminator = "关注推送")
public class SubscribePush {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("店铺id")
    private Integer shopId;

    @ApiModelProperty("间隔（秒）")
    private Integer intervalSecond;

    @ApiModelProperty("推送类型 0.关注推送 1.二维码海报;2.现金红包;3.礼券红包;4.商品列表;5.商品详情;6.图文列表7.图文详情;8.商城首页;9.提示语 10 微信图文")
    private Integer pushType;

    @ApiModelProperty("推送内容")
    private String pushContext;

    @ApiModelProperty("推送名称")
    private String pushName;

    @ApiModelProperty("总秒数")
    private Integer totalSecond;

    @ApiModelProperty("推送顺序")
    private int sort;

}
