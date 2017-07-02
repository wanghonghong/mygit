package com.jm.mvc.vo.system;

import com.jm.repository.po.system.JmPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/1/11
 */
@Data
@ApiModel(description = "聚社区Vo")
public class CommunityVo {

    @ApiModelProperty(value = "头像")
    private String headImg;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty("联系手机")
    private String  phoneNumber;

    @ApiModelProperty("联系邮箱")
    private Integer staff;

    @ApiModelProperty("qq号")
    private String qq;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区")
    private String district;

    @ApiModelProperty(value = "前八热帖")
    private List<JmPost> jmPosts;

    @ApiModelProperty(value = "帖子总数")
    private Integer postNum;

    @ApiModelProperty(value = "当天星期")
    private String week;

    @ApiModelProperty(value = "当天日期")
    private String date;
}
