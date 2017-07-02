package com.jm.repository.jpa.system;

import com.jm.repository.po.system.WxArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * <p>微信地区</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/10 9:47
 */
public interface WxAreaRepository extends JpaRepository<WxArea, Integer>{
    @Query(value = "select u.areaId from WxArea u where u.parentAreaId=?1 ")
    List findByParentAreaId(Integer pAreaId);
    
  /*  @Query(value = "select u.area_id,u.alias,u.level  from  wx_area u",nativeQuery=true)
    List<WxArea> findAllWxArea();*/
    
}
