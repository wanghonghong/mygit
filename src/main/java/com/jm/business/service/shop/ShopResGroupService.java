package com.jm.business.service.shop;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.shop.resource.ShopResGroupRo;
import com.jm.repository.jpa.shop.ShopResGroupRepository;
import com.jm.repository.jpa.shop.ShopResourceRepository;
import com.jm.repository.po.shop.ShopResGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/13 9:54
 */
@Service
public class ShopResGroupService {

    @Autowired
    private ShopResGroupRepository shopResGroupRepository;
    @Autowired
    private ShopResourceRepository shopResourceRepository;

	@Transactional
	public ShopResGroup add(ShopResGroup srg) {
		return shopResGroupRepository.save(srg);
	}

    public List<ShopResGroupRo> getShopResGroup(Integer shopId,Integer resType){
        List<ShopResGroup> shopResGroupList = shopResGroupRepository.getShopResGroup(shopId,resType);
		List<ShopResGroupRo> shopResGroupRoList = new ArrayList<>();
		for (ShopResGroup srg : shopResGroupList) {
			ShopResGroupRo shopResGroupRo = new ShopResGroupRo();
			BeanUtils.copyProperties(srg,shopResGroupRo);
			shopResGroupRoList.add(shopResGroupRo);
		}
		return shopResGroupRoList;
	}

    public ShopResGroup findShopResGroupById(Integer id){
		return shopResGroupRepository.getShopResGroupById(id);
	}

	@Transactional
    public ShopResGroup saveShopResGroup(ShopResGroup shopResGroup){
        return shopResGroupRepository.save(shopResGroup);
    }
	@Transactional
    public JmMessage delShopResGroup(Integer id) {
		boolean flag = false;
		ShopResGroup  srg = shopResGroupRepository.getShopResGroupById(id);
		Integer shorResGroupId = srg.getId();
			if(null != srg ){
				shopResGroupRepository.delete(id);
				shopResourceRepository.updateShopResByResGroupId(shorResGroupId);
				flag = true;
			}
		if(true==flag){
			return new JmMessage(0,"删除成功!!");
		}
		return new JmMessage(1,"删除失败!!");

	}
	@Transactional
	public JmMessage changeShopResGroup(List<Integer> shopResIdList, Integer resGroupId) {
		shopResourceRepository.updateShopResByIds(shopResIdList,resGroupId);
		return new JmMessage(0,"分组成功!!");
	}

}
