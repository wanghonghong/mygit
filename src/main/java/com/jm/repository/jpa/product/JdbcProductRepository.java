package com.jm.repository.jpa.product;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.product.area.*;
import com.jm.mvc.vo.product.group.ProductGroupQo;
import com.jm.mvc.vo.product.product.ProductQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.staticcode.converter.product.ProductConverter;
import com.jm.staticcode.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>JDBC查询</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/15
 */
@Slf4j
@Repository
public class JdbcProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Autowired
	private JdbcUtil jdbcUtil;
	/**
	 * 商品列表查询
	 * @param productQo
	 * @param shopId
	 * @return
	 */
	public PageItem<Map<String, Object>> getGoodsManager(
			ProductQo productQo, Integer shopId) {
		String sql1=" select ";
		String sql2=" p.pid,p.name,p.shop_id,p.price,p.pic_square,p.pic_rectangle,p.sale_count,p.total_count, p.create_time,p.sort,p.status,p.uv,p.pv,p.qrcode_url,p.share ";
		String sql3=" from product p where status != 9 and p.shop_id ="+shopId;
		String sql4=" count(*) ";
		if(StringUtil.isNotNull(productQo.getName())){
			sql3 += " and p.name like '%"+ productQo.getName() + "%'";
		}
		if(null != productQo.getStatus() && 4 != productQo.getStatus()){
			sql3 += " and p.status="+productQo.getStatus();
		}
		if(null != productQo.getGroupId()){
			sql3 += " and exists (select 1 from  product_group_relation g where g.status=0 and g.group_id="+productQo.getGroupId()+" and g.pid = p.pid)";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startDate = productQo.getStartDate();
		if(null != startDate && !"".equals(startDate)){
			sql3 += " and p.create_time >="+"'"+sdf.format(startDate)+"'";
		}
		Date endDate = productQo.getEndDate();
		if(null != endDate && !"".equals(endDate)){
			sql3 += " and p.create_time <="+"'"+sdf.format(endDate)+"'";
		}
		if(null !=productQo.getMinPrice() && productQo.getMinPrice()>0){
			sql3 += " and p.price >="+productQo.getMinPrice();
		}
		if(null !=productQo.getMaxPrice() && productQo.getMaxPrice()>0){
			sql3 += " and p.price <="+productQo.getMaxPrice();
		}

		String sql = sql1 + sql2 + sql3;
		String sql5 = sql1 + sql4 + sql3;
		sql +=" order by p.sort,  p.create_time desc limit "+productQo.getCurPage()*productQo.getPageSize()+","+productQo.getPageSize();
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		int counts = jdbcTemplate.queryForInt(sql5);
		PageItem<Map<String, Object>> pageItem = new PageItem<>();
		pageItem.setCount(counts);
		pageItem.setItems(list);
		return pageItem;

	}


	/**
	 * 手机端根据名称查询商品
	 * @param productQo
	 * @param shopId
	 * @return
	 */
	public PageItem<Map<String,Object>> getProductByName(ProductQo productQo,Integer shopId) {
		String sql1=" select ";
		String sql2=" p.pid, p.name, p.price, p.pic_square ,p.pic_rectangle,p.share";
		String sql3=" from  product p where p.status=0 and p.shop_id= "+shopId ;
		String sql4=" count(*) ";

		if("" != productQo.getName() && null != productQo.getName()){
			sql3 += " and p.name like '%"+ productQo.getName() + "%'";
		}
		String sql = sql1 + sql2 + sql3;
		String sql5 = sql1 + sql4 + sql3;
		sql +=" order by p.sort, p.create_time desc limit "+productQo.getCurPage()*productQo.getPageSize()+","+productQo.getPageSize();
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		int counts = jdbcTemplate.queryForInt(sql5);
		log.info("---------------------"+sql+"---------------------------");
		PageItem<Map<String, Object>> pageItem = new PageItem<>();
		pageItem.setCount(counts);
		pageItem.setItems(list);
		return pageItem;
	}

	/**
	 * 查询商家分组所有数据
	 * @param productGroupQo
	 * @return
	 */
	public PageItem<Map<String, Object>> queryGroupList(ProductGroupQo productGroupQo, Integer shopId) {
		String sql1=" select ";
		String sql2="pg.group_id,pg.group_name,pg.group_image_path,pg.group_slogan,pg.group_sort ";
		String sql3="from product_group pg where pg.shop_id= "+shopId;
		String sql4="count(*) ";
		if(StringUtil.isNotNull(productGroupQo.getGroupName())){
			sql3 += " and pg.group_name like '%"+ productGroupQo.getGroupName() + "%'";
		}

		String sql = sql1 + sql2 + sql3;
		String sql5 = sql1 + sql4 + sql3;
		sql +=" order by pg.group_sort desc limit "+ productGroupQo.getCurPage()* productGroupQo.getPageSize()+","+ productGroupQo.getPageSize();
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		int counts = jdbcTemplate.queryForInt(sql5);
		PageItem<Map<String, Object>> pageItem = new PageItem<>();
		pageItem.setCount(counts);
		pageItem.setItems(list);
		return pageItem;
	}

	public PageItem<ProductAreaVo> queryAreaProduct(ProductAreaQo productQo, Integer shopId) throws IOException {
		String sql="SELECT p.pid, p.pic_square, p.status, p.name, p.price, p.total_count, p.create_time, p.offer_type FROM product p WHERE 1 = 1 AND p.status != 9  ";
		String sql1=" AND EXISTS ( SELECT 1 FROM product_area_rel AR LEFT JOIN USER U ON U.user_id = AR.user_id WHERE  p.pid = AR.pid ";
        StringBuilder sqlCondition1 = new StringBuilder();
        sqlCondition1.append(jdbcUtil.appendLike("p.name",productQo.getName()));
        sqlCondition1.append(jdbcUtil.appendAnd("p.shop_id",shopId));
        if(-1 != productQo.getOfferType()){
            sqlCondition1.append(jdbcUtil.appendAnd("p.offer_type",productQo.getOfferType()));
        }
        sqlCondition1.append(jdbcUtil.appendOrderBy("p.create_time"));

        StringBuilder sqlCondition2 = new StringBuilder();
        sqlCondition2.append(jdbcUtil.appendAnd("u.phone_number",productQo.getPhoneNumber()));
        sqlCondition2.append(jdbcUtil.appendLike("ar.area_code",productQo.getAreaCode()));
        sqlCondition2.append(")");
        String querySql="";
        if(StringUtil.isNotNull(productQo.getPhoneNumber()) || StringUtil.isNotNull(productQo.getAreaCode())){
            querySql=sql+sql1+sqlCondition2+sqlCondition1;
        }else {
            querySql=sql+sqlCondition1;
        }
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(querySql,productQo.getCurPage(),productQo.getPageSize());
		return ProductConverter.productArea2v(pageItem);
	}

    public List<OfferRole> getOfferRoleList(int shopId) throws IOException {
		String sql="SELECT u.user_id, u.user_name, u.phone_number " +
				"FROM user_role r " +
				"LEFT JOIN user u ON r.user_id = u.user_id " +
				"LEFT JOIN role rr ON r.role_id = rr.role_id " +
				"WHERE rr.type = 2 AND r.shop_id =  "+shopId;
		List<Map<String,Object>> maps = jdbcUtil.queryList(sql);
		return ProductConverter.offerRole2v(maps);
    }

	public PageItem<ProductAreaRelVo> getProductAreaOfferList(ProductAreaOfferQo productQo) throws IOException {
		String sql="SELECT re.id, re.area_name, re.user_id, u.user_name,re.supply_price, u.phone_number FROM product_area_rel re LEFT JOIN user u ON u.user_id = re.user_id WHERE 1 = 1";
		StringBuilder sqlCondition = new StringBuilder();
		sqlCondition.append(jdbcUtil.appendAnd("re.pid",productQo.getPid()));
		PageItem<Map<String,Object>> pageItem = jdbcUtil.queryPageItem(sql+sqlCondition,productQo.getCurPage(),productQo.getPageSize());
//		sqlCondition.append(jdbcUtil.appendOrderBy("p.create_time"));
		return  ProductConverter.productAreaRel2v(pageItem);
	}
}
