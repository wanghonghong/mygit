package com.jm.mvc.vo.wx.wxuser;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.wx.level.WxUserLevelVo;
import com.jm.repository.po.wx.WxUserGroup;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OnlineUserVo  {
    private String hxAccount;

    private Integer userid;

    private Integer shopid;

    private String headImg;

    private String nickname;

    private List<WxUserLevelVo> levels;

    private List<WxUserGroup> groups;
}
