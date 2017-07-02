package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>帖子回复表</p>
 *
 * @author whh
 * @version latest
 * @date 2016/1/21
 */
@Data
@Entity
public class PostReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty(value = "回复人Id")
    private Integer userId;

    @ApiModelProperty(value = "回复人头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    @ApiModelProperty(value = "回复内容")
    private String context;

    @ApiModelProperty(value = "是否匿名，1，是，2，否")
    private Integer isHide;

    @ApiModelProperty(value = "点赞数")
    private Integer zanNumber;

    @ApiModelProperty(value = "评论数")
    private Integer replyNumber;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    public PostReply(){
        this.createDate = new Date();
    }

}
