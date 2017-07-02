package com.jm.business.service.shop.imageText;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.imageText.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.imageText.ImageTextTipRepository;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wx.Base64Util;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>图文打赏</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/20 10:14
 */
@Log4j
@Service
public class ImageTextTipService {
    @Autowired
    protected JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ImageTextTipRepository imageTextTipRepository;


    @ApiOperation("查询点赞图文信息分页")
    public Map findPageImageTextTip(Integer shopId, ImageTextTipQo imageTextTipQo) {
        Map map = new HashMap();
        String fromSql = findPageImageTextTipFromSql(shopId,imageTextTipQo);
        String sql = "select a.*,ifnull(sum(b.tip_money),0) as tip,ifnull(b.plat_form,"
                +Toolkit.parseObjForInt(imageTextTipQo.getPlatForm())+
                " )as platForm," +
                " case when a.isValid='Y' and a.status = 0 then '上架'" +
                " when a.isValid='Y' and a.status = 1 then '下架'" +
                " when a.isValid='N' then '删除' end as statusName" +fromSql+" group by a.id order by a.upperTime desc";
        PageItem page = jdbcUtil.queryAnalysisPageItem(sql," from("+sql+")a",imageTextTipQo.getCurPage(),imageTextTipQo.getPageSize());
        map.put("imageTextTipInfo",page);
        return map;
    }

    @ApiOperation("查询点赞图文信息分页 from语句")
    private String findPageImageTextTipFromSql(Integer shopId, ImageTextTipQo imageTextTipQo) {
        StringBuilder sqlCondition = new StringBuilder();
        StringBuilder sqlCondition2 = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("shop_id",shopId));
        if(99 == imageTextTipQo.getStatus()){

        }else if(-1 == imageTextTipQo.getStatus()){
            sqlCondition.append(JdbcUtil.appendAnd("is_valid","N")); // 删除
        }else if( 0 == imageTextTipQo.getStatus()){ // 上架
            sqlCondition.append(JdbcUtil.appendAnd("is_valid","Y"));
            sqlCondition.append(JdbcUtil.appendAnd("status",0)); // 上架
        }else if( 1 == imageTextTipQo.getStatus()){ // 下架
            sqlCondition.append(JdbcUtil.appendAnd("is_valid","Y"));
            sqlCondition.append(JdbcUtil.appendAnd("status",1)); // 下架
        }
        sqlCondition.append(JdbcUtil.appendLike("image_text_tile",imageTextTipQo.getImageTextTitle()));
        sqlCondition.append(JdbcUtil.appendAnd("upper_time",imageTextTipQo.getBeginTime(),imageTextTipQo.getEndTime()));
        sqlCondition2.append(JdbcUtil.appendAnd("plat_form",imageTextTipQo.getPlatForm()));
        String fromSql = " from (select id,image_text_tile as title,upper_time as upperTime,is_valid as isValid,status from image_text " +
                " where 1=1 " +sqlCondition+
                " ) a left join (select * from image_text_tip where pay_id is not null and status = 1 "+sqlCondition2 +") b on a.id = b.image_text_id ";
        return fromSql;
    }

    @ApiOperation("针对某个图文点赞金额数据")
    public Map findPageTip(ImageTextTipQo imageTextTipQo) {
        Map map = new HashMap();
        String fromSql = findPageTipFromSql(imageTextTipQo);
        String sql = "select a.tip,a.tipTime,b.nickname as nickName,b.headimgurl " +fromSql+" order by a.tipTime desc";
        PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,imageTextTipQo.getCurPage(),imageTextTipQo.getPageSize());
        converterNickName(page);
        map.put("tipInfo",page);
        return map;
    }

    @ApiOperation("针对某个图文点赞信息分页 from语句")
    private String findPageTipFromSql(ImageTextTipQo imageTextTipQo) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.image_text_id",imageTextTipQo.getImageTextId()));
        sqlCondition.append(JdbcUtil.appendAnd("a.plat_form",imageTextTipQo.getPlatForm()));
        String fromSql = " from (select a.create_time as tipTime,a.tip_money as tip,a.user_id from image_text_tip a where 1=1 " +sqlCondition+
                " and a.pay_id is not null and a.status = 1)a left join wx_user b on a.user_id = b.user_id";
        return fromSql;
    }

    @ApiOperation("微信昵称解码")
    private void converterNickName(PageItem page) {
        List<Map> list = page.getItems();
        for (Map map:list) {
            map.put("nickName", Base64Util.getFromBase64(Toolkit.parseObjForStr(map.get("nickName"))));
        }
    }
    @ApiOperation("打赏实时收入")
    public Map findPageShopTipList(ImageTextTipQo imageTextTipQo, Integer shopId) {
        Map map = new HashMap();
        String fromSql = findPageShopTipListFromSql(imageTextTipQo,shopId);
        String sql = "select b.headimgurl,b.user_name as userName,b.nickname as nickName," +
                "b.phone_number as phoneNum,ifnull(a.tip_money,0) as tip," +
                "a.create_time as tipTime ,c.image_text_tile as title" +fromSql+" order by a.create_time desc";
        PageItem page = jdbcUtil.queryAnalysisPageItem(sql,fromSql,imageTextTipQo.getCurPage(),imageTextTipQo.getPageSize());
        converterNickName(page);
        map.put("shopTipInfo",page);
        return map;
    }
    @ApiOperation("打赏实时收入 from语句")
    private String findPageShopTipListFromSql(ImageTextTipQo imageTextTipQo, Integer shopId) {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendLike("c.image_text_tile",imageTextTipQo.getImageTextTitle()));
        sqlCondition.append(JdbcUtil.appendAnd("a.create_time",imageTextTipQo.getBeginTime(),imageTextTipQo.getEndTime()));
        sqlCondition.append(JdbcUtil.appendAnd("b.phone_number",imageTextTipQo.getTelPhone()));
        sqlCondition.append(JdbcUtil.appendLike("b.nickname",Base64Util.enCoding(imageTextTipQo.getNickName())));
        sqlCondition.append(JdbcUtil.appendLike("b.user_name",imageTextTipQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendAnd("a.plat_form",imageTextTipQo.getPlatForm()));
        String fromSql = " from image_text_tip a left join wx_user b on a.user_id = b.user_id" +
                " left join image_text c on a.image_text_id = c.id where 1=1 and a.status =1 and a.pay_id is not null and a.shop_id="+shopId+" "
                +sqlCondition;
        return fromSql;
    }
}
