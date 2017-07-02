package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author whh
 * @version latest
 * @date 2017/2/3
 */
@Data
@ApiModel(description = "浏览和点赞co")
public class ReadZanCo {

    @ApiModelProperty("类型：1、浏览 2、点赞")
    private Integer type;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("帖子id")
    private Integer postId;
}
