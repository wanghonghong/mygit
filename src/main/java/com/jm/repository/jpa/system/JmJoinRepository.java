package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JmJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JmJoinRepository extends JpaRepository<JmJoin, Integer>{

    JmJoin findByUserIdAndType(Integer userId,Integer type);

    Page<JmJoin> findAll(Specification<JmJoin> spec, Pageable pageable);

    @Modifying
    @Query(value = "delete from jm_join where user_id=?1 and type=?2 ", nativeQuery = true)
    public void deleteJoin(Integer userId,Integer type);


    List<JmJoin> findByUserIdAndTypeAndStatus(Integer userId, Integer type, Integer status);
}
