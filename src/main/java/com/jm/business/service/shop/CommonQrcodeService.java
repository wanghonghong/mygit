package com.jm.business.service.shop;

import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.commQrcode.*;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.CommonQrcodeRepository;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.repository.po.shop.CommonQrcode;
import com.jm.staticcode.converter.shop.CommonQrcodeConverter;
import com.jm.staticcode.converter.shop.GoodsQrcodeConverter;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


/**
 * <p>通用商品条码</p>
 */
@Service
public class CommonQrcodeService {
	
	@Autowired
	private CommonQrcodeRepository commonQrcodeRepository;

	@Autowired
	private JdbcRepository jdbcRepository;
	@Autowired
	private JdbcUtil jdbcUtil;


	public CommonQrcode save(CommonQrcodeCo co){
	  return commonQrcodeRepository.save(CommonQrcodeConverter.toPo(co));
	}

	public CommonQrcode save(CommonQrcode po){
		return commonQrcodeRepository.save(po);
	}

	public PageItem<CommonQrcodeRo> findAll(CommonQrcodeQo qo,JmUserSession user) throws IOException {
		/*String sql = " select a.id,a.name,a.count,b.name product_name ,a.code_type,a.create_time,a.end_time,a.print_remark,a.valid_type" +
				" from common_qrcode a " +
				" left join product b on a.product_id = b.pid " +
				" left join common_qrcode_user c on a.id = c.common_qrcode_id " +
				" left join wx_user d on c.open_id = d.openid and d.user_id is not null " +
				" where a.id is not null and a.shop_id= "+shopId;*/
		String sql = "select a.*,b.count as user_count,c.name as product_name from common_qrcode a " +
				"left join " +
				"(select a.common_qrcode_id ,count(1) as count " +
				"from common_qrcode_user  a " +
				"left join wx_user b on a.open_id = b.openid " +
				"where b.appid='"+user.getAppId()+"'  " +
				"group by common_qrcode_id) b  " +
				"on a.id = b.common_qrcode_id " +
				"left join product c on a.product_id = c.pid " +
				"where a.shop_id="+user.getShopId();

		if(qo.getFansType()==1){
			sql+=" and b.count is null or b.count = 0 ";
		}else if(qo.getFansType()==2){
			sql+=" and b.count > 0 ";
		}
		sql += JdbcUtil.appendLike("a.name",qo.getName());
		sql += JdbcUtil.appendLike("c.name",qo.getProductName());
		if(null!=qo.getStartTime()&&!qo.getStartTime().equals("")&&null!=qo.getEndTime()&&!qo.getEndTime().equals("")){
			sql += " and a.end_time BETWEEN '" +qo.getStartTime() +"' and '"+qo.getEndTime()+"'";
		}
		if(null!=qo.getStartCreateTime()&&!qo.getStartCreateTime().equals("")&&null!=qo.getEndCreateTime()&&!qo.getEndCreateTime().equals("")){
			sql += " and a.create_time BETWEEN '" +qo.getStartCreateTime() +"' and '"+qo.getEndCreateTime()+"'";
		}

		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize());
		return  CommonQrcodeConverter.p2v(pageItem);
	}


	public PageItem<CommonQrcodeDetailRo> findDetailAll(CommonQrcodeQo qo, JmUserSession user) throws IOException {
		String sql = " select " +
				" a.id common_qrcode_detail_id,a.name,d.name as product_name,a.code_type,a.create_time,a.end_time,a.valid_type,e.nickname, " +
				" c.user_name,c.phone_number,e.headimgurl,IFNULL(b.userCount,0) as user_count " +
				" from " +
				" (select a.*, b.shop_id,b.`name`,b.create_time,b.end_time,b.valid_type,b.code_type " +
				" from common_qrcode_detail a " +
				" left join common_qrcode b ON a.common_qrcode_id = b.id " +
				" WHERE b.shop_id = " +user.getShopId()+
				" ) a " +
				" LEFT JOIN ( " +
				" SELECT ifnull(count(1), 0) AS userCount,a.common_qrcode_detail_id " +
				" FROM common_qrcode_user a LEFT JOIN wx_user b ON a.open_id = b.openid " +
				" WHERE b.appid = '"+user.getAppId()+"' GROUP BY a.common_qrcode_detail_id " +
				" ) b ON a.id = b.common_qrcode_detail_id " +
				" LEFT JOIN wx_user e ON a.user_id = e.user_id " +
				" LEFT JOIN shop_user c ON e.shop_user_id = c.id " +
				" LEFT JOIN product d ON a.product_id = d.pid " +
				" WHERE 1=1" ;
        if(qo.getFansType()==1){
            sql+=" and b.userCount is null or b.userCount = 0 ";
        }else if(qo.getFansType()==2){
            sql+=" and b.userCount > 0 ";
        }
		sql += JdbcUtil.appendLike("a.name",qo.getName());
		sql += JdbcUtil.appendLike("d.name",qo.getProductName());
		if(qo.getNickname()!=null){
			sql += JdbcUtil.appendLike("e.nickname", Base64Util.enCoding(qo.getNickname()));
		}
		if(null!=qo.getStartTime()&&!qo.getStartTime().equals("")&&null!=qo.getEndTime()&&!qo.getEndTime().equals("")){
			sql += " and a.end_time BETWEEN '" +qo.getStartTime() +"' and '"+qo.getEndTime()+"'";
		}
		if(null!=qo.getStartCreateTime()&&!qo.getStartCreateTime().equals("")&&null!=qo.getEndCreateTime()&&!qo.getEndCreateTime().equals("")){
			sql += " and a.create_time BETWEEN '" +qo.getStartCreateTime() +"' and '"+qo.getEndCreateTime()+"'";
		}

		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize());
		return  CommonQrcodeConverter.p2vs(pageItem);
	}

	public CommonQrcode getCommonQrcode(Integer id){
		return commonQrcodeRepository.findOne(id);
	}


	public PageItem<EffectUserRo> findEffectUser(CommonQrcodeQo qo,Integer id) throws IOException, InstantiationException, IllegalAccessException {
		String sql = " select c.nickname,d.user_name,d.phone_number,c.subscribe_time,d.agent_role,SUM(e.real_price) real_price " +
				" from common_qrcode_user b " +
				" left join wx_user c on b.open_id = c.openid " +
				" left join shop_user d on c.shop_user_id = d.id " +
				" left join order_info e on e.user_id = c.user_id and e.`status` !=4 and e.`status` !=0 " +
				" where c.user_id is not null and b.common_qrcode_detail_id = "+id +" group by c.user_id ";
		PageItem<EffectUserRo> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize(),EffectUserRo.class);
		for (EffectUserRo ro:pageItem.getItems()) {
			ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
		}
		return  pageItem;
	};

}
