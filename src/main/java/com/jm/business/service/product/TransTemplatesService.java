package com.jm.business.service.product;

import com.jm.mvc.vo.product.trans.*;
import com.jm.repository.jpa.product.TransTemplatesRelationRepository;
import com.jm.repository.jpa.product.TransTemplatesRepository;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.po.product.TransTemplates;
import com.jm.repository.po.product.TransTemplatesRelation;

import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.converter.product.TransTemplatesConverter;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>运费模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 15:51
 */
@Slf4j
@Service
public class TransTemplatesService {

	@Autowired
	private TransTemplatesRepository transTemplatesRepository;
	@Autowired
	private TransTemplatesRelationRepository transTemplatesRelationRepository;
	@Autowired
	private ShopRepository shopRepository;
	/**
	 * 商品模块查询运费模板ww
	 * @param shopId
	 * @return
	 */
	@Transactional
	public List<TransTempVo> getTemps(Integer shopId){
		List<TransTemplates> transTempsList = transTemplatesRepository.findByShopId(shopId);
		List<TransTempVo> transList = new ArrayList<TransTempVo>();
		if(transTempsList!=null && transTempsList.size()>0){
			for(TransTemplates transTemplates:transTempsList){
				TransTempVo transTempVo = TransTemplatesConverter.toTransTemplatesVo(transTemplates);
				transList.add(transTempVo);
			}
		}
		return transList;
	}

	@Transactional
	public boolean saveTransTemplates(TransTemplatesCo transTemplatesCo, String shopId) {
			//保存模板主表
			TransTemplates t = new TransTemplates();
			String  transTemName = transTemplatesCo.getTemplatesName();
			t.setTemplatesName(transTemName);
			t.setShopId(Toolkit.parseObjForInt(shopId));
			t.setCreatTime(new Date());
			TransTemplates templates=  transTemplatesRepository.save(t);
			//获取模板主表ID
			int templatesId =  templates.getTemplatesId();
			List<TransTemplatesRelation> list = new ArrayList<>();
			for (int i = 0; i < transTemplatesCo.getTransRelationList().size(); i++) {
				//填充模板主表id
				TransTemplatesRelation ttr = new TransTemplatesRelation();
				TransTemplatesRelationCo ttrCo = transTemplatesCo.getTransRelationList().get(i);
				ttrCo.setTemplatesId(templatesId);
				BeanUtils.copyProperties(ttrCo,ttr);
				list.add(ttr);
			}
			//保存模板附表
			if(list.size()>0){
				transTemplatesRelationRepository.save(list);
			}
			return true;
	}
	@Transactional
	public List findALL(Integer shopId){
		List list = new ArrayList();
		List<TransTemplatesRo>  transTemplatesRos = new ArrayList<TransTemplatesRo>();
		List<TransTemplates> transTemps = transTemplatesRepository.findByShopId(shopId);
		List ids = new ArrayList();
		for (TransTemplates transTemplates:transTemps) {
			ids.add(transTemplates.getTemplatesId());
		}
		if(ids.size()>0){
			List<TransTemplatesRelation> subTransTems = transTemplatesRelationRepository.findByTempIds(ids);
			for (TransTemplates transTemplate : transTemps) {
				TransTemplatesRo transTemplatesRo = new TransTemplatesRo();
				BeanUtils.copyProperties(transTemplate,transTemplatesRo);
				List<TransTemplatesRelationRo> ttr = new ArrayList();
				for (TransTemplatesRelation subTransTem : subTransTems) {
					if (subTransTem.getTemplatesId().intValue()==transTemplate.getTemplatesId().intValue()){
						TransTemplatesRelationRo transTempRelRo = new TransTemplatesRelationRo();
						BeanUtils.copyProperties(subTransTem,transTempRelRo);
						ttr.add(transTempRelRo);
					}
				}
				transTemplatesRo.setTransRelationList(ttr);
				transTemplatesRos.add(transTemplatesRo);
			}
		}
		list.add(transTemplatesRos);
		return list;
	}
	@Transactional
	public List<TransTemplates> findALLTemps(Integer shopId){
		List<TransTemplates> transTempsList = transTemplatesRepository.findByShopId(shopId);
		return transTempsList;
	}
	@Transactional
	public TransTemplatesRo findTransTempByTempId(Integer templatesId){
		TransTemplatesRo ttr = new TransTemplatesRo();
		List list = new ArrayList();
		TransTemplates transTemplates = transTemplatesRepository.findOne(templatesId);
		if(transTemplates != null){
			List<TransTemplatesRelation>  transTemplatesRelationsLists = transTemplatesRelationRepository.findByTempId(templatesId);
			for (TransTemplatesRelation transTempRel:transTemplatesRelationsLists) {
				TransTemplatesRelationRo transTempRelRo = new TransTemplatesRelationRo();
				BeanUtils.copyProperties(transTempRel,transTempRelRo);
				list.add(transTempRelRo);
			}
			ttr.setTemplatesId(transTemplates.getTemplatesId());
			ttr.setTemplatesName(transTemplates.getTemplatesName());
			ttr.setTransRelationList(list);
		}
		return ttr;
	}
	@Transactional
	public boolean updateTransTemplates(TransTemplatesUo transTemplatesUo) {
			//修改模板主表
		    Integer id = transTemplatesUo.getTemplatesId();
		    TransTemplates transTemp = transTemplatesRepository.findOne(id);
		 	if(transTemp != null){
				BeanUtils.copyProperties(transTemplatesUo,transTemp);
				transTemp.setUpdateTime(new Date());
				transTemplatesRepository.save(transTemp);
				//先删除子表数据，再新增子表
				transTemplatesRelationRepository.deleteTransTempRelationByTempId(id);
				List<TransTemplatesRelationUo> ttUos = transTemplatesUo.getTransRelationList();
				List list = new ArrayList();
				for (TransTemplatesRelationUo ttuo : ttUos) {
					TransTemplatesRelation ttr = new TransTemplatesRelation();
					BeanUtils.copyProperties(ttuo,ttr);
					ttr.setTemplatesId(id);
					list.add(ttr);
				}
				if(list.size()>0){
					transTemplatesRelationRepository.save(list);
				}
				return true;
			}
			return false;
	}

	@Transactional
	public boolean deleteTransTemplates(Integer templatesId) {
		//删除模板主表
		TransTemplates transTemplates = new TransTemplates();
		transTemplates.setTemplatesId(templatesId);
		transTemplatesRepository.delete(transTemplates);

		//删除模板子表
		List<TransTemplatesRelation> list = transTemplatesRelationRepository.findByTempId(templatesId);
		transTemplatesRelationRepository.delete(list);
		return true;
	}

}
