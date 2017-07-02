package com.jm.repository.jpa.system;

import com.jm.repository.po.system.Area;
import com.jm.repository.po.system.JmVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author: cj
 * @date: 2016-6-15
 */
public interface VisitRepository extends JpaRepository<JmVisit, Integer>{

}
