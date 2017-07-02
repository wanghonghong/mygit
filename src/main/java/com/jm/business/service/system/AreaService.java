package com.jm.business.service.system;

import com.jm.repository.jpa.system.AreaRepository;
import com.jm.repository.po.system.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * 地区服务层
 * @Created by cj on 2016/6/28 10:53
 */
@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;
   
    public List<Area> findAll(){
    	return areaRepository.findAll();
    }

    public List<Area> findAll(HashSet<Integer> areaIds){
        return areaRepository.findAll(areaIds);
    }

    public List<Area> findFilterArea() {
        return areaRepository.findFilterArea();
    }
}
