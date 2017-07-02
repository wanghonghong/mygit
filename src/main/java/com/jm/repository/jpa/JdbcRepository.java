package com.jm.repository.jpa;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.online.ChatUser;
import com.jm.mvc.vo.online.HxUserUo;
import com.jm.mvc.vo.qo.CustomerQo;
import com.jm.mvc.vo.qo.UserQo;
import com.jm.mvc.vo.qo.WxUserQueryVo;
import com.jm.mvc.vo.system.UserRo;
import com.jm.staticcode.converter.UserConverter;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.wx.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>JDBC查询</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/15
 */
@Slf4j
@Repository
public class  JdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 
     *<p>app端根据order_info_id来查询</p>
     *
     * @author liangrs
     * @version latest
     * @data 2016年6月13日
     */
    public PageItem<Map<String, Object>> getOrderByIds(String orderInfoId){
    	String sql = "SELECT p.name,p.pic_square,p.pid,oi.order_info_id,oi.status,oi.remark,oi.total_price,oi.send_fee,oi.user_id,oi.shop_id,od.price,oi.real_price,"
    			   + "od.count,ps.product_spec_id,ps.spec_pic,ps.spec_value_one,ps.spec_value_two,ps.spec_value_three FROM order_info oi LEFT JOIN order_detail od "
    			   + "on od.order_info_id=oi.order_info_id LEFT JOIN product p ON p.pid = od.pid LEFT JOIN product_spec ps ON ps.product_spec_id = od.product_spec_id "
    			   + "where oi.order_info_id in("+orderInfoId+")";
      	List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
      	PageItem<Map<String, Object>> pageItem = new PageItem<>();
      	pageItem.setItems(list);
		return pageItem;
    }
    

	public int queryBuyCountByUserId(int userId,Integer pid) {
		String sql = "SELECT sum(g.count) as count FROM (SELECT o.user_id, d.count AS count, d.pid FROM order_detail d, order_info o " +
				" where d.order_info_id = o.order_info_id " +
				" and d.pid = " + pid +
				" AND o.user_id =  " + userId +
				" AND o.status IN (0,1,2,3,5))g ";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		int count = 0;
		log.info("------------String.valueOf(map.get(count))--------------"+String.valueOf(map.get("count")));
		if ("null" != String.valueOf(map.get("count"))){
			count =Integer.parseInt(map.get("count").toString());
			log.info("------------count--------------"+count);
		}
		return count;
	}

	public List<Map<String,Object>> getPv(String strToday, String strTomday) {
		String sql = "SELECT count(pid) as count, pid FROM jm_visit j WHERE 1 = 1 and j.type=1 " +
				" AND j.visit_time >='" +strToday+
				"' AND j.visit_time < '" +strTomday+
				"' GROUP BY Pid ORDER BY pid";
		List<Map<String, Object>> listMap = jdbcTemplate.queryForList(sql);
		return listMap;
	}

	public List<Map<String,Object>> getUv(String strToday, String strTomday) {
		String sql = "SELECT count(t.cou) as count,t.pid FROM ( SELECT count(pid) AS cou, pid FROM jm_visit j WHERE 1 = 1 and j.type=1 " +
				" AND j.visit_time >='" +strToday+
				"'AND j.visit_time < '" +strTomday+
				"' GROUP BY j.Pid, j.ip ORDER BY pid ) t GROUP BY t.pid";
		List<Map<String, Object>> listMap = jdbcTemplate.queryForList(sql);
		return listMap;
	}
    /**
     * 获取微信用户分页
     */
    public PageItem<Map<String, Object>> getWxUsers(WxUserQueryVo wxuserVo, String appid){

		StringBuffer sql = new StringBuffer(getReplyUserQuery(wxuserVo,appid));
        String countSql = "select COUNT(1) from ("+sql.toString()+")A";
        int counts = jdbcTemplate.queryForInt(countSql);
		sql.append("   limit "+ wxuserVo.getCurPage()*wxuserVo.getPageSize()+","+wxuserVo.getPageSize());
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql.toString());
        PageItem pageItem  = new PageItem();
        pageItem.setCount(counts);
        pageItem.setItems(list);
        return pageItem;
    }
	public String getReplyUserQuery(WxUserQueryVo wxuserVo, String appid){
		String field = " T.nickname AS nickname,T.user_name AS NAME,T.phone_number AS phoneNum,T.area_code AS area,T.sex AS sex,T.subscribe_time AS starSubscribeTime,T.headimgurl,T.user_id AS user_id,";
		field +="HX.last_chat_date AS lastDate,HX.last_msg AS lastMsg,T.remark,L.level_name as levelName,G.name as groupName,T.appid AS appid,T.openid as openid,T.last_buy_time as endBuyTime,agent_role as agentRole ";
		StringBuffer sql = new StringBuffer("SELECT "+field+" FROM wx_user T ");
		sql.append(" LEFT JOIN  wx_user_level L ON T.level_id = L.id AND L.appid = '"+appid+"' ");
		sql.append(" LEFT JOIN  wx_user_group G ON G.groupid = T.groupid AND G.appid = '"+appid+"'");
		sql.append(" LEFT JOIN hx_user HX ON T.user_id = HX.user_id ");
		sql.append(" LEFT JOIN shop_user su ON T.shop_user_id = su.id ");
		sql.append(" where T.appid = '"+appid+"' AND T.is_subscribe = 1 AND HX.last_msg is not null");

		sql.append(JdbcUtil.appendLike("T.nickname",Base64Util.enCoding(wxuserVo.getNikename())));
		if(null!=wxuserVo.getStarSubscribeTime()&&!"".equals(wxuserVo.getStarSubscribeTime())){
			sql.append(" and T.subscribe_time >= '"+wxuserVo.getStarSubscribeTime()+"'");
		}
		if(null!=wxuserVo.getEndSubscribeTime()&&!"".equals(wxuserVo.getEndSubscribeTime())){
			sql.append(" and T.subscribe_time <='"+wxuserVo.getEndSubscribeTime()+"'");
		}
		if(null!=wxuserVo.getStarBuyTime()&&!"".equals(wxuserVo.getEndBuyTime())){
			sql.append(" and T.last_buy_time >= '"+wxuserVo.getStarBuyTime()+"'");
		}
		if(null!=wxuserVo.getEndBuyTime()&&!"".equals(wxuserVo.getEndBuyTime())){
			sql.append(" and T.last_buy_time <= '"+wxuserVo.getEndBuyTime()+"'");
		}
		if(wxuserVo.getName()!=null&&!wxuserVo.getName().equals("")){
			sql.append(" AND  su.user_name LIKE '%"+wxuserVo.getName()+"%' ");
		}
		if(wxuserVo.getPhoneNum()!=null&&!wxuserVo.getPhoneNum().equals("")){
			sql.append(" AND  su.phone_number = "+wxuserVo.getPhoneNum()+" ");
		}
		sql.append(" group by T.user_id order by endBuyTime DESC ");

		return sql.toString();
	}


	public void updateUsersLastMsgByHxAccount(List<HxUserUo> users){
		StringBuffer sql = new StringBuffer("update hx_user set  ");
		StringBuffer lastMsg = new StringBuffer(" last_msg = (CASE hx_account ");
		StringBuffer reply = new StringBuffer(" is_reply = (CASE hx_account ");
		StringBuffer chatDate = new StringBuffer(" last_chat_date = (CASE hx_account");
		StringBuffer whichUser = new StringBuffer(" where hx_account in ( 'nouser' ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = true;
		for (HxUserUo user: users){
			String msg = user.getLastMsg()==null?"":user.getLastMsg();
			Integer isReply = user.getIsReply()==null?0:user.getIsReply();
			Date lastDate = user.getLastChatDate();
			if(null!=user.getHxAccount()){
				lastMsg.append(" WHEN '"+user.getHxAccount()+"' THEN '"+Base64Util.enCoding(msg)+"' ");
				reply.append(" WHEN '"+user.getHxAccount()+"' THEN '"+isReply+"'");
				chatDate.append(" WHEN '"+user.getHxAccount()+"' THEN '"+sdf.format(lastDate)+"'");
				whichUser.append(" ,'"+user.getHxAccount()+"' ");
			}else{
				flag = false;
			}
		}
		lastMsg.append(" END),");
		reply.append(" END),");
		chatDate.append(" END)");
		whichUser.append(")");
		if(flag){
			sql.append(lastMsg);
			sql.append(reply);
			sql.append(chatDate);
			sql.append(whichUser);
			jdbcTemplate.update(sql.toString());
		}
	}

    public List<Map<String,Object>> queryWxUserByHxAccounts(String[] hxAccounts) {
    	StringBuffer sql = new StringBuffer("SELECT U.user_id as userId,U.headimgurl,U.nickname as nickname,H.hx_account as hxAccount,U.appid as appid,U.openid as openid FROM wx_user U  JOIN  hx_user H ON U.user_id = H.user_id AND ( ");
		for(int i = 0;i<hxAccounts.length;i++){
			if(i ==0){
				sql.append(" H.hx_account = '"+hxAccounts[i]+"' ");
			}else{
				sql.append(JdbcUtil.appendOr("H.hx_account",hxAccounts[i]));
			}
		}
		sql.append(")");
		return jdbcTemplate.queryForList(sql.toString());
    }

	public List<Map<String,Object>> findUsersByAppId(String appid) {
		String sql = "SELECT * FROM user U WHERE EXISTS (SELECT 1 FROM shop S LEFT JOIN user_role R ON R.shop_id = S.shop_id AND S.app_id='"+appid+"' WHERE U.user_id = R.user_id AND R.role_id = 2)ORDER BY U.user_id";
		List<Map<String,Object>> result = jdbcTemplate.queryForList(sql);
		return result;
	}

	public int countCardIdByActivity(Integer cardId) {
		String sql = "SELECT * FROM activity a left join activity_card ac on a.id=ac.activity_id where (ac.card_id="+cardId+" or ac.card_ids like '%"+ cardId + "%') and a.status in(0,1,2,3)";
		List<Map<String,Object>> result = jdbcTemplate.queryForList(sql);
		int count =0;
		if(result.size()>0){
			count=result.size();
		}
		return count;
	}


	public PageItem<UserRo> getUsers(UserQo qo, Integer shopId,int type) throws IOException {
		String sql1=" select ";
		String sql2=" u.user_id,u.user_name,u.phone_number,r.role_name,ur.id,u.head_img,ur.role_id,r.type ";
		String sql3=" from user u ";
		String sql4=" LEFT JOIN user_role ur  on ur.user_id = u.user_id ";
		String sql5=" LEFT JOIN role r on r.role_id  = ur.role_id ";
		String sql6=" WHERE 1=1 ";
		String sql7 = " count(*) ";

		if(StringUtil.isNotNull(qo.getPhoneNumber())){
			sql6 += " and u.phone_number = '"+ qo.getPhoneNumber()+ "'";
		}
			sql6+=" and ur.shop_id="+shopId;
			if(type == 0){
				sql6+=" and r.type = 1";
			}else if(type == 1){
				sql6+=" and r.type in (2,5,4)";
			}
		String sql = sql1 + sql2 + sql3 + sql4 + sql5 + sql6 ;
		sql +=" limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize();
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		int counts = jdbcTemplate.queryForInt(sql1+sql7 + sql3 + sql4 + sql5+sql6);
		PageItem<UserRo> pageItem = new PageItem<>();
		pageItem.setCount(counts);
		pageItem.setItems(UserConverter.toUserRos(list));
		return pageItem;
	}

	public void closeOrder(){
		String sql = "update order_info o set o.status=4 where o.status=0 and o.create_date<date_sub(now(),interval 2 day)";
		jdbcTemplate.update(sql);
	}

	public void confimOrder(){
		Date date = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr=sdf.format(date);
		String sql = "SELECT o.order_info_id FROM order_info o WHERE o.status = 2 AND o.send_date < date_sub(now(), INTERVAL 10 DAY)";
		List<Map<String,Object>> maps = jdbcTemplate.queryForList(sql);
		String orderInfoIds = "";
		for(Map<String,Object> map:maps){
			orderInfoIds += map.get("order_info_id").toString()+",";
		}
		if(orderInfoIds.length()>0){
			orderInfoIds = orderInfoIds.substring(0, orderInfoIds.length() - 1);
			String sql1 = "UPDATE brokerage_order bo SET bo.status=1,bo.take_date='"+dateStr+"' WHERE  bo.status=0 and bo.order_info_id in"
					+ "("+orderInfoIds+")";
			jdbcTemplate.update(sql1);
			String sql2 = "update order_info o set o.status=3,o.take_date='"+dateStr+"' where o.order_info_id in("+orderInfoIds+")";
			jdbcTemplate.update(sql2);
		}
	}

	public void deleteShoppingCart(){
		String sql = "delete from shopping_cart where type=1 and update_time<date_sub(now(),interval 2 day)";
		jdbcTemplate.update(sql);
	}

	/**
	 * 分页查询
	 * @param sqlList
	 * @return
	 */
	public PageItem<Map<String,Object>> queryPageItem(String sqlList,int curPage,int pageSize) {
		String sqlCount = "select count(*) " + sqlList.substring(sqlList.indexOf(" from "),sqlList.length());
		int count = jdbcTemplate.queryForInt(sqlCount);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+curPage*pageSize+","+pageSize);
		PageItem<Map<String,Object>> pageItem  = new PageItem<>();
		pageItem.setCount(count);
		pageItem.setItems(list);
		return pageItem;
	}

	public void deleteRecycleWeightConfig(Integer recycleWeightConfigId){
		String sql = "delete from recycle_weight_config where recycle_reward_id="+recycleWeightConfigId;
		jdbcTemplate.update(sql);
	}

}
