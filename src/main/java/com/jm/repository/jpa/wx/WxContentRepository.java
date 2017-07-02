package com.jm.repository.jpa.wx;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.repository.po.wx.WxContent;

public interface WxContentRepository extends JpaRepository<WxContent,Integer> {
	
	Page<WxContent> findAll(Specification<WxContent> specification, Pageable pageable);
	
	WxContent findByIdAndStatus(Integer id,Integer status);
	

}
