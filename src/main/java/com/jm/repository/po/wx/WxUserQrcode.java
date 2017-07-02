package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户二维码表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/15
 */
@Data
@Entity
public class WxUserQrcode {

    @Id
    @ApiModelProperty(value = "用户编码")
    private Integer userId;

    @ApiModelProperty(value = "二维码类型 0:临时  1：永久")
    private int type;

    @ApiModelProperty(value = "基础二维码地址   type=1")
    private String baseQrcode;

    @ApiModelProperty(value = "基础二维码过期时间")
    private Integer expiresAt;

    @ApiModelProperty(value = "乐享图文二维码海报  type=2")
    private String imgTextQrcode;

    @ApiModelProperty(value = "二维码海报")
    private String userQrcode;

    @ApiModelProperty(value = "内容营销海报")
    private String contentQrcode;

}
