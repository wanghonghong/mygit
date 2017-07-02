package com.jm.business.service.zb;

import com.jm.repository.jpa.zb.system.ZbUserRoleRepository;
import com.jm.repository.po.zb.user.ZbUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>用户角色</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Service
public class ZbUserRoleService {

    @Autowired
    private ZbUserRoleRepository zbUserRoleRepository;


    public ZbUserRole findByUserId(Integer userId){
        return zbUserRoleRepository.findByUserId(userId);
    }

    public ZbUserRole updateUserRole(Integer userId, Integer roleId ){
        ZbUserRole zbUserRole = zbUserRoleRepository.findByUserId(userId);
        zbUserRole.setRoleId(roleId);
        return  zbUserRoleRepository.save(zbUserRole);
    }
}
