package com.jm.repository.jpa.wx;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.wx.WxTemplateMsg;


public interface WxTemplateMsgRepository  extends JpaRepository<WxTemplateMsg,Integer> {

}
