package com.jm.repository.po.system;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 商户充值二维码
 * @author chenyy
 * @date 2016/12/12
 */
@Data
@Entity
public class JmRechargeQrcode {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    @ApiModelProperty("二维码地址")
	private String qrcodeUrl;
	
    @ApiModelProperty("创建时间")
	private Date createTime;
	
    @ApiModelProperty("用户id")
	private Integer userId;

}
