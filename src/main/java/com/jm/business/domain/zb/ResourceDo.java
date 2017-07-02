package com.jm.business.domain.zb;

import lombok.Data;

/**
 * <p>映射菜单表</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/10/24 14:41
 */
@Data
public class ResourceDo {

    private Integer resourceId;

    private Integer parentResourceId;

    private String resourceName;


}
