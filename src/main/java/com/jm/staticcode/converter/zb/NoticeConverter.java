package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.notice.NoticeCo;
import com.jm.mvc.vo.zb.notice.NoticeVo;
import com.jm.repository.po.zb.system.ZbNotice;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ME on 2016/8/17.
 */
public class NoticeConverter {

    public static ZbNotice toNotice(NoticeCo noticeCo){
        ZbNotice ZbNotice = new ZbNotice();
        BeanUtils.copyProperties(noticeCo, ZbNotice);
        return ZbNotice;
    }

    public static PageItem<NoticeVo> toNoticeList(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<NoticeVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<NoticeVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            NoticeVo NoticeVo = JsonMapper.map2Obj(map,NoticeVo.class);
            list.add(NoticeVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static NoticeVo toNoticeVo(ZbNotice ZbNotice){
        NoticeVo noticeVo = new NoticeVo();
        BeanUtils.copyProperties(ZbNotice, noticeVo);
        return noticeVo;
    }

}
