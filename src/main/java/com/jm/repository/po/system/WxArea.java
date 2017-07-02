package com.jm.repository.po.system;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>微信映射地区表</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/10 9:33
 */
@Data
@Entity
public class WxArea {

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
