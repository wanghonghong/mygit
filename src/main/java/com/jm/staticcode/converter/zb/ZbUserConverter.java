package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbPost;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ME on 2016/8/17.
 */
public class ZbUserConverter {

    public static JmUserSession toUserSession(ZbUser zbUser) {
        JmUserSession zbJmUserSession = new JmUserSession();
        BeanUtils.copyProperties(zbUser, zbJmUserSession);
        return zbJmUserSession;
    }

    public static ZbUser toUser(UserForCreateCo userForCreateCo){
        ZbUser zbUser = new ZbUser();
        BeanUtils.copyProperties(userForCreateCo, zbUser);
        return zbUser;
    }

    public static PageItem<ZbUserRo> toUserList(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<ZbUserRo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<ZbUserRo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            ZbUserRo zbUserRo = JsonMapper.map2Obj(map,ZbUserRo.class);
            if(null != map.get("head_img")){
                String url = Toolkit.parseObjForStr(map.get("head_img"));
                zbUserRo.setHeadImg(ImgUtil.appendUrl(url,100));
            }else{
                zbUserRo.setHeadImg(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
            }
            list.add(zbUserRo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static PageItem<LoginLogVo> toLoginLogs(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<LoginLogVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<LoginLogVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            LoginLogVo loginLogVo = JsonMapper.map2Obj(map,LoginLogVo.class);
            list.add(loginLogVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static ZbDepartment toZbDepartment(DepartmentCo departmentCo){
        ZbDepartment zbDepartment = new ZbDepartment();
        BeanUtils.copyProperties(departmentCo, zbDepartment);
        return zbDepartment;
    }

    public static PageItem<DepartmentVo> toDepartments(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<DepartmentVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<DepartmentVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            DepartmentVo departmentVo = JsonMapper.map2Obj(map,DepartmentVo.class);
            list.add(departmentVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }

    public static ZbPost toZbPost(PostCo postCo){
        ZbPost zbPost = new ZbPost();
        BeanUtils.copyProperties(postCo, zbPost);
        return zbPost;
    }

    public static PageItem<PostVo> toPosts(PageItem<Map<String,Object>> PageItemMap) throws IOException {
        PageItem<PostVo> PageItem = new PageItem<>();
        List<Map<String,Object>> maps = PageItemMap.getItems();
        List<PostVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            PostVo postVo = JsonMapper.map2Obj(map,PostVo.class);
            list.add(postVo);
        }
        PageItem.setCount(PageItemMap.getCount());
        PageItem.setItems(list);
        return PageItem;
    }
}
