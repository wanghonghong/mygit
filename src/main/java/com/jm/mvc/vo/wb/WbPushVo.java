package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * <p>微博推送(事件或消息推送)vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2017/3/10
 */
@Data
public class WbPushVo {
    @ApiModelProperty(value = "推送类型")
    private String type;

    @ApiModelProperty(value = "消息的接收者")
    private Long receiver_id;

    @ApiModelProperty(value = "消息的发送者")
    private Long sender_id;

    @ApiModelProperty(value = "消息创建时间")
    private String created_at;

    @ApiModelProperty(value = "默认文案。subtype为follow或unfollow时分别为“关注事件消息”、“取消关注事件消息”；")
    private String text;

    @ApiModelProperty(value = "消息内容")
    private WbPushDataVo data;
}
