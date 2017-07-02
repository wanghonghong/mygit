package com.jm.repository.jpa.shop.vote;

import com.jm.repository.po.shop.vote.VoteInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zx on 2017/3/31.
 */
public interface VoteInfoRepository  extends JpaRepository<VoteInfo,Integer> {

    @Query(value="select a from VoteInfo a where a.voteId=?1 " )
    Page findAll(Integer voteId, Pageable pageable);
}
