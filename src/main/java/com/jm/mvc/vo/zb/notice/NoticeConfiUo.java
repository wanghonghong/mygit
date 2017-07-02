package com.jm.mvc.vo.zb.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * Created by ME on 2016/8/17.
 */
@Data
public class NoticeConfiUo {

    @ApiModelProperty(value = "公告id")
    private Integer id;

    @ApiModelProperty(value = "消息可查看部们列表")
    private String departments;

}
