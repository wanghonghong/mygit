package com.jm.repository.po.wx;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>微信用户分组表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/7/29
 */
@Data
@Entity
public class WxUserGroup {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //微信上的分组id
    private int groupid;

    private String name;

    private int count;
    
    private String appid;
    
}
