package com.jm.business.service.zb;

import com.jm.business.service.system.JmJoinService;
import com.jm.business.service.system.RoleService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.join.DispatchJoinVo;
import com.jm.mvc.vo.join.DispatchVo;
import com.jm.mvc.vo.join.JoinVo;
import com.jm.mvc.vo.zb.dispatch.*;
import com.jm.mvc.vo.zb.join.CheckUo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.repository.client.MsaClient;

import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.DispatchHistoryRepository;
import com.jm.repository.jpa.zb.system.DispatchRepository;
import com.jm.repository.jpa.zb.system.JoinTypeRepository;
import com.jm.repository.jpa.zb.system.NoticeRepository;
import com.jm.repository.po.system.JmJoinCheck;
import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.zb.system.ZbDispatch;
import com.jm.repository.po.zb.system.ZbDispatchHistory;
import com.jm.repository.po.zb.system.ZbJoinType;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.staticcode.converter.zb.ReviewConverter;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>审核</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Service
public class ReviewService {
    @Autowired
    private MsaClient msaClient;
    @Autowired
    private ZbUserService zbUserService;
    @Autowired
    private DispatchRepository dispatchRepository;
    @Autowired
    private DispatchHistoryRepository dispatchHistoryRepository;
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JoinTypeRepository joinTypeRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private JmJoinService jmJoinService;
    @Autowired
    private RoleService roleService;

    public PageItem getJoinDtos(ZbJoinVo zbJoinVo) throws Exception {
        PageItem<JoinVo> joins = jmJoinService.queryJoinList(zbJoinVo);
        List<JoinVo> list = joins.getItems();
        if (list.size()>0){
            for (JoinVo joinVo : list) {
                String checkerId = Toolkit.parseObjForStr(joinVo.getCheckerId());
                if(null!=checkerId && ""!= checkerId){
                    ZbUser zbUser = zbUserService.getUserForUser(Integer.valueOf(checkerId));
                    if (null!=zbUser && null!=zbUser.getUserName() && ""!= zbUser.getUserName() ){
                        joinVo.setCheckerName(zbUser.getUserName());
                    }
                }
            }
        }
        joins.setItems(list);
        return  joins;
    }
    public JoinVo getJoinDto(ZbJoinVo zbJoinVo) throws IOException {
        JoinVo joinVo = jmJoinService.getJoinVo(zbJoinVo);
        List<JmJoinCheck> checkList = joinVo.getCheckList();
        if (checkList!=null){
            List<JmJoinCheck> jmJoinChecks = new ArrayList<>();
            for (JmJoinCheck jmJoinCheck : checkList) {
                ZbUser zbUser = zbUserService.getUserForUser(jmJoinCheck.getCheckerId());
                if (null!=zbUser && null!=zbUser.getUserName() && ""!= zbUser.getUserName()){
                    jmJoinCheck.setCheckerName(zbUser.getUserName());
                }
                jmJoinChecks.add(jmJoinCheck);
            }
            joinVo.setCheckList(jmJoinChecks);
        }
        return joinVo;
    }
    public void saveCheck(CheckUo checkUo) throws IOException {
        jmJoinService.addCheck(checkUo);
    }
    public JmMessage deleteJoin(ZbJoinVo zbJoinVo) throws IOException {
        jmJoinService.deleteJoin(zbJoinVo);
         return new JmMessage(0,"删除成功");
    }

    public List<Role> queryRoles() throws IOException {
        List<Role> roles = roleService.queryServiceRole();
        return roles;
    }

    /**
     * 保存业务派单
     * @param dispatchCo
     * @return
     */
    public ZbDispatch saveDispatch(DispatchCo dispatchCo) {
        ZbDispatch zbDispatch = new ZbDispatch();
        BeanUtils.copyProperties(dispatchCo, zbDispatch);
        return dispatchRepository.save(zbDispatch);
    }

    /**
     * 总部派单分页查询
     * @param
     * @return
     * @throws IOException
     */
    public PageItem<ZbDispatchVo> getDispatchList(DispatchQo dispatchQo) throws Exception {
        String sqlList = "select * from zb_dispatch d where 1=1 ";
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("d.source",dispatchQo.getSource()));
        sqlCondition.append(JdbcUtil.appendAnd("d.status",dispatchQo.getStatus()));
        sqlCondition.append(JdbcUtil.appendAnd("d.type",dispatchQo.getType()));
        sqlCondition.append(JdbcUtil.appendAnd("d.sub_type",dispatchQo.getSubType()));
        sqlCondition.append(JdbcUtil.appendAnd("d.business_name",dispatchQo.getBusinessName()));
        sqlCondition.append(JdbcUtil.appendAnd("d.phone_number",dispatchQo.getPhoneNumber()));
        sqlCondition.append(JdbcUtil.appendAnd("d.company_name",dispatchQo.getCompanyName()));
        sqlCondition.append(JdbcUtil.appendAnd("d.join_id",dispatchQo.getJoinId()));
        sqlCondition.append(JdbcUtil.appendAnd("d.create_date",dispatchQo.getStartTime(),dispatchQo.getEndTime()));
        sqlCondition.append(" order by d.create_date desc");
        PageItem<Map<String,Object>> PageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,dispatchQo.getCurPage(),dispatchQo.getPageSize());
        return ReviewConverter.toDispatchVo(PageItem);
    }

    public PageItem<DispatchJoinVo> getJoins(DispatchJoinQo dispatchJoinQo) throws Exception {
        return jmJoinService.queryJoins(dispatchJoinQo);
    }

    public DispatchVo queryDispatchVo() throws IOException {
        List<Role> roles = roleService.queryServiceRole();
        DispatchVo dispatchVo = new DispatchVo();
        dispatchVo.setRoles(roles);
        return dispatchVo;
    }

    /**
     * 保存派单历史
     * @param dispatchHistoryCo
     * @return
     */
    public void saveDispatchHistory(DispatchHistoryCo dispatchHistoryCo) {
        String str = dispatchHistoryCo.getDispatchId().substring(1,dispatchHistoryCo.getDispatchId().length()-1);
        Integer dispatchId = Integer.valueOf(str);
        ZbDispatch zbDispatch = dispatchRepository.findOne(dispatchId);
        zbDispatch.setJoinId(dispatchHistoryCo.getJoinId());
        zbDispatch.setStatus(2);
        dispatchRepository.save(zbDispatch);
        ZbDispatchHistory zbDispatchHistory = new ZbDispatchHistory();
        BeanUtils.copyProperties(dispatchHistoryCo, zbDispatchHistory);
        zbDispatchHistory.setDispatchId(dispatchId);
        dispatchHistoryRepository.save(zbDispatchHistory);
    }

    /**
     * 查询派单历史列表
     * @param dispatchId
     * @return
     */
    public List<ZbDispatchHistory> getDispatchHistoryList(Integer dispatchId) {
        List<ZbDispatchHistory> list = dispatchHistoryRepository.queryDispatchHistoryList(dispatchId);
        return list;
    }

    /**
     * 更新派单
     * @param dispatchId
     * @return
     */
    public void updateDispatch(Integer dispatchId) {
        ZbDispatch zbDispatch = dispatchRepository.findOne(dispatchId);
        zbDispatch.setStatus(5);
        dispatchRepository.save(zbDispatch);
    }

    public List<ZbJoinType> queryJoins(int id){
        String joins = noticeRepository.findOne(id).getJoins();
        List<String> list2 = Arrays.asList(joins.split(","));
        List<ZbJoinType> list3 = new ArrayList<ZbJoinType>();
        if(list2.contains("[0]")){
            ZbJoinType zbJoinType = new ZbJoinType();
            zbJoinType.setJoinName("全部");
            list3.add(zbJoinType);
        }
        ZbJoinType zbJoinType2;
        for(String list :list2){
            if (list.length()!=0){
                zbJoinType2 = joinTypeRepository.findOne(Integer.valueOf(list.substring(1,list.length()-1)));
                if (zbJoinType2 !=null){
                    list3.add(zbJoinType2);
                }
            }
        }
        return list3;
    }
}
