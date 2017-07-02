package com.jm.repository.po.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>访问记录表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@Entity
@ApiModel(description = "访问量PV、UV")
public class JmVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ip;

    private String requestURI;

    //访问时间
    private Date visitTime;

    private Integer pid;

    private Long userId;

    private Integer type; // 1: 商品 2：图文

    private Integer shopId;

    private Integer platForm; // 0 微信，1微博

}
