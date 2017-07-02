package com.jm.business.service.order.recycle;

import com.jm.mvc.vo.order.recycle.RecycleExplainCo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.recycle.RecycleExplainRepository;
import com.jm.repository.po.order.recycle.RecycleExplain;
import com.jm.staticcode.util.ImgUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class RecycleExplainService {

	@Autowired
	private RecycleExplainRepository recycleExplainRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;

	/**
	 * 查看配置
	 * @return
	 */
	public RecycleExplain queryRecycleExplain(Integer shopId){
		RecycleExplain recycleExplain = recycleExplainRepository.findRecycleExplainByShopId(shopId);
		if(recycleExplain!=null){
			recycleExplain.setImgUrl(ImgUtil.appendUrls(recycleExplain.getImgUrl()));
		}
		return recycleExplain;
	}

	/***
	 * 保存地区限定
	 * @param recycleExplainCo
	 * @return
	 */
	public RecycleExplain addOrderBookConfig(RecycleExplainCo recycleExplainCo ){
		RecycleExplain recycleExplain = new RecycleExplain();
		BeanUtils.copyProperties(recycleExplainCo,recycleExplain);
		recycleExplain.setImgUrl(ImgUtil.substringUrls(recycleExplain.getImgUrl()));
		RecycleExplain re = recycleExplainRepository.findRecycleExplainByShopId(recycleExplain.getShopId());
		if(re == null){
			re = recycleExplainRepository.save(recycleExplain);
		}else{
			re.setImgUrl(recycleExplain.getImgUrl());
			re.setNode(recycleExplain.getNode());
			re.setModule1(recycleExplain.getModule1());
			re.setModule2(recycleExplain.getModule2());
			re.setUsingModule1(recycleExplainCo.getUsingModule1());
			re.setUsingModule2(recycleExplainCo.getUsingModule2());
			re.setUsingModule(recycleExplain.getUsingModule());
			re.setNode1(recycleExplain.getNode1());
			recycleExplainRepository.save(re);
		}
		return re;
	}

}
