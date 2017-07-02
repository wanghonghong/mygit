package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JmCommunity;
import com.jm.repository.po.system.JmPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<JmPost, Integer>{

    JmPost findById(Integer id);

    List<JmPost> findByUserIdAndStatus(Integer userId,Integer status);

    @Query(value = "select count(*) from jm_post where user_id=?1 and status=?2 ",nativeQuery=true)
    Integer queryPostNum(Integer userId,Integer status);

    @Query(value = "select * from jm_post where feature=2 and status=1 order by create_date desc limit 8 ",nativeQuery=true)
    List<JmPost> queryPostList();

    @Query(value = "select count(*) from jm_post where status=1 ",nativeQuery=true)
    Integer queryPostNums();
}
