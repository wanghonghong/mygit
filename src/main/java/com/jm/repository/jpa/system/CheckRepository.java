package com.jm.repository.jpa.system;

import com.jm.repository.po.system.JmJoinCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ME on 2016/8/17.
 */
public interface CheckRepository extends JpaRepository<JmJoinCheck,Integer>{

    @Query(value = "select * from jm_join_check c"+
            " left join jm_join j on c.user_id = j.user_id and c.type = j.type "+
            " where c.user_id=?1 and c.type=?2", nativeQuery = true)
    List<JmJoinCheck> queryCheckByJoinId(Integer userId,Integer type);

    @Modifying
    @Query(value = "delete from jm_join_check where user_id=?1 and type =?2", nativeQuery = true)
    public void deleteCheck(Integer userId,Integer type);

    @Query(value = "select * from jm_join_check where user_id=?1 and type=?2 order by check_time desc limit 1",nativeQuery = true)
    JmJoinCheck queryCheck(Integer userId,Integer type);


}
