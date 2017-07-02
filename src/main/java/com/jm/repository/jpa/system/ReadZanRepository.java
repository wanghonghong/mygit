package com.jm.repository.jpa.system;


import com.jm.mvc.vo.system.ReadZanCo;
import com.jm.repository.po.system.ReadZan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadZanRepository extends JpaRepository<ReadZan, Integer>{

    ReadZan findByTypeAndUserIdAndPostId(Integer type,Integer userId,Integer postId);

    @Query(value = "select count(*) from read_zan where type=?1 and post_id=?2 ",nativeQuery=true)
    Integer queryReadCount(Integer type,Integer postId);

    @Query(value = "select count(*) from read_zan where type=?1 and post_id=?2 ",nativeQuery=true)
    Integer queryZanCount(Integer type,Integer postId);
}
