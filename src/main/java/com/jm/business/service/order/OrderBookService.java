package com.jm.business.service.order;

import com.jm.business.domain.wx.WxTemplateDo;
import com.jm.business.service.shop.IntegralService;
import com.jm.business.service.wx.WxMessageService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookQo;
import com.jm.mvc.vo.order.OrderBookUo;
import com.jm.mvc.vo.order.OrderBookVo;
import com.jm.mvc.vo.order.recycle.RecycleDetailUo;
import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.OrderBookRepository;
import com.jm.repository.jpa.order.recycle.RecycleDetailRepository;
import com.jm.repository.po.order.OrderBook;
import com.jm.repository.po.order.recycle.RecycleDetail;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.order.OrderBookConverter;
import com.jm.staticcode.util.csv.CsvToolUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class OrderBookService {

	@Autowired
	private OrderBookRepository orderBookRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private WxUserService wxUserService;
	@Autowired
	private WxMessageService wxMessageService;
	@Autowired
	private RecycleDetailRepository recycleDetailRepository;

	public PageItem<OrderBookVo> queryOrder(OrderBookQo orderBookQo,Integer status) throws IOException, IllegalAccessException, InstantiationException, ParseException {
		PageItem<OrderBookVo> orderBookVoPageItem = queryOrderList(orderBookQo,status);
		return orderBookVoPageItem;
	}


	//修改订单的状态
	public void updateOrderBook(Long id,OrderBookUo orderBookUo) throws Exception {
		OrderBook orderBook = orderBookRepository.findOrderInfoById(id);
		//只有已预约的订单才能被确认收单
		if(orderBookUo.getStatus()==3 && orderBook.getStatus()==0){
			orderBook.setStatus(3);
		}
		if(orderBookUo.getStatus()==4 && orderBook.getStatus()==0){
			orderBook.setStatus(4);
		}
		OrderBook ob = orderBookRepository.save(orderBook);
		if(ob.getStatus()==4){
			pushCloseWxMsg(ob);
		}
	}

	//触发奖励
	public void triggerReward(Long id) throws Exception {
		OrderBook orderBook = orderBookRepository.findOrderInfoById(id);
		integralService.setRecycleAward(orderBook);
	}

	public RecycleDetailVo getRecycleDetail(Long id){
        RecycleDetailVo recycleDetailVo = new RecycleDetailVo();
	    RecycleDetail recycleDetail = recycleDetailRepository.findRecycleDetailByOrderId(id);
        BeanUtils.copyProperties(recycleDetail,recycleDetailVo);
	    return recycleDetailVo;
    }

	//保存客服备注
	public void updateRecycleDetailUo(Long id, RecycleDetailUo recycleDetailUo){
		RecycleDetail recycleDetail = recycleDetailRepository.findRecycleDetailByOrderId(id);
		if(!StringUtils.isEmpty(recycleDetailUo.getCustomRemark())){
			recycleDetail.setCustomRemark(recycleDetailUo.getCustomRemark());
		}
		if(!StringUtils.isEmpty(recycleDetailUo.getReceiveRemark())){
			recycleDetail.setReceiveRemark(recycleDetailUo.getReceiveRemark());
		}
		recycleDetailRepository.save(recycleDetail);
	}

	//查看普通订单列表
	private PageItem<OrderBookVo> queryOrderList(OrderBookQo orderBookQo,Integer status) throws IOException, ParseException {
		String sqlList = "select ob.id,ob.user_id,ob.order_num,ob.create_time,ob.status,ob.type as order_book_type,ob.book_time,"
				+ "ob.user_name,ob.phone_number,ob.book_time_scope,rd.user_remark,rd.receive_remark,rd.type,rd.custom_remark,rd.reward_type,"
				+ "rd.img_url,rw.weight,rd.user_id,rd.reward,wx.headimgurl,wua.provice,wua.city,wua.third,wua.address,rd.rec_address,rd.kd_name,"
				+ "su.phone_number as shop_user_phone,su.user_name as shop_user_name "
				+ "from order_book ob LEFT JOIN recycle_detail rd on rd.order_id=ob.id "
				+ "LEFT JOIN wx_user_address wua on wua.id=rd.address_id "
				+ "LEFT JOIN wx_user wx on wx.user_id = ob.user_id "
				+ "LEFT JOIN shop_user su ON su.id = wx.shop_user_id "
				+ "LEFT JOIN recycle_weight_config rw on rw.id=rd.weight "
				+ "where 1=1";
		StringBuilder sqlCondition = new StringBuilder();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if(!StringUtils.isEmpty(orderBookQo.getOrderBeginDate()) && !StringUtils.isEmpty(orderBookQo.getOrderEndDate())){
			java.util.Date orderBeginDate=sdf.parse(orderBookQo.getOrderBeginDate().toString());
			java.util.Date OrderEndDate=sdf.parse(orderBookQo.getOrderEndDate().toString());
			sqlCondition.append(JdbcUtil.appendAnd("ob.create_time",orderBeginDate,OrderEndDate));
		}
		if(!StringUtils.isEmpty(orderBookQo.getOrderBeginDate1()) && !StringUtils.isEmpty(orderBookQo.getOrderEndDate1())){
			sqlCondition.append(JdbcUtil.appendAnd("ob.book_time",orderBookQo.getOrderBeginDate1(),orderBookQo.getOrderEndDate1()));
		}
		sqlCondition.append(JdbcUtil.appendAnd("rd.type",orderBookQo.getType()));
		sqlCondition.append(JdbcUtil.appendAnd("su.user_name",orderBookQo.getUserName()));
		sqlCondition.append(JdbcUtil.appendAnd("su.phone_number",orderBookQo.getUserNumber()));
		sqlCondition.append(JdbcUtil.appendAnd("ob.phone_number",orderBookQo.getPhoneNumber()));
		sqlCondition.append(JdbcUtil.appendAnd("ob.shop_id",orderBookQo.getShopId()));
		if (status ==0){
			//回收订单
			if(orderBookQo.getStatus()==null){
				sqlCondition.append(JdbcUtil.appendIn("ob.status","0,1,4"));
			}else{
				sqlCondition.append(JdbcUtil.appendAnd("ob.status",orderBookQo.getStatus()));
			}
		}else{
			//收单奖励
			sqlCondition.append(JdbcUtil.appendAnd("ob.status",3));
			if(orderBookQo.getStatus()!=null){
				sqlCondition.append(JdbcUtil.appendAnd("rd.reward",orderBookQo.getStatus()));
			}
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("ob.id"));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sqlList+sqlCondition,orderBookQo.getCurPage(),orderBookQo.getPageSize());
		return OrderBookConverter.p2v(pageItem);
	}

	/**
	 * 微信推送取消收单消息
	 */
	public void pushCloseWxMsg(OrderBook orderBook) throws Exception {
		WxUser wxuser = wxUserService.getWxUser(orderBook.getUserId());
		WxTemplateDo wxTemplateDo = new WxTemplateDo();
		wxTemplateDo.setTouser(wxuser.getOpenid());
		wxTemplateDo.setAppid(wxuser.getAppid());
		wxTemplateDo.setType(12);
		wxTemplateDo.setUrl(Constant.APP_DOMAIN + "/shop/index?shopId=" + orderBook.getShopId());//点击跳转的链接
		String first = "预约订单取消";
		String remark = "订单编号："+orderBook.getOrderNum()+"\r\n本次预约取消，有问题请致电4009010995";
		wxTemplateDo.setFirst(first);
		wxTemplateDo.setRemark(remark);
		wxTemplateDo.setWaitForTask("请确认");
		wxTemplateDo.setDelayTask("暂无");
		wxMessageService.sendTemplate(wxTemplateDo);
	}

	/**
	 *订单导出功能
	 * @param items
	 */
	public void exportOrderBook(List<Map<String, Object>> items,HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition","attachment;filename=order.xls");
		String headers = "订单编号,姓名,手机,订单日期,分类,重量,省,市,区/县,取件地址,取件时间,取件联系人姓名,取件联系人手机,快递商,仓库地址,订单状态,是否奖励,用户备注,收货备注,客服备注";
		CsvToolUtil csvToolUtil = new CsvToolUtil();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		csvToolUtil.exportCsv(items,headers,"回收订单报表-"+df.format(new Date()),response);
	}

	/**************************************************订单导出功能用到的查询*************************************************/
	public List<Map<String, Object>> exportOrderBook(OrderBookQo orderBookQo,Integer status) throws IOException, ParseException {
		List<Map<String, Object>> maps = exportOrderList(orderBookQo,status);
		return doOrderBookMaps(maps);
	}

	private List<Map<String, Object>> exportOrderList(OrderBookQo orderBookQo,Integer status) throws IOException, ParseException {
		String sqlList = "select ob.order_num,su.user_name as shop_user_name,su.phone_number as shop_user_phone,ob.create_time,"
				+ "rd.type,rw.weight,wua.provice,wua.city,wua.third,wua.detail_address,ob.book_time,ob.user_name,ob.phone_number,"
				+ "rd.kd_name,rd.rec_address,ob.status,rd.reward,rd.user_remark,rd.receive_remark,rd.custom_remark "
				+ "from order_book ob LEFT JOIN recycle_detail rd on rd.order_id=ob.id "
				+ "LEFT JOIN wx_user_address wua on wua.id=rd.address_id "
				+ "LEFT JOIN wx_user wx on wx.user_id = ob.user_id "
				+ "LEFT JOIN shop_user su ON su.id = wx.shop_user_id "
				+ "LEFT JOIN recycle_weight_config rw on rw.id=rd.weight "
				+ "where 1=1";
		StringBuilder sqlCondition = new StringBuilder();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟
		if(!StringUtils.isEmpty(orderBookQo.getOrderBeginDate()) && !StringUtils.isEmpty(orderBookQo.getOrderEndDate())){
			java.util.Date orderBeginDate=sdf.parse(orderBookQo.getOrderBeginDate().toString());
			java.util.Date OrderEndDate=sdf.parse(orderBookQo.getOrderEndDate().toString());
			sqlCondition.append(JdbcUtil.appendAnd("ob.create_time",orderBeginDate,OrderEndDate));
		}
		if(!StringUtils.isEmpty(orderBookQo.getOrderBeginDate1()) && !StringUtils.isEmpty(orderBookQo.getOrderEndDate1())){
			sqlCondition.append(JdbcUtil.appendAnd("ob.book_time",orderBookQo.getOrderBeginDate1(),orderBookQo.getOrderEndDate1()));
		}
		sqlCondition.append(JdbcUtil.appendAnd("rd.type",orderBookQo.getType()));
		sqlCondition.append(JdbcUtil.appendAnd("su.user_name",orderBookQo.getUserName()));
		sqlCondition.append(JdbcUtil.appendAnd("su.phone_number",orderBookQo.getUserNumber()));
		sqlCondition.append(JdbcUtil.appendAnd("ob.phone_number",orderBookQo.getPhoneNumber()));
		sqlCondition.append(JdbcUtil.appendAnd("ob.shop_id",orderBookQo.getShopId()));
		if (status ==0){
			//回收订单
			if(orderBookQo.getStatus()==null){
				sqlCondition.append(JdbcUtil.appendIn("ob.status","0,1,4"));
			}else{
				sqlCondition.append(JdbcUtil.appendAnd("ob.status",orderBookQo.getStatus()));
			}
		}else{
			//收单奖励
			sqlCondition.append(JdbcUtil.appendAnd("ob.status",3));
			if(orderBookQo.getStatus()!=null){
				sqlCondition.append(JdbcUtil.appendAnd("rd.reward",orderBookQo.getStatus()));
			}
		}
		sqlCondition.append(JdbcUtil.appendOrderBy("ob.id"));
		return jdbcUtil.queryList(sqlList+sqlCondition);
	}

	private List<Map<String, Object>> doOrderBookMaps(List<Map<String, Object>> maps){
		for(Map<String, Object> map:maps){
			if(!StringUtils.isEmpty(map.get("type"))){
				if(map.get("type").equals(0)){
					map.put("type","衣服");
				}else if(map.get("type").equals(1)){
					map.put("type","鞋帽");
				}else if(map.get("type").equals(2)){
					map.put("type","包包");
				}else{
					map.put("type","混合");
				}
			}
			if(!StringUtils.isEmpty(map.get("reward"))){
				if(map.get("reward").equals(0)){
					map.put("reward","未奖励");
				}else{
					map.put("reward","已奖励");
				}
			}
			if(!StringUtils.isEmpty(map.get("status"))){
				if(map.get("status").equals(4)){
					map.put("status","已取消");
				} else if(map.get("status").equals(0)) {
					map.put("status", "待收单");
				} else if(map.get("status").equals(2)){
					map.put("status","已收单");
				}else if(map.get("status").equals(3)){
					map.put("status","已完成");
				}
			}
		}
		return maps;
	}


	public void updateOrderBook(String orderNumLists){
		String sql = "update order_book o set o.status=3 where o.order_num in("+orderNumLists+") and o.status=0";
		jdbcUtil.update(sql);
	}

}
