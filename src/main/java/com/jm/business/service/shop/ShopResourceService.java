package com.jm.business.service.shop;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.shop.resource.ShopResVideoCo;
import com.jm.mvc.vo.shop.resource.ShopResourceVo;
import com.jm.repository.jpa.shop.ShopResourceRepository;
import com.jm.repository.po.shop.ShopResource;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author: cj
 * @date: 2016-6-12
 */
@Service
public class ShopResourceService {

    @Autowired
    private ShopResourceRepository shopResourceRepository;
    
    
    public List getShopResourceList(Integer shopId,Integer resType,Integer isDel){
        return shopResourceRepository.getShopResource(shopId, resType,isDel);
    }
	@Transactional
    public ShopResource saveShopResource(ShopResource shopResource){
        return shopResourceRepository.save(shopResource);
    }
	@Transactional
    public boolean delShopResource(Integer id) {
		boolean flag = false;
		ShopResource  sr = shopResourceRepository.getShopResourceById(id);
			if(null != sr ){
				sr.setIsDel(1);  // 伪删除
				shopResourceRepository.save(sr);
				flag = true;
			}
		return flag;
	}
	@Transactional
	public boolean delShopResource(String ids) {
		boolean flag = false;
		if(!"".equals(ids)){
			String[] str  = ids.split(",");
			for (Object obj :str) {
				Integer id = Toolkit.parseObjForInt(obj);
				ShopResource  sr = shopResourceRepository.getShopResourceById(id);
				if(null != sr ){
					sr.setIsDel(1);  // 伪删除
					shopResourceRepository.save(sr);
				}
			}
			flag = true;
		}

		return flag;
	}
	public List<ShopResource> getResourceList(ShopResourceVo resVo) {
		return shopResourceRepository.getShopResource(resVo.getShopId(),resVo.getResType(),0);
	}

	public Page<ShopResource> findAll(Specification spec, Pageable pageable ) {

		return shopResourceRepository.findAll(spec,pageable);
	}
    @Transactional
	public ShopResource updateShopResourceByResName(Integer shopResId, String audioName) {
		ShopResource shopRes = shopResourceRepository.findOne(shopResId);
		shopRes.setResName(audioName);
		return shopResourceRepository.save(shopRes);
	}

	public ShopResource findShopResourceById(Integer shopResId) {
		return shopResourceRepository.findOne(shopResId);
	}

	@Transactional
	public JmMessage saveVideo(ShopResVideoCo shopResVideoCo, Integer resGroupId, String shopId) throws UnsupportedEncodingException {

		String resName  = Toolkit.parseObjForStr(shopResVideoCo.getResName());
		String ykResUrl = Toolkit.parseObjForStr(shopResVideoCo.getYkResUrl());
		String txResUrl = Toolkit.parseObjForStr(shopResVideoCo.getTxResUrl());
		List<ShopResource> shopResList = new ArrayList<>();
		if(!"".equals(ykResUrl)){
			ShopResource sr2 = new ShopResource();
			sr2.setResName("优酷_"+resName);
			sr2.setResType(2);
			sr2.setShopId(Toolkit.parseObjForInt(shopId));
			sr2.setResGroupId(Toolkit.parseObjForInt(resGroupId));
			sr2.setIsDel(0);
			sr2.setResUrl(URLDecoder.decode(ykResUrl,"UTF-8"));
			shopResList.add(sr2);
		}
		if(!"".equals(txResUrl)){
			ShopResource sr1 = new ShopResource();
			sr1.setResName("腾讯_"+resName);
			sr1.setResType(4);
			sr1.setShopId(Toolkit.parseObjForInt(shopId));
			sr1.setResGroupId(Toolkit.parseObjForInt(resGroupId));
			sr1.setIsDel(0);
			sr1.setResUrl(URLDecoder.decode(txResUrl,"UTF-8"));
			shopResList.add(sr1);
		}
		if(shopResList.size()>0){
			shopResourceRepository.save(shopResList);
		}
		return new JmMessage(0,"新增成功!");
	}

	public List searchRes(String searchName, Integer shopId) {
		searchName = "%"+searchName+"%";
		return shopResourceRepository.searchRes(searchName,shopId);
	}
}
