package com.jm.repository.client;

import com.jm.mvc.vo.wx.group.WxGroupReturnParam;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.repository.client.dto.wxuser.WxGroupListDto;
import com.jm.staticcode.constant.WxUrl;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class WxGroupClient extends BaseClient {
	
	/**
	 * 获取用户所有分组
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年7月29日
	 */
	public WxGroupListDto getGroups(String accessToken) throws Exception{
		String url = WxUrl.GET_GROUPS.replace("ACCESS_TOKEN", accessToken);
		return getForObject(url, WxGroupListDto.class);
	}
	/**
	 * 创建分组
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年7月29日
	 */
	public WxGroupReturnParam createGroup(String accessToken,Map<String,Object> group) throws Exception{
		String url = WxUrl.CREATE_GROUP.replace("ACCESS_TOKEN", accessToken);
		WxGroupReturnParam sss = postForObject(url, group, WxGroupReturnParam.class);
		return sss;
	}
	
	/**
	 *<p>修改分组</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws Exception 
	 * @data 2016年7月30日
	 */
	public  ResultMsg updateGroup(String accessToken,Map<String,Object> group) throws Exception{
		String url = WxUrl.UPDATE_GROUP.replace("ACCESS_TOKEN", accessToken);
		return postForObject(url, group,  ResultMsg.class);
	}
	
	/**
	 * 删除分组
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年8月2日
	 */
	public ResultMsg deleteGroup(String accessToken,Map<String,Object> group) throws Exception{
		String url = WxUrl.DELETE_GROUP.replace("ACCESS_TOKEN", accessToken);
		return postForObject(url, group,  ResultMsg.class);
	}
	
	/**
	 * 移动分组
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年8月2日
	 */
	public ResultMsg moveGroup(String accessToken,Map<String,Object> map)throws Exception{
		String url = WxUrl.MOVE_GROUP.replace("ACCESS_TOKEN", accessToken);
		return postForObject(url, map, ResultMsg.class);
	}
	
	/**
	 * 批量移动分组
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年8月2日
	 */
	public ResultMsg batchMoveGroup(String accessToken,Map<String,Object> map)throws Exception{
		String url = WxUrl.BATCH_MOVE_GROUP.replace("ACCESS_TOKEN", accessToken);
		return postForObject(url, map, ResultMsg.class);
	}
	/**
	 * 修改备注
	 * @author chenyy
	 * @throws Exception 
	 * @data 2016年8月2日
	 */
	public ResultMsg updateMark(String accessToken,Map<String,Object> map) throws Exception{
		String url = WxUrl.WX_USER_MARK.replace("ACCESS_TOKEN", accessToken);
		return postForObject(url, map, ResultMsg.class);
		
	}

}
