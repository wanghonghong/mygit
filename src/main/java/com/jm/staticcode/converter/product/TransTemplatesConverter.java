package com.jm.staticcode.converter.product;

import com.jm.mvc.vo.product.trans.TransTempVo;
import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.product.trans.TransTemplatesCo;
import com.jm.repository.po.product.TransTemplates;

/**
 * 运费模板相关bean类转化
 * @author zhengww
 *
 */
public class TransTemplatesConverter {

	public static TransTemplates toTransTemplates(TransTemplatesCo transTemplatesCo) {
		TransTemplates transTemplates = new TransTemplates();
		BeanUtils.copyProperties(transTemplatesCo, transTemplates);
		return transTemplates;
	}

	public static TransTempVo toTransTemplatesVo(TransTemplates transTemplates) {
		TransTempVo transTempVo = new TransTempVo();
		BeanUtils.copyProperties(transTemplates,transTempVo);
		return transTempVo;
	}
}
