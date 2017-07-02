package com.jm.business.service.wx;

import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.system.AreaService;
import com.jm.business.service.user.ShopUserService;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.erp.ErpWxUserQo;
import com.jm.mvc.vo.erp.ErpWxUserRo;
import com.jm.mvc.vo.online.ChatUser;
import com.jm.mvc.vo.online.HxUserUo;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.CustomerRo;
import com.jm.mvc.vo.qo.UserQo;
import com.jm.mvc.vo.qo.WxUserQueryVo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQo;
import com.jm.mvc.vo.shop.commQrcode.ChannelRo;
import com.jm.mvc.vo.system.UserRo;
import com.jm.mvc.vo.wx.WxUserDetailRo;
import com.jm.mvc.vo.wx.content.WxContentVo;
import com.jm.mvc.vo.wx.wxuser.WxUserRo;
import com.jm.mvc.vo.wx.wxuser.WxUserVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.distribution.BrokerageOrderRepository;
import com.jm.repository.jpa.system.WxUserAddressRepository;
import com.jm.repository.jpa.wx.WxGroupRepository;
import com.jm.repository.jpa.wx.WxUserCardRepository;
import com.jm.repository.jpa.wx.WxUserRepository;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.system.Area;
import com.jm.repository.po.wx.*;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.GoodsQrcodeConverter;
import com.jm.staticcode.converter.shop.CustomerConverter;
import com.jm.staticcode.util.*;
import com.jm.staticcode.util.wx.Base64Util;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ui.ModelMap;

/**
 * <p>微信用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/18
 */
@Slf4j
@Service
public class WxUserService {

    @Autowired
    private WxUserRepository wxUserRepository;
    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private WxUserQrcodeService wxUserQrcodeService;
    @Autowired
    private WxMessageService wxMessageService;
    @Autowired
    private WxUserAddressRepository wxUserAddressRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired JdbcUtil jdbcUtil;
    @Autowired
    private WxUserLevelService wxUserLevelService;
    @Autowired
    private WxGroupService wxGroupService;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private WxGroupRepository wxGroupRepository;
    @Autowired
    private BrokerageOrderRepository brokerageOrderRepository;
    @Autowired
    private  WxUserAccountService wxUserAccountService;
    @Autowired
    private WxUserCardRepository wxUserCardRepository;
    @Autowired
    private AreaService areaService;



    public List<WxUser> findAll(List<Integer> userids){
        return wxUserRepository.findAll(userids);
    }

    public WxUser getWxUser(int userId){
        return wxUserRepository.findOne(userId);
    }


    public WxUser updateWxUserRemark(Integer userId, String remark) {
        WxUser wxUser1= getWxUser(userId);
        wxUser1.setRemark(remark);
        return wxUserRepository.save(wxUser1);
    }

    public Object  getWxUserUpperOneById(Integer wxuserId){
        Object o =wxUserRepository.getWxUserUpperOneById(wxuserId);
        return o;
    }

    public Object getWxUserUpperTwoById(Integer wxuserId) {
        Object o =wxUserRepository.getWxUserUpperTwoById(wxuserId);
        return o;
    }


    public List<WxUser> getWxUserUpperOneListById(Integer wxuserId) {
        return wxUserRepository.getWxUserUpperOneListById(wxuserId);
    }

    public List<WxUser> getWxUserUpperTwoListById(Integer wxuserId) {
        return wxUserRepository.getWxUserUpperTwoListById(wxuserId);
    }

    /**
     *<p>新增微信用户</p>
     *
     * @author chenyy
     * @version latest
     * @data 2016年6月15日
     */
    @Transactional
    public List<WxUser> saveWxUser(Iterable<WxUser> wxUsers){
        return wxUserRepository.save(wxUsers);
    }

    public WxUser findWxUserByAppidAndOpenid(String appid,String openid){
        return wxUserRepository.findWxUserByAppidAndOpenid(appid,openid);
    }

    public WxUser saveUser(WxUser wxUser){
        return wxUserRepository.save(wxUser);
    }

    @Transactional
    public void updateAllWxUser(String appid){
        wxUserRepository.updateAllWxUser(appid);
    }

    /**
     * 获取二维码
     * @param shareId
     * @param appId
     * @return 二维码地址
     * @throws Exception
     */
    public String getQrcodeUrl(Integer shareId,String appId) throws Exception{
        String qrcodeurl="";
        if(shareId!=null&&shareId!=0){
            WxUser shareWxuser = getWxUser(shareId);
            qrcodeurl =  wxUserQrcodeService.getBaseQrcode(shareWxuser,0,0);
        }
        return qrcodeurl;
    }

    public WxUser findWxUserByUserId(Integer userId){
        return wxUserRepository.findWxUserByUserId(userId);
    }

    public List<WxUser> findWxUserByUserIds(List<Integer> userIds){
        return wxUserRepository.findWxUserByUserIds(userIds);
    }


    public PageItem<Map<String,Object>> queryWxuser(WxUserQueryVo wxuesrQueryVo, String appid){
        return jdbcRepository.getWxUsers(wxuesrQueryVo,appid);
    }



    public PageItem<ChatUser> findReplyUsers(WxUserQueryVo wxuserVo, String appid) throws Exception {
        StringBuffer sql = new StringBuffer(jdbcRepository.getReplyUserQuery(wxuserVo,appid));
        String countSql = "select COUNT(1) from ("+sql.toString()+")A";
        int counts = jdbcTemplate.queryForInt(countSql);
        sql.append("   limit "+ wxuserVo.getCurPage()*wxuserVo.getPageSize()+","+wxuserVo.getPageSize());
        List<ChatUser> users = jdbcUtil.queryList(sql.toString(), ChatUser.class);
        PageItem<ChatUser> pageItem = new PageItem<>();
        pageItem.setCount(counts);
        pageItem.setItems(users);
        return pageItem;
    }

    public void sendMsg(WxUser wxuser,WxUser upUser,int shopId) throws Exception {
        if (upUser==null || wxuser == null){
            return;
        }
        if(shopId<=0){
            return;
        }
        WxTemplateDo tdo = new WxTemplateDo();
        tdo.setTouser(upUser.getOpenid());
        tdo.setUrl(Constant.DOMAIN+"/wxuser/lastuser?shopId="+shopId);
        tdo.setAppid(upUser.getAppid());
        tdo.setType(6);
        tdo.setFirst("新的下级微客产生啦");
        tdo.setWaitForTask("等待确认");
        tdo.setDelayTask("暂无");
        tdo.setRemark("好友【"+Base64Util.getFromBase64(wxuser.getNickname())+"】成为你的微客了，点击查看");
        wxMessageService.sendTemplate(tdo);

    }

    public void updateUsersLastMsgByHxAccount(List<HxUserUo> users){
        jdbcRepository.updateUsersLastMsgByHxAccount(users);
    }

    /**
     * 查询客户中心 新粉丝
     */
    public PageItem<CustomerRo> findNewFans(HttpServletRequest request, CustomerQo qo) throws ParseException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = "";
        if(null != user){
            appid = user.getAppId()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            ;
        }
        String sqlList = "SELECT u.*,ul.level_name,ug.name groupname,hu.is_reply,su.user_name susername,su.phone_number sphonenumber " +
                " from wx_user u  " +
                "LEFT JOIN wx_user_level ul on u.level_id = ul.id " +
                "LEFT JOIN wx_user_group ug on u.groupid = ug.groupid " +
                "LEFT JOIN hx_user hu on u.user_id = hu.user_id "+
                "left join shop_user su on u.shop_user_id = su.id "+
                "WHERE u.appid='"+appid+"' and ug.appid='"+appid+"'" ;
        sqlList += " AND NOT EXISTS (SELECT 1 FROM order_info oi WHERE oi.user_id = u.user_id  )";
        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getNikename()!=""&&null!=qo.getNikename()){
            sqlCondition.append(" and u.nickname like '%"+Base64Util.enCoding(qo.getNikename())+"%' ") ;
        }
        if(qo.getName()!=""&&null!=qo.getName()){
            sqlCondition.append(" and su.user_name like '%"+qo.getName()+"%' ");
        }
        if(qo.getPhoneNum()!=""&&null!=qo.getPhoneNum()){
            sqlCondition.append(" and su.phone_number='"+qo.getPhoneNum()+"'  ");
        }
        if(null!=qo.getSex()){
            sqlCondition.append(" and u.sex=" + qo.getSex() + " ");
        }
        if(qo.getArea()!=""&&null!=qo.getArea()){
            sqlCondition.append(" and u.area_code in(" + qo.getArea() + "'-1') ");
        }

        if(qo.getStarSubscribeTime().equals("") && qo.getEndSubscribeTime().equals("")){
            Date endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -2); //得到前两天
            Date startTime=  calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String start = formatter.format(startTime);
            String end = formatter.format(endTime);
            sqlCondition.append(" and u.subscribe_time BETWEEN '" + start + "' and '"+end+"'");
        }

        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append(" and u.subscribe_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        sqlCondition.append(" and u.is_subscribe=1 ");
        sqlCondition.append(" ORDER BY u.subscribe_time desc ");
        PageItem<Map<String,Object>> pageItem = jdbcRepository.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
        PageItem<CustomerRo> cuRos = new PageItem<>();
        try {
            cuRos =CustomerConverter.p2v(pageItem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cuRos;
    }

    public PageItem<UserRo> findUsers(HttpServletRequest request, UserQo qo,int type) throws ParseException, IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return  jdbcRepository.getUsers(qo,user.getShopId(),type);
    }

    public List<WxUserVo> getWxUserByHxAccounts(String[] hxAccounts) throws Exception {
        if(hxAccounts==null||hxAccounts.length<1){
            throw new Exception("查询条件为空");
        }
        List<Map<String,Object>> result = jdbcRepository.queryWxUserByHxAccounts(hxAccounts);
        List<WxUserVo> users = new ArrayList<>();
        for(Map<String,Object> row:result){
            WxUserVo user = BeanCopyUtils.mapToBean(row,WxUserVo.class);
            users.add(user);
        }
        return users;
    }

    public List<String> queryUser(WxContentVo wxContentVo,String appid){
    	String sqlList = "select u.openid from wx_user u left join shop_user s"
    			+ " on s.id=u.shop_user_id  where u.subscribe_time + INTERVAL 2 DAY <= SYSDATE()" +
                "and u.last_control_time + INTERVAL 2 DAY > SYSDATE() and u.appid='"+appid+"' and u.is_subscribe=1";

    	 StringBuilder sqlCondition = new StringBuilder("");
    	 if(wxContentVo.getGroupid()!=null && wxContentVo.getGroupid()>-1){
    		 sqlCondition.append(" and u.groupid='"+wxContentVo.getGroupid()+"'  ");
    	 }
    	 if(wxContentVo.getSex()!=null && wxContentVo.getSex()>-1){
    		 sqlCondition.append(" and u.sex='"+wxContentVo.getSex()+"'  ");
    	 }
    	 if(wxContentVo.getAreaIds().size()>0){
    		 String areaCode = "";
    		 int size = wxContentVo.getAreaIds().size();
    		 for (int i = 0; i < size; i++) {
    			 areaCode+=wxContentVo.getAreaIds().get(i)+",";
			}
    		 areaCode = areaCode.substring(0,areaCode.length()-1);
    		 sqlCondition.append(" and u.area_code in("+areaCode+")");
    	 }
    	 if(wxContentVo.getRole()!=null && wxContentVo.getRole()>-1){
    		 sqlCondition.append(" and s.agent_role ="+wxContentVo.getRole()+"  ");
    	 }
    	 sqlCondition.append(JdbcUtil.appendAnd("u.subscribe_time",wxContentVo.getSubStartDate(),wxContentVo.getSubEndtDate()));
    	
    	 List<Map<String,Object>> sss = jdbcTemplate.queryForList(sqlList+sqlCondition);
    	 List<String> openids = new ArrayList<>();
    	 for (Map<String, Object> map : sss) {
    		 openids.add(map.get("openid").toString());
		}
    	return openids;
    }


    public PageItem<Map<String,Object>> distriUser(CustomerQo qo,String appid) throws IOException {
        String sql = "select a.user_id,a.headimgurl,a.nickname,b.user_name susername,b.phone_number sphonenumber,a.frist_subscribe_time," +
                    "a.sex,b.agent_role,b.update_time,c.total_count,a.is_subscribe,a.area_code," +
                    "c.kit_balance balance,br.order_date,br.commission_price,a.level_id,a.groupid,b.create_time su_update_time " +
                    " from shop_user b " +
                    " LEFT JOIN wx_user a on a.shop_user_id = b.id " +
                    " LEFT JOIN wx_user_account c on c.user_id = a.user_id and c.account_type=1 " +
                    " LEFT JOIN ( select user_id,max(order_date) order_date,commission_price  FROM brokerage_order GROUP BY user_id ) br on br.user_id = a.user_id " +
                    " where 1=1 and a.appid = '"+appid+"'";
        sql+= JdbcUtil.appendLike("a.nickname",Base64Util.enCoding(qo.getNikename()));
        sql+= JdbcUtil.appendLike("b.user_name",qo.getName());
        sql+= JdbcUtil.appendAnd("b.phone_number",qo.getPhoneNum());
        sql+= JdbcUtil.appendAnd("a.sex",qo.getSex());

        if(qo.getAgentRole()!=null ){
            sql+= JdbcUtil.appendAnd("b.agent_role",qo.getAgentRole());
        }else{
            sql+= JdbcUtil.appendIn("b.agent_role","5,6,7");
        }
        if( qo.getArea()!="" && null!=qo.getArea() ){
            sql += " and a.area_code in(" + qo.getArea() + "'-1') ";
        }
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sql+= " and b.update_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'";
        }
        sql+=" order by br.order_date DESC,a.user_id DESC ";
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize());
        return pageItem;
    }

    /**
     * 查询代理客户
     * @param qo
     * @param appid
     * @return
     */
    public PageItem<Map<String,Object>> agentUser(CustomerQo qo,String appid){
        String sqlList = "SELECT u.user_id,u.nickname,u.headimgurl,u.sex,u.subscribe_time,u.is_subscribe,u.level_id,u.remark," +
                "u.groupid,u.shop_user_id,u.frist_subscribe_time,u.area_code" +
                ",su.phone_number sphonenumber,su.user_name susername,su.agent_role,u.sex,su.create_time su_create_time, " +
                "ua.total_count,ua.kit_balance balance,su.create_time su_update_time,br.order_date,br.commission_price " +
                "from shop_user su " +
                "LEFT JOIN wx_user u  on su.id = u.shop_user_id " +
                "LEFT JOIN wx_user_account ua on ua.user_id = u.user_id and ua.account_type=1 " +
                "LEFT JOIN ( select user_id,max(order_date) order_date,commission_price  FROM brokerage_order GROUP BY user_id ) br"+
                " on br.user_id = u.user_id " +
                "WHERE u.shop_user_id >0 and u.appid='"+appid+"' " ;

        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getNikename()!=""&&null!=qo.getNikename()){
            sqlCondition.append(" and u.nickname like '%"+Base64Util.enCoding(qo.getNikename())+"%' ") ;
        }
        if(qo.getName()!=""&&null!=qo.getName()){
            sqlCondition.append(" and su.user_name like '%"+qo.getName()+"%' ");
        }
        if(qo.getPhoneNum()!=""&&null!=qo.getPhoneNum()){
            sqlCondition.append(" and su.phone_number='"+qo.getPhoneNum()+"'  ");
        }
        if(null!=qo.getSex()){
            sqlCondition.append(" and u.sex=" + qo.getSex() + " ");
        }
        if(qo.getArea()!=""&&null!=qo.getArea()){
            sqlCondition.append(" and u.area_code in(" + qo.getArea() + "'-1') ");
        }
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append(" and su.create_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        if(qo.getAgentRole()!=null){
            sqlCondition.append( " and su.agent_role = "+qo.getAgentRole());
        }else {
            sqlCondition.append( " and su.agent_role BETWEEN 1 and 4 ");
        }
        sqlCondition.append(" ORDER BY u.subscribe_time desc ");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
        return pageItem;
    }


    /**
     *  根据用户ID获取用户地址列表
     * @param userId
     * @return
     */
    public List<WxUserAddress> findUserAddrByUserId(Integer userId){
        return wxUserAddressRepository.findUserAddrByUserId(userId);
    }

    public WxUserAddress findAddressByUserIdAndDefaultShow(Integer userId,int defaultShow){
    	return wxUserAddressRepository.findByUserIdAndDefaultShow(userId, defaultShow);
    }

    /**
     *  保存用户地址
     * @return
     */
    public WxUserAddress saveAddress(WxUserSession wxUserSession,WxUserAddress wxUserAddress){
    	List<WxUserAddress> addressList = findUserAddrByUserId(wxUserSession.getUserId());
		String newAdd = wxUserAddress.getProvice()+wxUserAddress.getCity()+wxUserAddress.getThird()+wxUserAddress.getAddress();
		newAdd = Toolkit.matcher("\"|\'|\r|\n|,",newAdd);
		String newPhone = wxUserAddress.getPhoneNumber();
		String userName= wxUserAddress.getUserName();
		boolean flag = true;
		WxUserAddress returnAddress = new WxUserAddress();
		log.info(newAdd);
		//判断是否已经存在，已经存在修改为默认显示
		for (WxUserAddress address : addressList) {
			if(newAdd.equals(address.getDetailAddress())
					&& newPhone.equals(address.getPhoneNumber())
					&& userName.equals(address.getUserName())){
				address.setDefaultShow(1); //显示状态为非默认显示的时候才修改为默认显示
				flag = false;
			}else{
				address.setDefaultShow(0);
			}
		}

		//匹配不到，做新增
		if(flag){
			//新增并且设为默认显示
			wxUserAddress.setUserId(wxUserSession.getUserId());
			wxUserAddress.setDefaultShow(1);
			wxUserAddress.setDetailAddress(newAdd);
			addressList.add(wxUserAddress);
		}
		List<WxUserAddress> returnAdds = wxUserAddressRepository.save(addressList);
		for (WxUserAddress returnAdd : returnAdds) {
			if(returnAdd.getDefaultShow()==1){
				returnAddress = returnAdd;
				break;
			}
		}
		return returnAddress;
    }

    @ApiOperation("总部平台 公众号列表")
    public PageItem<ErpWxUserRo> findErpWxUsers(ErpWxUserQo qo) throws IOException {
        String sql = " select a.user_id,a.headimgurl,a.sex,b.user_name,b.phone_number,a.nickname,a.is_buy,a.is_subscribe,b.agent_role,a.area_code,c.area_name " +
                     " from wx_user a " +
                     " LEFT JOIN shop_user b on a.shop_user_id = b.id " +
                     " LEFT JOIN wx_area c on c.area_id = a.area_code " +
                     " where 1=1  and a.appid='wx46506c1578a96cac' ";
        sql += JdbcUtil.appendLike("b.user_name",qo.getUserName());
        sql += JdbcUtil.appendLike("b.phone_number",qo.getPhoneNumber());
        if(null!=qo.getRole()){
            if(qo.getRole().equals(11)){  //跑路粉丝
                sql += JdbcUtil.appendAnd("a.is_subscribe",0);
            }else if(qo.getRole().equals(12)){ //关注粉丝
                sql += JdbcUtil.appendAnd("a.is_subscribe",1);
            }else if(qo.getRole().equals(13)){ //购买粉丝
                sql += JdbcUtil.appendAnd("a.is_subscribe",1);
            }else{ //渠道粉丝
                sql += JdbcUtil.appendAnd("b.agent_role",qo.getRole());
            }
        }
        if(null!=qo.getNickname()){
             sql += JdbcUtil.appendLike("a.nickname",Base64Util.enCoding(qo.getNickname()));
        }
        sql += JdbcUtil.appendAnd("a.sex",qo.getSex());
        if(qo.getStarTime()!=""&&null!=qo.getStarTime()&&qo.getEndTime()!=""&&null!=qo.getEndTime()){
            sql+=" and a.subscribe_time BETWEEN '" + qo.getStarTime() + "' and '"+qo.getEndTime()+"'";
        }
        int count = jdbcTemplate.queryForInt("SELECT count(*) from ( "+sql +") a");
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<ErpWxUserRo> userRos = new ArrayList<>();
        for (Map<String,Object> map : list){
            ErpWxUserRo ro =   JsonMapper.map2Obj(map,ErpWxUserRo.class);
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
            if (ro.getHeadimgurl()==null){
                ro.setHeadimgurl(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
            }
            userRos.add(ro);
        }
        PageItem<ErpWxUserRo> pageItem = new PageItem<>();
        pageItem.setCount(count);
        pageItem.setItems(userRos);
        return pageItem;
    }


    public PageItem<WxUserRo> getWxUserUpperOnePageItem(int wxuserId,WxUserQueryVo qo,JmUserSession jmUserSession) throws IOException {
        if(wxuserId<0){
            return null;
        }
        if(jmUserSession == null){
            return null;
        }
        String sql = "";
        if(qo.getLastType()==0){
            sql = "select a.headimgurl,a.nickname,b.user_name,b.phone_number,a.subscribe_time,a.upper_one,a.upper_two from wx_user a " +
                    "LEFT JOIN shop_user b on a.shop_user_id = b.id where a.upper_one= "+wxuserId +" or a.upper_two = "+wxuserId;
        }else if(qo.getLastType()==1){
            sql = "select a.headimgurl,a.nickname,b.user_name,b.phone_number,a.subscribe_time,a.upper_one,a.upper_two from wx_user a " +
                    "LEFT JOIN shop_user b on a.shop_user_id = b.id where a.upper_one= "+wxuserId;

        }else if(qo.getLastType()==2){
            sql = "select a.headimgurl,a.nickname,b.user_name,b.phone_number,a.subscribe_time,a.upper_one,a.upper_two from wx_user a " +
                    "LEFT JOIN shop_user b on a.shop_user_id = b.id where a.upper_two = "+wxuserId;
        }
        int count = jdbcTemplate.queryForInt("SELECT count(*) from ( "+sql +") a");
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<WxUserRo> ros = new ArrayList<>();
        for (Map<String,Object> map : list){
            WxUserRo ro =   JsonMapper.map2Obj(map,WxUserRo.class);
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
            ros.add(ro);
        }
        PageItem<WxUserRo> pageItem = new PageItem<>();
        pageItem.setCount(count);
        pageItem.setItems(ros);
        return pageItem;
    }



    public PageItem<ChannelRo> getChannelData(Integer shopId,ChannelQo qo) throws IOException {
        String sql = "select b.user_id,a.phone_number,a.agent_role,a.user_name,b.nickname,b.headimgurl from shop_user a " +
                    "LEFT JOIN wx_user b on a.id = b.shop_user_id " +
                    "where a.shop_id="+shopId+" and a.agent_role  BETWEEN 1 and 7";
        sql += JdbcUtil.appendAnd("a.phone_number",qo.getPhoneNumber());
        PageItem<Map<String,Object>> pageItem = jdbcRepository.queryPageItem(sql,qo.getCurPage(),qo.getPageSize());
        return  GoodsQrcodeConverter.p2vs(pageItem);
    }


    public  List<WxUser>  findWxUserByShopUserId (Integer shopUserId){
        return wxUserRepository.findWxUserByShopUserId(shopUserId);
    }


    public PageItem<ChatUser> findOldFans(WxUserQueryVo wxuserQo) throws IllegalAccessException, IOException, InstantiationException {
        String field = "wu.*, hu.hx_account as account, hu.last_msg, hu.is_reply ";
        StringBuffer sql = new StringBuffer("SELECT "+field+" FROM wx_user wu LEFT JOIN hx_user hu ON hu.user_id = wu.user_id WHERE ");
        sql.append(" wu.appid ='"+wxuserQo.getAppid()+"'");
        sql.append(" AND wu.subscribe_time < date_sub(now(), interval 48 hour) ");
        sql.append(" AND wu.last_control_time >= DATE_SUB(now(),INTERVAL 48 hour)");
        sql.append(" AND NOT EXISTS (SELECT 1 FROM order_info O WHERE O.user_id = wu.user_id AND O.`status` = 0 )");
        StringBuilder sqlCondition = new StringBuilder("");
        if(wxuserQo.getNikename()!=null){
            sqlCondition.append(jdbcUtil.appendLike("wu.nickname",Base64Util.enCoding(wxuserQo.getNikename())));
        }
        if(wxuserQo.getName()!=null&&!wxuserQo.getName().equals("")){
            sqlCondition.append(" AND EXISTS ( SELECT 1 FROM shop_user su WHERE su.id = wu.shop_user_id AND su.user_name LIKE '%"+wxuserQo.getName()+"%' )");
        }
        if(wxuserQo.getPhoneNum()!=null&&!wxuserQo.getPhoneNum().equals("")){
            sqlCondition.append(" AND EXISTS ( SELECT 1 FROM shop_user su WHERE su.id = wu.shop_user_id AND su.phone_number = "+wxuserQo.getPhoneNum()+" )");
        }
        sqlCondition.append(jdbcUtil.appendAnd("wu.sex",wxuserQo.getSex()));
        if(wxuserQo.getArea()!=""&&null!=wxuserQo.getArea()){
            sqlCondition.append(" and u.area_code in(" + wxuserQo.getArea() + "'-1') ");
        }
        sql.append(sqlCondition);
        PageItem<ChatUser> pageItem = jdbcUtil.queryPageItem(sql.toString(),wxuserQo.getCurPage(),wxuserQo.getPageSize(),ChatUser.class);
        List<ChatUser> list = pageItem.getItems();
        for(ChatUser chatUser :list){
            if(chatUser.getNickname()!=null&&!chatUser.getNickname().equals("")){
                chatUser.setNickname(Base64Util.getFromBase64(chatUser.getNickname()));
            }
            if(chatUser.getLastMsg()!=null){
                chatUser.setLastMsg(Base64Util.getFromBase64(chatUser.getLastMsg()));
            }
        }
        return pageItem;
    }

    public PageItem<CustomerRo> newfindAll(HttpServletRequest request, CustomerQo qo,Integer type) throws ParseException, IOException {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String appid = "";
        if(null != user){
            appid = user.getAppId();
        }
        //地区
        if( qo.getArea()!=null && !qo.getArea().equals("")){
            String areacode[] = qo.getArea().split(",");
            String area = "";
            for (int i = 0; i < areacode.length ; i++) {
                area += "'"+areacode[i]+"',";
                area.replace("","");
            }
            qo.setArea(area);
        }
        //地区
        PageItem<CustomerRo> pageItem = new PageItem<CustomerRo>();
        PageItem<Map<String,Object>> mapPageItem = new PageItem<>();
        List<Integer> shopUserIds = new ArrayList<>();
        HashSet<Integer> areaIds = new HashSet<>();
        if(type.equals(1)){ //全部粉丝 关注粉丝 跑路粉丝
            mapPageItem= newqueryAllUser(qo,appid);
        }else if(type.equals(3)){ //购买客户
            mapPageItem = buyUser(qo,appid);
        }else if(type.equals(4)){ //分销客户
            mapPageItem = distriUser(qo,appid);
        }else if(type.equals(5)){ //代理客户  分享客
            mapPageItem = agentUser(qo,appid);
        }

        if(type.equals(1)) {
            for (Map<String, Object> map : mapPageItem.getItems()) {
                if (Toolkit.parseObjForInt(map.get("shop_user_id")) != 0) {
                    shopUserIds.add(Toolkit.parseObjForInt(map.get("shop_user_id")));
                }
                if (Toolkit.parseObjForInt(map.get("area_code")) != 0) {
                    areaIds.add(Toolkit.parseObjForInt(map.get("area_code")));
                }
            }
            List<ShopUser> shopUsers = shopUserService.findListById(shopUserIds);//店铺用户
            mapPageItem.setItems(Toolkit.converge(mapPageItem.getItems(), "shop_user_id", shopUsers, "id", new String[]{"userName", "phoneNumber","agentRole"}, new String[]{"susername", "sphonenumber","agentRole"}));//聚合数据
            List<Area> areas =  areaService.findAll(areaIds);
            mapPageItem.setItems(Toolkit.converge(mapPageItem.getItems(), "area_code", areas, "areaId", "areaName", "areaName"));//聚合数据
        }
            List<WxUserLevel> levels = wxUserLevelService.findAllWxUserLevel(appid);//等级
            mapPageItem.setItems(Toolkit.converge(mapPageItem.getItems(), "level_id", levels, "id", "levelName", "levelName"));//聚合数据
            List<WxUserGroup> groups = wxGroupService.findAllGroup(appid);//分组
            mapPageItem.setItems(Toolkit.converge(mapPageItem.getItems(), "groupid", groups, "groupid", "name", "groupname"));//聚合数据
            pageItem = CustomerConverter.p2vv(mapPageItem);
        return pageItem;
    }



    /**
     * 查询客户总表
     * @param qo
     * @param appid
     * @return
     */
    public PageItem<Map<String,Object>> newqueryAllUser(CustomerQo qo,String appid) throws IOException {
        String sqlList = " select u.user_id,u.nickname,u.headimgurl,u.sex,u.subscribe_time,u.is_subscribe,u.level_id,u.remark," +
                "u.groupid,u.shop_user_id,u.frist_subscribe_time,u.un_subscribe_time,u.area_code " +
                " from wx_user u " +
                " where u.appid='"+appid+"' and u.is_subscribe!=2 ";
        sqlList += JdbcUtil.appendAnd("u.user_id",qo.getUserId());
        sqlList += JdbcUtil.appendLike("u.nickname",Base64Util.enCoding(qo.getNikename()));
        sqlList += JdbcUtil.appendAnd("u.sex",qo.getSex());
        sqlList += JdbcUtil.appendAnd("u.is_subscribe",qo.getIsSubscribe());
        if(qo.getArea()!=null && !qo.getArea().equals("")){
            sqlList += JdbcUtil.appendIn("u.area_code",qo.getArea()+"'-1'");
        }
        String shopUserIds = getShopUserIds(qo.getName(),qo.getPhoneNum()); //店铺用户条件
        if(shopUserIds!=null && !shopUserIds.equals("")){
            sqlList += JdbcUtil.appendIn("u.shop_user_id",shopUserIds);
        }

        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append(" and u.subscribe_time BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        sqlCondition.append(" ORDER BY u.user_id desc ");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
        return pageItem;
    }

    /**
     * 用户购买数据
     * @param userIds
     * @return
     */
    public List<Map<String,Object>> getBuyData(List<Integer> userIds){
        if(userIds!=null && userIds.size()>0){
            String ids = "";
            for (Integer id:userIds) {
                ids = id+","+ids;
            }
            if(ids.length()>0){
                ids = ids.substring(0,ids.length()-1);
                String sql  = " select c.user_id,max(c.create_date) lasttime,sum(c.send_fee+c.total_price) allprice," +
                        " count(c.user_id) as frequency,avg(c.send_fee+c.total_price) as average from order_info c" +
                        " where c.user_id in(" +ids +")"+
                        " group by c.user_id";
                List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
                return list;
            }
        }
        return null;
    }


    /**
     * 查询购买粉丝
     * @param qo
     * @param appid
     * @return
     */
    public PageItem<Map<String,Object>> buyUser(CustomerQo qo,String appid) throws IOException {
        String sqlList = " select a.user_id,a.headimgurl,a.nickname,a.shop_user_id," +
                " a.is_subscribe,a.groupid,a.level_id,a.remark,a.frist_subscribe_time,a.area_code," +
                " a.sex,max(c.create_date) lasttime,sum(c.send_fee+c.total_price) allprice, " +
                " count(c.user_id) as frequency,avg(c.send_fee+c.total_price) as average " +
                " from wx_user a " +
                " left join order_info c on a.user_id = c.user_id " +
                " where 1=1 and a.is_buy = 1 ";
        sqlList += JdbcUtil.appendLike("a.nickname",Base64Util.enCoding(qo.getNikename()));
        sqlList += JdbcUtil.appendAnd("a.sex",qo.getSex());
        StringBuilder sqlCondition = new StringBuilder("");
        if(qo.getArea()!=""&&null!=qo.getArea()){
            sqlCondition.append(" and a.area_code in(" + qo.getArea() + "'-1')");
        }
        if(qo.getStarSubscribeTime()!=""&&null!=qo.getStarSubscribeTime()&&qo.getEndSubscribeTime()!=""&&null!=qo.getEndSubscribeTime()){
            sqlCondition.append( " and c.create_date BETWEEN '" + qo.getStarSubscribeTime() + "' and '"+qo.getEndSubscribeTime()+"'");
        }
        String shopUserIds = getShopUserIds(qo.getName(),qo.getPhoneNum()); //店铺用户条件
        if(shopUserIds!=null && !shopUserIds.equals("")){
            sqlList += JdbcUtil.appendIn("a.shop_user_id",shopUserIds);
        }
        sqlCondition.append( " and a.appid='"+appid+"' group by c.user_id order by c.create_date desc ");
        PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,qo.getCurPage(),qo.getPageSize());
        return pageItem;
    }


    /**
     * 根据用户名 、手机号 获取店铺用户编号
     * @param userName
     * @param phoneNumber
     * @return
     */
    public String getShopUserIds(String userName,String phoneNumber){
        List<ShopUser> shopUsers = null;
        String shopUserIds = "";
        if(userName!=""&&null!=userName && phoneNumber!=""&&null!=phoneNumber){
            shopUsers = shopUserService.findByLikeUserNameAndPhoneNumber(userName,phoneNumber);
            if(shopUsers==null || shopUsers.size()<=0){
                return "-1";
            }
        }else{
            if(userName!=""&&null!=userName){
                shopUsers = shopUserService.findByLikeUserName(userName);
                if(shopUsers==null || shopUsers.size()<=0){
                    return "-1";
                }
            }
            if(phoneNumber!=""&&null!=phoneNumber){
                shopUsers = shopUserService.findByLikePhoneNumber(phoneNumber);
                if(shopUsers==null || shopUsers.size()<=0){
                    return "-1";
                }
            }
        }
        if(shopUsers!=null && shopUsers.size()>0){
            for (ShopUser shopUser:shopUsers) {
                shopUserIds = shopUser.getId()+","+shopUserIds;
            }
        }
        if(shopUserIds.length()>0){
            shopUserIds = shopUserIds.substring(0,shopUserIds.length()-1);
        }
        return shopUserIds;
    }


    public WxUserDetailRo getUserDetail(String appid, Integer userId){
       WxUser wxUser = wxUserRepository.findByAppidAndUserId(appid,userId);
       WxUserDetailRo ro = new WxUserDetailRo();
       ro.setUserId(userId);
       if(wxUser == null){
           return null;
       }
        if(wxUser.getShopUserId()!=null){
            ShopUser shopUser = shopUserService.findShopUser(wxUser.getShopUserId());//店铺用户
            if(shopUser!=null){
                ro.setUserName(shopUser.getUserName());
                ro.setPhoneNumber(shopUser.getPhoneNumber());
                ro.setAgentRole(shopUser.getAgentRole()+"");
            }
        }
        WxUserGroup wxUserGroup = wxGroupRepository.findWxUserGroupByAppidAndGroupid(wxUser.getAppid(),wxUser.getGroupid());//分组
        WxUserLevel wxUserLevel = new WxUserLevel();
        if(wxUser.getLevelId()!=null){
            wxUserLevel = wxUserLevelService.findWxUserLevelById(wxUser.getLevelId());//等级
        }
        if(wxUser.getUpperOne()!=null){
            WxUser upWxUser = wxUserRepository.findOne(wxUser.getUpperOne());
            if(upWxUser!=null){
                ro.setUpNickname(Base64Util.getFromBase64(upWxUser.getNickname()));
                if(upWxUser.getShopUserId()!=null){
                    ShopUser shopUser = shopUserService.findShopUser(upWxUser.getShopUserId());//店铺用户
                     ro.setUpUserName(shopUser.getUserName());
                     ro.setUpPhoneNumber(shopUser.getPhoneNumber());
                }
            }
        }
        Object one = wxUserRepository.getWxUserUpperOneById(wxUser.getUserId());//下一级数量
        Object two = wxUserRepository.getWxUserUpperTwoById(wxUser.getUserId());//下二级数量
        List<WxUserAddress> addresses = wxUserAddressRepository.findUserAddrByUserId(wxUser.getUserId());//收货地址

        Object sumPrice = brokerageOrderRepository.getSumPriceNot3(wxUser.getUserId());
        Integer intPrice = Toolkit.parseObjForInt(sumPrice);
        WxUserAccount brokerAccount = wxUserAccountService.findWxUserAccountById(wxUser.getUserId(),1);//佣金账户
        WxUserAccount jfAccount = wxUserAccountService.findWxUserAccountById(wxUser.getUserId(),2);//积分账户
        int noSendBro = intPrice;
        if(brokerAccount!=null){
            noSendBro = intPrice - brokerAccount.getTotalCount() + brokerAccount.getKitBalance();//未发佣金
        }
        if(jfAccount!=null){
            ro.setSumIntegral(jfAccount.getTotalCount());
            ro.setIntegral(jfAccount.getKitBalance());
        }
        Object cardCount = wxUserCardRepository.cardCountByUserId(wxUser.getUserId());//累计礼券
        Object cardCount1 = wxUserCardRepository.cardCountByUserIdAndStatus(wxUser.getUserId(),1);//未用卡券
        ro.setCard(Toolkit.parseObjForInt(cardCount1));
        ro.setSumCard(Toolkit.parseObjForInt(cardCount));
        ro.setNoSendBrokerage(noSendBro);
        ro.setSumBrokerage(intPrice);
        ro.setNickname(Base64Util.getFromBase64(wxUser.getNickname()));
        ro.setRemark(wxUser.getRemark());
        ro.setSex(wxUser.getSex());
        if(wxUserGroup!=null){
            ro.setGroupName(wxUserGroup.getName());
        }
        if(wxUserLevel!=null) ro.setLevelName(wxUserLevel.getLevelName());
        List<String> adds = new ArrayList<>();
        for (WxUserAddress address:addresses) {
            adds.add(address.getDetailAddress());
        }
        ro.setAdds(adds);
        ro.setOneCount(Toolkit.parseObjForInt(one));
        ro.setTwoCount(Toolkit.parseObjForInt(two));
        return  ro;
    }

    List<WxUser> findByShopUserId(Integer shopUserId){
      return wxUserRepository.findWxUserByShopUserId(shopUserId);
    }
}



