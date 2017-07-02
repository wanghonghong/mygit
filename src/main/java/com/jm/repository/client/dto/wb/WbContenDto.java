package com.jm.repository.client.dto.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>微博内容表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/3/1
 */
@Data
@ApiModel(description = "微博内容表Dto")
public class WbContenDto {

    @ApiModelProperty(value = "微博创建时间")
    private String createdAt;

    @ApiModelProperty(value = "微博ID")
    private String id;

    @ApiModelProperty(value = "微博MID")
    private String mid;

    @ApiModelProperty(value = "字符串型的微博ID")
    private String idstr;

    @ApiModelProperty(value = "微博信息内容")
    private String text;

    @ApiModelProperty(value = "微博来源")
    private String source;

    @ApiModelProperty(value = "是否已收藏，true：是，false：否")
    private Boolean favorited;

    @ApiModelProperty(value = "是否被截断，true：是，false：否")
    private Boolean truncated;

    @ApiModelProperty(value = "（暂未支持）回复ID")
    private String inReplyToStatusId;

    @ApiModelProperty(value = "（暂未支持）回复人UID")
    private String inReplyToUserId;

    @ApiModelProperty(value = "（暂未支持）回复人昵称")
    private String inReplyToScreenName;

    @ApiModelProperty(value = "缩略图片地址，没有时不返回此字段")
    private String thumbnailPic;

    @ApiModelProperty(value = "中等尺寸图片地址，没有时不返回此字段")
    private String bmiddlePic;

    @ApiModelProperty(value = "原始图片地址，没有时不返回此字段")
    private String originalPic;

    @ApiModelProperty(value = "地理信息字段")
    private Object geo;

    @ApiModelProperty(value = "微博作者的用户信息字段")
    private Object user;

    @ApiModelProperty(value = "被转发的原微博信息字段，当该微博为转发微博时返回")
    private Object retweetedStatus;

    @ApiModelProperty(value = "转发数")
    private Integer repostsCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentsCount;

    @ApiModelProperty(value = "表态数")
    private Integer attitudesCount;

    @ApiModelProperty(value = "微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号")
    private Object visible;

    @ApiModelProperty(value = "微博配图ID。多图时返回多图ID，用来拼接图片url。用返回字段thumbnail_pic的地址配上该返回字段的图片ID，即可得到多个图片url。")
    private Object picIds;

    @ApiModelProperty(value = "微博流内的推广微博ID")
    private Object ad;

    private List<WbContenDto> statuses;

    @ApiModelProperty(value = "")
    private Integer nextCursor;

    @ApiModelProperty(value = "")
    private Integer previousCursor;

    @ApiModelProperty(value = "")
    private Integer totalNumber;

}
