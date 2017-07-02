package com.jm.repository.jpa;

import com.jm.business.domain.PageItemDo;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.PageItemExt;
import com.jm.staticcode.util.JsonMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>JDBC查询</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/24
 */
@Slf4j
@Repository
public class JdbcUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	public static String appendOr(String condition,String value){
		if (StringUtils.isNotEmpty(value)){
			return " or "+condition+"='"+value+ "'";
		}else {
			return "";
		}
	}

	public static String appendOr(String condition,Integer value){
		if (value != null && !"".equals(value)){
			return " or "+condition+"="+value;
		}else {
			return "";
		}
	}

	public static String appendAnd(String condition,String value){
		if (StringUtils.isNotEmpty(value)){
			return " and "+condition+"='"+value+ "'";
		}else {
			return "";
		}
	}

	public static String appendAnd(String condition,Long value){
		if (value != null && !"".equals(value)){
			return " and "+condition+"='"+value+ "'";
		}else {
			return "";
		}
	}

	public static String appendAnd(String condition,Integer value){
		if (value!=null){
			return " and "+condition+"="+value;
		}else {
			return "";
		}
	}

	public static String appendMore(String condition,Integer value){
		if (value!=null){
			return " and "+condition+">"+value;
		}else {
			return "";
		}
	}
	public static String appendLess(String condition,Integer value){
		if (value!=null){
			return " and "+condition+">"+value;
		}else {
			return "";
		}
	}

	public static String appendNotAnd(String condition,Integer value){
		if (value!=null){
			return " and "+condition+"!="+value;
		}else {
			return "";
		}
	}

	public static String appendIsNull(String condition){
		return " and "+condition+" is null";
	}

	public static String appendIn(String condition,String value){
		if (value!=null){
			return " and "+condition+" in("+value+")";
		}else {
			return "";
		}
	}

	public static String appendNotIn(String condition,String value){
		if (value!=null){
			return " and "+condition+" not in("+value+")";
		}else {
			return "";
		}
	}

	public static String appendAnd(String condition,Date beginDate,Date endDate){
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (beginDate!=null){
			str+= " and "+condition+" >= "+"'"+sdf.format(beginDate)+"'";
		}
		if (endDate!=null){
			str+= " and "+condition+" <= "+"'"+sdf.format(endDate)+"'";
		}
		return str;
	}

	public static String appendAnd(String condition,String beginDate,String endDate){
		String str = "";
		if (beginDate!=null){
			str+= " and "+condition+" >= "+"'"+beginDate+"'";
		}
		if (endDate!=null){
			str+= " and "+condition+" <= "+"'"+endDate+"'";
		}
		return str;
	}

	public static String appendAnd(String condition){
		String str = " and "+condition+">date_sub(now(),interval 2 day)";
		return str;
	}

	public static String appendLike(String condition,String value){
		if (StringUtils.isNotEmpty(value)){
			return " and "+condition+" like '%"+ value+"%'";
		}else {
			return "";
		}
	}

	public static String appendOrderBy(String condition){
		return " order by "+condition+" desc ";
	}
	public static String appendOrderBy(String conditions[]){
		String sql= " order by  ";
		for (String tmp:conditions) {
			sql=sql+tmp+",";
		}
		sql=sql+"1";
		return sql;
	}

	public static String appendGroupBy(String condition) {
		return " group by "+condition;
	}

	public static PageRequest getPageRequest(Integer curPage,Integer pageSize,String sortBy){
		curPage = curPage==null ? 0 : curPage;
		pageSize = pageSize==null ? 20 : pageSize;
		Sort sort=new Sort(Sort.Direction.DESC, sortBy);
		PageRequest pageRequest = new PageRequest(curPage,pageSize,sort);
		return pageRequest;
	}


	public void update(String sql){
		jdbcTemplate.update(sql);
	}

	public int queryCount(String sqlList) {
		return jdbcTemplate.queryForInt(sqlList);
	}

	/**
	 * 列表查询
	 * @param sqlList
	 * @return
	 */
	public List<Map<String,Object>> queryList(String sqlList) {
		return jdbcTemplate.queryForList(sqlList);
	}

	/**
	 * 列表查询
	 * */
	 public <T> List<T> queryList(String sqlList,Class<T> clazz) throws IllegalAccessException, InstantiationException, IOException {
		 List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList);
		 List<T> results = new ArrayList<>();
		 for(Map map:list){
			 T result =  clazz.newInstance();
			 result = (T) JsonMapper.map2Obj(map,clazz);
			 results.add(result);
		 }
		 return results;
	 }
    /**
     *
     *
     */
    public int updateOrder(Long orderInfoId,Date sendTime){
    	String sql = " UPDATE order_info o SET status=2,look_status=2,send_date="+sendTime+" WHERE o.order_info_id ="+orderInfoId
                   + " AND (SELECT count(od.order_delivery_id) FROM order_delivery od"
                   + " LEFT JOIN order_delivery_detail odd ON odd.order_delivery_id = od.order_delivery_id"
                   + " WHERE od.order_info_id = o.order_info_id ) = (SELECT count(ode.order_detail_id) FROM"
                   + " order_detail ode WHERE ode.order_info_id = o.order_info_id )";
        int count = jdbcTemplate.update(sql);
		return count;
    }

	/**
	 * 分页查询
	 * @param sqlList
	 * @return
	 */
	public PageItem<Map<String,Object>> queryPageItem(String sqlList, int curPage, int pageSize) {
//		String sqlCount = "select count(*) " + sqlList.substring(sqlList.indexOf(" from "),sqlList.length());
//      String sqlCount = "select count(*) from (" + sqlList +" ) tb ";
		String lowerSql = sqlList.toLowerCase();
		String sqlCount = "";
		if(sqlList.indexOf("group by")>=0){
			sqlCount = "select count(*) from (" + sqlList +" ) tb ";
		}else{
			sqlCount = "select count(*) " + sqlList.substring(lowerSql.indexOf(" from "),sqlList.length());
		}
		int count = jdbcTemplate.queryForInt(sqlCount);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+curPage*pageSize+","+pageSize);
		PageItem<Map<String,Object>> pageItem  = new PageItem<>();
		pageItem.setCount(count);
		pageItem.setItems(list);
		return pageItem;
	}

	public <T> PageItem<T> queryPageItem(String sqlList, int curPage, int pageSize, Class<T> T) throws IOException, IllegalAccessException, InstantiationException {
		String sqlCount = "";
		String lowerSql = sqlList.toLowerCase();
		if(sqlList.indexOf("group by")>=0){
			sqlCount = "select count(*) from (" + sqlList +" ) tb ";
		}else{
			sqlCount = "select count(*) " + sqlList.substring(lowerSql.indexOf(" from "),sqlList.length());
		}
		int count = jdbcTemplate.queryForInt(sqlCount);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+curPage*pageSize+","+pageSize);
		List<T> results = new ArrayList<>();
		PageItem<T> pageItem  = new PageItem<>();
		pageItem.setCount(count);
		for(Map map:list){
			T result =  T.newInstance();
			result = (T) JsonMapper.map2Obj(map,T);
			results.add(result);
		}
		pageItem.setItems(results);

		return pageItem;
	}


	/**
	 * 分页查询
	 * @param pageItemDo
	 * @return
	 */
	public PageItem<Map<String,Object>> queryPageItem(PageItemDo pageItemDo) {
		return queryPageItem(pageItemDo.getSqlList(),pageItemDo.getCurPage(),pageItemDo.getPageSize());
	}

	public static <T,E>PageItemExt<T,E> pageItem2Ext(PageItem<T> pageItem, E e ){
		PageItemExt ext = new PageItemExt();
		ext.setItems(pageItem.getItems());
		ext.setCount(pageItem.getCount());
		ext.setExt(e);
		return ext;
	}

	@ApiOperation("报表专用 cj 2016-11-28")
	public static String appendDateOr(String condition,Date beginDate,Date endDate){
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (beginDate!=null && endDate!=null){
			str+= " and ( "+condition+" < "+"'"+sdf.format(beginDate)+"' or "+condition+" > "+"'"+sdf.format(endDate)+"' )";
		}else if (beginDate!=null){
			str+= " and "+condition+" < "+"'"+sdf.format(beginDate)+"'";
		}else if(endDate!=null){
			str+= " and "+condition+" > "+"'"+sdf.format(endDate)+"'";
		}
		return str;
	}
	@ApiOperation("报表专用 cj 2016-11-28")
	public PageItem<Map<String,Object>> queryAnalysisPageItem(String sqlList, String fromSql, int curPage, int pageSize) {
		String sqlCount = "";
		if(!"".equals(fromSql)){
			sqlCount = "select count(*) "+fromSql;
		}
		int count = jdbcTemplate.queryForInt(sqlCount);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sqlList+ " limit "+curPage*pageSize+","+pageSize);
		PageItem<Map<String,Object>> pageItem  = new PageItem<>();
		pageItem.setCount(count);
		pageItem.setItems(list);
		return pageItem;
	}

	/**
	 * 获取in 的sql 语句 格式为 xxx in ( x,y,z)
	 * @param  fieldName 条件字段
	 * @param it 可循环的对象
	 * @param  clazz 对象的类型
	 * @param name 对象中的哪个变量 首字母大写
	 * */
	public static  <T> String getInSql( String fieldName,Iterable<T> it,Class<T> clazz,String name) throws Exception {
		if(it==null){
			return null;
		}
		StringBuffer buffer = new StringBuffer(" AND "+fieldName+" in ( ");
		Iterator<T> loop=  it.iterator();
		boolean flag = false;
		int i =0;
		while(loop.hasNext()){
			if(flag){
				buffer.append(",");
			}
			i++;
			T t = loop.next();
			Method method = clazz.getMethod("get"+name);
			Object obj = method.invoke(t);
			if(obj==null){
				if(flag){
					int end = buffer.lastIndexOf(",");
					if(end>0){
						buffer = new StringBuffer(buffer.substring(0,end));
					}
				}
				continue;
			}else{
				flag =true;
			}
            String result = obj.toString();
			if(obj instanceof  String || obj instanceof  Date ){
                buffer.append("'"+result+"'");
            }else{
                buffer.append(result);
            }
		}
		buffer.append(" )");
		if(i==0||!flag){
			return "";
		}
		return buffer.toString();
	}
	public <T> T findOne(String sql,Class<T> clazz) throws IllegalAccessException, IOException, InstantiationException {
		List<T> list = queryList(sql,clazz);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public static <T> String getClassField(Class<T> clazz,String alias){
		Field[] fields = clazz.getDeclaredFields();
		if(fields==null||fields.length<1){
			return "";
		}
		StringBuffer fieldBuffer =new StringBuffer("");
		for(int i=0;i<fields.length;i++){
			if(i>0){
				fieldBuffer.append(",");
			}
			String name = fields[i].getName();
			for(int j = 0;j<name.length();j++){
				if(Character.isUpperCase(name.charAt(j))){
					String subStr = name.substring(0,j);
					String upper = name.charAt(j)+"";
					name = subStr+"_"+upper.toLowerCase()+name.substring(j+1,name.length());
					j++;
				}
			}
			if(alias!=null){
				fieldBuffer.append(alias+"."+name);
			}else{
				fieldBuffer.append(name);
			}
		}
		return fieldBuffer.toString();
	}

	public static <T> String getClassField(Class<T> clazz){
		return getClassField(clazz,null);
	}


}
