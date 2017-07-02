package com.jm.repository.po.system.user;

import lombok.Data;

import javax.persistence.*;

/**
 * <p>资源表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity
public class Resource {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourceId;

    //主菜单顺序
    private Integer sequence;

    //菜单顺序
    private Integer seqencing;

    private String resourceName;

    private Integer parentRsourceId;
    
    private String url;
    
    private String imgPath;
    
    //菜单状态0可用，1不可用
    private Integer status;

    private String tplName;

}
