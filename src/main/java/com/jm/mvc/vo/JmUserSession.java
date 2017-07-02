/**
 * 
 */
package com.jm.mvc.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 *<p></p>
 *
 * @author wukf
 * @version latest
 * @data 2016年7月21日
 */
@Data
public class JmUserSession  implements Serializable {

     @ApiModelProperty(value="当前用户ID")
	 private Integer userId;

     @ApiModelProperty(value="当前用户名称")
     private String userName;

     @ApiModelProperty(value="当前店铺Id")
     private Integer shopId;

     @ApiModelProperty(value="当前店铺名称")
     private String shopName;
     
     private String appId;

     @ApiModelProperty(value="当前用户在当前店铺的角色Id")
     private Integer roleId;

     @ApiModelProperty(value="当前用户头像")
     private String imgUrl;

     private List<JmShopSession> shopLs;
     
     private String qrCode;

     @ApiModelProperty(value="商家环信聊天账号")
     private String hxAccount;

     @ApiModelProperty(value = "头像")
     private String headImg;

     @ApiModelProperty(value = "上次在線時間刷新")
     private Long refreshTime;

     @ApiModelProperty(value = "用户基础二维码")
     private String pubQrcodeUrl;

     @ApiModelProperty("店铺商家微博uid -- cj 2017-03-04")
     private Long uid;
}
