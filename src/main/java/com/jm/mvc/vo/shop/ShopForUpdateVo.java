package com.jm.mvc.vo.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>店铺管理</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/22
 */
@Data
@ApiModel(description = "店铺更新")
public class ShopForUpdateVo {

		private Integer shopId;
	 	@ApiModelProperty(value = "店铺名称")
	    private String shopName;

	    @ApiModelProperty(value = "主营项目")
	    private Integer shopType;

	    private String appId;

	    private String appSecret;

	    @ApiModelProperty(value = "手机号")
	    private String phoneNumber;
	    
	    @ApiModelProperty(value = "经营地址")
	    private String addr;
	    
	    @ApiModelProperty(value = "具体地址")
	    private String specificAddr;
	    
	    @ApiModelProperty(value = "主图地址")
	    private String imgUrl;
	    
	    @ApiModelProperty(value = "联系人")
	    private String  linkMan;
	    
	    @ApiModelProperty(value = "微信账号")
	    private String wxNum;
	    
	    @ApiModelProperty(value = "QQ邮箱")
	    private String qqMail;
	    
	    @ApiModelProperty(value = "分享语1")
	    private String shareLan1;
	    
	    @ApiModelProperty(value = "分享语2")
	    private String shareLan2;
	    
	    @ApiModelProperty(value = "省")
	    private String province;

	    @ApiModelProperty(value = "省地区代码")
	    private String provinceCode;
	    
	    @ApiModelProperty(value = "市")
	    private String city;
	    
	    @ApiModelProperty(value = "市地区代码")
	    private String cityCode;
	    
	    @ApiModelProperty(value = "区")
	    private String district;
	    
	    @ApiModelProperty(value = "区地区代码")
	    private String districtCode;
	    
	    private Integer status;
	    
	    private Integer tempId;
	    

}
