package com.jm.business.service.shop.shopSet;

import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.shop.shopSet.UserCenterConfigRo;
import com.jm.mvc.vo.shop.shopSet.UserCenterCustomRo;
import com.jm.mvc.vo.shop.shopSet.UserCenterFunsRo;
import com.jm.mvc.vo.zb.system.UserCenterVersionVo;
import com.jm.repository.jpa.shop.shopSetting.UserCenterConfigRepository;
import com.jm.repository.jpa.shop.shopSetting.UserCenterFunsRepository;
import com.jm.repository.jpa.shop.shopSetting.UserCenterVersionRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import com.jm.repository.po.shop.shopSet.UserCenterVersion;
import com.jm.staticcode.util.BeanCopyUtils;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员中心软件版本
 */
@Service
public class UserCenterVersionService {
	
	@Autowired
	private UserCenterVersionRepository userCenterVersionRepository;
	@Autowired
	private ShopService shopService;
	@Autowired
	private UserCenterFunsRepository userCenterFunsRepository;
	@Autowired
	private UserCenterCustomService userCenterCustomService;
	@Autowired
	private UserCenterConfigRepository userCenterConfigRepository;

	public List<UserCenterVersion> save(List<UserCenterVersion> versionList){
		return userCenterVersionRepository.save(versionList);
	}

	@Transactional
	public void saveVersion(UserCenterVersionVo vo){
		String[] ids =  vo.getResourceIds().split(",");
		if(ids.length>0){
			//删除原数据
			deleteBySoftId(vo.getSoftId());
		}
		List<UserCenterVersion> versions = new ArrayList<>();
		for (String id:ids) {
			UserCenterVersion version = new UserCenterVersion();
			version.setFunsId(Toolkit.parseObjForInt(id));
			version.setSoftId(vo.getSoftId());
			versions.add(version);
		}
		if(versions.size()>0){
			save(versions);
		}
	}

	public void deleteBySoftId(Integer softId){
		userCenterVersionRepository.deleteBySoftId(softId);
	}

	public List<UserCenterVersion> findBySoftId(Integer softId){
		return userCenterVersionRepository.findBySoftId(softId);
	}

	public UserCenterConfigRo getUserCenterFuns(Integer shopId){
		UserCenterConfigRo ro = new UserCenterConfigRo();
		Shop shop = shopService.getShop(shopId);
		List<UserCenterFuns> funs = new ArrayList<>();
		if(shop.getSoftId()!=null){
			List<UserCenterVersion> userCenterVersions = findBySoftId(shop.getSoftId());
			List<Integer> ids = new ArrayList<>();
			for (UserCenterVersion version:userCenterVersions) {
				ids.add(version.getFunsId());
			}
			if(ids.size()>0){
				funs = userCenterFunsRepository.findByFunId(ids);
			}
		}

		UserCenterConfig config = userCenterConfigRepository.findUserCenterConfigByShopId(shopId);
		config.setTopPic(ImgUtil.appendUrl(config.getTopPic()));
		List<UserCenterCustom> customs = userCenterCustomService.findByShopId(shopId);
		List<UserCenterCustomRo> customRos = new ArrayList<>();
		for (UserCenterCustom custom:customs) {
			UserCenterCustomRo customRo = new UserCenterCustomRo();
			BeanUtils.copyProperties(custom, customRo);
			for (UserCenterFuns fun:funs) {
				if(custom.getFunsId().equals(fun.getFunId())){
					customRo.setFunName(fun.getFunName());
					customRo.setGroup(fun.getGroup());
					customRo.setItemIcon(fun.getItemIcon());
				}
			}
			customRos.add(customRo);
		}


		List<UserCenterFunsRo> centerFunsRos = new ArrayList<>();
		for (UserCenterFuns fun:funs) {
			UserCenterFunsRo funro = new UserCenterFunsRo();
			BeanUtils.copyProperties(fun, funro);
			for (UserCenterCustomRo centerCustomRo:customRos) {
				if(centerCustomRo.getFunsId().equals(funro.getFunId())){
					funro.setIsShow(1);
				}
			}
			centerFunsRos.add(funro);
		}
		ro.setFuns(centerFunsRos);
		ro.setCustoms(customRos);
		ro.setConfig(config);
		return ro;
	}

}
