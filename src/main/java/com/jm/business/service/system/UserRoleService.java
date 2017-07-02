package com.jm.business.service.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.system.UserRoleRo;
import com.jm.mvc.vo.system.UserRoleUo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.po.system.JmJoin;
import com.jm.staticcode.util.Equalizer;
import com.jm.staticcode.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.system.UserRoleRepository;
import com.jm.repository.po.system.user.UserRole;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
public class UserRoleService {
	@Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JmJoinService jmJoinService;
    @Autowired
    private JdbcUtil jdbcUtil;


    public UserRole createUserRole(UserRole userRole){
        return userRoleRepository.save(userRole);
    }

    public List<UserRoleRo>  findByUserAndType(Integer userId, Integer type,Integer shopid) throws IOException {
        String sql= "select u.*,r.type from user_role u left join role r on r.role_id = u.role_id where u.user_id="+userId+" and r.type="+type+" and u.shop_id="+shopid;
        List<Map<String, Object>> list =jdbcTemplate.queryForList(sql);
        List<UserRoleRo> userRoles = new ArrayList<>();
        for (Map<String, Object> map :list){
            UserRoleRo userRole = JsonMapper.map2Obj(map,UserRoleRo.class);
            userRoles.add(userRole);
        }
        return userRoles;
    }

    public void updateUser(UserRole userrole){
        StringBuffer sql = new StringBuffer("update user_role set hx_account ='"+userrole.getHxAccount()+"'");
        sql.append("where 1=1 ");
        sql.append(jdbcUtil.appendAnd("user_id",userrole.getUserId()));
        sql.append(jdbcUtil.appendAnd("shop_id",userrole.getShopId()));
        jdbcUtil.update(sql.toString());
        //return userRoleRepository.save(userrole);
    }

    public UserRole updateUserRo(UserRoleRo userrole){
        UserRole r = userRoleRepository.findOne(userrole.getId());
        r.setRoleId(userrole.getRoleId());
        return userRoleRepository.save(r);
    }


    public JmMessage updateUserRole(UserRoleUo userRoleUo,JmUserSession user) throws IOException {
        if(userRoleUo.getType().equals(3)){//修改成为代理商
            List<JmJoin> list = jmJoinService.checkUser(userRoleUo.getUserId(),1);
            if(list == null || list.size()<=0){
                return new JmMessage(1, "用户未成为平台代理商，无法授权");
            }
        }
        if(userRoleUo.getType().equals(4)){//修改成为服务商
            List<JmJoin> list = jmJoinService.checkUser(userRoleUo.getUserId(),2);
            if(list == null || list.size()<=0){
                return new JmMessage(1, "用户未成为平台服务商，无法授权");
            }
        }

        if(userRoleUo.getUserRoleId().equals(0)){
            List<UserRoleRo>  roles=findByUserAndType(userRoleUo.getUserId(),userRoleUo.getType(),user.getShopId());
            if(roles.size()>0){
                UserRoleRo userrole1 =  roles.get(0);
                userrole1.setRoleId(userRoleUo.getRoleId());
                updateUserRo(userrole1);
            }else{
                UserRole userrole = new UserRole();
                userrole.setShopId(user.getShopId());
                userrole.setRoleId(userRoleUo.getRoleId());
                userrole.setUserId(userRoleUo.getUserId());
                createUserRole(userrole);
            }
        }else{//已经授权过的
            List<UserRoleRo>  roles=findByUserAndType(userRoleUo.getUserId(),userRoleUo.getType(),user.getShopId());
            if(roles.size()>0){
                UserRoleRo userrole1 =  roles.get(0);
                userrole1.setRoleId(userRoleUo.getRoleId());
                updateUserRo(userrole1);
            }else{
                UserRole userrole = new UserRole();
                userrole.setShopId(user.getShopId());
                userrole.setRoleId(userRoleUo.getRoleId());
                userrole.setUserId(userRoleUo.getUserId());
                createUserRole(userrole);
            }
              /*ZbUserRole userRole =  userRoleService.findById(userRoleUo.getUserRoleId());
                if(userRole!=null){
                    userRoleService.updateUserRole(userRoleUo.getUserRoleId(),userRoleUo.getRoleId());
                }*/
        }
        return new JmMessage(0, "成功");
    }




    @Transactional
	public void deleteUserRole(Integer id) {
		userRoleRepository.deleteUserRole(id);
	}
    
    
    public UserRole findShopMasterByShopId(Integer shopId){
        return userRoleRepository.queryUserMasterbyShopId(shopId);
    }

    public UserRole findOnlineUserByShopIdAndUserId(Integer shopId,Integer userId) throws Exception {
        StringBuffer sql = new StringBuffer("select * from user_role ur where 1=1");
        sql.append(jdbcUtil.appendAnd("ur.shop_id",shopId));
        sql.append(jdbcUtil.appendAnd("ur.user_id",userId));
        sql.append(" order by ur.role_id ");
        List<UserRole> roles = jdbcUtil.queryList(sql.toString(),UserRole.class);
        if(roles!=null&&roles.size()>0){
            return roles.get(0);
        }
        return null;
    }

    public UserRole findOnlineUserByAppidAndUserId(String appid,Integer userId){
        return null;
    }

    public String initShopUserHxAcct(JmUserSession user) throws Exception {
        if(user==null){
            return null;
        }
        UserRole role;
        String hxAcct = user.getHxAccount();
        if(hxAcct==null||"".equals(hxAcct)){
            role = findOnlineUserByShopIdAndUserId(user.getShopId(),user.getUserId());
            if(role!=null&&role.getHxAccount()!=null){
                log.info("首次获取客服人员信息"+role.getHxAccount());
                hxAcct = role.getHxAccount();
                user.setHxAccount(hxAcct);
            }else{
                log.info("-----获取角色为空"+user.getShopId()+user.getUserId()+"-------");
            }
        }
        if(hxAcct!=null){
            log.info("添加客服人员信息到内存中");
            Equalizer.addOnlineServiceUser(user.getShopId(),user.getUserId(),hxAcct);
        }
        return hxAcct;
    }

}
