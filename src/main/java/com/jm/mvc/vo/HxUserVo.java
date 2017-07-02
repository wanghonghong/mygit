/**
 * 
 */
package com.jm.mvc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 
 *<p></p>
 *
 * @author hantp
 */
@Data
public class HxUserVo {

     private Integer shopId;
	
	 private Integer userId;

     private String userName;

     @ApiModelProperty(value="商家环信聊天账号")
     private String hxAccount;

     @ApiModelProperty(value = "头像")
     private String headImg;
}
