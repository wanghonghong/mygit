package com.jm.mvc.vo.wx.content;
import lombok.Data;


@Data
public class WxContentFilter {
    private boolean isToAll;//是否群发
    private Integer groupId;//分组id
}
