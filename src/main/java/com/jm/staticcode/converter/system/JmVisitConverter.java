package com.jm.staticcode.converter.system;

import com.jm.mvc.vo.system.JmVisitVo;
import com.jm.repository.po.system.JmVisit;
import org.springframework.beans.BeanUtils;

/**
 * 商品类型bean类转化
 * @author zhengww
 *
 */
public class JmVisitConverter {

	public static JmVisit toJmVisit(JmVisitVo jmVisitVo) {
		JmVisit jmVisit = new JmVisit();
		BeanUtils.copyProperties(jmVisitVo, jmVisit);
		return jmVisit;

	}
}
