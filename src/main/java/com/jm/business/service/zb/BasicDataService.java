package com.jm.business.service.zb;


import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.notice.*;
import com.jm.mvc.vo.zb.system.BasicDataCo;
import com.jm.mvc.vo.zb.system.BasicDataRo;
import com.jm.mvc.vo.zb.system.BasicDataUo;
import com.jm.repository.client.MsaClient;
import com.jm.repository.client.dto.zb.BusinessDto;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.BasicDataRepository;
import com.jm.repository.jpa.zb.system.DepartmentRepository;
import com.jm.repository.jpa.zb.system.JoinTypeRepository;
import com.jm.repository.jpa.zb.system.NoticeRepository;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbJoinType;
import com.jm.repository.po.zb.system.ZbNotice;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.staticcode.converter.zb.NoticeConverter;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by ME on 2016/8/17.
 */
@Service
public class BasicDataService {

    /**
     *
     * @param loginVo
     * @return
     */
    @Autowired
    private BasicDataRepository basicDataRepository;
    @Autowired
    private MsaClient msaClient;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private JoinTypeRepository joinTypeRepository;

    public ZbUser add(BasicDataCo basicDataCo){
        ZbUser zbUser = new ZbUser();
        BeanUtils.copyProperties(basicDataCo, zbUser);
        ZbUser newZbUser = basicDataRepository.save(zbUser);
        return newZbUser;
    }

    public void update(int id, BasicDataUo basicDataUo){
        ZbUser zbUser = basicDataRepository.findOne(id);
        zbUser.setSex(basicDataUo.getSex());
        zbUser.setHeadImg(basicDataUo.getHeadSculpture());
        basicDataRepository.save(zbUser);
    }

    public BasicDataRo getUser(int id){
        ZbUser zbUser =  basicDataRepository.findOne(id);
        BasicDataRo basicDataRo = new BasicDataRo();
        BeanUtils.copyProperties(zbUser,basicDataRo);
        return basicDataRo;
    }

    public List<BasicDataRo> queryBasicData(){
        List<ZbUser> zbUsers =  basicDataRepository.findAll();
        List<BasicDataRo> basicDataRos = new ArrayList<>();
        for (ZbUser zbUser : zbUsers){
            BasicDataRo basicDataRo = new BasicDataRo();
            BeanUtils.copyProperties(zbUser,basicDataRo);
            basicDataRos.add(basicDataRo);
        }
        return basicDataRos;
    }

    public ZbNotice saveNotice(NoticeCo noticeCo){
        return  noticeRepository.save(NoticeConverter.toNotice(noticeCo));
    }

    /**
     * 公告分页查询
     * @param
     * @return
     * @throws Exception
     */
    public PageItem<NoticeVo> queryNoticeList(NoticeQo noticeQo) throws Exception {
        String sqlList = "select * from zb_notice where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("look_type", noticeQo.getLookType()));
        sqlCondition.append(JdbcUtil.appendAnd("status", noticeQo.getStatus()));
        if (noticeQo.getBigType()!=null){
            if (noticeQo.getBigType()==2|| noticeQo.getBigType()==3){
                sqlCondition.append(JdbcUtil.appendAnd("user_id", noticeQo.getUserId()));
            }
            if (noticeQo.getBigType()==1&& noticeQo.getDepartmentId()!=null&&noticeQo.getLookType()==1){
                sqlCondition.append(JdbcUtil.appendLike("departments", noticeQo.getDepartmentId()));
                sqlCondition.append(JdbcUtil.appendOr("departments", "[0]"));
                sqlCondition.append(JdbcUtil.appendAnd("status", 2));
            }
        }
        sqlCondition.append(JdbcUtil.appendAnd("type", noticeQo.getType()));
        sqlCondition.append(JdbcUtil.appendAnd("create_date", noticeQo.getStartTime(), noticeQo.getEndTime()));
        sqlCondition.append(" order by create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, noticeQo.getCurPage(), noticeQo.getPageSize());
        return NoticeConverter.toNoticeList(PageItem);
    }

    public NoticeVo getNotice(int id){
        List<ZbDepartment>  list = departmentRepository.findAll();
        List<ZbJoinType>  list2 = joinTypeRepository.findAll();
        NoticeVo noticeVo;
        if (id==0){
            noticeVo = new NoticeVo();
        }else{
            noticeVo = NoticeConverter.toNoticeVo(noticeRepository.findOne(id));
        }
        noticeVo.setZbDepartmentList(list);
        noticeVo.setJoinList(list2);
        return noticeVo;
    }

    @Transactional
    public JmMessage deleteNotice(int id){
        noticeRepository.delete(noticeRepository.findOne(id));
        return new JmMessage(0,"删除成功");
    }

    public JmMessage updateNotice(int id){
        ZbNotice ZbNotice = noticeRepository.findOne(id);
        ZbNotice.setStatus(1);
        noticeRepository.save(ZbNotice);
        return new JmMessage(0,"撤回成功");
    }

    public NoticeConfiVo getNoticeConfig(int id){
        List<ZbDepartment>  list = departmentRepository.findAll();
        NoticeConfiVo noticeConfiVo = new NoticeConfiVo();
        noticeConfiVo.setZbDepartmentList(list);
        noticeConfiVo.setDepartments(noticeRepository.findOne(id).getDepartments());
        return noticeConfiVo;
    }
    public JmMessage updateNoticeConfig(NoticeConfiUo noticeConfiUo){
        ZbNotice ZbNotice = noticeRepository.findOne(noticeConfiUo.getId());
        ZbNotice.setDepartments(noticeConfiUo.getDepartments());
        noticeRepository.save(ZbNotice);
        return new JmMessage(0,"保存成功");
    }
    public List<ZbDepartment> queryNoticeConfig(int id){
        String departments = noticeRepository.findOne(id).getDepartments();
        List<String> list2 = Arrays.asList(departments.split(","));
        List<ZbDepartment> list3 = new ArrayList<ZbDepartment>();
        if(list2.contains("[0]")){
            ZbDepartment zbDepartment = new ZbDepartment();
            zbDepartment.setDepartmentName("全部");
            list3.add(zbDepartment);
        }else {
            ZbDepartment zbDepartment2;
            for(String list :list2){
                if (list.length()!=0){
                    zbDepartment2 = departmentRepository.findOne(Integer.valueOf(list.substring(1,list.length()-1)));
                    if (zbDepartment2 !=null){
                        list3.add(zbDepartment2);
                    }
                }
            }
        }
        return list3;
    }

    public JmMessage updateNotices(NoticeUo noticeUo){
        ZbNotice ZbNotice = noticeRepository.findOne(noticeUo.getId());
        Toolkit.copyPropertiesIgnoreNull(noticeUo, ZbNotice);
        ZbNotice.setCreateDate(new Date());
        noticeRepository.save(ZbNotice);
        return new JmMessage(0,"保存成功");
    }
    public NoticeVo queryNotice(int id){
        NoticeVo noticeVo = NoticeConverter.toNoticeVo(noticeRepository.findOne(id));
        return noticeVo;
    }

}
