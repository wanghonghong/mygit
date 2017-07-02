package com.jm.staticcode.converter.order;

import org.springframework.beans.BeanUtils;

import com.jm.mvc.vo.order.PayRecordVo;
import com.jm.mvc.vo.wx.RefundRecodCo;
import com.jm.repository.po.order.PayRecord;
import com.jm.repository.po.order.RefundRecod;
import com.jm.staticcode.util.Toolkit;


/**
 * 
 *<p></p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月23日
 */
public class PayRecordConverter {
	
	public static PayRecord toPayRecord(PayRecordVo payRecordVo){
		PayRecord payRecord = new PayRecord();
        BeanUtils.copyProperties(payRecordVo,payRecord);
        payRecord.setPayEndDate(Toolkit.strToDate(payRecordVo.getTimeEnd()));
        return payRecord;
    }
}
