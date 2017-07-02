package com.jm.mvc.vo.wx;
import lombok.Data;

/**
 *<p>微信收货地址返回参数</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年7月1日
 */
@Data
public class OpenAddResult {
	/**
	 * 获取编辑收货地址成功返回“openAddress:ok”。
	 */
	private String errMsg;
	/**
	 * 收货人姓名。
	 */
	private String userName;
	
	/**
	 * 邮编
	 */
	private String postalCode;
	/**
	 * 国标收货地址第一级地址（省）。
	 */
	private String provinceName;
	
	/**
	 * 国标收货地址第二级地址（市）。
	 */
	private String cityName;
	
	/**
	 * 国标收货地址第三级地址（国家）。
	 */
	private String countryName;
	
	/**
	 * 详细收货地址信息。
	 */
	private String detailInfo;
	
	/**
	 * 收货地址国家码。
	 */
	private String nationalCode;

}
