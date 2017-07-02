package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.RoleCo;
import com.jm.mvc.vo.zb.system.RoleTypeCo;
import com.jm.mvc.vo.zb.system.RoleTypeVo;
import com.jm.mvc.vo.zb.system.RoleVo;
import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbRoleType;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Role的bean转化</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 11:57
 */
public class RoleConverter {
    public static ZbRole toRole(RoleCo roleCo){
        ZbRole zbRole = new ZbRole();
        BeanUtils.copyProperties(roleCo, zbRole);
        return zbRole;
    }

    public static PageItem<RoleVo> toRoles(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<RoleVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<RoleVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            RoleVo roleVo = JsonMapper.map2Obj(map,RoleVo.class);
            list.add(roleVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static ZbRoleType toRoleType(RoleTypeCo roleTypeCo){
        ZbRoleType zbRoleType = new ZbRoleType();
        BeanUtils.copyProperties(roleTypeCo, zbRoleType);
        return zbRoleType;
    }

    public static PageItem<RoleTypeVo> toRoleTypes(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<RoleTypeVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<RoleTypeVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            RoleTypeVo roleTypeVo = JsonMapper.map2Obj(map,RoleTypeVo.class);
            list.add(roleTypeVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static RoleVo toRoleVo(ZbRole zbRole){
        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(zbRole, roleVo);
        return roleVo;
    }
}
