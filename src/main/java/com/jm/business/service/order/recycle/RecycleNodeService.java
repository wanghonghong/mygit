package com.jm.business.service.order.recycle;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.recycle.RecycleNodeCo;
import com.jm.mvc.vo.order.recycle.RecycleNodeQo;
import com.jm.mvc.vo.order.recycle.RecycleNodeUo;
import com.jm.mvc.vo.order.recycle.RecycleNodeVo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.order.recycle.RecycleNodeRepository;
import com.jm.repository.po.order.recycle.RecycleNode;
import com.jm.staticcode.util.ImgUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */
@Log4j
@Service
public class RecycleNodeService {

	@Autowired
	private RecycleNodeRepository recycleNodeRepository;
	@Autowired
	protected JdbcUtil jdbcUtil;


	/***
	 * 查看回收网点
	 * @return
	 */
	public PageItem<RecycleNodeVo> findRecycleNodeByShopId(RecycleNodeQo recycleNodeQo) throws IllegalAccessException, IOException, InstantiationException {
		String sql = "";
		if(recycleNodeQo.getType()==1){
			//网点回收
			sql = "select * from recycle_node rn where rn.shop_id="+recycleNodeQo.getShopId()+" and rn.type in (0,1) order by id desc";
		}else{
			//公益回收
			sql = "select * from recycle_node rn where rn.shop_id="+recycleNodeQo.getShopId()+" and rn.type=2 order by id desc";
		}
		PageItem<RecycleNodeVo> pageItem = jdbcUtil.queryPageItem(sql,recycleNodeQo.getCurPage(),recycleNodeQo.getPageSize(),RecycleNodeVo.class);
		List<RecycleNodeVo> recycleNodeVoList = pageItem.getItems();
		List<RecycleNodeVo> recycleNodeVoList1 = new ArrayList<>();
		for(RecycleNodeVo recycleNodeVo : recycleNodeVoList){
			recycleNodeVo.setImgUrl(ImgUtil.appendUrls(recycleNodeVo.getImgUrl()));
			recycleNodeVoList1.add(recycleNodeVo);
		}
		pageItem.setItems(recycleNodeVoList1);
		return pageItem;
	}

	/***
	 * 保存回收网点
	 * @param recycleNodeCo
	 * @return
	 */
	public RecycleNode addRecycleNode(RecycleNodeCo recycleNodeCo ) throws ParseException {
		RecycleNode recycleNode = new RecycleNode();
		BeanUtils.copyProperties(recycleNodeCo,recycleNode);
		recycleNode.setImgUrl(ImgUtil.substringUrl(recycleNode.getImgUrl()));
		if(recycleNodeCo.getType()==2){
			Date date =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(recycleNodeCo.getPublicNetworkDate());
			recycleNode.setPublicNetworkDate(date);
		}
		recycleNode.setStatus(1);
		RecycleNode rr = recycleNodeRepository.save(recycleNode);
		return rr;
	}

	/***
	 * 保存回收网点
	 * @param recycleNodeUo
	 * @return
	 */
	public RecycleNode updateRecycleNode(Integer id,RecycleNodeUo recycleNodeUo) throws ParseException {
		RecycleNode recycleNode = recycleNodeRepository.findOne(id);
		BeanUtils.copyProperties(recycleNodeUo,recycleNode);
		recycleNode.setImgUrl(ImgUtil.substringUrl(recycleNode.getImgUrl()));
		if(recycleNodeUo.getType()==2){
			Date date =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(recycleNodeUo.getPublicNetworkDate());
			recycleNode.setPublicNetworkDate(date);
		}
		RecycleNode rr = recycleNodeRepository.save(recycleNode);
		return rr;
	}

	/***
	 * 修改上下架
	 *
	 */
	public RecycleNode updateRecycleNodeStatus(Integer id,RecycleNodeUo recycleNodeUo){
		RecycleNode recycleNode = recycleNodeRepository.findOne(id);
		recycleNode.setStatus(recycleNodeUo.getStatus());
		RecycleNode rr = recycleNodeRepository.save(recycleNode);
		return rr;
	}

	/***
	 * 根据id查看RecycleNode
	 *
	 */
	public RecycleNodeVo findRecycleNode(Integer id){
		RecycleNode recycleNode = recycleNodeRepository.findOne(id);
		RecycleNodeVo recycleNodeVo = new RecycleNodeVo();
		BeanUtils.copyProperties(recycleNode,recycleNodeVo);
		recycleNodeVo.setImgUrl(ImgUtil.appendUrl(recycleNodeVo.getImgUrl()));
		return recycleNodeVo;
	}

	/***
	 * 删除回收网点
	 * @param id
	 * @return
	 */
	public void deleteRecycleNode(Integer id){
		recycleNodeRepository.delete(id);
	}

}
