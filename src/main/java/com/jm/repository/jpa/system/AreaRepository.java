package com.jm.repository.jpa.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jm.repository.po.system.Area;

/**
 * 
 * @author: cj
 * @date: 2016-6-15
 */
public interface AreaRepository extends JpaRepository<Area, Integer>{
    /*@Query(value = "select a from Area a where right(a.areaId,2) ='00' ")*/
    @Query(value = "select a.* from area a where right(a.area_id,2) ='00'" ,nativeQuery = true)
    List<Area> findFilterArea();
    
    @Query(value = "select * from area where area_id in(?1)" ,nativeQuery = true)
    List<Area>findAreaAll(List ids);
}
