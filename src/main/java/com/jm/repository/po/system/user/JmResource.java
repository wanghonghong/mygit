package com.jm.repository.po.system.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>资源表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity
public class JmResource {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty(value = "给父ID为0的菜单区分 0左 1右")
    private Integer type;

}
