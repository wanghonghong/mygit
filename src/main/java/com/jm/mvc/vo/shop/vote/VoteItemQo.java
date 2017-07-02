package com.jm.mvc.vo.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Date;

/**
 * zx
 */
@Data
@ApiModel(description = "投票选项")
public class VoteItemQo {

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "平台")
    private int platform;//0为微信，1为微博

    @ApiModelProperty(value = "选项名称")
    private String name;

    @ApiModelProperty("投票类型  1 图片 2 视频 3 音频")
    private Integer voteType;

    @ApiModelProperty("创建时间查询起始时间")
    private Date beginCreateTime;

    @ApiModelProperty("创建时间查询截止时间")
    private Date endCreateTime;


    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "状态 0正常  9删除")
    private int status;

    public VoteItemQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }
}
