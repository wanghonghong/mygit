package com.jm.business.service.system;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jm.business.service.order.PayRecordService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.UserAccountVo;
import com.jm.mvc.vo.order.PayRecordVo;
import com.jm.mvc.vo.qo.JmRechargeOrderQo;
import com.jm.mvc.vo.system.JmRechargeOrderCo;
import com.jm.mvc.vo.system.JmRechargeOrderVo;
import com.jm.mvc.vo.wx.template.WxTemplateMsgVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.resource.JmResourceRepository;
import com.jm.repository.jpa.system.JmRechargeOrderRepository;
import com.jm.repository.jpa.system.UserAccountReposiory;
import com.jm.repository.po.order.PayRecord;
import com.jm.repository.po.system.JmRechargeOrder;
import com.jm.repository.po.system.user.UserAccount;
import com.jm.staticcode.converter.order.PayRecordConverter;
import com.jm.staticcode.converter.system.UserAccountConverter;
import com.jm.staticcode.util.Toolkit;
/**
 * 用户账户Service
 * @author chenyy
 *
 */
@Service
public class UserAccountService {
	
	@Autowired
	private UserAccountReposiory userAccountReposiory;
	
	@Autowired
	private JmRechargeOrderRepository jmRechargeOrderRepository;
	
 	@Autowired
	protected JdbcUtil jdbcUtil;
	@Autowired
	private PayRecordService recordService;

	
	public UserAccount findByUserId(Integer userId,int accountType){
		UserAccount account = userAccountReposiory.findByUserIdAndAccountType(userId,accountType);
		if(account==null){
			//为空创建
			UserAccount newAccount = new UserAccount();
			newAccount.setAccountType(accountType);
			newAccount.setBalance(0);
			newAccount.setTotalCount(0);
			newAccount.setUserId(userId);
			account = userAccountReposiory.save(newAccount);
		}
		return account;
	}
	
	/**
	 * 获取充值记录列表
	 * @return
	 * @throws Exception  
	 */
	public PageItem<JmRechargeOrderVo> getJmRechargeList(JmRechargeOrderQo orderQo) throws Exception{
		String sqlList = "select r.order_info_id,r.create_date,r.money,r.pay_date,r.pay_id,"
				+ " r.user_id,p.pay_type from jm_recharge_order r"
				+ " left join pay_record p on r.pay_id=p.pay_id"
				+ " where r.type="+orderQo.getType()+" and r.user_id="+orderQo.getUserId()+" and r.status=1"
				+ " order by r.create_date";
		PageItem<JmRechargeOrderVo> pageItem = jdbcUtil.queryPageItem(sqlList, orderQo.getCurPage(), orderQo.getPageSize(), JmRechargeOrderVo.class);
		return pageItem;
	}


	/**
	 * 获取聚客红包用户充值记录(管理平台使用)
	 * @return
	 * @throws Exception
	 */
	public PageItem<JmRechargeOrderVo> jmRechargeToManage(JmRechargeOrderQo orderQo) throws Exception{
		String sqlList = "select r.order_info_id,r.create_date,r.money,r.pay_date,r.pay_id,r.appid,"
				+ " p.pay_type,wu.nickname,wu.headimgurl,wu2.nickname as up_nickname,wu2.headimgurl as up_headimgurl"
				+" ,u.phone_number from jm_recharge_order r"
				+ " left join pay_record p on r.pay_id=p.pay_id"
				+ " left join user u on u.user_id=r.user_id "
				+ " left join shop_user su on su.jm_user_id=u.user_id"
				+ " left join wx_user wu on su.id=wu.shop_user_id"
				+ " left join wx_user wu2 on wu2.user_id=wu.upper_one"
				+ " where r.type="+orderQo.getType()+" and r.recharge_type=1 and r.status=1";
		StringBuilder sqlCondition = new StringBuilder("");

		if(orderQo.getNickname()!=""&&null!=orderQo.getNickname()){
			sqlCondition.append(" and wu.nickname like '%"+Base64Util.enCoding(orderQo.getNickname())+"%' ") ;
		}
		if(orderQo.getPhoneNumber()!=""&&null!=orderQo.getPhoneNumber()){
			sqlCondition.append(" and u.phone_number like '%"+orderQo.getPhoneNumber()+"%' ") ;
		}

		sqlCondition.append(JdbcUtil.appendAnd("r.pay_date",orderQo.getStartDate(),orderQo.getEndtDate()));
		sqlCondition.append(" ORDER BY r.pay_date desc ");
		PageItem<JmRechargeOrderVo> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition, orderQo.getCurPage(), orderQo.getPageSize(), JmRechargeOrderVo.class);
		for (JmRechargeOrderVo rechargeOrderVo:pageItem.getItems()) {
			rechargeOrderVo.setNickname(Base64Util.getFromBase64(rechargeOrderVo.getNickname()));
			rechargeOrderVo.setUpNickname(Base64Util.getFromBase64(rechargeOrderVo.getUpNickname()));
		}
		return pageItem;
	}
	
	/**
	 * 根据充值订单ID获取充值记录
	 * @return
	 */
	public JmRechargeOrder findRechargeOrderById(Long orderInfoId){
		return jmRechargeOrderRepository.findByOrderInfoId(orderInfoId);
	}
	
	/**
	 * 添加充值订单
	 * @param orderCo
	 * @return 
	 */
	@Transactional
	public JmRechargeOrder createRechargeOrder(JmRechargeOrderCo orderCo){
		JmRechargeOrder order = new JmRechargeOrder();
		order.setMoney(orderCo.getMoney());
		order.setType(orderCo.getType());
		order.setStatus(0);
		order.setUserId(orderCo.getUserId());
		order.setRechargeType(orderCo.getRechargeType());
		return jmRechargeOrderRepository.save(order);
	}
	
	@Transactional
	public void qrcodePaySuccess(JmRechargeOrder rechargeOrder,Map<String,Object> xmlMap){
		Date date = new Date();
		PayRecordVo recordVo = new PayRecordVo();
		recordVo.setPayStatus(1);//支付状态设置为1
		recordVo.setPayDate(date);
		recordVo.setPayType(1);//微信支付
		recordVo.setPayMoney(rechargeOrder.getMoney());
		recordVo.setPayNo(Toolkit.getOrderNum("J"));//流水号
		recordVo.setTransactionId(Toolkit.parseObjForStr(xmlMap.get("transaction_id")));
		recordVo.setTimeEnd(Toolkit.parseObjForStr(xmlMap.get("time_end")));
		PayRecord payRecord = PayRecordConverter.toPayRecord(recordVo);
		PayRecord pr = recordService.createPayRecord(payRecord);//添加流水
		
		//修改订单状态
		rechargeOrder.setStatus(1);
		rechargeOrder.setPayDate(date);
		rechargeOrder.setPayId(pr.getPayId());
		jmRechargeOrderRepository.save(rechargeOrder);
		
		//修改账户信息
		UserAccount account = userAccountReposiory.findByUserIdAndAccountType(rechargeOrder.getUserId(),0);
		account.setBalance(account.getBalance()+rechargeOrder.getMoney());
		account.setTotalCount(account.getTotalCount()+rechargeOrder.getMoney());
		account.setUpdateTime(date);
		userAccountReposiory.save(account);
	}
	
	public void saveAccount(UserAccount userAccount){
		userAccountReposiory.save(userAccount);
	}
	
}
