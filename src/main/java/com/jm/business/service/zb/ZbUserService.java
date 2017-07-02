package com.jm.business.service.zb;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.*;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbLoginLog;
import com.jm.repository.po.zb.system.ZbPost;
import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.repository.po.zb.user.ZbUserRole;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.zb.ZbUserConverter;
import com.jm.staticcode.util.AddressUtil;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by ME on 2016/8/17.
 */
@Service
public class ZbUserService {

    /**
     * 验证用户名密码是否正确
     * @param loginVo
     * @return
     */
    @Autowired
    private ZbUserRepository zbUserRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ZbPostRepository zbPostRepository;
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private ZbUserRoleRepository zbUserRoleRepository;
    @Autowired
    private ZbRoleRepository zbRoleRepository;
    @Autowired
    private ZbLoginLogRepository zbLoginLogRepository;

    public ZbUser findUser(ZbLoginVo zbLoginVo){
        return zbUserRepository.findZbUserByPhoneNumberAndPassword(zbLoginVo.getPhoneNumber(), zbLoginVo.getPassword());
    }

    public ZbUser add(ZbUserCo zbUserCo){
        ZbUser zbUser = new ZbUser();
        BeanUtils.copyProperties(zbUserCo, zbUser);
        zbUser.setCreateDate(new Date());
        ZbUser newZbUser = zbUserRepository.save(zbUser);
        return newZbUser;
    }

    public void del(int id){
        zbUserRepository.delete(id);
    }

    public ZbUser update(int id, ZbUserUo zbUserUo){
        ZbUser zbUser = zbUserRepository.findOne(id);
        zbUser.setPhoneNumber(zbUserUo.getPhoneNumber());
        zbUser.setUserName(zbUserUo.getUserName());
        zbUser.setDepartment( zbUserUo.getDepartment());
        zbUser.setMail( zbUserUo.getMail());
        zbUser.setSex(  zbUserUo.getSex());
        zbUser.setStaffCode( zbUserUo.getStaffCode());
        zbUser.setWxnum( zbUserUo.getWxnum() );
       return zbUserRepository.save(zbUser);
    }

    public ZbUserRo getUserRo(int id){
        ZbUser zbUser =  zbUserRepository.findOne(id);
        ZbUserRo zbUserRo = new ZbUserRo();
        BeanUtils.copyProperties(zbUser, zbUserRo);
        if(null != zbUserRo.getHeadImg()){
            zbUserRo.setHeadImg(ImgUtil.appendUrl(zbUserRo.getHeadImg(),200));
        }
        if(null != zbUser.getDepartment()){
            zbUserRo.setDepartmentName(departmentRepository.findOne(zbUser.getDepartment()).getDepartmentName());
        }
        if(null != zbUser.getPost()){
            zbUserRo.setPostName(zbPostRepository.findOne(zbUser.getPost()).getPostName());
        }
        if (zbUser.getStatus()==1){
            zbUserRo.setNewStaffCode(this.getStaffCode());
        }
        List<ZbDepartment> zbDepartmentList = departmentRepository.findAll();
        List<ZbPost> zbPostList = zbPostRepository.findAll();
        List<ZbRole> zbRoleList = zbRoleRepository.findAll();
        zbUserRo.setZbDepartmentList(zbDepartmentList);
        zbUserRo.setZbPostList(zbPostList);
        zbUserRo.setZbRoleList(zbRoleList);
        ZbUserRole zbUserRole = zbUserRoleRepository.findByUserId(id);
        if (zbUserRole !=null){
            zbUserRo.setRoleId(zbUserRole.getRoleId());
        }
        return zbUserRo;
    }
    public ZbUserVo getUserVo(int id){
        ZbUser zbUser =  zbUserRepository.findOne(id);
        ZbUserVo zbUserVo = new ZbUserVo();
        BeanUtils.copyProperties(zbUser, zbUserVo);
        if(null != zbUserVo.getHeadImg()){
            zbUserVo.setHeadImg(ImgUtil.appendUrl(zbUserVo.getHeadImg(),200));
        }else{
            zbUserVo.setHeadImg(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
        }
        if(null != zbUser.getDepartment()){
            zbUserVo.setDepartmentName(departmentRepository.findOne(zbUser.getDepartment()).getDepartmentName());
        }
        return zbUserVo;
    }

    public ZbUser getUserForUser(int userId){
        return zbUserRepository.findOne(userId);
    }

    public ZbUser updateUser(Integer userId, ZbUserUo zbUserUo){
        ZbUser zbUser = zbUserRepository.findOne(userId);
        Toolkit.copyPropertiesIgnoreNull(zbUserUo, zbUser);
        if(null != zbUser.getHeadImg()){
            zbUser.setHeadImg(ImgUtil.substringUrl(zbUser.getHeadImg()));
        }
        return zbUserRepository.save(zbUser);
    }

    public ZbUser updateStaffUser(ZbUser zbUser, UserForStaffUo userUo){
        Toolkit.copyPropertiesIgnoreNull(userUo, zbUser);
        if(null != zbUser.getHeadImg()){
            zbUser.setHeadImg(ImgUtil.substringUrl(zbUser.getHeadImg()));
        }
        zbUser.setStatus(2);
        zbUser.setReviewTime(new Date());
        return zbUserRepository.save(zbUser);
    }

   public List<ZbUserRo> queryUser(){
        List<ZbUser> zbUsers =  zbUserRepository.findAll();
        List<ZbUserRo> zbUserRos = new ArrayList<>();
        for (ZbUser zbUser : zbUsers){
            ZbUserRo zbUserRo = new ZbUserRo();
            BeanUtils.copyProperties(zbUser, zbUserRo);
            zbUserRos.add(zbUserRo);
        }
        return zbUserRos;
    }

    public ZbUser findUserByPhoneNumber(String phoneNumber){
        return zbUserRepository.findZbUserByPhoneNumber(phoneNumber);
    }

    public ZbUser createUser(ZbUser zbUser){
        zbUser.setStatus(1);
        zbUserRepository.save(zbUser);
        ZbUserRole zbUserRole = new ZbUserRole();
        zbUserRole.setUserId(zbUser.getUserId());
        zbUserRole.setRoleId(16);
        zbUserRoleRepository.save(zbUserRole);
        return zbUser;
    }
    public ZbUser saveUser(ZbUser zbUser){
        zbUserRepository.save(zbUser);
        return zbUser;
    }

    @ApiOperation("获取员工号")
    public String getStaffCode() {
        ZbUser zbUser = zbUserRepository.queryStaffCodeByReviewTime();
        if(null==zbUser){
            return "0001";
        }
        String staffCode =zbUser.getStaffCode();
        Integer newCode = Toolkit.parseObjForInt(staffCode);
        String nextCode = (++newCode).toString();
        Integer length =nextCode.length();
        if (length==1){
            nextCode = "000"+ nextCode;
        }else if(length==2){
            nextCode = "00"+ nextCode;
        }else if(length==3){
            nextCode = "0"+ nextCode;
        }
        return nextCode;
    }

    /**
     * 员工管理分页查询
     * @param
     * @return
     * @throws Exception
     */
    public PageItem<ZbUserRo> queryUserList(ZbUserQo zbUserQo) throws Exception {
        String sqlList = "select u.*,d.department_name,p.post_name " +
                "from zb_user u LEFT JOIN zb_department d on u.department=d.department_id " +
                "LEFT JOIN zb_post p on u.post=p.post_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendLike("u.user_name", zbUserQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("u.phone_number", zbUserQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("u.sex", zbUserQo.getSex()));
        sqlCondition.append(JdbcUtil.appendLike("u.staff_code", zbUserQo.getStaffCode()));
        sqlCondition.append(JdbcUtil.appendLike("u.wxnum", zbUserQo.getWxnum()));
        sqlCondition.append(JdbcUtil.appendAnd("u.department", zbUserQo.getDepartment()));
        sqlCondition.append(JdbcUtil.appendAnd("u.status", zbUserQo.getStatus()));
        sqlCondition.append(" order by u.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, zbUserQo.getCurPage(), zbUserQo.getPageSize());
        return ZbUserConverter.toUserList(PageItem);
    }

    @Transactional
    public void deleteUsers(List<Integer> ids){
        zbUserRepository.deleteByIds(ids);
    }

    @ApiOperation("获取部门列表")
    public List<ZbDepartment> getDepartmentList(){
        return departmentRepository.findAll();
    }

    @ApiOperation("保存登录日志")
    public void saveLoginLog(HttpServletRequest request,ZbUser zbUser){
        ZbLoginLog zbLoginLog = new ZbLoginLog();
        zbLoginLog.setUserId(zbUser.getUserId());
//        zbLoginLog.setIp(AddressUtil.getPublicIP());//获取外网ip，要付费，免费1000次
        zbLoginLog.setIp(request.getRemoteHost());//获取外网ip
        zbLoginLog.setAddress(AddressUtil.getProvinceName(request.getRemoteHost()));
        String s1 = request.getHeader("user-agent");
        if (s1.contains("Windows")){
            zbLoginLog.setTool("电脑");
        }else if(s1.contains("Android")) {
            zbLoginLog.setTool("Android移动客户端");
        } else if(s1.contains("iPhone")) {
            zbLoginLog.setTool("iPhone移动客户端");
        }  else if(s1.contains("iPad")) {
            zbLoginLog.setTool("iPad客户端");
        }  else {
            zbLoginLog.setTool("其他客户端");
        }
        zbLoginLogRepository.save(zbLoginLog);
    }

    /**
     * 登录日志分页
     * @param
     * @return
     * @throws Exception
     */
    public PageItem<LoginLogVo> getLoginLogs(LoginLogQo loginLogQo) throws Exception {
        String sqlList = "select * from zb_login_log where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("user_id", loginLogQo.getUserId()));
        sqlCondition.append(" order by create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, loginLogQo.getCurPage(), loginLogQo.getPageSize());
        return ZbUserConverter.toLoginLogs(PageItem);
    }

    public ZbDepartment addDepartment(ZbDepartment zbDepartment) {
        return departmentRepository.save(zbDepartment);
    }

    public ZbDepartment updateDepartment(DepartmentUo departmentUo){
        ZbDepartment zbDepartment = departmentRepository.findOne(departmentUo.getDepartmentId());
        Toolkit.copyPropertiesIgnoreNull(departmentUo, zbDepartment);
        return  departmentRepository.save(zbDepartment);
    }

    @Transactional
    public JmMessage deleteDepartment(int id){
        List<ZbUser> zbUsers = zbUserRepository.findByDepartment(id);
        if (zbUsers.size()>0){
            return new JmMessage(1,"该部门下尚有未删除的员工，请先删除或转移员工后再进行操作！");
        }
        departmentRepository.delete(id);
        return new JmMessage(0,"删除成功！");
    }

    @ApiOperation("部门分页查询")
    public PageItem<DepartmentVo> queryDepartments(DepartmentQo departmentQo) throws Exception{
        String sqlList = "select d.*,u.user_name from zb_department d " +
                "LEFT JOIN zb_user u on d.user_id = u.user_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("d.user_id", departmentQo.getUserId()));
        sqlCondition.append(" order by d.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, departmentQo.getCurPage(), departmentQo.getPageSize());
        return ZbUserConverter.toDepartments(PageItem);
    }

    public ZbPost addPost(ZbPost zbPost) {
        return zbPostRepository.save(zbPost);
    }

    public ZbPost updatePost(PostUo postUo){
        ZbPost zbPost = zbPostRepository.findOne(postUo.getPostId());
        Toolkit.copyPropertiesIgnoreNull(postUo, zbPost);
        return  zbPostRepository.save(zbPost);
    }

    @Transactional
    public JmMessage deletePost(int id){
        List<ZbUser> zbUsers = zbUserRepository.findByPost(id);
        if (zbUsers.size()>0){
            return new JmMessage(1,"该岗位下尚有未删除的员工，请先删除或转移员工后再进行操作！");
        }
        zbPostRepository.delete(id);
        return new JmMessage(0,"删除成功！");
    }

    @ApiOperation("岗位分页查询")
    public PageItem<PostVo> queryPosts(PostQo postQo) throws Exception{
        String sqlList = "select p.*,u.user_name from zb_post p " +
                "LEFT JOIN zb_user u on p.user_id = u.user_id where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("p.user_id", postQo.getUserId()));
        sqlCondition.append(" order by p.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, postQo.getCurPage(), postQo.getPageSize());
        return ZbUserConverter.toPosts(PageItem);
    }

}
