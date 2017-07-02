package com.jm.business.domain;

import lombok.Data;

/**
 * <p>映射地区表</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/24 14:41
 */
@Data
public class AreaDo {

    private Integer areaId;

    private Integer parentAreaId;

    private String areaName;


}
