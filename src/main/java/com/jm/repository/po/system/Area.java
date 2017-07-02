package com.jm.repository.po.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 地区表
 * @author: cj
 * @date: 2016-6-15
 */
@Data
@Entity
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer areaId;

    private String areaName;

    private Integer parentAreaId;
    
    private Integer status;
    
    private Integer level;
    
    private Integer sort;
    
    private String alias;
    
    private String pinyin;
    
    private String pinyinjc;

}
