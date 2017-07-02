package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.system.ZbSoftMenu;
import com.jm.repository.po.zb.user.ZbUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZbSoftMenuRepository extends JpaRepository<ZbSoftMenu,Integer>{

    List<ZbSoftMenu> findBySoftId(Integer softId);

    @Modifying
    @Query(value = "delete from ZbSoftMenu  where softId=?1")
    void deleteBySoftId(Integer softId);

}
