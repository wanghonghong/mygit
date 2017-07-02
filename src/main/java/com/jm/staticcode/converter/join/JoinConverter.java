package com.jm.staticcode.converter.join;

import com.jm.business.service.system.SystemService;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.join.DispatchJoinVo;
import com.jm.mvc.vo.join.JoinClassVo;
import com.jm.mvc.vo.join.JoinCo;
import com.jm.mvc.vo.join.JoinVo;
import com.jm.repository.po.system.JmJoin;
import com.jm.repository.po.system.user.Role;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>平台转换器</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/30
 */
public class JoinConverter {

	@Autowired
	private SystemService systemService;

	public static JmJoin toJoin(JoinCo joinCo) {
		JmJoin jmJoin = new JmJoin();
		BeanUtils.copyProperties(joinCo, jmJoin);
		jmJoin.setBusinessLicense(ImgUtil.substringUrl(jmJoin.getBusinessLicense()));
		return jmJoin;
	}

	public static PageItem<JoinClassVo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<JoinClassVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<JoinClassVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			JoinClassVo joinClassVo = JsonMapper.map2Obj(map,JoinClassVo.class);
			if (null!=joinClassVo.getHeadImg()&&""!=joinClassVo.getHeadImg()){
				joinClassVo.setHeadImg(ImgUtil.appendUrl(joinClassVo.getHeadImg(),200));
			}else {
				joinClassVo.setHeadImg(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
			}
			list.add(joinClassVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static PageItem<JoinVo> toJoinVo(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<JoinVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<JoinVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			JoinVo joinVo = JsonMapper.map2Obj(map,JoinVo.class);
			joinVo.setCreateDate(Toolkit.dateToStr(map.get("create_date"),"yyyy-MM-dd HH:mm:ss"));
			joinVo.setApplyRole(Toolkit.parseObjForStr(map.get("role_name")));
			if (null!=map.get("head_img")&&""!=map.get("head_img")){
				String url = Toolkit.parseObjForStr(map.get("head_img"));
				joinVo.setHeadImg(ImgUtil.appendUrl(url,100));
			}else {
				joinVo.setHeadImg(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
			}
			list.add(joinVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static JoinVo toJoinVo(Role role, JmJoin jmJoin) {
		JoinVo joinVo = new JoinVo();
		Toolkit.copyPropertiesIgnoreNull(jmJoin,joinVo);
		joinVo.setApplyRole(role.getRoleName());
		joinVo.setApplyRoleId(role.getRoleId());
		if (null!=joinVo.getBusinessLicense()&&""!=joinVo.getBusinessLicense()){
			joinVo.setBusinessLicense(ImgUtil.appendUrl(joinVo.getBusinessLicense(),200));
		}else {
			joinVo.setBusinessLicense(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
		}
		return joinVo;
	}
	public static JoinVo toJoinVo(JmJoin jmJoin) {
		JoinVo joinVo = new JoinVo();
		if (jmJoin!=null){
			Toolkit.copyPropertiesIgnoreNull(jmJoin,joinVo);
			joinVo.setApplyRoleId(jmJoin.getApplyRole());
			if (null!=joinVo.getBusinessLicense()&&""!=joinVo.getBusinessLicense()){
				joinVo.setBusinessLicense(ImgUtil.appendUrl(joinVo.getBusinessLicense(),200));
			}else {
				joinVo.setBusinessLicense(Constant.THIRD_URL+"/css/pc/img/no_picture.png");
			}
		}
		return joinVo;
	}

	public static PageItem<DispatchJoinVo> toDispatchJoinVo(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<DispatchJoinVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<DispatchJoinVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			DispatchJoinVo dispatchJoinVo = JsonMapper.map2Obj(map,DispatchJoinVo.class);
			list.add(dispatchJoinVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

}
