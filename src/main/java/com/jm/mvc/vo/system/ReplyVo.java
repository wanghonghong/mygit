package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/22
 */
@Data
@ApiModel(description = "帖子回复vo")
public class ReplyVo {

    @ApiModelProperty("帖子id")
    private Integer postId;

    @ApiModelProperty(value = "回复人Id")
    private Integer userId;

    @ApiModelProperty(value = "回复人头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "回复内容")
    private String context;

    @ApiModelProperty(value = "是否匿名，1，是，2，否")
    private Integer isHide;

    @ApiModelProperty(value = "点赞数")
    private Integer zanNumber;

    @ApiModelProperty(value = "评论数")
    private Integer replyNumber;

    @ApiModelProperty("帖子索引顺序")
    private Integer index;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
