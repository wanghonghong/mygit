package com.jm.business.service.zb;

import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.zb.system.ResourceUo;
import com.jm.mvc.vo.zb.system.ZbResourceCo;
import com.jm.repository.jpa.zb.system.ZbResourceRepository;
import com.jm.repository.jpa.zb.system.ZbRoleResourceRepository;
import com.jm.repository.po.zb.system.ZbResource;
import com.jm.repository.po.zb.user.ZbRoleResource;
import com.jm.staticcode.converter.zb.ZbResourceConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>菜单权限</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Service
public class ZbResourceService {

    @Autowired
    private ZbResourceRepository zbResourceRepository;

    @Autowired
    private ZbRoleResourceRepository zbRoleResourceRepository;

    public List<ZbResource> queryResource(){
        return zbResourceRepository.findAll();
    }

    public ZbResource add(ZbResourceCo zbResourceCo){
        ZbResource zbResource = zbResourceRepository.save(ZbResourceConverter.toResource(zbResourceCo));
        return zbResource;
    }

    public void deleteResource(int id){
        zbResourceRepository.delete(id);
    }

    @Transactional
    public void save(ZbResource zbResource) {
        zbResourceRepository.save(zbResource);
    }

    @Transactional
    public void update(int id, ResourceUo resourceUo){
        ZbResource zbResource = zbResourceRepository.findOne(id);
        zbResource.setResourceName(resourceUo.getResourceName());
        zbResourceRepository.save(zbResource);
    }


    public List<ZbResource> findResourceByRole(JmUserSession user, int status) {
        if(user ==null){
            return null;
        }
        if(user.getRoleId()==2){//店主获取所有菜单
            List<ZbResource> list = zbResourceRepository.findResourceByStatus(status);
            return list ;
        }
        //根据角色获取菜单
        List<ZbRoleResource> zbRoleResources = zbRoleResourceRepository.findByRoleId(user.getRoleId());
        List<Integer> resourceIds = new ArrayList<>();
        for (ZbRoleResource zbRoleResource : zbRoleResources) {
            resourceIds.add(zbRoleResource.getResourceId());
        }
        if(resourceIds.size()>0){
            List<ZbResource> zbResources = zbResourceRepository.findResourceByIdsAndStatus(resourceIds,status);
            return zbResources;
        }
        return null;
    }
    //保存角色菜单信息
    public void saveRoleResource(ZbResourceCo co){
        List<ZbRoleResource> zbRoleResourceList = zbRoleResourceRepository.findByRoleId(co.getRoleId());
        if(zbRoleResourceList.size()>0){
            zbRoleResourceRepository.delete(zbRoleResourceList);
        }
        List<ZbRoleResource> zbRoleResources =new ArrayList<ZbRoleResource>();
        for (Integer resourceId:co.getResourceIds()){
            ZbRoleResource zbRoleResource = new ZbRoleResource();
            zbRoleResource.setRoleId(co.getRoleId());
            zbRoleResource.setResourceId(resourceId);
            zbRoleResources.add(zbRoleResource);
        }
        zbRoleResourceRepository.save(zbRoleResources);
    }
    //获取角色应对菜单id串
    public String getResourceIds(Integer roleId){
        List<Integer> roleIds = zbRoleResourceRepository.findResourceIdsByRoleId(roleId);
        String ids="";
        if(roleIds.size()>0){
            for(Integer id:roleIds){
                ids+=id+",";
            }
        }
        return ids;
    }
}
