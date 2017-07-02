package com.jm.business.service.shop.shopSet;

import com.jm.mvc.vo.shop.shopSet.UserCenterCustomCo;
import com.jm.repository.jpa.shop.shopSetting.UserCenterConfigRepository;
import com.jm.repository.jpa.shop.shopSetting.UserCenterCustomRepository;
import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserCenterCustomService {
	
	@Autowired
	private UserCenterCustomRepository userCenterCustomRepository;
	@Autowired
	private UserCenterConfigRepository userCenterConfigRepository;

	@Transactional
	public List<UserCenterCustom> saveUserCenterCustom(UserCenterCustomCo co,Integer shopId){
		if(shopId==null){
			return null;
		}
		userCenterCustomRepository.deleteByShopId(shopId);
		List<UserCenterCustom> customs = new ArrayList<>();
		for (UserCenterCustom custo:co.getCustoms()) {
			UserCenterCustom custom = new UserCenterCustom();
			custom.setFunsId(custo.getFunsId());
			custom.setName(custo.getName());
			custom.setShopId(shopId);
			custom.setId(custo.getId());
			customs.add(custom);
		}
		UserCenterConfig config = co.getConfig();
		config.setTopPic(ImgUtil.substringUrl(config.getTopPic()));
		config.setShopId(shopId);
		userCenterConfigRepository.save(config);
		return userCenterCustomRepository.save(customs);
	}

	public List<UserCenterCustom> findByShopId(Integer shopId){
		return userCenterCustomRepository.findByShopId(shopId);
	}


}
