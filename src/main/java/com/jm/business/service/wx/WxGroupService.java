package com.jm.business.service.wx;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.repository.client.dto.wxuser.WxGroupListDto;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.mvc.vo.wx.group.WxGroupReturnParam;
import com.jm.mvc.vo.wx.group.WxGroupVo;
import com.jm.repository.client.WxGroupClient;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.jpa.wx.WxGroupRepository;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserGroup;
import com.jm.staticcode.converter.wx.WxGroupConverter;

/**
 *<p>微信用户分组管理</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年7月29日
 */
@Service
public class WxGroupService {
	
	@Autowired
	private WxAuthService authService;
	@Autowired
	private WxGroupClient wxGroupClient;
	@Autowired
	private WxGroupRepository wxGroupRepository;
	@Autowired
	private WxUserService wxUserService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

	/**
	 * 从微信获取分组信息保存到数据库
	 * @author chenyy
	 * @data 2016年8月15日
	 */
    @Transactional
	public List<WxUserGroup> saveGroups(String accessToken,String appid) throws Exception{
		WxGroupListDto dto = wxGroupClient.getGroups(accessToken);
		List<WxUserGroup> groups = WxGroupConverter.d2p(dto,appid);
        wxGroupRepository.deleteWxUserGroupByAppid(appid);
        return wxGroupRepository.save(groups);
	}
	/**
	 * 调用接口创建微信分组
	 */
	public WxGroupVo createWxGroup(String appid,Map<String,Object> gruop) throws Exception{
		String accessToken = authService.getAuthAccessToken(appid);
		WxGroupReturnParam sss = wxGroupClient.createGroup(accessToken, gruop);
		return sss.getGroup();
	}
	/**
	 * 调用接口修改微信分组
	 */
	public ResultMsg updateWxGroup(String appid,Map<String,Object> group) throws Exception{
		String accessToken = authService.getAuthAccessToken(appid);
		return wxGroupClient.updateGroup(accessToken, group);
	}
	/**
	 * 调用接口删除微信分组
	 */
	public ResultMsg deleteWxGroup(String appid,Map<String,Object> group) throws Exception{
		String accessToken = authService.getAuthAccessToken(appid);
		return wxGroupClient.deleteGroup(accessToken, group);
	}
	/**
	 * 调用接口移动微信分组
	 */
	public ResultMsg moveWxGroup(String appid,Map<String,Object> map) throws Exception{
		String accessToken = authService.getAuthAccessToken(appid);
		return wxGroupClient.moveGroup(accessToken, map);
	}
	/**
	 * 调用接口批量移动微信分组
	 */
	public ResultMsg batchMoveWxGroup(String appid,Integer toGroupid,List<String>openids) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("openid_list", openids);
		map.put("to_groupid", toGroupid);
		String accessToken = authService.getAuthAccessToken(appid);
		return wxGroupClient.batchMoveGroup(accessToken, map);
	}
	/**
	 * 调用接口修改微信备注
	 */
	public ResultMsg updateWxMark(String appid,Map<String,Object> map) throws Exception{
		String accessToken = authService.getAuthAccessToken(appid);
		return wxGroupClient.updateMark(accessToken, map);
	}

	/**
	 * 获取自有数据库用户的分组
	 */
	public List<WxUserGroup> findAllGroup(String appid){
		 List<WxUserGroup> groups = wxGroupRepository.findWxUserGroupByAppid(appid);
		 return groups;
	}
	/**
	 * 在本地库创建分组
	 */
	public WxUserGroup createGroup(String appid,String name,Integer groupId){
		WxUserGroup wxUserGroup = new WxUserGroup();
		wxUserGroup.setAppid(appid);
		wxUserGroup.setName(name);
		wxUserGroup.setCount(0);
		wxUserGroup.setGroupid(groupId);
		return wxGroupRepository.save(wxUserGroup);
	}
	/**
	 * 修改本地数据库分组
	 */
	public WxUserGroup updateGroup(String appid,String name,Integer groupId){
		WxUserGroup userGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(appid, groupId);
		userGroup.setName(name);
		return wxGroupRepository.save(userGroup);
	}
	/**
	 * 删除本地数据库分组
	 */
	public void deleteGroupByGroupId(String appid,Integer groupId){
		WxUserGroup userGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(appid, groupId);
		int id = 0;
		if(null!=userGroup){
			id=userGroup.getId();
		}
		wxGroupRepository.delete(id);
	}
	/**
	 * 根据appid删除本地分组（取消授权需要删除）
	 */
    @Transactional
	public void deleteGroupByAppid(String appid){
		wxGroupRepository.deleteWxUserGroupByAppid(appid);
	}
	/**
	 * 本地数据库移动用户分组
	 */
	@Transactional
	public void moveGroup(String appid,WxGroupVo group){
		//先根据appid,userid,旧的分组id查询出信息，然后修改用户的新分组id
		WxUser wxUser = wxUserService.findWxUserByUserId(group.getUserId());
		wxUser.setGroupid(group.getGroupid());
		wxUserService.saveUser(wxUser);
		//移动完原来的分组人数-1，目标分组人数+1
		WxUserGroup oldGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(appid, wxUser.getGroupid());//旧分组
		WxUserGroup newGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(appid, group.getGroupid());//新分组
		oldGroup.setCount(oldGroup.getCount()-1);
		wxGroupRepository.save(oldGroup);
		newGroup.setCount(newGroup.getCount()+1);
		wxGroupRepository.save(newGroup);
	}

    /**
     * 本地数据库批量移动用户分组
     */
    @Transactional
    public void plMoveGroup(String userids,String appid,Integer groupId) throws Exception {
            String userIds[] = userids.split(",");
			List<Integer> ids = new ArrayList<>();
			for (String id:userIds){
				ids.add(Toolkit.parseObjForInt(id));
			}
             List<WxUser> wxusers = wxUserService.findWxUserByUserIds(ids);
             List<WxUser> newWxusers = new ArrayList<>();
             List<String> openids= new ArrayList<>();
            int count = 0;
              for (WxUser wxUser:wxusers) {
                    wxUser.setGroupid(groupId);
                    newWxusers.add(wxUser);
                    openids.add(wxUser.getOpenid());
                    count++;
              }
             batchMoveWxGroup(appid,groupId,openids);//批量移动微信分组
             wxUserService.saveWxUser(newWxusers);//移动本地用户分组

             WxUserGroup newGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(appid,groupId);
             newGroup.setCount(newGroup.getCount()+count);
             wxGroupRepository.save(newGroup);//添加新分组数量

            //扣除旧分组数量
            List<WxUserGroup> groups = getGroupsByUserIds(userids,appid);
            wxGroupRepository.save(groups);

    }

    private List<WxUserGroup> getGroupsByUserIds(String userids,String appid) throws IOException {
        String sql ="SELECT count(c.id) user_count,c.* from ( " +
                " select b.* from wx_user_group b " +
                " LEFT JOIN wx_user a on a.groupid = b.groupid " +
                " where a.user_id in("+userids+") and a.appid='"+appid+"' GROUP BY a.user_id " +
                " ) c  GROUP BY c.id ";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<WxUserGroup> groups = new ArrayList<>();
        for (Map<String, Object> map : list){
            WxUserGroup group =  JsonMapper.map2Obj(map,WxUserGroup.class);
			int newcount = group.getCount()-Toolkit.parseObjForInt(map.get("user_count"));
			if(newcount>0){
				group.setCount(newcount);
			}else{
				group.setCount(0);
			}
            groups.add(group);
        }
        return groups;
    }


}
