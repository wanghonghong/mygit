package com.jm.business.service.zb;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.ZbRoleRepository;
import com.jm.repository.jpa.zb.system.ZbRoleTypeRepository;
import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbRoleType;
import com.jm.staticcode.converter.zb.RoleConverter;
import com.jm.staticcode.converter.zb.ZbUserConverter;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>角色</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Service
public class ZbRoleService {

    @Autowired
    private ZbRoleRepository zbRoleRepository;

    @Autowired
    private ZbRoleTypeRepository zbRoleTypeRepository;

    @Autowired
    protected JdbcUtil jdbcUtil;

    public List<ZbRole> queryRole(){
        return zbRoleRepository.findAll();
    }

    @ApiOperation("角色分页查询")
    public PageItem<RoleVo> queryRoles(RoleQo roleQo) throws IOException {
        String sqlList = "select r.*,t.type_name,u.user_name from zb_role r " +
                "LEFT JOIN zb_role_type t on r.role_type = t.id " +
                "LEFT JOIN zb_user u on r.user_id = u.user_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("t.user_id", roleQo.getUserId()));
        sqlCondition.append(" order by t.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, roleQo.getCurPage(), roleQo.getPageSize());
        return RoleConverter.toRoles(PageItem);
    }


    public RoleVo queryRoleById(Integer id){
         ZbRole zbRole = zbRoleRepository.findOne(id);
        RoleVo roleVo = RoleConverter.toRoleVo(zbRole);
        roleVo.setZbRoleTypes(getRoleType());
        return roleVo;
    }

    public ZbRole add(RoleCo roleCo){
        ZbRole newZbRole = zbRoleRepository.save(RoleConverter.toRole(roleCo));
        return newZbRole;
    }
    @Transactional
    public void deleteRoles(List<Integer> ids){
        zbRoleRepository.deleteByIds(ids);
    }

    public ZbRole addRole(ZbRole zbRole) {
        return zbRoleRepository.save(zbRole);
    }

    public ZbRole update(RoleUo roleUo){
        ZbRole zbRole = zbRoleRepository.findOne(roleUo.getId());
        Toolkit.copyPropertiesIgnoreNull(roleUo, zbRole);
        return  zbRoleRepository.save(zbRole);
    }

    public ZbRoleType addRoleType(ZbRoleType zbRoleType) {
        return zbRoleTypeRepository.save(zbRoleType);
    }

    public ZbRoleType updateType(RoleTypeUo typeUo){
        ZbRoleType zbRoleType = zbRoleTypeRepository.findOne(typeUo.getId());
        Toolkit.copyPropertiesIgnoreNull(typeUo, zbRoleType);
        return  zbRoleTypeRepository.save(zbRoleType);
    }

    @ApiOperation("角色类型分页查询")
    public PageItem<RoleTypeVo> queryRoleTypes(RoleTypeQo roleQo) throws Exception{
        String sqlList = "select t.*,u.user_name from zb_role_type t " +
                "LEFT JOIN zb_user u on t.user_id = u.user_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("t.user_id", roleQo.getUserId()));
        sqlCondition.append(" order by t.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, roleQo.getCurPage(), roleQo.getPageSize());
        return RoleConverter.toRoleTypes(PageItem);
    }

    @Transactional
    public JmMessage deleteRoleType(int id){
        List<ZbRole> zbRoles = zbRoleRepository.findByRoleType(id);
        if (zbRoles.size()>0){
            return new JmMessage(1,"该类型下尚有未删除的角色，请先删除角色后再进行操作！");
        }
        zbRoleTypeRepository.delete(id);
        return new JmMessage(0,"删除成功！");
    }

    public List<ZbRoleType> getRoleType(){
        return zbRoleTypeRepository.findAll();
    }

}
