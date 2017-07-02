package com.jm.staticcode.converter.wx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.system.WxAreaVo;
import com.jm.repository.po.system.WxArea;

/**
 * 微信地区转换
 * @author chenyy
 *
 */
public class WxAreaConverter {
	
	public static List<WxAreaVo> ps2vs(List<WxArea> wxAreas){
		List<WxAreaVo> wxAreaVos = new ArrayList<>();
		for (WxArea wxArea : wxAreas) {
			WxAreaVo wxAreaVo = new WxAreaVo();
			wxAreaVo.setAreaId(wxArea.getAreaId());
			wxAreaVo.setAlisa(wxArea.getAlias());
			wxAreaVo.setLevel(wxArea.getLevel());
			wxAreaVo.setPrentAreaId(wxArea.getParentAreaId());
			wxAreaVos.add(wxAreaVo);
		}
		return wxAreaVos;
	}

}
