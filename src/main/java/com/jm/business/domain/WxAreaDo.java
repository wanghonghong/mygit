package com.jm.business.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>微信映射地区表</p>
 *
 * @version latest
 * @Author wukf
 * @Date 2016/10/10
 */
@Data
public class WxAreaDo {

    private Integer areaId;

    private Integer pAreaId;

    private String areaName;

    private String parentAreaName;

}
