package com.jm.business.service.system;

import com.jm.mvc.vo.zb.system.ZbSoftRoleVo;
import com.jm.repository.jpa.system.RoleRepository;
import com.jm.repository.po.system.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/31
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> queryRole(){
        return roleRepository.queryRole();
    }

    public Role queryRoleById(Integer queryId){
        return roleRepository.findOne(queryId);
    }

    public List<Role> queryRoleByUserId(Integer userId){
        return roleRepository.queryRoleByUserId(userId);
    }

//    public List<Role> queryRoleFrom9to11(){
//        return roleRepository.queryRoleFrom9to11();
//    }

    public List<Role> queryServiceRole(){
        return roleRepository.queryServiceRole();
    }


    public List<Role> findByType(int type){
        return roleRepository.findByType(type);
    }

    public List<Role> findByTypeAndSoftId(int type,Integer softId){
        return roleRepository.findByTypeAndSoftId(type,softId);
    }

    public Role save(Role role){
        return  roleRepository.save(role);
    }

    public List<Role> findBySoftId(Integer softId,Integer type){
        return roleRepository.findBySoftId(softId,type);
    }

    public List<Role> findRoleByType(){
        return roleRepository.findRoleByType();
    }

    @Transactional
    public void saveSoftRole(ZbSoftRoleVo vo){
        List<Role> roles = new ArrayList<>();
        for (Role role:vo.getRoles()) {
            role.setSoftId(vo.getSoftId());
            roles.add(role);
        }
        roleRepository.deleteBySoftId(vo.getSoftId());
        roleRepository.save(roles);

    }

}
