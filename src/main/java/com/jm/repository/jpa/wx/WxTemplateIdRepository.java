package com.jm.repository.jpa.wx;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.wx.WxTemplate;


public interface WxTemplateIdRepository extends JpaRepository<WxTemplate, String> {
	
	WxTemplate findByAppidAndTypeAndTemplateNum(String appid,int type,String templateNum);

}
