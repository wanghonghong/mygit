package com.jm.business.service.order.recycle;

import com.jm.mvc.vo.order.recycle.RecycleRewardCo;
import com.jm.mvc.vo.order.recycle.RecycleRewardVo;
import com.jm.mvc.vo.order.recycle.RecycleWeightConfigVo;
import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.recycle.RecycleRewardRepository;
import com.jm.repository.jpa.order.recycle.RecycleWeightConfigRepository;
import com.jm.repository.po.order.recycle.RecycleReward;
import com.jm.repository.po.order.recycle.RecycleWeightConfig;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>奖励方式</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class RecycleRewardService {

	@Autowired
	private RecycleRewardRepository recycleRewardRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;
	@Autowired
	private RecycleWeightConfigRepository recycleWeightConfigRepository;
	@Autowired
	private JdbcRepository jdbcRepository;

	/***
	 * 查看奖励方式
	 * @return
	 */
	public RecycleRewardVo queryRecycleReward(Integer shopId){
		RecycleRewardVo recycleRewardVo = new RecycleRewardVo();
		RecycleReward recycleReward= recycleRewardRepository.findRecyCleRewardByShopId(shopId);
		List<RecycleWeightConfig> recycleWeightConfigList = new ArrayList<>();
		if(recycleReward!=null){
			BeanUtils.copyProperties(recycleReward,recycleRewardVo);
			recycleWeightConfigList = recycleWeightConfigRepository.findRecycleWeightConfigByRecycleRewardId(recycleReward.getId());
		}
		recycleRewardVo.setRecycleWeightConfigList(recycleWeightConfigList);
		return recycleRewardVo;
	}

	/***
	 * 保存奖励方式
	 * @param recycleRewardCo
	 * @return
	 */
	@Transactional
	public RecycleReward addRecycleReward(RecycleRewardCo recycleRewardCo ){
		RecycleReward recycleReward = new RecycleReward();
		BeanUtils.copyProperties(recycleRewardCo,recycleReward);
		RecycleReward rr = recycleRewardRepository.findRecyCleRewardByShopId(recycleRewardCo.getShopId());
		if(rr==null){
			rr = recycleRewardRepository.save(recycleReward);
			saveRecycleWeightConfig(rr,recycleRewardCo);
		}else{
			rr.setRewardType(recycleRewardCo.getRewardType());
			rr.setShowType(recycleRewardCo.getShowType());
			rr = recycleRewardRepository.save(rr);
			saveRecycleWeightConfig(rr,recycleRewardCo);
		}
		return rr;
	}

	//保存奖励方式对应重量数据
	public void saveRecycleWeightConfig(RecycleReward rr,RecycleRewardCo recycleRewardCo){
		if(rr!=null){
			jdbcRepository.deleteRecycleWeightConfig(rr.getId());
		}
		List<RecycleWeightConfig> recycleWeightConfigList = new ArrayList<>();
		List<RecycleWeightConfigVo> recycleWeightConfigVoList = recycleRewardCo.getRecycleWeightConfigVoList();
		for(RecycleWeightConfigVo recycleWeightConfigVo:recycleWeightConfigVoList){
			RecycleWeightConfig recycleWeightConfig = new RecycleWeightConfig();
			BeanUtils.copyProperties(recycleWeightConfigVo,recycleWeightConfig);
			recycleWeightConfig.setRecycleRewardId(rr.getId());
			recycleWeightConfigList.add(recycleWeightConfig);
		}
		recycleWeightConfigRepository.save(recycleWeightConfigList);
	}
}
