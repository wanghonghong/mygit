package com.jm.staticcode.converter.shop.distribution;

import com.jm.mvc.vo.shop.distribution.*;
import com.jm.repository.po.shop.brokerage.PutSet;
import org.springframework.beans.BeanUtils;

/**
 * 发放设置bean类转化
 * @author zhengww
 *
 */
public class PutSetConverter {

	public static PutSetVo toPutSettingVo(PutSet putSet) {
		PutSetVo putSetVo = new PutSetVo();
		BeanUtils.copyProperties(putSet, putSetVo);
		return putSetVo;
	}

	public static PutSet toPutSetting(PutSetCo putSetCo) {
		PutSet putSet = new PutSet();
		BeanUtils.copyProperties(putSetCo, putSet);
		return putSet;
	}

	public static PutSet toPutSetting(PutSetUo putSetUo, PutSet putSet) {
		BeanUtils.copyProperties(putSetUo, putSet);
		return putSet;
	}

}
