package com.jm.repository.jpa.zb.system;

import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZbRoleTypeRepository extends JpaRepository<ZbRoleType, Integer>{

}
