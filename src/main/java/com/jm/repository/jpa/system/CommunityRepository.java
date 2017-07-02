package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JmCommunity;
import com.jm.repository.po.system.JmJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<JmCommunity, Integer>{

    JmCommunity findByUserId(Integer userId);

}
