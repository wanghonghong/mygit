package com.jm.staticcode.converter.zb;


import com.jm.mvc.vo.zb.system.RoleResourceCo;
import com.jm.repository.po.zb.user.ZbRoleResource;
import org.springframework.beans.BeanUtils;

/**
 * <p>Resource的bean转化</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/24
 */
public class RoleResourceConverter {
    public static ZbRoleResource toRoleResource(RoleResourceCo roleResourceCo){
        ZbRoleResource zbRoleResource = new ZbRoleResource();
        BeanUtils.copyProperties(roleResourceCo, zbRoleResource);
        return zbRoleResource;
    }


}
