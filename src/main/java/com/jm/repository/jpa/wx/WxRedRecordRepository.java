package com.jm.repository.jpa.wx;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.wx.WxRedRecord;

public interface WxRedRecordRepository   extends JpaRepository<WxRedRecord,Integer> {

	List<WxRedRecord> findByStatus(int status);
	
	WxRedRecord findByMchBillno (String mchBillno);
}
