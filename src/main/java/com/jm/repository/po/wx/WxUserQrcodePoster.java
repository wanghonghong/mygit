package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户二维码海报表</p>
 *
 */
@Data
@Entity
public class WxUserQrcodePoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//此id是根据关注公众号的微信用户而产生的序列id

    private Integer  wxUserQrcodeId;

    @ApiModelProperty(value = "二维码海报地址")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "底板编号")
    private Integer qrcodePosterId;
/*  微信媒体库3天清除数据
    @ApiModelProperty(value = "微信媒体Id")
    private String mediaId;*/

}
