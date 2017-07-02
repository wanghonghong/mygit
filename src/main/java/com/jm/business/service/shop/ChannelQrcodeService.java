package com.jm.business.service.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.commQrcode.EffectUserRo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQrcodeCo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQrcodeQo;
import com.jm.mvc.vo.shop.commQrcode.ChannelQrcodeRo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.ChannelQrcodeRepository;
import com.jm.repository.po.shop.ChannelQrcode;
import com.jm.staticcode.converter.shop.GoodsQrcodeConverter;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * <p>商品条码</p>
 */
@Service
public class ChannelQrcodeService {
	
	@Autowired
	private ChannelQrcodeRepository channelQrcodeRepository;
    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcUtil jdbcUtil;
	
	public void save(ChannelQrcodeCo co){
		channelQrcodeRepository.save(GoodsQrcodeConverter.toPo(co));
	}

    public ChannelQrcode save(ChannelQrcode gq){
        return channelQrcodeRepository.save(gq);
    }


    public PageItem<ChannelQrcodeRo> findAll(ChannelQrcodeQo qo, Integer shopId) throws IOException, InstantiationException, IllegalAccessException {
        String sql = " select a.id goods_qrcode_id,c.headimgurl,c.nickname,d.user_name,d.phone_number,a.name,b.name product_name, " +
                "a.code_type,a.end_time,a.create_time,a.print_remark,a.valid_type,a.fans_type " +
                     "from channel_qrcode a " +
                    " left join product b on a.product_id = b.pid " +
                    " left join wx_user c on a.user_id = c.user_id " +
                    " left join shop_user d on c.shop_user_id = d.id " +
                    " where a.shop_id= "+shopId;
        sql += JdbcUtil.appendLike("a.name",qo.getName());
        sql += JdbcUtil.appendLike("b.name",qo.getProductName());
        if(qo.getFansType()!=0){
            if(qo.getFansType()==1){
                sql += JdbcUtil.appendAnd("a.fans_type",0);
            }else if(qo.getFansType()==2){
                sql += JdbcUtil.appendAnd("a.fans_type",1);
            }
        }
        if(qo.getChannelName()!=null){
            sql += JdbcUtil.appendLike("c.nickname", Base64Util.enCoding(qo.getChannelName()));
        }
        if(null!=qo.getStartEndTime()&&!qo.getStartEndTime().equals("")&&null!=qo.getEndEndTime()&&!qo.getEndEndTime().equals("")){
            sql += " and a.end_time BETWEEN '" +qo.getStartEndTime() +"' and '"+qo.getEndEndTime()+"'";
        }
        if(null!=qo.getStartCreateTime()&&!qo.getStartCreateTime().equals("")&&null!=qo.getEndCreateTime()&&!qo.getEndCreateTime().equals("")){
            sql += " and a.create_time BETWEEN '" +qo.getStartCreateTime() +"' and '"+qo.getEndCreateTime()+"'";
        }

        sql += " group by a.id ";
        PageItem<ChannelQrcodeRo> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize(),ChannelQrcodeRo.class);
        for (ChannelQrcodeRo ro:pageItem.getItems()) {
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
        }
        return  pageItem;
    }

    public ChannelQrcode getChannelQrcode(Integer id){
        return channelQrcodeRepository.findOne(id);
    }




    public PageItem<EffectUserRo> findEffectUser(ChannelQrcodeQo qo, Integer goodsQrcodeId) throws IOException, InstantiationException, IllegalAccessException {
        String sql = " select b.nickname,c.user_name,c.phone_number,b.subscribe_time,c.agent_role,SUM(d.real_price) real_price " +
                     " from product_qrcode_detail a " +
                     " left JOIN wx_user b on a.user_id = b.user_id " +
                     " left join shop_user c on b.shop_user_id = c.id " +
                     " left join order_info d on d.user_id = b.user_id and d.`status` !=4 and d.`status` !=0 " +
                     " where a.goods_qrcode_id="+goodsQrcodeId + " and b.user_id is not null group by b.user_id ";
       /* PageItem<Map<String,Object>> pageItem = jdbcRepository.queryPageItem(sql,qo.getCurPage(),qo.getPageSize());
        return  GoodsQrcodeConverter.p2EffectUser(pageItem);*/

        PageItem<EffectUserRo> pageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize(),EffectUserRo.class);
        for (EffectUserRo ro:pageItem.getItems()) {
            ro.setNickname(Base64Util.getFromBase64(ro.getNickname()));
        }
        return  pageItem;
    }


}
