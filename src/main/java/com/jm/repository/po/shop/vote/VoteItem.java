package com.jm.repository.po.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zx on 2017/3/30.
 */
@Data
@Entity
@ApiModel(description = "投票选项")
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "平台")
    private int platform;//0为微信，1为微博

    @ApiModelProperty(value = "选项名称")
    private String name;

    @ApiModelProperty("投票类型  1 图片 2 视频 3 音频")
    private Integer voteType;

    @ApiModelProperty("主媒体资源")
    private Integer resId;

    @Lob
    @ApiModelProperty("详情内容")
    private String detail;

    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty(value = "状态 0正常  9删除")
    private int status;

}
