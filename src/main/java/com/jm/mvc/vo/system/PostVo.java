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
 * @date 2017/1/12
 */
@Data
@ApiModel(description = "帖子")
public class PostVo {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "主题分类")
    private Integer type;

    @ApiModelProperty(value = "帖子内容")
    private String postContext;

    @ApiModelProperty(value = "帖子特征:1、强烈推荐 2、精 3、热帖")
    private Integer feature;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "帖子浏览次数")
    private Integer readCount;

    @ApiModelProperty(value = "帖子点赞次数")
    private Integer zanCount;

    @ApiModelProperty(value = "帖子评论次数")
    private Integer replyCount;

    @ApiModelProperty(value = "帖子总数")
    private Integer postNum;

    @ApiModelProperty(value = "是否赞过帖子：0、未赞过，1、已赞")
    private Integer isZan;
}
