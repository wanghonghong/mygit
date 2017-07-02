package com.jm.business.service.system;


import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.join.*;
import com.jm.mvc.vo.zb.dispatch.DispatchJoinQo;
import com.jm.mvc.vo.zb.join.CheckUo;
import com.jm.mvc.vo.zb.join.ZbClassDataVo;
import com.jm.mvc.vo.zb.join.ZbJoinClassVo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.repository.client.ErpClient;
import com.jm.repository.client.dto.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.CheckRepository;
import com.jm.repository.jpa.system.JmJoinRepository;
import com.jm.repository.jpa.system.RoleRepository;
import com.jm.repository.jpa.system.UserRepository;
import com.jm.repository.po.system.*;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.system.user.User;
import com.jm.staticcode.converter.join.JoinConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>加盟服务层</p>
 *
 * @author whh
 * @version latest
 * @date 2016-11-1
 */
@Service
public class JmJoinService {

    @Autowired
    private JmJoinRepository jmJoinRepository;

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    protected JdbcUtil jdbcUtil;

    @Autowired
    private SystemService s;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ErpClient erpClient;

    @ApiOperation("根据id和加盟类型查询单个加盟信息")
    public JmJoin findJmJoin(Integer userId,Integer type){
        return jmJoinRepository.findByUserIdAndType(userId,type);
    }

    @ApiOperation("根据id和加盟类型查询单个加盟信息,并返回给前端")
    public JoinVo findJmJoinVo(JmJoin jmJoin){
        Role role = roleService.queryRoleById(jmJoin.getApplyRole());
        JoinVo joinVo = JoinConverter.toJoinVo(role,jmJoin);
        return joinVo;
    }

    /**
     * 审核分页查询
     * @param
     * @return
     * @throws IOException
     */
    public PageItem<JoinVo> queryJoinList(ZbJoinVo zbJoinVo) throws Exception {
        String sqlList = "select r.role_name,u.head_img,j.*  " +
                "from jm_join j LEFT JOIN role r on r.role_id=j.apply_role "+
                "LEFT JOIN user u on u.user_id=j.user_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("j.apply_role",zbJoinVo.getApplyRole()));
        sqlCondition.append(JdbcUtil.appendAnd("j.status",zbJoinVo.getStatus()));
        sqlCondition.append(JdbcUtil.appendAnd("j.type",zbJoinVo.getType()));
        sqlCondition.append(JdbcUtil.appendAnd("j.sub_type",zbJoinVo.getSubType()));
        sqlCondition.append(JdbcUtil.appendLike("j.user_name",zbJoinVo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("j.phone_number",zbJoinVo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendLike("j.company_name",zbJoinVo.getCompanyName()));
        sqlCondition.append(JdbcUtil.appendAnd("j.create_date",zbJoinVo.getStartTime(),zbJoinVo.getEndTime()));
        sqlCondition.append(" order by j.check_time desc,j.create_date asc");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,zbJoinVo.getCurPage(),zbJoinVo.getPageSize());
        return JoinConverter.toJoinVo(pageItem);
    }

    @ApiOperation("总部系统获取加盟信息JoinVo")
    public JoinVo getJoinVo(ZbJoinVo zbJoinVo){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(zbJoinVo.getUserId(),zbJoinVo.getType());
        Role role = roleService.queryRoleById(jmJoin.getApplyRole());
        JoinVo joinVo = JoinConverter.toJoinVo(role,jmJoin);
        List<JmJoinCheck> checks = checkRepository.queryCheckByJoinId(jmJoin.getUserId(),jmJoin.getType());
        joinVo.setCheckList(checks);
        return joinVo;
    }

    @ApiOperation("审核建议更新")
    public void addCheck(CheckUo checkUo){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(checkUo.getUserId(),checkUo.getType());
        jmJoin.setStatus(checkUo.getStatus());
        jmJoin.setCheckTime(new Date());
        jmJoin.setCheckerId(checkUo.getCheckerId());
        JmJoinCheck jmJoinCheck = new JmJoinCheck();
        jmJoinCheck .setType(checkUo.getType());
        jmJoinCheck.setUserId(checkUo.getUserId());
        jmJoinCheck.setCheckerId(checkUo.getCheckerId());
        jmJoinCheck.setCheckContext(checkUo.getCheckContext());
        jmJoinCheck.setCheckerId(checkUo.getCheckerId());
        jmJoinRepository.save(jmJoin);
        checkRepository.save(jmJoinCheck);
    }

    @Transactional
    @ApiOperation("删除单个加盟数据")
    public void deleteJoin(ZbJoinVo zbJoinVo){
        jmJoinRepository.deleteJoin(zbJoinVo.getUserId(),zbJoinVo.getType());
        checkRepository.deleteCheck(zbJoinVo.getUserId(),zbJoinVo.getType());
    }

    @ApiOperation("加盟申请页面保存")
    public JmJoin addJoin(Integer userId,JoinCo joinCo){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(userId,joinCo.getType());
        Toolkit.copyPropertiesIgnoreNull(joinCo,jmJoin);
        jmJoin.setBusinessLicense(ImgUtil.substringUrl(joinCo.getBusinessLicense()));
        jmJoin.setStatus(1);
        return  jmJoinRepository.save(jmJoin);
    }

    @ApiOperation("加盟申请页面保存")
    public JmJoin saveJoin(JmJoin jmJoin){
        return jmJoinRepository.save(jmJoin);
    }

    @ApiOperation("加盟申请页面更新")
    public JmJoin updateJoin(JoinUo joinUo){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(joinUo.getUserId(),joinUo.getType());
        Toolkit.copyPropertiesIgnoreNull(joinUo,jmJoin);
        jmJoin.setBusinessLicense(ImgUtil.substringUrl(joinUo.getBusinessLicense()));
        return  jmJoinRepository.save(jmJoin);
    }

    @ApiOperation("商家个人资料获取加盟信息")
    public JoinVo getJoinVoForData(Integer id,Integer type){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(id,type);
        Role role = roleService.queryRoleById(jmJoin.getApplyRole());
        JoinVo joinVo = JoinConverter.toJoinVo(role,jmJoin);
        joinVo.setApplyRoleId(jmJoin.getApplyRole());
        List<Role> roles = roleRepository.queryServiceRole();
        joinVo.setRoles(roles);
        return joinVo;
    }

    @ApiOperation("查询最后一条审核意见")
    public JmJoinCheck queryCheck(Integer userId,Integer type){
        return checkRepository.queryCheck(userId,type);
    }


    /**
     * 总部平台用户中心客户中心分页查询
     * @param
     * @return
     * @throws IOException
     */
    public PageItem<JoinClassVo> queryClassVos(ZbJoinClassVo zbJoinClassVo) throws Exception {
        String sqlList = "select u.head_img,u.sex,r.role_name,j.* " +
                "from jm_join j LEFT JOIN user u on j.user_id=u.user_id " +
                "LEFT JOIN role r on j.apply_role= r.role_id " +
                "where 1=1 " ;
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("u.sex",zbJoinClassVo.getSex()));
        sqlCondition.append(JdbcUtil.appendAnd("j.type",zbJoinClassVo.getType()));
        sqlCondition.append(JdbcUtil.appendAnd("j.status",3));
        sqlCondition.append(JdbcUtil.appendLike("j.user_name",zbJoinClassVo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("j.phone_number",zbJoinClassVo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("j.apply_role",zbJoinClassVo.getApplyRole()));
        sqlCondition.append(JdbcUtil.appendLike("j.company_name",zbJoinClassVo.getCompanyName()));
        sqlCondition.append(JdbcUtil.appendAnd("j.province",zbJoinClassVo.getArea()));
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,zbJoinClassVo.getCurPage(),zbJoinClassVo.getPageSize());
        return JoinConverter.p2v(pageItem);
    }

    @ApiOperation("获取总部平台代理类编辑弹窗基本资料Vo" )
    public ClassDataVo getClassVo(ZbJoinVo zbJoinVo){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(zbJoinVo.getUserId(),zbJoinVo.getType());
        ClassDataVo classDataVo = new ClassDataVo();
        BeanUtils.copyProperties(jmJoin,classDataVo);
        classDataVo.setBusinessLicense(ImgUtil.appendUrl(classDataVo.getBusinessLicense(),300));
        String wxnum = userRepository.findByUserId(zbJoinVo.getUserId()).getWxnum();
        classDataVo.setWxnum(wxnum);
        return classDataVo;
    }

    @ApiOperation("总部平台代理类编辑弹窗基本资料页面保存")
    public void saveClassData(JmJoin jmJoin, User user, ZbClassDataVo zbClassDataVo){
        Toolkit.copyPropertiesIgnoreNull(zbClassDataVo,jmJoin);
        jmJoin.setBusinessLicense(ImgUtil.substringUrl(jmJoin.getBusinessLicense()));
        user.setWxnum(zbClassDataVo.getWxnum());
        jmJoinRepository.save(jmJoin);
        userRepository.save(user);
    }


    public List<JmJoin> checkUser(Integer userId,Integer type){
        return jmJoinRepository.findByUserIdAndTypeAndStatus(userId,type,3);
    }

    /**
     * 派单操作分页查询
     * @param
     * @return
     * @throws IOException
     */
    public PageItem<DispatchJoinVo> queryJoins(DispatchJoinQo dispatchJoinQo) throws Exception {
        String sqlList = "select r.role_name,j.*  " +
                "from jm_join j LEFT JOIN role r on r.role_id=j.apply_role "+
                "where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("j.status",3));
        sqlCondition.append(JdbcUtil.appendAnd("j.type",dispatchJoinQo.getType()));
        sqlCondition.append(JdbcUtil.appendLike("j.user_name",dispatchJoinQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("j.phone_number",dispatchJoinQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("j.apply_role",dispatchJoinQo.getApplyRoleId()));
        sqlCondition.append(JdbcUtil.appendAnd("j.province",dispatchJoinQo.getProvince()));
        sqlCondition.append(JdbcUtil.appendAnd("j.city",dispatchJoinQo.getCity()));
        sqlCondition.append(JdbcUtil.appendAnd("j.district",dispatchJoinQo.getDistrict()));
        sqlCondition.append(" order by j.create_date asc");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,dispatchJoinQo.getCurPage(),dispatchJoinQo.getPageSize());
        return JoinConverter.toDispatchJoinVo(pageItem);
    }

    @ApiOperation("返回加盟信息和角色列表")
    public JoinVo getJoinVo(Integer userId,Integer type){
        JmJoin jmJoin = jmJoinRepository.findByUserIdAndType(userId,type);
        JoinVo joinVo = JoinConverter.toJoinVo(jmJoin);
        joinVo.setPhoneNumber(userRepository.findByUserId(userId).getPhoneNumber());
        if(jmJoin!=null){
            joinVo.setApplyRole(roleService.queryRoleById(jmJoin.getApplyRole()).getRoleName());
            JmJoinCheck check = checkRepository.queryCheck(userId,type);
            if (check!=null){
                joinVo.setCheckContext(check.getCheckContext());
            }
        }
        List<Role> roleList = roleRepository.queryServiceRole();
        joinVo.setRoles(roleList);
        return joinVo;
    }

    @ApiOperation("获取总部平台派单列表")
    public PageItem getDispatchList(DispatchDto dispatchDto) throws IOException{
        return  erpClient.getDispatchList(dispatchDto);
    }

    @ApiOperation("获取总部平台通知列表")
    public PageItem getNoticeList(NoticeDto noticeDto) throws IOException{
        return  erpClient.getNoticeList(noticeDto);
    }

    @ApiOperation("获取总部平台单个通知")
    public NoticeContextDto getNotice(NoticeDto noticeDto) throws IOException{
        return  erpClient.getNotice(noticeDto);
    }
}
