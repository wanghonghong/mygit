package com.jm.staticcode.converter.order;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.order.OrderBookVo;
import com.jm.mvc.vo.order.recycle.RecycleDetailVo;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.JsonMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author liangr
 * @version latest
 * @date 2017/4/6
 */
public class OrderBookConverter {

	public static PageItem<OrderBookVo> p2v(PageItem<Map<String,Object>> pageItemMap) throws IOException {
		PageItem<OrderBookVo> pageItem = new PageItem<>();
		List<Map<String,Object>> maps = pageItemMap.getItems();
		List<OrderBookVo> list = new ArrayList<>();
		for (Map<String,Object> map : maps){
			OrderBookVo orderBookVo = JsonMapper.map2Obj(map,OrderBookVo.class);
			if(!StringUtils.isEmpty(map.get("img_url"))){
				String[] urlStr = map.get("img_url").toString().split(",");
				if(urlStr.length>=1){
					orderBookVo.setImgUrl(ImgUtil.appendUrls(urlStr[0]));
					if(urlStr.length>=2){
						orderBookVo.setImgUrl2(ImgUtil.appendUrls(urlStr[1]));
						if(urlStr.length>=3){
							orderBookVo.setImgUrl3(ImgUtil.appendUrls(urlStr[2]));
						}
					}
				}
			}else{
				orderBookVo.setImgUrl(ImgUtil.appendUrls(map.get("img_url").toString()));
			}
			if(!StringUtils.isEmpty(map.get("headimgurl"))) {
				orderBookVo.setHeadImgUrl(map.get("headimgurl").toString());
			}
			list.add(orderBookVo);
		}
		pageItem.setCount(pageItemMap.getCount());
		pageItem.setItems(list);
		return pageItem;
	}

	public static List<RecycleDetailVo> m2v(List<Map<String,Object>> maps) throws IOException {
		List<RecycleDetailVo> recycleDetailVoList = new ArrayList<>();
		for(Map<String,Object> map : maps){
			RecycleDetailVo recycleDetailVo = JsonMapper.map2Obj(map,RecycleDetailVo.class);
			recycleDetailVoList.add(recycleDetailVo);
		}
		return recycleDetailVoList;
	}

}
