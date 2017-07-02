package com.jm.repository.jpa.shop.vote;

import com.jm.repository.po.shop.vote.VoteTheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by zx on 2017/3/31.
 */
public interface VoteThemeRepository extends JpaRepository<VoteTheme,Integer> {
//    @Query(value="select a from VoteTheme a where a.shopId=?1 " )
//    Page findAll(Integer shopId, Pageable pageable);

//    List<Vote> findVoteById();
    @Modifying
    @Query(value="update vote_theme set status = 2 where status in (0,1) and SYSDATE() >= start_time ",nativeQuery=true)
    void updateThemeTaskIng();

    @Modifying
    @Query(value="update vote_theme set status = 3 where status in (0,1,2) and SYSDATE() >= end_time ",nativeQuery=true)
    void updateThemeTaskOver();

}
