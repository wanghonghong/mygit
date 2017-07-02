package com.jm.business.service.shop.enrolmentActivity;


import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.enrolmentActivity.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.activity.*;
import com.jm.repository.po.shop.activity.*;
import com.jm.staticcode.util.ImgUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>报名活动</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 14:49
 */
@Log4j
@Service
public class EnrolmentActivityService {

    @Autowired
    private EnrolmentConfRepository enrolmentConfRepository;
    @Autowired
    private EnrolmentActivityRepository enrolmentActivityRepository;
    @Autowired
    private EnrolmentUserRepository enrolmentUserRepository;
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JmMessage addEnrolmentConf(EnrolmentConfCo enrolmentConfCo) {
        EnrolmentConf enrolmentConf = new EnrolmentConf();
        BeanUtils.copyProperties(enrolmentConfCo,enrolmentConf);
        enrolmentConfRepository.save(enrolmentConf);
        return new JmMessage(0,"活动配置新增成功!!");
    }

    @Transactional
    public JmMessage updateEnrolmentConf(EnrolmentConfUo enrolmentConfUo, Integer shopId) {
        Integer id = enrolmentConfUo.getId();
        EnrolmentConf enrolmentConf = enrolmentConfRepository.getOne(id);
        Integer shopid = enrolmentConf.getShopId();
        if(!shopid.equals(shopId)){
            return new JmMessage(1,"无权限修改!!");
        }
        BeanUtils.copyProperties(enrolmentConfUo,enrolmentConf);
        enrolmentConfRepository.save(enrolmentConf);
        return new JmMessage(0,"编辑成功!!");
    }

    @Transactional
    public JmMessage deleteEnrolmentConf(Integer id, Integer shopId) {
        EnrolmentConf enrolmentConf = enrolmentConfRepository.getOne(id);
        Integer shopid = enrolmentConf.getShopId();
        if(!shopid.equals(shopId)){
            return new JmMessage(1,"无权限删除!!");
        }
        enrolmentConf.setIsValid(1);
        enrolmentConfRepository.save(enrolmentConf);
        return new JmMessage(0,"删除成功!!");
    }

    public PageItem<EnrolmentConfVo> findEnrolmentConfList(EnrolmentConfQo enrolmentConfQo, Integer shopId) throws IllegalAccessException, IOException, InstantiationException {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendOrderBy("a.create_date"));
        String sql = "select a.id,a.title_name,a.set_info ";
        String fromSql = " from enrolment_conf a where 1=1 and a.is_valid =0 and a.shop_id = "+shopId+sqlCondition;

        return jdbcUtil.queryPageItem(sql+fromSql,enrolmentConfQo.getCurPage(),enrolmentConfQo.getPageSize(),EnrolmentConfVo.class);
    }

/*    public JmMessage addEnrolmentUser(EnrolmentUserCo enrolmentUserCo, Integer shopId) {
        EnrolmentUser enrolmentUser = new EnrolmentUser();
        BeanUtils.copyProperties(enrolmentUserCo,enrolmentUser);
        enrolmentUser.setShopId(shopId);
        enrolmentUserRepository.save(enrolmentUser);
        return new JmMessage(0,"报名成功!!");
    }*/


    public PageItem<EnrolmentUserVo> findEnrolmentUserList(EnrolmentUserQo enrolmentUserQo) throws IllegalAccessException, IOException, InstantiationException {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.activity_id",enrolmentUserQo.getActivityId()));
        sqlCondition.append(JdbcUtil.appendLike("a.user_name",enrolmentUserQo.getUserName()));
        sqlCondition.append(JdbcUtil.appendLike("a.tel_phone",enrolmentUserQo.getTelPhone()));
        sqlCondition.append(JdbcUtil.appendOrderBy("a.create_date"));
        String sql = "select a.user_name,a.tel_phone,a.sex,a.position," +
                " a.email,a.address,a.web_url,a.create_date ";
        String fromSql = " from enrolment_user a where 1=1 "+sqlCondition;
        return jdbcUtil.queryPageItem(sql+fromSql,enrolmentUserQo.getCurPage(),enrolmentUserQo.getPageSize(),EnrolmentUserVo.class);
    }

    public JmMessage addEnrolmentActivity(EnrolmentActivityCo enrolmentActivityCo, Integer shopId) {
        EnrolmentActivity enrolmentActivity = new EnrolmentActivity();
        BeanUtils.copyProperties(enrolmentActivityCo,enrolmentActivity);
        enrolmentActivity.setImg(ImgUtil.substringUrl(enrolmentActivityCo.getImg()));
        enrolmentActivity.setShopId(shopId);
        enrolmentActivity.setStatus(0);
        enrolmentActivityRepository.save(enrolmentActivity);
        return new JmMessage(0,"报名活动保存成功!!");
    }

    @Transactional
    public JmMessage delEnrolmentActivity(Integer id, Integer shopId) {
        EnrolmentActivity enrolmentActivity =  enrolmentActivityRepository.findOne(id);
        Integer shopid = enrolmentActivity.getShopId();
        if(!shopid.equals(shopId)){
            return new JmMessage(1,"无权限删除!!");
        }
        enrolmentActivity.setStatus(3); //删除
        enrolmentActivityRepository.save(enrolmentActivity);
        return new JmMessage(0,"删除报名活动成功!!");
    }

    public PageItem<EnrolmentActivityVo> findEnrolmentActivityList(EnrolmentActivityQo enrolmentActivityQo, Integer shopId) throws IllegalAccessException, IOException, InstantiationException {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        if(-1 == enrolmentActivityQo.getStatus()){
            sqlCondition.append(JdbcUtil.appendNotAnd("a.status",3));
        }else{
            sqlCondition.append(JdbcUtil.appendAnd("a.status",enrolmentActivityQo.getStatus()));
        }

        sqlCondition.append(JdbcUtil.appendLike("a.activity_name",enrolmentActivityQo.getActivityName()));
        sqlCondition.append(JdbcUtil.appendAnd("a.create_date",enrolmentActivityQo.getCreateStartDate(),enrolmentActivityQo.getCreateEndDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(enrolmentActivityQo.getStartDate() != null){
            sqlCondition.append(" and a.start_date >= '" + sdf.format(enrolmentActivityQo.getStartDate())+"'");
        }
        if(enrolmentActivityQo.getEndDate() != null){
            sqlCondition.append(" and a.end_date <= '" + sdf.format(enrolmentActivityQo.getEndDate())+"'");
        }
        sqlCondition.append(JdbcUtil.appendOrderBy("a.create_date"));
        String sql = "select a.id,a.img,a.activity_name,a.create_date," +
                " a.start_date,a.end_date,a.status,b.count ";
        String fromSql = " from enrolment_activity a left join " +
                " (select a.activity_id, ifnull(count(1),0) as count from enrolment_user a where a.shop_id =" +shopId +
                " group by a.activity_id ) b" +
                " on a.id = b.activity_id where 1=1 "+sqlCondition ;
        PageItem<EnrolmentActivityVo> pageItem = jdbcUtil.queryPageItem(sql+fromSql,enrolmentActivityQo.getCurPage(),enrolmentActivityQo.getPageSize(),EnrolmentActivityVo.class);
        converterImg(pageItem);
        return pageItem;
    }

    public EnrolmentActivityVo getEnrolmentActivity(Integer id, Integer shopId) throws IOException, InstantiationException, IllegalAccessException {
        StringBuilder sqlCondition = new StringBuilder();
        sqlCondition.append(JdbcUtil.appendAnd("a.shop_id",shopId));
        sqlCondition.append(JdbcUtil.appendAnd("a.id",id));
        String sql = "select a.id,b.set_info,a.activity_name ,a.img,a.sms,a.notice_date ," +
                " a.detail_json ,a.conf_id , a.start_date,a.end_date" +
                " from enrolment_activity a left join enrolment_conf b on a.conf_id = b.id where 1=1 "+sqlCondition;
        return jdbcUtil.findOne(sql,EnrolmentActivityVo.class);
       /* Map map = jdbcTemplate.queryForMap(sql);
        EnrolmentActivityVo enrolmentActivityVo = JsonMapper.map2Obj(map,EnrolmentActivityVo.class);
        enrolmentActivityVo.setImg(ImgUtil.appendUrl(enrolmentActivityVo.getImg()));
        return enrolmentActivityVo;*/
    }

    @Transactional
    public JmMessage updateEnrolmentActivity(EnrolmentActivityUo enrolmentActivityUo, Integer shopId) {
        Integer id = enrolmentActivityUo.getId();
        EnrolmentActivity enrolmentActivity = enrolmentActivityRepository.findOne(id);
        if(!shopId.equals(enrolmentActivity.getShopId())){
             return new JmMessage(1,"无权限编辑!!");
        }
        enrolmentActivityUo.setImg(ImgUtil.substringUrl(enrolmentActivityUo.getImg()));
        BeanUtils.copyProperties(enrolmentActivityUo,enrolmentActivity);
        return new JmMessage(0,"编辑成功!!");
    }

    @ApiOperation("图片地址拼接域名")
    private void converterImg(PageItem page) {
        List<EnrolmentActivityVo> list = page.getItems();
        for (EnrolmentActivityVo vo:list) {
            vo.setImg(ImgUtil.appendUrl(vo.getImg()));
        }
    }

    @Transactional
    public void updateActivityTask() {
        enrolmentActivityRepository.updateActivityTaskIng();
        enrolmentActivityRepository.updateActivityTaskOver();
    }

    public EnrolmentConfVo getEnrolmentConf(Integer id, Integer shopId) throws Exception {
        EnrolmentConf enrolmentConf = enrolmentConfRepository.getOne(id);
        EnrolmentConfVo enrolmentConfVo = new EnrolmentConfVo();
        Integer shopid = enrolmentConf.getShopId();
        if(!shopid.equals(shopId)){
            throw new Exception ("无权限删除!!") ;
        }
        BeanUtils.copyProperties(enrolmentConf,enrolmentConfVo);
        return enrolmentConfVo;
    }
}