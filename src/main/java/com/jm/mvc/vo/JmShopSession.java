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
 * @author hantp
 * @version latest
 * @data 2016年8月2日
 */
@Data
public class JmShopSession implements Serializable {
	
     private Integer shopId;
     
     private String shopName;
     
     private String imgUrl="";
     
     private Integer roleId;

     private String appId;
     
     private Integer status;

     private Integer type;

     @ApiModelProperty(value = "微博商家用户id （ cj 2017-03-04 ）")
     private Long  wbUid;
}
