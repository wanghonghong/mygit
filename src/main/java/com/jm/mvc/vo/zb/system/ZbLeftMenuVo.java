package com.jm.mvc.vo.zb.system;


import com.jm.repository.po.zb.system.ZbResource;
import lombok.Data;

import java.util.List;

/**
 * <p>左部菜单Vo</p>
 *
 * @author htp
 * @version latest
 * @date 2016/8/25
 */
@Data
public class ZbLeftMenuVo {

    private Integer resourceId;

    private String resourceName;

    private Integer parentResourceId;

    private String url;

    private String img;

    //菜单状态0可用，1不可用
    private Integer status;

    //菜单顺序
    private Integer seq;

    private String tplName;

    //子菜单列表
    private List<ZbResource> zbResources;

}
