package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户卡券</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/11/25
 */
@Data
@Entity
public class WxUserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "活动ID")
    private Integer activityId;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "卡券ID")
    private Integer cardId;

    @ApiModelProperty(value = "到期时间")
    private Date expireTime;

    @ApiModelProperty(value = "使用状态：0未领取,1未使用，2已使用 ,9删除")
    private Integer status;

    @ApiModelProperty(value = "卡券类型：0自己领取，1赠送的")
    private int type;

    @ApiModelProperty(value = "分享人id")
    private Integer shareId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public WxUserCard(){
        this.createTime = new Date();
    }
}
