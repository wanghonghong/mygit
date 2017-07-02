package com.jm.mvc.vo.system;

import lombok.Data;

/**
 * 地区Vo，本类只存放三个字段，从数据库取出，在静态方法里放入
 * @author chenyy
 *
 */
@Data
public class WxAreaVo {
	
	private Integer areaId;
	
	private String alisa;
	
	private Integer level;
	
	private Integer prentAreaId;

}
