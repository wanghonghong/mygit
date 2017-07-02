package com.jm.business.service.wb;

import com.jm.business.domain.wb.WbRepeatMsg;
import com.jm.business.service.user.ShopUserService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.mvc.vo.wb.WbPushVo;
import com.jm.repository.client.dto.wb.*;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.wb.WbShopUserRepository;
import com.jm.repository.jpa.wb.WbUserGroupRepository;
import com.jm.repository.jpa.wb.WbUserRelRepository;
import com.jm.repository.jpa.wb.WbUserRepository;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserGroup;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.CustomerConverter;
import com.jm.staticcode.converter.wb.WbConverter;
import com.jm.staticcode.util.Toolkit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>微博用户分组</p>
 */
@Slf4j
@Service
public class WbUserGroupService {
    @Autowired
    private WbClient wbClient;
    @Autowired
    private WbUserRelRepository wbUserRelRepository;
    @Autowired
    private WbUserGroupRepository wbUserGroupRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WbUserRelService wbUserRelService;

    /**
     * 拉取微博用户分组并保存
     * @return
     * @throws Exception
     */
    public void getWbUserGroup(String accessToken, String ruleType,Long wbUid) throws Exception{
        WbGroupDto wbGroupDto = wbClient.getWbUserGroup(accessToken,ruleType);
        if (wbGroupDto.getError_code()==null){
            List<WbUserGroup> wbUserGroups = WbConverter.toWbUserGroupList(wbGroupDto,wbUid);
            for(WbUserGroup wbUserGroup:wbUserGroups){
                WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(wbUserGroup.getGroupid(),wbUserGroup.getWbUid());
                if (group!=null){
                    int id = group.getId();
                    Toolkit.copyPropertiesIgnoreNull(wbUserGroup,group);
                    group.setId(id);
                    wbUserGroupRepository.save(group);
                }else {
                    wbUserGroupRepository.save(wbUserGroup);
                }
            }
        } else {
            return ;
        }
    }

    /**
     * 查询商家所有用户所在分组并保存
     * @return
     * @throws Exception
     */
    @Transactional
    public void saveWbUserGroup(String accessToken,Long pid) throws Exception{
        List<WbUserRel> wbUserRels = wbUserRelRepository.findByPid(pid);
        for (WbUserRel wbUserRel:wbUserRels){
            WbUserGroupDto wbUserGroupDto = wbClient.queryWbUserGroup(accessToken,wbUserRel.getUid().toString());
            if (wbUserGroupDto.getError_code()==null){
                WbUserGroup wbUserGroup = wbUserGroupRepository.findByGroupidAndWbUid(wbUserGroupDto.getGroupid(),pid);
                if (wbUserGroup!=null){
                    wbUserRel.setGroupid(wbUserGroup.getId());
                    wbUserRelRepository.save(wbUserRel);
                }else {
                    getWbUserGroup(accessToken,"2",pid);
                    WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(wbUserGroupDto.getGroupid(),pid);
                    if (group!=null) {
                        wbUserRel.setGroupid(group.getId());
                        wbUserRelRepository.save(wbUserRel);
                    }else {
                        return;
                    }
                }
            }else {
                return;
            }
        }
    }

    /**
     * 查询单个用户所在分组并保存
     * @return
     * @throws Exception
     */
    @Transactional
    public void saveWbUserGroupForOne(String accessToken,Long pid,Long uid) throws Exception{
        WbUserRel rel = wbUserRelRepository.findWbUserRelByPidAndUid(pid,uid);
        WbUserGroupDto wbUserGroupDto = wbClient.queryWbUserGroup(accessToken,uid.toString());
        if (wbUserGroupDto.getError_code()==null) {
            WbUserGroup wbUserGroup = wbUserGroupRepository.findByGroupidAndWbUid(wbUserGroupDto.getGroupid(), pid);
            if (wbUserGroup != null) {
                rel.setGroupid(wbUserGroup.getId());
                wbUserRelRepository.save(rel);
            } else {
                getWbUserGroup(accessToken, "2", pid);
                WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(wbUserGroupDto.getGroupid(), pid);
                if (group != null) {
                    rel.setGroupid(group.getId());
                    wbUserRelRepository.save(rel);
                } else {
                    return;
                }
            }
        }
    }

    /**
     * 该微博下的所有分组
     * @param wbUid
     * @return
     */
    public List<WbUserGroup> findAllWbUserGroup(Long wbUid){
        return wbUserGroupRepository.findByWbUid(wbUid);
    }


    /**
     * 创建微博分组
     * @return
     * @throws Exception
     */
    public WbUserGroup createWbGroup(String accessToken, String name,Long wbUid) throws Exception{
        WbUserGroupDto wbUserGroupDto = wbClient.createGroup(accessToken,name);
        if (wbUserGroupDto.getError_code()==null){
           return wbUserGroupRepository.save(WbConverter.toWbUserGroup(wbUserGroupDto,wbUid));
        }else {
            log.info(wbUserGroupDto.getError());
            return null;
        }
    }

    /**
     * 初次删除微博分组
     * @return
     * @throws Exception
     */
    public JmMessage deleteWbGroup(String accessToken, Long groupid,Long pid) throws Exception{
        WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(groupid,pid);
        if (group!=null){
            List<WbUserRel> wbUserRels = wbUserRelRepository.findByGroupid(group.getId());
            if (wbUserRels.size()>0){
                return new JmMessage(1,"该分组下还有用户，是否要强制删除？");
            }else {
                WbUserGroupDto wbUserGroupDto = wbClient.deleteGroup(accessToken,groupid.toString());
                if ("0".equals(wbUserGroupDto.getError_code())){
                    wbUserGroupRepository.delete(group);
                    return new JmMessage(0,"删除成功");
                }else {
                    return new JmMessage(1,wbUserGroupDto.getError());
                }
            }
        }else {
            return new JmMessage(1,"删除失败，不存在该组！");
        }
    }

    /**
     * 二次（直接）删除微博分组
     * @return
     * @throws Exception
     */
    public JmMessage deleteGroup(String accessToken, Long groupid,Long pid) throws Exception{
        WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(groupid,pid);
        if(group!=null){
            WbUserGroupDto wbUserGroupDto = wbClient.deleteGroup(accessToken,groupid.toString());
            if ("0".equals(wbUserGroupDto.getError_code())){
                wbUserGroupRepository.delete(group);
                List<WbUserRel> wbUserRels = wbUserRelRepository.findByGroupid(group.getId());
                if (wbUserRels.size()>0){
                    WbUserGroup group2 = wbUserGroupRepository.findByGroupidAndWbUid(10000L,pid);
                    String sql = "update wb_user_rel set groupid = " +
                            group2.getId() + " where groupid = "+group.getId();
                    jdbcTemplate.update(sql);
                }
                getWbUserGroup(accessToken,"2",pid);
                return new JmMessage(0,"删除成功");
            }else {
                return new JmMessage(1,wbUserGroupDto.getError());
            }
        }
        return new JmMessage(1,"删除失败，不存在该组！");
    }

    /**
     * 修改分组名
     * @return
     * @throws Exception
     */
    public JmMessage updateGroup(String accessToken, Long groupid,String name,Long pid) throws Exception{
        WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(groupid,pid);
        if (group!=null){
            WbUserGroupDto wbUserGroupDto = wbClient.updateGroup(accessToken,groupid.toString(),name);
            if ("0".equals(wbUserGroupDto.getError_code())){
                group.setName(name);
                wbUserGroupRepository.save(group);
                return new JmMessage(0,"修改成功");
            }else {
                return new JmMessage(1,wbUserGroupDto.getError());
            }
        }else {
            return new JmMessage(1,"修改失败，不存在该组");
        }
    }

    /**
     * 移动用户分组
     * @return
     * @throws Exception
     */
    public JmMessage moveUserGroup(String accessToken, Long followerId,Long toGroupId,Long pid) throws Exception{
        WbUserGroup group = wbUserGroupRepository.findByGroupidAndWbUid(toGroupId,pid);
        if (group!=null){
            WbUserRel wbUserRel = wbUserRelService.getWbUserRel(followerId);
            if (wbUserRel.getIsSubscribe()==1){
                WbUserGroupDto wbUserGroupDto = wbClient.moveUserGroup(accessToken,followerId.toString(),toGroupId.toString());
                if ("0".equals(wbUserGroupDto.getError_code())){
                    wbUserRel.setGroupid(group.getId());
                    wbUserRelRepository.save(wbUserRel);
                    getWbUserGroup(accessToken,"2",pid);
                    return new JmMessage(0,"保存成功");
                }else {
                    return new JmMessage(1,wbUserGroupDto.getError());
                }
            }else { //非关注粉丝
                return new JmMessage(1,"该用户不是粉丝");
            }
        }else {
            return new JmMessage(1,"无法将用户移入不存在的组！");
        }
    }



    public List<WbUserGroup> findListById(List<Integer> ids){
        return wbUserGroupRepository.findAll(ids);
    }


}
