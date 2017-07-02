package com.jm.repository.jpa.shop.vote;

import com.jm.repository.po.shop.vote.VoteItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zx on 2017/3/31.
 */
public interface VoteItemRepository extends JpaRepository<VoteItem,Integer> {

    @Query(value="select a from VoteItem a where a.shopId=?1 " )
    Page findAll(Integer shopId, Pageable pageable);
}
