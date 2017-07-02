package com.jm.staticcode.converter.zb;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.dispatch.ZbDispatchVo;
import com.jm.staticcode.util.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Review的bean转化</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/11/16 
 */
public class ReviewConverter {
    
    public static PageItem<ZbDispatchVo> toDispatchVo(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<ZbDispatchVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<ZbDispatchVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ZbDispatchVo ZbDispatchVo = JsonMapper.map2Obj(map,ZbDispatchVo.class);
            list.add(ZbDispatchVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }
}
