package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.system.ZbDispatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ME on 2016/11/16.
 */
public interface DispatchHistoryRepository extends JpaRepository<ZbDispatchHistory,Integer>{

    @Query(value = "select * from zb_dispatch_history "+
            " where dispatch_id=?1", nativeQuery = true)
    List<ZbDispatchHistory> queryDispatchHistoryList(Integer dispatchId);

}
