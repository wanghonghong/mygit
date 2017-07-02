package com.jm.mvc.vo.shop.vote;

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
@ApiModel(description = "投票信息表")
public class VoteInfoVo {
    @ApiModelProperty(value = "主键id")
    private Integer Id;

    @ApiModelProperty("用户id")
    private Integer userId;


    @ApiModelProperty("创建时间")
    private Date createTime;
}
