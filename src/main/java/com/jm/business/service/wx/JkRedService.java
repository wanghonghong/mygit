package com.jm.business.service.wx;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.shop.distribution.BrokerageService;
import com.jm.business.service.shop.distribution.BrokerageSetService;
import com.jm.business.service.user.ShopUserService;
import com.jm.mvc.vo.shop.distribution.BrokerageSetVo;
import com.jm.repository.jpa.system.JkActRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopUser;
import com.jm.repository.po.shop.brokerage.BrokerageSet;
import com.jm.repository.po.system.JkActCommission;
import com.jm.repository.po.wx.WxUser;
import com.jm.repository.po.wx.WxUserAccount;
import com.jm.staticcode.constant.Constant;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.*;


import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.business.domain.wx.WxRedQueryDo;
import com.jm.business.service.shop.WxPubAccountService;
import com.jm.business.service.system.UserAccountService;
import com.jm.mvc.vo.shop.activity.ActivityUserVo;
import com.jm.mvc.vo.wx.wxred.WeixinActInfo;
import com.jm.repository.client.WxClient;
import com.jm.repository.client.dto.WxRedQueryDto;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.activity.ActivityRepository;
import com.jm.repository.jpa.shop.activity.ActivityUserRepository;
import com.jm.repository.jpa.wx.WxRedRecordRepository;
import com.jm.repository.po.shop.activity.Activity;
import com.jm.repository.po.shop.activity.ActivityUser;
import com.jm.repository.po.system.user.UserAccount;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxRedRecord;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.TenpayUtil;
import com.jm.staticcode.util.wx.WeixinPayUtil;
import com.jm.staticcode.util.wx.WeixinRedUtils;
import com.jm.staticcode.util.wx.WeixinUtil;
import com.jm.staticcode.util.wx.WxUtil;

/**
 * 聚客红包service
 * @author chenyy
 *
 */
@Service
public class JkRedService {
	
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private WxpublicAccountService wxpublicAccountService;
    @Autowired
    public WxClient wxClient;
	@Autowired
	private WxRedRecordRepository wxRedRecordRepository;
 	@Autowired
 	private UserAccountService userAccountService;
 	@Autowired
 	private WxPubAccountService pubAccountService;
 	@Autowired
 	private ActivityUserRepository activityUserRepository;
 	@Autowired
 	private ActivityRepository activityRepository;
	@Autowired
	private JkActRepository jkActRepository;
	@Autowired
	private BrokerageSetService brokerageSetService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopUserService shopUserService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxUserAccountService wxUserAccountService;
	
    
	/**
	 * 查询已发送的红包状态
	 * @return
	 * @throws Exception 
	 */
	public void queryRedStatus() throws Exception{
		//查询已经发送出去的红包
		String sqlList = "select * from wx_red_record rr where rr.status=2";
		List<WxRedRecord> records = jdbcUtil.queryList(sqlList, WxRedRecord.class);
		for (WxRedRecord wxRedRecord : records) {
			WxRedQueryDo rdo = new WxRedQueryDo();
			rdo.setMchBillno(wxRedRecord.getMchBillno());
			rdo.setAppid(wxRedRecord.getAppid());
			WxRedQueryDto queryDto = sendRedQueryRequest(rdo);
			String mchBillno= queryDto.getMchBillno();//微信返回的订单id
			WxRedRecord returnRecord = wxRedRecordRepository.findByMchBillno(mchBillno);
			returnRecord.setDetailId(queryDto.getDetailId());
			if(queryDto.getStatusType()!=returnRecord.getStatus()){//本地与微信返回的状态结果不一致才做修改操作
				returnRecord.setStatus(queryDto.getStatusType());//将微信返回的状态修改到记录表
				//修改红包用户记录状态    状态值不一致 做对应赋值
				//@ApiModelProperty(value = "状态 1：发放中    2：已发放待领取   3：发放失败   4：已领取  5：退款中   6：已退款")
				int status = -1;
				if(queryDto.getStatusType()==3){
					status = 3;
					returnRecord.setReason(queryDto.getReason());
				}else if(queryDto.getStatusType()==4){
					returnRecord.setTakeTime(Toolkit.strToDate(queryDto.getRcvTime(), "yyyy-MM-dd HH:mm:ss"));
					status = 2;
				}else if(queryDto.getStatusType()==5||queryDto.getStatusType()==6){
					returnRecord.setRefundTime(Toolkit.strToDate(queryDto.getRefundTime() , "yyyy-MM-dd HH:mm:ss"));
					status = 4;
				}
				wxRedRecordRepository.save(returnRecord);
				ActivityUser  actUser=  activityUserRepository.findByRedPayId(returnRecord.getRedPayId());
				if(actUser!=null){
					if(status>-1){
						actUser.setStatus(status);
						activityUserRepository.save(actUser);
					}
				}
				
			}
		}
	}
	
	
	/**
	 * 发送红包查询请求
	 * @return
	 * @throws Exception 
	 */
	public  WxRedQueryDto sendRedQueryRequest(WxRedQueryDo queryDo) throws Exception{
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length()); //8位日期
		String strRandom = TenpayUtil.buildRandom(4) + ""; //四位随机数
		String nonce_str  = strTime + strRandom; //随机数
		WxPubAccount account = wxpublicAccountService.findWxPubAccountByAppid(queryDo.getAppid());
		SortedMap<String, String> map  = new TreeMap<String,String>(); 
		map.put("nonce_str", nonce_str);
		map.put("mch_billno", queryDo.getMchBillno());
		map.put("mch_id",account.getMchId());
		map.put("appid",queryDo.getAppid());
		map.put("sign", WeixinPayUtil.createSign(map,account.getAppKey()));
		String requestXML = WxUtil.mapToXml(map);
		String sss = WeixinRedUtils.checkApiclientcert(account.getMchId(), requestXML, WxUrl.WX_RED_SEND_STATUS);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		WeixinUtil.element2Map(resultMap,WeixinUtil.XmlToElement(sss));
		WxRedQueryDto dto = resultToWxRedQueryDto(resultMap);
		return dto;
	}
	
	/**
	 * 将查询返回的Map转成对象，因为有多层，所以部分需要手动
	 * @param resultMap
	 * @return
	 * @throws Exception 
	 */
	public WxRedQueryDto  resultToWxRedQueryDto(Map<String,Object> resultMap) throws Exception{
		WxRedQueryDto resultDto = JsonMapper.map2Obj(resultMap, WxRedQueryDto.class);
		//一些返回的信息是嵌套2-3层，所以部分需要手动赋值
		if(resultDto.getStatusType()==4){//红包已经领取才有以下字段
			Map<String,Object> hblistMap = (Map<String,Object>) resultMap.get("hblist");
			Map<String,Object> hbinfoMap =(Map<String,Object>)  hblistMap.get("hbinfo");
			String openid = (String)hbinfoMap.get("openid");
			resultDto.setOpenid(openid);
			String rcvTime = (String)hbinfoMap.get("rcv_time");
			String amount = (String)hbinfoMap.get("amount");
			resultDto.setAmount(Integer.parseInt(amount));
			resultDto.setRcvTime(rcvTime);
		}
		return resultDto;
		
	}
	
	
	
	
	 /**
     * 获取活动用户红包发送记录
     * @return
     * @throws Exception 
     */
    public List<ActivityUserVo> findActivityUser(Integer status,Integer activityId) throws Exception{
    	String sqlList = "select au.*,au.money as red_money from activity_user au "
    			         + " left join wx_red_record rr on rr.red_pay_id=au.red_pay_id "
    			         + " left join activity a on au.activity_id=a.id "
    			         + " where 1=1 and au.activity_id="+activityId;
    	 StringBuilder sqlCondition = new StringBuilder();
    	 if(status!=null){
    		 sqlCondition.append(JdbcUtil.appendAnd("au.status",status));
    	 }
    	 List<ActivityUserVo> actUsers = jdbcUtil.queryList(sqlList+sqlCondition, ActivityUserVo.class);
    	 return actUsers;
    }

    
    /**
     * 聚客红包活动结算
     * @throws Exception 
     */
    public synchronized void activitySettlement() throws Exception{
    	String sqlList = "select a.* from "
    			+ " (select date_add(a.end_time ,INTERVAL 25 hour) as date,a.* from activity  a) a "
    			+ "  where a.is_settlement=0 and a.type=5 and a.status=3 and a.date<SYSDATE()";
    	List<Activity> activitys = jdbcUtil.queryList(sqlList, Activity.class);
    	for (Activity activity : activitys) {
    		if(activity.getIsSettlement()==0){//未结算才进入
				activity.setIsSettlement(99);//设置为结算中
				activityRepository.save(activity);
				Integer actMoney = activity.getTotalMoney();//活动总金额
				List<ActivityUserVo> actUserVos =  findActivityUser(2, activity.getId());

				int sendMoneyCount = 0;//活动发出的总金额
				int reFundCount = 0;//应退还金额(等于活动总金额减去已发送的金额)
				for (ActivityUserVo activityUserVo : actUserVos) {//统计发放出去的总金额
					sendMoneyCount+=activityUserVo.getRedMoney();
				}
				// -----统计平台手续费扣取与推广佣金计算----
				//平台手续费等于：实际支出金额*平台收取的点数
				//佣金等于：平台得到的手续费*佣金比例
				Integer	counterFee = (sendMoneyCount*3)/100;  //平台手续费 定为百分之3
				Shop shop = shopService.getShopByAppId(Constant.JK_MANAGE_APPID);
				//根据appid查出活动所属的公众号
				WxPubAccount wxPub = wxpublicAccountService.findWxPubAccountByAppid(activity.getAppId());
				//使用公众号所属的userId查出shopUser
				ShopUser shopUser = shopUserService.findByJmUserId(wxPub.getUserId());
				if(shopUser!=null){//不为空表示用户是从手机端注册过来的
					//使用shopUser的id 查出wxUser，再查出上级然后给予佣金
					List<WxUser> wxUsers = wxUserService.findByShopUserId(shopUser.getId());
					if(wxUsers!=null && wxUsers.size()>0){
						WxUser user = wxUsers.get(0);
						if(user.getUpperOne()!=null){
							Integer upOneUserId = user.getUpperOne();//上级的id，需要给予佣金的账户
							BrokerageSetVo brokerageSetVo =  brokerageSetService.getBrokerageSetting(shop.getShopId());
							int comission = (counterFee*brokerageSetVo.getOne())/10000;//佣金  为平台手续费*设定的佣金比例
							WxUserAccount wxUserAccount = wxUserAccountService.findWxUserAccountById(upOneUserId,1);
							JkActCommission actCommission = new JkActCommission();
							actCommission.setWxUserId(upOneUserId);
							actCommission.setActivityId(activity.getId());
							actCommission.setMoney(comission);
							jkActRepository.save(actCommission);//保存聚客活动佣金
							if(wxUserAccount!=null){
								wxUserAccount.setTotalBalance(wxUserAccount.getTotalBalance()+comission);
								wxUserAccount.setKitBalance(wxUserAccount.getKitBalance()+comission);
								wxUserAccount.setTotalCount(wxUserAccount.getTotalCount()+comission);
								wxUserAccount.setUpdateTime(new Date());
							}else{
								//不存在，创建账户
								wxUserAccount = new WxUserAccount();
								wxUserAccount.setUserId(upOneUserId);
								wxUserAccount.setShopId(shop.getShopId());
								wxUserAccount.setAccountType(1);
								wxUserAccount.setTotalBalance(comission);//总金额
								wxUserAccount.setKitBalance(comission);//可提金额
								wxUserAccount.setTotalCount(comission);//累计总金额
							}
							wxUserAccountService.saveWxUserAccount(wxUserAccount);
							shopUser.setAgentRole(8);//佣金结算后将角色改为分享客
							shopUserService.save(shopUser);
						}
					}
				}
				reFundCount = actMoney - (sendMoneyCount+counterFee);// 退还的金额等于 实际支付的金额加平台手续费
				if(reFundCount > 0){//退款金额不等于0才做退回操作
					WxPubAccount account  = pubAccountService.findWxPubAccountByAppid(activity.getAppId());
					UserAccount userAccount = userAccountService.findByUserId(account.getUserId(),0);
					userAccount.setBalance(userAccount.getBalance()+reFundCount);//将当前的余额加退还的金额
					userAccountService.saveAccount(userAccount);
					activity.setIsSettlement(1);
					activityRepository.save(activity);
				}
			}

		}
    }

}
