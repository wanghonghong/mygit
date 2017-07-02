package com.jm.mvc.vo.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zx on 2017/3/30.
 */
@Data
@ApiModel(description = "投票主题表")
public class VoteThemeVo {
    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty(value = "店铺ID")
    private int shopId;

    @ApiModelProperty(value = "平台")
    private int platform;//0为微信，1为微博

    @ApiModelProperty(value = "投票主题名称")
    private String name;

    @ApiModelProperty("投票类型  1 图片 2 视频 3 音频")
    private Integer voteType;

    @Lob
    @ApiModelProperty(value = "详情内容")
    private String detailJson;

    @ApiModelProperty(value = "状态 1未发布  2 投票进行中 3 投票结束 9下架")
    private int status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("发布时间")
    private Date startTime;

    @ApiModelProperty("发布时间")
    private Date endTime;

    @ApiModelProperty("选项数")
    private  int votenum;
//
//    @ApiModelProperty(value = "投票选项表")
//    public List<VoteItemVo> voteItemList;

    @ApiModelProperty(value = "投票选项表")
    public List<VoteRelVo> voteRelVos;






}
