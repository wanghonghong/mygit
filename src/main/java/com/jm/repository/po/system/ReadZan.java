package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>帖子浏览和点赞统计表</p>
 *
 * @author whh
 * @version latest
 * @date 2016/2/3
 */
@Data
@Entity
public class ReadZan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("类型：1、浏览 2、点赞")
    private Integer type;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("帖子id")
    private Integer postId;

}
