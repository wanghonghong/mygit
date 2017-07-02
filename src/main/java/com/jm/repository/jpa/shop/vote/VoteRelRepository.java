package com.jm.repository.jpa.shop.vote;

import com.jm.repository.po.shop.vote.VoteRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zx on 2017/3/31.
 */
public interface VoteRelRepository extends JpaRepository<VoteRel,Integer> {

    @Query(value="select a from VoteRel a where a.themeId=?1 " )
    Page findAll(Integer themeId, Pageable pageable);
}
