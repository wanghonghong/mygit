package com.jm.repository.po.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zx on 2017/3/30.
 */
@Data
@Entity
@ApiModel(description = "投票信息表")
public class VoteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty("投票项目id")
    private Integer voteId;

    @ApiModelProperty("用户id")
    private Integer userId;


    @ApiModelProperty("创建时间")
    private Date createTime;
}
