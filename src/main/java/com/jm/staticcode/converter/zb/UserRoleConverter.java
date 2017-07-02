package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.zb.system.UserRoleCo;
import com.jm.mvc.vo.zb.system.ZbUserRoleUo;
import com.jm.repository.po.zb.user.ZbUserRole;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Role的bean转化</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 11:57
 */
public class UserRoleConverter {

    public static List<ZbUserRole> toUserRole(List<UserRoleCo> userRoleCos){
        List<ZbUserRole> zbUserRoles = new ArrayList<>();
        for (UserRoleCo userRoleCo : userRoleCos){
            ZbUserRole zbUserRole = new ZbUserRole();
            BeanUtils.copyProperties(userRoleCo, zbUserRole);
            zbUserRoles.add(zbUserRole);
        }
        return zbUserRoles;
    }

    public static List<ZbUserRole> toUserRoleU(List<ZbUserRoleUo> zbUserRoleUos){
        List<ZbUserRole> zbUserRoles = new ArrayList<>();
        for (ZbUserRoleUo zbUserRoleUo : zbUserRoleUos){
            ZbUserRole zbUserRole = new ZbUserRole();
            BeanUtils.copyProperties(zbUserRoleUo, zbUserRole);
            zbUserRoles.add(zbUserRole);
        }
        return zbUserRoles;
    }
}
