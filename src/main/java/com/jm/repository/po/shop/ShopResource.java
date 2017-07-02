package com.jm.repository.po.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>店铺管理</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6/006
 */
@Data
@Entity
@ApiModel(description = "店铺资源")
public class ShopResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺标识")
    private Integer shopId;

    @ApiModelProperty(value = "资源类型：图片1，视频2，语音3，5是其他图")
    private Integer resType;

    @ApiModelProperty(value = "资源类型：腾讯视频0，优酷视频1")
    private int resSubType;

    @ApiModelProperty(value = "资源地址")
    private String resUrl;

    @ApiModelProperty(value = "资源分组标识 /默认0")
    private Integer resGroupId;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;//0不删除，1删除

    @ApiModelProperty(value = "资源名")
    private String resName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public ShopResource(){
        this.createTime = new Date();
    }
}
