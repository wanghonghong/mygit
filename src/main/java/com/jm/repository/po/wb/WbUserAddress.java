package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@ApiModel(description = "商家微博信息")
public class WbUserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "地址id")
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

    public WbUserAddress(){
        this.createTime = new Date();
    }

}
