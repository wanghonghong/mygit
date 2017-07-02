package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>图文打赏</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/18 11:32
 */

@Data
@Entity
@ApiModel(discriminator = "图文打赏")
public class ImageTextTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("图文id")
    private Integer imageTextId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("打赏金额(分)")
    private Integer tipMoney;

    @ApiModelProperty("店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "订单流水")
    private String orderNum;

    @ApiModelProperty(value = "状态 0：待付款; 1:已付款")
    private int status;

    @ApiModelProperty("支付id")
    private Long payId;

    @ApiModelProperty(value = "平台：0微信，1微博")
    private int platForm;

    @ApiModelProperty("打赏时间")
    private Date createTime;
    
}
