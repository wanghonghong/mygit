package com.jm.business.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.order.PayRecordRepository;
import com.jm.repository.po.order.PayRecord;

/**
 *<p>支付流水</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月23日
 */
@Service
public class PayRecordService {
	
	@Autowired
	private PayRecordRepository payRecordRepository;
	
	public PayRecord createPayRecord(PayRecord payRecord){
		return  payRecordRepository.save(payRecord);
	}

}
