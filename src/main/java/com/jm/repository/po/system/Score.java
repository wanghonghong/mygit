package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>论坛帖子评分表</p>
 *
 * @author whh
 * @version latest
 * @date 2016/1/12
 */
@Data
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "评分内容")
    private String scoreContext;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public Score(){
        this.createDate = new Date();
    }

}
