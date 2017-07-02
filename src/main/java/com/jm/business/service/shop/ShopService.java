package com.jm.business.service.shop;

import com.jm.mvc.vo.JmShopSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.erp.ErpShopQo;
import com.jm.mvc.vo.erp.ErpShopRo;
import com.jm.mvc.vo.erp.ErpShopUo;
import com.jm.mvc.vo.erp.ErpUserShopRo;
import com.jm.mvc.vo.shop.ShopForUpdateVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.po.shop.Shop;

import com.jm.repository.po.system.user.User;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>店铺</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/12
 */
@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Shop getShop(Integer shopId){
        return shopRepository.findOne(shopId);
    }

    @Cacheable(value ="pc_shop_cache", key="#shopId")
    public Shop getCacheShop(Integer shopId){
        return shopRepository.findOne(shopId);
    }

    public Shop getShopByAppId(String appid){
        return shopRepository.findShopByAppId(appid);
    }

    @Cacheable(value ="pc_shop_appid", key="#appid")
    public Shop getCacheShopByAppId(String appid){
        return shopRepository.findShopByAppId(appid);
    }

    public Shop saveShop(Shop shop) {
        return shopRepository.save(shop);
    }


    public List<JmShopSession> getShopList(Integer userId) throws IOException {
        String sql=" select  s.shop_id,s.shop_name,s.img_url,ur.role_id,r.level,s.app_id,s.status,r.type,s.wb_uid "
                + " from user u "
                + " left join user_role ur  on ur.user_id = u.user_id "
                + " left join role r on  r.role_id = ur.role_id "
                + " left join shop  s on ur.shop_id = s.shop_id "
                + " where ur.user_id="+userId;

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        List<JmShopSession> shops = new ArrayList<JmShopSession>();
        for (Map<String,Object> map : list){
            JmShopSession shopSession =   JsonMapper.map2Obj(map,JmShopSession.class);
            shops.add(shopSession);
        }
        return shops;
    }

    /**
     * 根据状态 用户编号  获取店铺列表
     * @param userId
     * @param status
     * @return
     */
    public List<Shop> getShopStatusList(Integer userId,Integer status){
        return shopRepository.getShopStatusList(userId, status);
    }

    public Shop findShopById(Integer shopId){
        return shopRepository.findShopByShopId(shopId);
    }

    public Shop updateShopall(ShopForUpdateVo shopVo){
        Shop shop=	findShopById(shopVo.getShopId());
        shop.setShopName(shopVo.getShopName());
        shop.setShopType(shopVo.getShopType());
        shop.setPhoneNumber(shopVo.getPhoneNumber());
        shop.setAddr(shopVo.getAddr());
        shop.setSpecificAddr(shopVo.getSpecificAddr());
        shop.setImgUrl(ImgUtil.substringUrl(shopVo.getImgUrl()));
        shop.setLinkMan(shopVo.getLinkMan());
        shop.setWxNum(shopVo.getWxNum());
        shop.setQqMail(shopVo.getQqMail());
        shop.setShareLan1(shopVo.getShareLan1());
        shop.setShareLan2(shopVo.getShareLan2());
        shop.setProvince(shopVo.getProvince());
        shop.setProvinceCode(shopVo.getProvinceCode());
        shop.setCity(shopVo.getCity());
        shop.setCityCode(shopVo.getCityCode());
        shop.setDistrict(shopVo.getDistrict());
        shop.setDistrictCode(shopVo.getDistrictCode());
        return shopRepository.save(shop);
    }



    public Shop setTemplate(int tempId, Integer shopId) {
        Shop shop = shopRepository.findShopByShopId(shopId);
        shop.setTempId(tempId);
        return shopRepository.save(shop);
    }

    public List<User> findUserByAppid(String appid) throws InstantiationException, IllegalAccessException, IOException {
        //shopRepository.findUsersByAppId(appid);
        List<Map<String,Object>> list =  jdbcRepository.findUsersByAppId(appid);
        List<User> users = new ArrayList<>();
        for(Map map :list){
            User user =  JsonMapper.map2Obj(map,User.class);
           /* map =  Toolkit.map2UpperMap(map);
            User user = BeanCopyUtils.mapToBean(map,User.class);*/
            users.add(user);
        }
        return users;
    }


    @ApiOperation("总部系统查询商家类列表")
    public PageItem<ErpShopRo> erpGetShopList(ErpShopQo qo) throws IOException {
        String sqlList = "select COUNT(*) shop_count,a.user_id,b.head_img,b.phone_number,b.user_name,b.create_date " +
                "from user_role a " +
                "LEFT JOIN `user` b  ON a.user_id = b.user_id " +
                "LEFT JOIN shop c on a.shop_id = c.shop_id " ;

        StringBuilder sqlCondition = new StringBuilder("");
        sqlCondition.append(" where a.role_id = 2 and c.app_id!='' and c.`status`=0 ");
        sqlCondition.append(JdbcUtil.appendAnd("b.user_name",qo.getUserName()));
        sqlCondition.append(JdbcUtil.appendAnd("b.sex",qo.getSex()));
        sqlCondition.append(JdbcUtil.appendAnd("b.phone_number",qo.getPhoneNumber()));
        if(qo.getStarTime()!=""&&null!=qo.getStarTime()&&qo.getEndTime()!=""&&null!=qo.getEndTime()){
            sqlCondition.append(" and b.create_date BETWEEN '" + qo.getStarTime() + "' and '"+qo.getEndTime()+"'");
        }
        sqlCondition.append(" GROUP BY a.user_id ");

        String countsql=" select count(*) from ( "+sqlList+sqlCondition+") a";
        int count = jdbcTemplate.queryForInt(countsql);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+sqlCondition+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<ErpShopRo> erpShopls = new ArrayList<>();
        for (Map<String,Object> map : list){
            ErpShopRo customerRo =   JsonMapper.map2Obj(map,ErpShopRo.class);
            if (map.get("head_img")!=null){
                customerRo.setHeadImg(ImgUtil.appendUrl(Toolkit.parseObjForStr(map.get("head_img")),100));
            }else {
                customerRo.setHeadImg(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
            }
            erpShopls.add(customerRo);
        }
        PageItem<ErpShopRo> erpShopRos = new PageItem<>();
        erpShopRos.setCount(count);
        erpShopRos.setItems(erpShopls);
        return erpShopRos;
    }


    @ApiOperation("根据用户编号获取店铺列表")
    public PageItem<ErpUserShopRo> erpUserShops(ErpShopQo qo) throws IOException {
        if(qo==null){
            return null;
        }
        if(qo.getUserId()==null){
            return null;
        }
        String sqlList = " select c.pub_qrcode_url,b.shop_name,count(d.user_id) user_count,f.user_name " +
                " from user_role a " +
                " LEFT JOIN shop b on a.shop_id = b.shop_id " +
                " LEFT JOIN wx_pub_account c on b.app_id = c.appid " +
                " LEFT JOIN wx_user d on b.app_id=d.appid " +
                " LEFT JOIN user_role e on b.shop_id = e.shop_id and e.role_id = 24 " +
                " LEFT JOIN `user` f on e.user_id = f.user_id " +
                " where a.role_id = 2 and a.user_id ="+qo.getUserId()+" and b.app_id!='' and b.`status`=0 " +
                " GROUP BY b.shop_id " ;
        int count = jdbcTemplate.queryForInt("SELECT count(*) from ( "+sqlList +") a");
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<ErpUserShopRo> erpShopls = new ArrayList<>();
        for (Map<String,Object> map : list){
            ErpUserShopRo customerRo =   JsonMapper.map2Obj(map,ErpUserShopRo.class);
            erpShopls.add(customerRo);
        }
        PageItem<ErpUserShopRo> erpShopRos = new PageItem<>();
        erpShopRos.setCount(count);
        erpShopRos.setItems(erpShopls);
        return erpShopRos;
    }




    @ApiOperation("根据用户编号获取店铺列表")
    public PageItem<ErpUserShopRo> erpShops(ErpShopQo qo) throws IOException {
        if(qo==null){
            return null;
        }
        if(qo.getUserId()==null){
            return null;
        }
        String sqlList = "select c.pub_qrcode_url,b.shop_name,b.create_date,b.company_auth,b.user_auth,b.jm_auth,b.promise," +
                "b.exchange,b.direct_sell,b.safety " +
                " from user_role a " +
                " LEFT JOIN shop b on a.shop_id = b.shop_id " +
                " LEFT JOIN wx_pub_account c ON c.appid = b.app_id " +
                " where a.role_id=2 and a.user_id="+qo.getUserId()+" and b.app_id!='' and b.status=0 " ;
        int count = jdbcTemplate.queryForInt("SELECT count(*) from ( "+sqlList +") a");
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<ErpUserShopRo> erpShopls = new ArrayList<>();
        for (Map<String,Object> map : list){
            ErpUserShopRo customerRo =   JsonMapper.map2Obj(map,ErpUserShopRo.class);
            erpShopls.add(customerRo);
        }
        PageItem<ErpUserShopRo> erpShopRos = new PageItem<>();
        erpShopRos.setCount(count);
        erpShopRos.setItems(erpShopls);
        return erpShopRos;
    }


    @ApiOperation(" 店铺审核列表")
    public PageItem<ErpUserShopRo> auditShops(ErpShopQo qo) throws IOException {
        String sqlList = "SELECT a.img_url,d.pub_qrcode_url,a.shop_name,c.user_name oper_user_name,c.phone_number oper_phone_number ,a.create_date,a.shop_status,a.shop_id " +
                "from shop a " +
                "LEFT JOIN user_role b on a.shop_id = b.shop_id and b.role_id=2 " +
                "LEFT JOIN user c on b.user_id = c.user_id " +
                "LEFT JOIN wx_pub_account d on d.appid = a.app_id " +
                "where 1=1 " ;
        sqlList+=JdbcUtil.appendLike("c.user_name",qo.getUserName());
        sqlList+=JdbcUtil.appendLike("a.shop_name",qo.getShopName());
        sqlList+=JdbcUtil.appendLike("c.phone_number",qo.getPhoneNumber());
        sqlList+=JdbcUtil.appendAnd("a.shop_status",qo.getShopStatus());
        if(qo.getStarTime()!=""&&null!=qo.getStarTime()&&qo.getEndTime()!=""&&null!=qo.getEndTime()){
            sqlList+=" and a.create_date BETWEEN '" + qo.getStarTime() + "' and '"+qo.getEndTime()+"'";
        }
        int count = jdbcTemplate.queryForInt("SELECT count(*) from ( "+sqlList +") a");
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+qo.getCurPage()*qo.getPageSize()+","+qo.getPageSize());
        List<ErpUserShopRo> erpShopls = new ArrayList<>();
        for (Map<String,Object> map : list){
            ErpUserShopRo customerRo =   JsonMapper.map2Obj(map,ErpUserShopRo.class);
            if(customerRo.getImgUrl()!=null){
                customerRo.setImgUrl(ImgUtil.appendUrl(customerRo.getImgUrl()));
            }else {
                customerRo.setImgUrl(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
            }
            if(customerRo.getPubQrcodeUrl()==null){
                customerRo.setPubQrcodeUrl(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
            }
            erpShopls.add(customerRo);
        }
        PageItem<ErpUserShopRo> erpShopRos = new PageItem<>();
        erpShopRos.setCount(count);
        erpShopRos.setItems(erpShopls);
        return erpShopRos;
    }

    @ApiOperation("修改店铺状态 ")
    public void updateShopStatus(ErpShopUo uo){
        Shop shop = shopRepository.findOne(uo.getShopId());
        shop.setShopStatus(uo.getShopStatus());
        shopRepository.save(shop);
    }

    @ApiOperation("查询店铺是否已经微博授权 ")
    public Shop findShopByWbUid(String accessToken) {
        return shopRepository.findShopByWbUid(accessToken);
    }
}

