package com.jm.repository.jpa.zb.system;


import com.jm.repository.po.zb.user.ZbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by ME on 2016/8/17.
 */
public interface ReviewRepository extends JpaRepository<ZbUser,Integer>{

    @Query(value = "select * from test.software_agent where status=1 order by create_date asc ", nativeQuery = true)
    List querySoftwareAgentList();

}
