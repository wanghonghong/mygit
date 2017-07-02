package com.jm.repository.po.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>图文消息</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/9/9
 */
@Data
@Entity
@ApiModel(description = "图文消息")
public class ImgTextMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgTextId;
    private int type;
    private int id;
    private Date createTime;
    private String openid;
    private String appid;

}
