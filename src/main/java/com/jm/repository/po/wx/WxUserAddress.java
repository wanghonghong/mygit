package com.jm.repository.po.wx;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>用户地址表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/7/11
 */
@Data
@Entity
public class WxUserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    
    private String provice;

    private String city;

    private String third;

    private String userName;

    private String phoneNumber;

    private String address;

    private String areaCode;
    
    private String detailAddress;
    
    private int defaultShow;//默认显示 1显示 0不显示

    private Date createTime;

    public WxUserAddress(){
        this.createTime = new Date();
    }
}
