package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

import java.util.Date;
@Data
@Entity
@ApiModel(discriminator = "点赞记录表")
public class DzUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("点赞用户openid")
    private String openId;
    @ApiModelProperty("图文id")
    private Integer imageTextId;
    @ApiModelProperty("appid")
    private String appId;
    @ApiModelProperty("时间")
    private Date createDate;

}
