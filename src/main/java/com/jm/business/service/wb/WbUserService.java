package com.jm.business.service.wb;

import com.jm.business.domain.AreaDo;
import com.jm.business.domain.wb.WbRepeatMsg;
import com.jm.business.service.user.ShopUserService;
import com.jm.business.service.wx.WxGroupService;
import com.jm.business.service.wx.WxUserLevelService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.mvc.vo.wb.WbPushVo;
import com.jm.repository.client.dto.wb.*;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.jpa.wb.*;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.wb.WbShopUser;
import com.jm.repository.po.wb.WbUser;
import com.jm.repository.po.wb.WbUserGroup;
import com.jm.repository.po.wb.WbUserRel;
import com.jm.repository.po.wx.WxUserLevel;
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
import java.util.*;

/**
 * <p>微博服务层</p>
 * @version latest
 * @Author cj
 * @Date 2017/2/27 17:04
 */
@Slf4j
@Service
public class WbUserService {
    @Autowired
    private WbClient wbClient;
    @Autowired
    private WbShopUserRepository wbShopUserRepository;
    @Autowired
    private WbUserRepository wbUserRepository;
    @Autowired
    private WbUserRelRepository wbUserRelRepository;
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WbUserGroupService wbUserGroupService;
    @Autowired
    private WxUserLevelService wxUserLevelService;
    @Autowired
    private AreaRepository areaRepository;


    /**
     * 获取用户信息
     * @return
     * @throws Exception
     */
    public WbUserDto3 getWbUser(String accessToken, String uid) throws Exception{
        return wbClient.getWbUser(accessToken,uid);
    }

    /**
     * 获取用户的关注列表
     * @return
     * @throws Exception
     */
    public WbUserDto3 getWbFriends(String accessToken, String uid) throws Exception{
        return wbClient.getWbFriends(accessToken,uid);
    }

    /**
     * 获取某个用户最新发表的微博列表
     * @return
     * @throws Exception
     */
    public WbContenDto getNewWbContext(String accessToken, String uid) throws Exception{
        return wbClient.getNewWbContext(accessToken,uid);
    }

    /**
     * 获取当前登录用户及其所关注（授权）用户的最新微博
     * @return
     * @throws Exception
     */
    public WbContenDto getHomeWbContext(String accessToken, String uid) throws Exception{
        return wbClient.getHomeWbContext(accessToken,uid);
    }

    /**
     * 根据uid获取access_token
     */
    public String getAccessToken(Long uid) {
        return wbShopUserRepository.findWbShopUserByUid(uid).getAccessToken();
    }

    /**
     * 获取订阅列表
     * @return
     * @throws Exception
     */
    @Transactional
    public WbUidDto getWbUids(String accessToken, long pid) throws Exception{
        long nextUid = 0;
        WbUidDto wbUidDto =  wbClient.getFollowerUids(accessToken,nextUid);
        if (wbUidDto.getError_code()!=null){
            log.info(wbUidDto.getError());
        }else {
            while(wbUidDto.getNext_uid()!=null){
                saveFollowers(wbUidDto,accessToken,pid);//根据uids保存粉丝信息
                wbUidDto =  wbClient.getFollowerUids(accessToken, wbUidDto.getNext_uid());
            }
            saveFollowers(wbUidDto,accessToken,pid);
        }
        return wbUidDto;
    }

    /**
     * 根据uids保存粉丝信息
     * @return
     * @throws Exception
     */
    @Transactional
    public void saveFollowers(WbUidDto wbUidDto,String accessToken,long pid) throws Exception{
        Map<String,List<String>> data = wbUidDto.getData();
        List<String> uids = data.get("uids");
        List<WbUser> wbUsers = new ArrayList<>();
        List<WbUserRel> wbUserRels = new ArrayList<>();
        for(String uid : uids){
            WbUserDto wbUserDto = wbClient.getWbFollower(accessToken,uid);
            if (wbUserDto.getError_code()!=null){
                continue;
            }else {
                WbUserDto dto = this.getAreaCode(wbUserDto);
                WbUser wbUser = wbUserRepository.findOne(Long.valueOf(uid));
                if (wbUser!=null){
                    Toolkit.copyPropertiesIgnoreNull(dto,wbUser);
                    wbUsers.add(wbUser);
                }else {
                    wbUsers.add(WbConverter.toWbUser(dto));
                }
                WbUserRel rel = wbUserRelRepository.findWbUserRelByPidAndUid(pid,Long.valueOf(uid));
                if (rel!=null){
                    wbUserRels.add(WbConverter.toWbUserRelTwo(rel,pid,wbUserDto));
                }else {
                    wbUserRels.add(WbConverter.toWbUserRel(pid,wbUserDto));
                }
            }
        }
        wbUserRepository.save(wbUsers);
        wbUserRelRepository.save(wbUserRels);
    }

    /**
     * 处理微博发来的请求
     *
     * @param request
     * @return
     */
    public String processRequest(HttpServletRequest request,WbPushVo wbPushVo) throws Exception {
        if(isDuplicate(wbPushVo)){//消息排重
            return "";
        }
        String type = wbPushVo.getType();//推送类型
        String subtype = wbPushVo.getData().getSubtype();
        if ("event".equals(type)){//事件推送
            if ("subscribe".equals(subtype) || "follow".equals(subtype)){//订阅(关注)事件
                subscribe(wbPushVo);
            }else if ("unsubscribe".equals(subtype) || "unfollow".equals(subtype) ){//取消订阅(关注)事件
                unsubscribe(wbPushVo);
            }
        }
        return null;
    }

    private static final int MESSAGE_CACHE_SIZE = 500;//缓存的消息条数
    private static List<WbRepeatMsg> MESSAGE_CACHE_LIST = new ArrayList<WbRepeatMsg>(MESSAGE_CACHE_SIZE);

    /**
     * 消息排重
     * @param
     * @return
     */
    public static boolean isDuplicate(WbPushVo wbPushVo) {
        String fromUserName = wbPushVo.getSender_id().toString();
        String createTime = wbPushVo.getCreated_at();
        WbRepeatMsg repeatMsg  = new WbRepeatMsg();
        repeatMsg.setCreateTime(createTime);
        repeatMsg.setFromUserName(fromUserName);
        if (MESSAGE_CACHE_LIST.contains(repeatMsg)) {
            // 缓存中存在，直接pass
            return true;
        } else {
            if (MESSAGE_CACHE_LIST.size() >= MESSAGE_CACHE_SIZE) {
                MESSAGE_CACHE_LIST.clear();
            }
            MESSAGE_CACHE_LIST.add(repeatMsg);
            return false;
        }
    }

    /**
     * 订阅事件
     *
     * @param
     * @return
     */
    public void subscribe(WbPushVo wbPushVo) throws Exception {
        Long pid = wbPushVo.getReceiver_id();
        Long uid = wbPushVo.getSender_id();
        String accessToken = getAccessToken(pid);
        WbUser wbUser = wbUserRepository.findOne(uid);
        WbUserRel rel = wbUserRelRepository.findWbUserRelByPidAndUid(pid,uid);
        if (wbUser==null){
            WbUserDto wbUserDto = wbClient.getWbFollower(accessToken,uid.toString());
            if (wbUserDto.getError_code()==null){
                wbUserRepository.save(WbConverter.toWbUser(wbUserDto));
                wbUserRelRepository.save(WbConverter.toWbUserRel(pid,wbUserDto));
                wbUserGroupService.saveWbUserGroupForOne(accessToken,pid,uid);
            }else {
             return;
            }
        }else {
            if (rel==null){
                WbUserDto wbUserDto = wbClient.getWbFollower(accessToken,uid.toString());
                if (wbUserDto.getError_code()==null){
                    wbUserRelRepository.save(WbConverter.toWbUserRel(pid,wbUserDto));
                    wbUserGroupService.saveWbUserGroupForOne(accessToken,pid,uid);
                }else {
                    return;
                }
            }else {
                rel.setIsSubscribe(1);
                Date date = new Date(wbPushVo.getCreated_at());
                rel.setSubscribeTime(Toolkit.inteTimeToDate(date));
                wbUserRelRepository.save(rel);
                wbUserGroupService.saveWbUserGroupForOne(accessToken,pid,uid);
                return;
            }
        }
    }

    /**
     * 取消订阅事件
     *
     * @param
     * @return
     */
    public void unsubscribe(WbPushVo wbPushVo) throws Exception {
        Long pid = wbPushVo.getReceiver_id();
        Long uid = wbPushVo.getSender_id();
        String accessToken = getAccessToken(pid);
        WbUser wbUser = wbUserRepository.findOne(uid);
        WbUserRel rel = wbUserRelRepository.findWbUserRelByPidAndUid(pid,uid);
        if (wbUser==null){
            WbUserDto wbUserDto = wbClient.getWbFollower(accessToken,uid.toString());
            if (wbUserDto.getFollow()==0){
                return;
            }else {

            }
        }else {
            WbUserDto wbUserDto = wbClient.getWbFollower(accessToken,uid.toString());
            if (wbUserDto.getFollow()==0){
                if (rel==null){
                    if (wbUserDto.getError_code()==null){
                        wbUserDto.setUnSubscribeTime(wbPushVo.getCreated_at());
                        wbUserDto.setId(uid);
                        WbUserRel wbUserRel = WbConverter.toWbUserRel(pid,wbUserDto);
                        wbUserRel.setGroupid(0);
                        wbUserRelRepository.save(wbUserRel);
                    }else {
                        return;
                    }
                }else {
                    if (wbUserDto.getError_code()==null){
                        wbUserDto.setUnSubscribeTime(wbPushVo.getCreated_at());
                        rel.setPid(pid);
                        rel.setUid(uid);
                        rel.setIsSubscribe(0);
                        rel.setGroupid(0);
                        Date date = new Date(wbPushVo.getCreated_at());
                        rel.setUnSubscribeTime(Toolkit.inteTimeToDate(date));
                        wbUserRelRepository.save(rel);
                    }else {
                        return;
                    }
                }
            }

        }
    }

    public WbUser getWbUser(Long id){
        return wbUserRepository.findOne(id);
    }

    public PageItem<CustomerRo> findAllwb(HttpServletRequest request, CustomerQo qo, Integer type) throws ParseException, IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        //地区
        if( qo.getArea()!=null && !qo.getArea().equals("")){
            String areacode[] = qo.getArea().split(",");
            String area = "";
            for (int i = 0; i < areacode.length ; i++) {
                area += "'"+areacode[i]+"',";
            }
            qo.setArea(area);
        }
        //地区
        PageItem<CustomerRo> pageItem = new PageItem<CustomerRo>();
        if(type.equals(1)){ //客户总表
            pageItem= queryAllWbUser(qo,user.getUid(),0,0,user);
        }else if(type.equals(2)){ //关注客户
            pageItem= queryAllWbUser(qo,user.getUid(),1,0,user);
        }else if(type.equals(3)){ //跑路客户
            pageItem= queryAllWbUser(qo,user.getUid(),2,0,user);
        }else if(type.equals(4)){ //购买粉丝
            pageItem= queryAllWbUser(qo,user.getUid(),0,1,user);
        }else if(type.equals(5)){ //分销粉丝
            pageItem= queryFxUser(qo,user.getUid(),1,user);
        }else if(type.equals(6)){ //代理粉丝
            pageItem= queryFxUser(qo,user.getUid(),2,user);
        }else if(type.equals(7)){ //分享客
            pageItem= queryAllWbUser(qo,user.getUid(),1,2,user);
        }
        return pageItem;
    }


    /**
     * 查询客户总表
     * @param type  是否关注
     * @return
     */
    public PageItem<CustomerRo> queryAllWbUser(CustomerQo qo,Long uid,int type,int selecctType,JmUserSession user) throws IOException {
        String sqlList = " select b.headimgurl,b.nickname,b.sex,a.subscribe_time,a.id user_id,a.shop_user_id,a.is_subscribe,a.groupid,a.level_id,a.remark " +
                "from wb_user_rel a " +
                "left join wb_user b on a.uid = b.id " +
                "where a.pid = "+uid ;
        if(selecctType==1){  //购买用户
            sqlList+= JdbcUtil.appendAnd("a.is_buy",1);
        }else if(selecctType == 2){ //分享客
            sqlList+= JdbcUtil.appendAnd("a.is_share",1);
        }
        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getNikename()!=""&&null!=qo.getNikename()){
            sqlCondition.append(" and b.nickname like '%"+qo.getNikename()+"%' ") ;
        }
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append(" and a.subscribe_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        if(null!=qo.getSex()){
            sqlCondition.append(" and b.sex=" + qo.getSex() + " ");
        }
        if(type==1){ //是否关注
            sqlCondition.append(" and a.is_subscribe=1 ");
        }else if(type == 2){
            sqlCondition.append(" and a.is_subscribe=0 ");
        }else{
            if(qo.getIsSubscribe()!=null){
                sqlCondition.append(" and a.is_subscribe= "+qo.getIsSubscribe());
            }
        }
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());

        List<Integer> shopUserIds = new ArrayList<>();
        List<Integer> groupIds = new ArrayList<>();
        for (Map<String, Object> map : pageItem.getItems()) {
            if (Toolkit.parseObjForInt(map.get("shop_user_id")) != 0) {
                shopUserIds.add(Toolkit.parseObjForInt(map.get("shop_user_id")));
            }
            if (Toolkit.parseObjForInt(map.get("groupid")) != 0) {
                groupIds.add(Toolkit.parseObjForInt(map.get("groupid")));
            }
        }
        List<ShopUser> shopUsers = shopUserService.findListById(shopUserIds);//店铺用户
        List<WbUserGroup> groups = wbUserGroupService.findListById(groupIds);
        List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(user.getAppId());
        if(shopUsers!=null && shopUsers.size()>0){
            pageItem.setItems(Toolkit.converge(pageItem.getItems(), "shop_user_id", shopUsers, "id", new String[]{"userName", "phoneNumber"}, new String[]{"susername", "sphonenumber"}));//聚合数据
        }
        if(groups!=null && groups.size()>0){
            pageItem.setItems(Toolkit.converge(pageItem.getItems(), "groupid", groups, "id", "name", "groupname"));//聚合数据
        }
        if(levels!=null && levels.size()>0){
            pageItem.setItems(Toolkit.converge(pageItem.getItems(), "level_id", levels, "id", "levelName", "levelName"));//聚合数据
        }
        PageItem<CustomerRo> cuRos = CustomerConverter.wbp2v(pageItem);
        return cuRos;
    }

    /**
     * 查询分销用户
     */
    public PageItem<CustomerRo> queryFxUser(CustomerQo qo,Long uid,int selecctType,JmUserSession user) throws IOException {
        String sqlList = " select c.headimgurl,c.nickname,c.sex,a.subscribe_time,a.id user_id,a.groupid,a.level_id,a.is_subscribe,a.remark," +
                "b.agent_role,b.user_name susername,b.phone_number sphonenumber " +
                "from wb_user_rel a " +
                "LEFT JOIN shop_user b on a.shop_user_id = b.id " +
                "LEFT JOIN wb_user c on a.uid = c.id " +
                "where a.pid = "+uid ;
        if(selecctType==1){  //分销用户
            sqlList+= " and b.agent_role BETWEEN 5 and 7 ";
        }else if(selecctType==2){ //代理用户
            sqlList+= " and b.agent_role BETWEEN 1 and 4 ";
        }
        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getNikename()!=""&&null!=qo.getNikename()){
            sqlCondition.append(" and c.nickname like '%"+qo.getNikename()+"%' ") ;
        }
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append(" and a.subscribe_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        if(null!=qo.getSex()){
            sqlCondition.append(" and c.sex=" + qo.getSex() + " ");
        }
        if(qo.getAgentRole()!=null ){
            sqlList+= JdbcUtil.appendAnd("b.agent_role",qo.getAgentRole());
        }
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
        List<Integer> groupIds = new ArrayList<>();
        for (Map<String, Object> map : pageItem.getItems()) {
            if (Toolkit.parseObjForInt(map.get("groupid")) != 0) {
                groupIds.add(Toolkit.parseObjForInt(map.get("groupid")));
            }
        }
        List<WbUserGroup> groups = wbUserGroupService.findListById(groupIds);
        if(groups!=null && groups.size()>0){
            pageItem.setItems(Toolkit.converge(pageItem.getItems(), "groupid", groups, "id", "name", "groupname"));//聚合数据
        }
        List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(user.getAppId());
        if(levels!=null && levels.size()>0){
            pageItem.setItems(Toolkit.converge(pageItem.getItems(), "level_id", levels, "id", "levelName", "levelName"));//聚合数据
        }
        PageItem<CustomerRo> cuRos = CustomerConverter.wbp2v(pageItem);
        return cuRos;
    }

    /**
     * 定时更新 拉取所有商家粉丝列表
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateWbUserInfoTask() throws Exception{
        List<WbShopUser> wbShopUsers = wbShopUserRepository.findAll();
        if (wbShopUsers.size()>0){
            for(WbShopUser wbShopUser : wbShopUsers){
                getWbUids(wbShopUser.getAccessToken(),wbShopUser.getUid());
                wbUserGroupService.saveWbUserGroup(wbShopUser.getAccessToken(),wbShopUser.getUid());
            }
        }
    }

    /**
     * 拉取所有商家粉丝列表
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateWbUserGroupTask() throws Exception{
        List<WbShopUser> wbShopUsers = wbShopUserRepository.findAll();
        if (wbShopUsers.size()>0){
            for(WbShopUser wbShopUser : wbShopUsers){
                getWbUids(wbShopUser.getAccessToken(),wbShopUser.getUid());
                wbUserGroupService.saveWbUserGroup(wbShopUser.getAccessToken(),wbShopUser.getUid());
            }
        }
    }

    /**
     * 拉取所有商家粉丝分组
     * @return
     * @throws Exception
     */
    @Transactional
    public void getWbUserGroupTask() throws Exception{
        List<WbShopUser> wbShopUsers = wbShopUserRepository.findAll();
        if (wbShopUsers.size()>0){
            for(WbShopUser wbShopUser : wbShopUsers){
                if (wbShopUser.getIsGet()==0){
                    wbUserGroupService.getWbUserGroup(wbShopUser.getAccessToken(),"2",wbShopUser.getUid());
                }
            }
        }
    }


    /**
     * 拉取所有商家粉丝列表
     * @return
     * @throws Exception
     */
    @Transactional
    public void getWbUserInfoTask() throws Exception{
        List<WbShopUser> wbShopUsers = wbShopUserRepository.findAll();
        if (wbShopUsers.size()>0){
            for(WbShopUser wbShopUser : wbShopUsers){
                if (wbShopUser.getIsGet()==0){
                    getWbUids(wbShopUser.getAccessToken(),wbShopUser.getUid());
                    wbUserGroupService.saveWbUserGroup(wbShopUser.getAccessToken(),wbShopUser.getUid());
                    wbShopUser.setIsGet(1);
                    wbShopUserRepository.save(wbShopUser);
                }
            }
        }
    }

    /*
     * 查询微博用户areacode
     * @return
     * @throws Exception
     */
    public WbUserDto getAreaCode(WbUserDto wbUserDto)throws Exception{
        String city = wbUserDto.getCity();
        String province = wbUserDto.getProvince();
        if (null!=city && city!="" &&null!=province &&province!=""){
            province = province.substring(0,2);
            List<AreaDo> list = Constant.AREA_LIST;
            for (AreaDo areaDo:list) {
                String areaName = areaDo.getAreaName();
                if (areaName.length()>=city.length()){
                    areaName =  areaName.substring(0,city.length());
                }
                if (city.equals(areaName)){
                    Area area = areaRepository.findOne(areaDo.getParentAreaId());
                    if (null!=area){
                        String parentName =area.getAreaName().substring(0,2);
                        if (province.equals(parentName)){
                            wbUserDto.setAreaCode(areaDo.getAreaId());
                            break;
                        }
                    }

                }
            }
        }
        return wbUserDto;
    }
}
