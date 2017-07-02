package com.jm.business.service.system;

import com.jm.repository.jpa.JdbcRepository;
import com.jm.repository.jpa.product.ProductRepository;
import com.jm.repository.jpa.system.VisitRepository;
import com.jm.repository.po.product.Product;
import com.jm.repository.po.system.JmVisit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>微信用户</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/18
 */
@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private JdbcRepository jdbcRepository;


	public void saveVisitLog(Vector<JmVisit> vector) {
		visitRepository.save(vector);
	}

	@Transactional
    public void updateProductUvAndPv() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String strToday = sdf.format(today);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(today);
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		String strTomday = sdf.format(calendar.getTime());

		//查询当天pv访问量(页面访问量)
		List<Map<String,Object>> listPv = jdbcRepository.getPv(strToday,strTomday);
		List<Product> productListPv = new ArrayList<Product>();
		if(listPv!= null && listPv.size()>0){
			for (Map<String,Object> map : listPv){
				int count = Integer.parseInt(map.get("count").toString());
				int pid = Integer.parseInt(map.get("pid").toString());
				Product product = productRepository.findOne(pid);
				product.setPv(product.getPv()+count);
				productListPv.add(product);
			}
			productRepository.save(productListPv);
		}

    	//查询当天uv访问量
		List<Map<String,Object>> listUv = jdbcRepository.getUv(strToday,strTomday);
		List<Product> productListUv = new ArrayList<Product>();
		if(listUv != null && listUv.size()>0){
			for (Map<String,Object> map : listUv){
				int count = Integer.parseInt(map.get("count").toString());
				int pid = Integer.parseInt(map.get("pid").toString());
				Product product = productRepository.findOne(pid);
				product.setUv(product.getUv()+count);
				productListUv.add(product);
			}
			productRepository.save(productListUv);
		}
    }
}
