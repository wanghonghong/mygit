package com.jm.repository.po.shop.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by zx on 2017/3/30.
 */
@Data
@Entity
@ApiModel(description = "投票主题和投票选项的关联表")
public class VoteRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ApiModelProperty(value = "投票主题id")
    private Integer themeId;

    @ApiModelProperty(value = "投票选项id")
    private Integer itemId;

    @ApiModelProperty(value = "投票数")
    private Integer votesNum;
}
