package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JmPost;
import com.jm.repository.po.system.PostReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<PostReply, Integer>{

    PostReply findById(Integer id);

    @Query(value = "select count(*) from post_reply where post_id=?1 ",nativeQuery=true)
    Integer queryReplyCount(Integer postId);
}
